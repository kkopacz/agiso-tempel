/* org.agiso.tempel.core.DefaultTemplateExecutor (02-10-2012)
 * 
 * DefaultTemplateExecutor.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.agiso.tempel.Temp;
import org.agiso.tempel.core.convert.ITemplateParamConverter;
import org.agiso.tempel.core.engine.ITempelEngine;
import org.agiso.tempel.core.lang.MapStack;
import org.agiso.tempel.core.lang.SimpleMapStack;
import org.agiso.tempel.core.model.Repository;
import org.agiso.tempel.core.model.Template;
import org.agiso.tempel.core.model.Template.Scope;
import org.agiso.tempel.core.model.TemplateParam;
import org.agiso.tempel.core.model.TemplateReference;
import org.agiso.tempel.core.model.TemplateResource;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class DefaultTemplateExecutor implements ITemplateExecutor {
	private Map<Scope, String> repositories;

	// FIXME: Zastosować wstrzykiwanie zależności
	private IExpressionEvaluator expressionEvaluator = new VelocityExpressionEvaluator();

//	--------------------------------------------------------------------------
	/**
	 * 
	 */
	public DefaultTemplateExecutor() {
		// Inicjalizacja repozytoriów z zasobami dla poszczególnych poziomów:
		repositories = new HashMap<Scope, String>();
		String path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		int index = path.lastIndexOf("/repo/");
		if(index != -1) {
			// Rzeczywiste środowisko uruchomieniowe (uruchomienie z linni komend):
			repositories.put(Scope.GLOBAL, path.substring(0, index) + "/repository");
			repositories.put(Scope.USER, System.getProperty("user.home") + "/.tempel/repository");
			repositories.put(Scope.RUNTIME, System.getProperty("user.dir") + "/.tempel");
		} else {
			// Deweloperskie środowisko uruchomieniowe (uruchomienie z eclipse'a):
			index = path.lastIndexOf("/target/classes/");			// FIXME: Rodzielić katalogi repozytoriów
			repositories.put(Scope.GLOBAL, path.substring(0, index) + "/src/test/resources/repository");
			repositories.put(Scope.USER, path.substring(0, index) + "/src/test/resources/repository");
			repositories.put(Scope.RUNTIME, path.substring(0, index) + "/src/test/resources/repository");
		}
	}

