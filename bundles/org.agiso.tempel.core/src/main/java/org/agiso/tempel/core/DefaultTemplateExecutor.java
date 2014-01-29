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

import static org.agiso.tempel.Temp.AnsiUtils.*;
import static org.agiso.tempel.Temp.AnsiUtils.AnsiElement.*;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.agiso.core.lang.MapStack;
import org.agiso.core.lang.SimpleMapStack;
import org.agiso.core.logging.Logger;
import org.agiso.core.logging.util.LogUtils;
import org.agiso.tempel.Temp;
import org.agiso.tempel.api.ITempelEngine;
import org.agiso.tempel.api.ITemplateParamConverter;
import org.agiso.tempel.api.ITemplateParamValidator;
import org.agiso.tempel.api.internal.IExpressionEvaluator;
import org.agiso.tempel.api.internal.IParamReader;
import org.agiso.tempel.api.internal.ITemplateExecutor;
import org.agiso.tempel.api.internal.ITemplateProvider;
import org.agiso.tempel.api.internal.ITemplateVerifier;
import org.agiso.tempel.api.model.Template;
import org.agiso.tempel.api.model.TemplateParam;
import org.agiso.tempel.api.model.TemplateParamConverter;
import org.agiso.tempel.api.model.TemplateParamFetcher;
import org.agiso.tempel.api.model.TemplateParamValidator;
import org.agiso.tempel.api.model.TemplateReference;
import org.agiso.tempel.api.model.TemplateResource;
import org.agiso.tempel.core.converter.DateParamConverter;
import org.agiso.tempel.core.converter.IntegerParamConverter;
import org.agiso.tempel.core.converter.LongParamConverter;
import org.agiso.tempel.core.model.exceptions.AbstractTemplateException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
@Component
public class DefaultTemplateExecutor implements ITemplateExecutor {
	private static final Logger logger = LogUtils.getLogger(DefaultTemplateExecutor.class);

	private ITemplateProvider templateProvider;
	private ITemplateVerifier templateVerifier;

	private IParamReader paramReader;
	private IExpressionEvaluator expressionEvaluator;

//	--------------------------------------------------------------------------
	@Autowired
	public void setTemplateProvider(ITemplateProvider templateProvider) {
		this.templateProvider = templateProvider;
	}

	@Autowired
	public void setTemplateVerifier(ITemplateVerifier templateVerifier) {
		this.templateVerifier = templateVerifier;
	}

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
	 * @param templateName Szablon do uruchomienia.
	 * @param properties Mapa parametrów uruchomienia szablonu.
	 * @param workDir Ścieżka do katalogu roboczego.
	 */
	@Override
	public void executeTemplate(String templateName, Map<String, Object> properties, String workDir) {
		// Pobieranie definicji szablonu do użycia:
		Template<?> template = templateProvider.get(templateName, null, null, null);
		if(template == null) {
			throw new RuntimeException("Nie znaleziono szablonu " + templateName);
		}
		if(template.isAbstract()) {
			throw new AbstractTemplateException(templateName);
		}

		// Weryfikowanie definicji szablonu, szablonu nadrzędnego i wszystkich
		// szablonów używanych. Sprawdzanie dostępność klas silników generatorów.
		templateVerifier.verifyTemplate(template, templateProvider);

		MapStack<String, Object> propertiesStack = new SimpleMapStack<String,Object>();
		propertiesStack.push(new HashMap<String, Object>(properties));
		doExecuteTemplate(template, propertiesStack, workDir);
		propertiesStack.pop();
	}

