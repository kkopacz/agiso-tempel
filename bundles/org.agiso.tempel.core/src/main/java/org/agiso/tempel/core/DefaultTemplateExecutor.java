/* org.agiso.tempel.core.DefaultTemplateExecutor (02-10-2012)
 * 
 * DefaultTemplateExecutor.java
 * 
 * Copyright 2012 agiso.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.agiso.tempel.core;

import java.util.HashMap;
import java.util.Map;

import org.agiso.core.lang.MapStack;
import org.agiso.core.lang.SimpleMapStack;
import org.agiso.tempel.Temp;
import org.agiso.tempel.api.ITempelEngine;
import org.agiso.tempel.api.ITemplateParamConverter;
import org.agiso.tempel.api.internal.IExpressionEvaluator;
import org.agiso.tempel.api.internal.IParamReader;
import org.agiso.tempel.api.internal.ITemplateExecutor;
import org.agiso.tempel.api.internal.ITemplateProvider;
import org.agiso.tempel.api.model.Template;
import org.agiso.tempel.api.model.TemplateParam;
import org.agiso.tempel.api.model.TemplateReference;
import org.agiso.tempel.api.model.TemplateResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
@Component
public class DefaultTemplateExecutor implements ITemplateExecutor {
	private IParamReader paramReader;
	private IExpressionEvaluator expressionEvaluator;

//	--------------------------------------------------------------------------
	@Override
	@Autowired
	public void setParamReader(IParamReader paramReader) {
		this.paramReader = paramReader;
	}

	@Autowired
	public void setExpressionEvaluator(IExpressionEvaluator expressionEvaluator) {
		this.expressionEvaluator = expressionEvaluator;
	}

//	--------------------------------------------------------------------------
	/**
	 * Uruchamia proces generacji treści w oparciu o szablon.
	 * 
	 * @param workDir Ścieżka do katalogu roboczego.
	 * @param template Szablon do uruchomienia.
	 * @param templates Mapa wszystkich dostępnych szablonów.
	 */
	@Override
	public void executeTemplate(String workDir, Template template, ITemplateProvider templateProvider, Map<String, Object> properties) {
		MapStack<String, Object> stack = new SimpleMapStack<String,Object>();

		stack.push(new HashMap<String, Object>(properties));
		executeTemplate(workDir, template, templateProvider, stack, properties, "");
		stack.pop();
	}
	public void executeTemplate(String workDir, Template template, ITemplateProvider templateProvider, MapStack<String, Object> stack, Map<String, Object> properties, String depth) {
		if(!Temp.StringUtils_isEmpty(template.getWorkDir())) {
			workDir = workDir + "/" + template.getWorkDir();
		}

		System.err.println(depth + "Executing template '" + template.getKey() + "': "+
			template.getGroupId() +":" + template.getTemplateId() + ":" + template.getVersion()
		);

		//depth = depth + "  ";

		// Instancjonowanie klasy silnika generatora:
		ITempelEngine engine = null;
		if(template.getEngine() != null) {
			try {
				engine = template.getEngine().newInstance();
			} catch(Exception e) {
				throw new RuntimeException(e);
			}
		}

		// Wczytywanie parametrów wejściowych ze standardowego wejścia:
		Map<String, Object> params = stack.peek();

		if(template.getParams() != null) {
			for(TemplateParam param : template.getParams()) {
				// Wypełnianie parametrów wewnętrznych i konwersja parametru:
				String value = getParamValue(param, stack.peek());
				Object object = convertParamValue(value, param.getConverter());

				params.put(param.getKey(), object);
			}
		}

		// Wykonywanie podszablonów w oparciu o zdefiniowane referencje:
		if(template.getReferences() != null) {
			for(TemplateReference refTemplate : template.getReferences()) {
				// Map<String, Object> params2 = new HashMap<String, Object>(params);
				// stack.push(params2);

				// Sprawdzanie, czy istnieje szablon opisywany przez podszablon:
				String key = Temp.StringUtils_nullIfBlank(refTemplate.getKey());
				String gId = Temp.StringUtils_emptyIfBlank(refTemplate.getGroupId());
				String tId = Temp.StringUtils_emptyIfBlank(refTemplate.getTemplateId());
				String ver = Temp.StringUtils_nullIfBlank(refTemplate.getVersion());		// dla nieokreślonej wersji null

				if(key == null) {
					key = gId + ":" + tId + ":" + ver;
				}

				Template subTemplate = templateProvider.get(key, gId, tId, ver);
				if(subTemplate == null) {
					throw new IllegalStateException("Nieznany podszablon '" + refTemplate.getKey() + "' szablonu '" + template.getKey() + "'" );
				}

				// Szablon istnieje. Kopiowanie jego standardowej definicji i aktualizowanie
				// w oparciu o informacje zdefiniowane w podszablonie:
				System.err.println(depth + " Preparing template '" + subTemplate.getKey() + "': "+
						subTemplate.getGroupId() +":" + subTemplate.getTemplateId() + ":" + subTemplate.getVersion()
				);
				subTemplate = subTemplate.clone();								// kopia podszablonu z repozytorium (do modyfikacji)
				// subTemplate.setScope(template.getScope());						// podszablon ma to samo repozytorium co szablon

				Map<String, Object> subParams = new HashMap<String, Object>(properties);	// parametry dodane podzablonu
				subParams.put("top", params);
				stack.push(subParams);

				// Aktualizacja parametrów przeciążonych i dodawanie nowych:
				if(refTemplate.getParams() != null) {
					for(TemplateParam refParam : refTemplate.getParams()) {
						// Wyszukujemy parametr pośród parametrów kopii standardowej definicji
						// szablonu i aktualizujemy jego definicję na podstawie podszablonu:
						String id = refParam.getKey();
						TemplateParam param = null;
						if(subTemplate.getParams() != null) {
							for(TemplateParam subParam : subTemplate.getParams()) {
								if(subParam.getKey().equals(id)) {
									param = subParam;
									break;
								}
							}
						}
						if(param == null) {
							// throw new IllegalStateException("Nie znaleziono parametru '" + id + "' podszablonu '" + reference.getKey() + "'");

							boolean byReference = true;
							if(byReference) {
								subTemplate.getParams().add(refParam);
							} else {
								String value = getParamValue(refParam, stack.peek());
								Object object = convertParamValue(value, refParam.getConverter());
								subParams.put(refParam.getKey(), object);
							}
						} else {
							if(refParam.getValue() != null) {
								String refParamValue = expressionEvaluator.evaluate(refParam.getValue(), stack.peek());
								System.err.println(depth + "  Setting parameter '" + refParam.getKey() + "': "+
										param.getValue() + " <- " + refParamValue
								);
	
								param.setValue(refParamValue);
							}
							if(refParam.getFixed() != null) {
								param.setFixed(refParam.getFixed());
							}
							if(refParam.getConverter() != null) {
								param.setConverter(refParam.getConverter());
							}
						}
					}
				}

				// Aktualizacja listy zasobów tworzonych przez szablon:
				if(refTemplate.getResources() != null) {
					for(TemplateResource refResource : refTemplate.getResources()) {
						refResource.setParentTemplateReference(template);
						subTemplate.getResources().add(refResource);
					}
				}

				// Ustalanie katalogu roboczego dla podszablonu:
				if(!Temp.StringUtils_isEmpty(refTemplate.getWorkDir())) {
					subTemplate.setWorkDir(expressionEvaluator.evaluate(refTemplate.getWorkDir(), stack.peek()));
				}

				// Wykonywanie szablonu w zaktualizowanej wersji:
				String subWorkDir = workDir;
//				if(!Temp.StringUtils_isEmpty(template.getWorkDir())) {
//					subWorkDir = workDir + "/" + template.getWorkDir();
//				}
				executeTemplate(subWorkDir, subTemplate, templateProvider, stack, properties, "  " + depth);

				stack.pop();
			}
		}

		// Generacja zasobów:
		if(engine != null) {
			System.err.println(depth + "Running template '" + template.getKey() + "': "+
				template.getGroupId() +":" + template.getTemplateId() + ":" + template.getVersion()
				// + " " + "with resources: " + stack.peek()
			);

			// Uruchomienie silnika do generacji zasobów tworzonych przez szablon:
			if(template.getResources() != null && !template.getResources().isEmpty()) {
				// Katalog roboczy dla wszystkich zasobów geneorowanych przez aktualnie
				// wykonywany szablon jest określany na poziomie tego szablonu i jest
				// wspólny dla wszystkich zdefiniowanych w nim zasobów:
				for(TemplateResource resource : template.getResources()) {
					template = resource.getParentTemplateReference();

					doEngineRun(
							engine, template, resource.getSource(),
							workDir, resource.getTarget(), stack
					);
				}
			} else {
				doEngineRun(engine, template, null, workDir, null, stack);
			}
		}
	}

	/**
	 * @param param
	 * @param params 
	 * @return
	 */
	private String getParamValue(TemplateParam param, Map<String, Object> params) {
		String value = expressionEvaluator.evaluate(param.getValue(), params);
		if(param.getFixed() != null && param.getFixed() == true) {
			// Parametr oznaczony musi mieć zdefiniowaną wartość:
			if(Temp.StringUtils_isEmpty(value)) {
				throw new IllegalStateException("Brak wartości dla parametru oznaczonego '" + param.getKey() + "'");
			}
		} else {
			// Wartości pozostałych parametrów są określane w trakcie wykonania:
			System.out.println(paramReader.getClass().getSimpleName() + 
					"#getParamValue(" + param.getKey() +", " + param.getName() + ", " + value +")"
			);
			value = paramReader.getParamValue(param.getKey(), param.getName(), value);
		}
		return value;
	}
	/**
	 * @param engine 
	 * @param srcDir
	 * @param workDir
	 * @param resource
	 * @param params
	 */
	private void doEngineRun(ITempelEngine engine, Template template, String source, String workDir, String target, MapStack<String, Object> stack) {
		if(Temp.StringUtils_isEmpty(target)) {
			target = workDir + "/";
		} else {
			target = workDir + "/" + expressionEvaluator.evaluate(target, stack.peek());
		}

		engine.run(template.getTemplateSource(source), stack, target);
	}

//	--------------------------------------------------------------------------
	/**
	 * @param value
	 * @param clazz
	 * @return
	 */
	private Object convertParamValue(String value, Class<? extends ITemplateParamConverter<?>> clazz) {
		if(clazz == null) {
			return value;
		}

		try {
			return clazz.newInstance().convert(value);
		} catch(Exception e) {
			return new RuntimeException(e);
		}
	}
}