//	--------------------------------------------------------------------------
	/**
	 * Uruchamia proces generacji treści w oparciu o szablon.
	 * 
	 * @param workDir Ścieżka do katalogu roboczego.
	 * @param repoDir Ścieżka bazowa repozytorium szablonów.
	 * @param template Szablon do uruchomienia.
	 * @param templates Mapa wszystkich dostępnych szablonów.
	 */
	@Override
	public void executeTemplate(String workDir, String repoDir, Template template, ITemplateProvider templateProvider, Map<String, Object> globalProperties) {
		MapStack<String, Object> stack = new SimpleMapStack<String,Object>();

		stack.push(new HashMap<String, Object>(globalProperties));
		executeTemplate(workDir, repoDir, template, templateProvider, stack, globalProperties, "");
		stack.pop();
	}
	public void executeTemplate(String workDir, String repoDir, Template template, ITemplateProvider templateProvider, MapStack<String, Object> stack, Map<String, Object> globalProperties, String depth) {
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
				engine.initialize(repositories);
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
				String gId = Temp.StringUtils_emptyIfBlank(refTemplate.getGroupId());
				String tId = Temp.StringUtils_emptyIfBlank(refTemplate.getTemplateId());
				String ver = Temp.StringUtils_nullIfBlank(refTemplate.getVersion());		// dla nieokreślonej wersji null

				Template subTemplate = templateProvider.get(gId, tId, ver);
				if(subTemplate == null) {
					subTemplate = templateProvider.get(refTemplate.getKey());
					if(subTemplate == null) {
						throw new IllegalStateException("Nieznany podszablon '" + refTemplate.getKey() + "' szablonu '" + template.getKey() + "'" );
					}
				}

				// Szablon istnieje. Kopiowanie jego standardowej definicji i aktualizowanie
				// w oparciu o informacje zdefiniowane w podszablonie:
				System.err.println(depth + " Preparing template '" + subTemplate.getKey() + "': "+
						subTemplate.getGroupId() +":" + subTemplate.getTemplateId() + ":" + subTemplate.getVersion()
				);
				subTemplate = subTemplate.clone();								// kopia podszablonu z repozytorium (do modyfikacji)
				// subTemplate.setScope(template.getScope());						// podszablon ma to samo repozytorium co szablon

				Map<String, Object> subParams = new HashMap<String, Object>(globalProperties);	// parametry dodane podzablonu
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
				executeTemplate(subWorkDir, repoDir, subTemplate, templateProvider, stack, globalProperties, "  " + depth);

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
				// Wyznaczanie katalogu roboczego dla wszystkich zasobów geneorowanych przez
				// aktualnie wykonywany szablon. Jest określany na poziomie całego szablonu,
				// więc jest wspólny dla wszystkich zdefiniowanych w nim zasobów:
				String resWorkDir = workDir;
//				if(!Temp.StringUtils_isEmpty(template.getWorkDir())) {
//					resWorkDir = workDir + "/" + template.getWorkDir();
//				}

				for(TemplateResource resource : template.getResources()) {
					template = resource.getParentTemplateReference();

					String srcDir = null;
					if(!Temp.StringUtils_isEmpty(template.getGroupId())) {
						// Szablony bez określonej grupy, identyfikatora i wersji mogą generować zasoby
						// o ile nie wymagają do tego celu żadnych plików źródłowych (np. szablonów velocity).
						// Nie mają one bowiem określonej ścieżki w repozytorium.
						// Przykładem tego typu szablonów są szablony tworzące katalogi.
						Repository r = template.getRepository();

						srcDir = (r == null? "" : r.getValue());
						srcDir = srcDir + '/' + template.getGroupId().replace('.', '/');
						srcDir = srcDir + '/' + template.getTemplateId();
						srcDir = srcDir + '/' + template.getVersion();
					}

					if(!Temp.StringUtils_isBlank(resource.getSource())) {
						srcDir = srcDir + '/' + resource.getSource();
					}
					doEngineRun(template.getScope(), engine, resWorkDir, srcDir, resource.getTarget(), stack);
				}
			} else {
				String resWorkDir = workDir;

				String srcDir = null;
				if(!Temp.StringUtils_isEmpty(template.getGroupId())) {
					// Szablony bez określonej grupy, identyfikatora i wersji mogą generować zasoby
					// o ile nie wymagają do tego celu żadnych plików źródłowych (np. szablonów velocity).
					// Nie mają one bowiem określonej ścieżki w repozytorium.
					// Przykładem tego typu szablonów są szablony tworzące katalogi.
					Repository r = template.getRepository();

					srcDir = (r == null? "" : r.getValue());
					srcDir = srcDir + '/' + template.getGroupId().replace('.', '/');
					srcDir = srcDir + '/' + template.getTemplateId();
					srcDir = srcDir + '/' + template.getVersion();
				}

				doEngineRun(template.getScope(), engine, resWorkDir, srcDir, null, stack);
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
			String name;
			if(Temp.StringUtils_isBlank(param.getName())) {
				name = "Param '" + param.getKey() + "'";
			} else {
				name = param.getName();
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			try {
				if(value == null) {
					System.out.print(name + " []: ");
				} else {
					System.out.print(name + " [" + value + "]: ");
				}
				String line = br.readLine();
				if(!Temp.StringUtils_isEmpty(line)) {
					value = line;
				}
			} catch(IOException e) {
				throw new RuntimeException(e);
			}
		}
		return value;
	}
	/**
	 * @param engine 
	 * @param workDir
	 * @param srcDir
	 * @param resource
	 * @param params
	 */
	private void doEngineRun(Template.Scope scope, ITempelEngine engine, String workDir, String srcDir, String target, MapStack<String, Object> stack) {
		if(Temp.StringUtils_isEmpty(target)) {
			target = workDir + "/";
		} else {
			target = workDir + "/" + expressionEvaluator.evaluate(target, stack.peek());
		}
		engine.run(scope, srcDir, stack, target);
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