	private void doExecuteTemplate(Template<?> template, MapStack<String, Object> properties, String workDir) {
		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			// FIXME: Zastosować zbiór Set<URL>
			Set<String> classPath = template.getTemplateClassPath();
			if(classPath != null && !classPath.isEmpty()) {
				List<URL> urls = new ArrayList<URL>(classPath.size());
				for(String classPathEntry : classPath) {
					urls.add(new File(classPathEntry).toURI().toURL());
				}
				ClassLoader classLoader = new URLClassLoader(urls.toArray(new URL[urls.size()]), contextClassLoader);
				Thread.currentThread().setContextClassLoader(classLoader);
			}

			doExecuteTemplateInternal(template, properties, workDir);
		} catch(Exception e) {
			throw new RuntimeException(e);
		} finally {
			Thread.currentThread().setContextClassLoader(contextClassLoader);
		}
	}

	private void doExecuteTemplateInternal(Template<?> template, MapStack<String, Object> properties, String workDir) {
		if(!Temp.StringUtils_isEmpty(template.getWorkDir())) {
			workDir = workDir + "/" + template.getWorkDir();
		}

		logger.info("Executing template {}", ansiString(GREEN,
				template.getKey() + ": " +
				template.getGroupId() +":" + template.getTemplateId() + ":" + template.getVersion())
		);

		// Instancjonowanie klasy silnika generatora:
		ITempelEngine engine = template.getEngine().getInstance();

		// Wczytywanie parametrów wejściowych ze standardowego wejścia:
		Map<String, Object> params = properties.peek();

		if(template.getParams() != null) {
			for(TemplateParam<?, ?, ?> param : template.getParams()) {
				// Wypełnianie parametrów wewnętrznych i konwersja parametru:

				String count = param.getCount();
				int countParam = 1;
				Object object = null;
				// jeśli jest określony atrybut count - to wartości parametrów będą zbierane do listy
				if(count != null){
					List<Object> list  = new ArrayList<Object>();
					// jeśli wartość count jest liczbowa to zapytanie o wartość parametru wykonywane jest 
					// dokładnie tyle razy ile określa atrybut count
					if(StringUtils.isNumeric(count)){
						countParam = Integer.parseInt(count);
						for(int i = 0; i < countParam; i++){
							list.add(processParam(param, properties.peek(), template));
						}
					} else {
						// jeśli count określone jest symbolem '*' to po każdym pobraniu wartości 
						// pytanie jest czy zakończyć podawanie parametru
						if(count.equals("*")){
							boolean finish = false;
							do {
								list.add(processParam(param, properties.peek(), template));

								finish = paramReader.getParamValue("finish",
										"Do you want to add another? (0 - NO, 1 - YES)", "0").equals("0");
							} while(!finish);
						}
					}
					object = list;
				} else {
					object = processParam(param, properties.peek(), template);
				}
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

				Template<?> subTemplate = templateProvider.get(key, gId, tId, ver);
				if(subTemplate == null) {
					throw new IllegalStateException("Nieznany podszablon '" + refTemplate.getKey() + "' szablonu '" + template.getKey() + "'" );
				}

				// Szablon istnieje. Kopiowanie jego standardowej definicji i aktualizowanie
				// w oparciu o informacje zdefiniowane w podszablonie:
				logger.debug("Preparing template {}", ansiString(GREEN,
						subTemplate.getKey() + ": " +
						subTemplate.getGroupId() +":" + subTemplate.getTemplateId() + ":" + subTemplate.getVersion())
				);

				subTemplate = subTemplate.clone();								// kopia podszablonu z repozytorium (do modyfikacji)
				// subTemplate.setScope(template.getScope());					// podszablon ma to samo repozytorium co szablon

				Map<String, Object> subParams = new HashMap<String, Object>();	// parametry dodane podszablonu
				subParams.put("top", params);
				properties.push(subParams);

				// Aktualizacja parametrów przeciążonych i dodawanie nowych:
				if(refTemplate.getParams() != null) {
					for(TemplateParam<?, ?, ?> refParam : refTemplate.getParams()) {
						// Wyszukujemy parametr pośród parametrów kopii standardowej definicji
						// szablonu i aktualizujemy jego definicję na podstawie podszablonu:
						String id = refParam.getKey();
						TemplateParam param = null;
						if(subTemplate.getParams() != null) {
							for(TemplateParam<?, ?, ?> subParam : subTemplate.getParams()) {
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
								Object value = fetchParamValue(refParam, properties.peek(), refParam.getFetcher());
								value = convertParamValue(value, refParam.getType(), refParam.getConverter());
								validateParamValue(value, refParam.getValidator());
								subParams.put(refParam.getKey(), value);
							}
						} else {
							if(refParam.getValue() != null) {
								String refParamValue = expressionEvaluator.evaluate(refParam.getValue(), properties.peek());
								logger.debug("Reference property {}: {} <-- {}",
										refParam.getKey(), param.getValue(), refParamValue
								);
	
								param.setValue(refParamValue);
							}
							if(refParam.getFixed() != null) {
								param.setFixed(refParam.getFixed());
							}
							if(refParam.getConverter() != null) {
								param.setConverter(refParam.getConverter());
							}
							if(refParam.getValidator() != null) {
								param.setValidator(refParam.getValidator());
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
					subTemplate.setWorkDir(expressionEvaluator.evaluate(refTemplate.getWorkDir(), properties.peek()));
				}

				// Wykonywanie szablonu w zaktualizowanej wersji:
				String subWorkDir = workDir;
//				if(!Temp.StringUtils_isEmpty(template.getWorkDir())) {
//					subWorkDir = workDir + "/" + template.getWorkDir();
//				}
				doExecuteTemplate(subTemplate, properties, subWorkDir);

				properties.pop();
			}
		}

		// Generacja zasobów:
		if(engine != null) {
			logger.info("Running template {}", ansiString(GREEN,
					template.getKey() + ": " +
					template.getGroupId() +":" + template.getTemplateId() + ":" + template.getVersion())
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
							workDir, resource.getTarget(), properties
					);
				}
			} else {
				doEngineRun(engine, template, null, workDir, null, properties);
			}
		}
	}

	/**
	 * Przetwarzanie warości parametru (pobieranie, konwersja i walidacja)
	 * @param param
	 * @param params
	 * @param template
	 * @return
	 */
	private Object processParam(TemplateParam<?, ?, ?> param, Map<String, Object> params, Template<?> template) {
		Object value = fetchParamValue(param, params, param.getFetcher());
		value = convertParamValue(value, param.getType(), param.getConverter());
		validateParamValue(value, param.getValidator());
		return value;
	}

	/**
	 * @param engine 
	 * @param srcDir
	 * @param workDir
	 * @param resource
	 * @param params
	 */
	private void doEngineRun(ITempelEngine engine, Template<?> template, String source, String workDir, String target, MapStack<String, Object> stack) {
		if(Temp.StringUtils_isEmpty(target)) {
			target = workDir + "/";
		} else {
			target = workDir + "/" + expressionEvaluator.evaluate(target, stack.peek());
		}

		engine.run(template.getTemplateSource(source), stack, target);
	}

//	--------------------------------------------------------------------------
	private static final Map<String, Class<?>> paramTypes =
			new HashMap<String, Class<?>>();
	private static final List<ITemplateParamConverter<?, ?>> paramConverters =
			new ArrayList<ITemplateParamConverter<?, ?>>();
	static {
		paramConverters.add(new IntegerParamConverter());
		paramConverters.add(new LongParamConverter());
		paramConverters.add(new DateParamConverter());
	}

	/**
	 * @param param
	 * @param params 
	 * @return
	 */
	private Object fetchParamValue(TemplateParam<?, ?, ?> param, Map<String, Object> params, TemplateParamFetcher fetcher) {
		boolean fixed = param.getFixed() != null && param.getFixed() == true;
		String value = expressionEvaluator.evaluate(param.getValue(), params);

		// Parametr oznaczony musi mieć zdefiniowaną wartość:
		if(fixed && Temp.StringUtils_isEmpty(value)) {
			throw new IllegalStateException("Brak wartości dla parametru oznaczonego '" + param.getKey() + "'");
		}

		if(params.containsKey(param.getKey())) {
			// Parametr może być zdefiniowany jako parametr wywołania (przez -Dkey=value):
			value = expressionEvaluator.evaluate(params.get(param.getKey()).toString(), params);
		} else if(!fixed) {
			param.setValue(value);
			return fetcher.getInstance().fetch(paramReader, param);
		}

		param.setValue(value);
		return value;
	}

	/**
	 * @param value
	 * @param type
	 * @param converter
	 * @return
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	private Object convertParamValue(Object value, String type, TemplateParamConverter converter) {
		Class<?> valueClass = (value == null? null : value.getClass());
		ITemplateParamConverter typeConverter = converter.getInstance();
		if(typeConverter == null) {
			if(type == null || valueClass == null || type.equals(valueClass.getCanonicalName())) {
				return value;
			} else {
				Class<?> typeClass;
				if(paramTypes.containsKey(type)) {
					typeClass = paramTypes.get(type);
				} else {
					try {
						typeClass = Class.forName(type);
					} catch(ClassNotFoundException e) {
						throw new RuntimeException(e);
					}
				}
				for(ITemplateParamConverter<?, ?> paramConverter : paramConverters) {
					if(paramConverter.canConvert(valueClass, typeClass)) {
						typeConverter = paramConverter;
						break;
					}
				}
				if(typeConverter == null) {
					throw new RuntimeException("Brak konwertera dla parametru typu: " + type);
				}
			}
		}
		return typeConverter.convert(value);
	}

	/**
	 * @param value
	 * @param validator
	 */
	@SuppressWarnings("unchecked")
	private void validateParamValue(Object value, TemplateParamValidator validator) {
		ITemplateParamValidator<?> valueValidator = validator.getInstance();
		if(valueValidator != null) {
			((ITemplateParamValidator<Object>)valueValidator).validate(value);
		}
	}
}
