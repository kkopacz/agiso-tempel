/* org.agiso.tempel.core.XStreamTemplateProvider (02-10-2012)
 * 
 * XStreamTemplateProvider.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.agiso.tempel.Temp;
import org.agiso.tempel.core.model.Repository;
import org.agiso.tempel.core.model.Template;
import org.agiso.tempel.core.model.TemplateResource;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class XStreamTemplateProvider implements ITemplateProvider {
	private Map<String, Object> globalProperties;

//	private List<ITemplateRepository> repositories = new ArrayList<ITemplateRepository>();

	private ITemplateRepository templateRepository = new HashBasedTemplateRepository();
	private IExpressionEvaluator expressionEvaluator = new VelocityExpressionEvaluator();

	private X x = new X();

//	--------------------------------------------------------------------------
	@Override
	public void initialize(Map<String, Object> globalProperties) throws IOException {
		this.globalProperties = globalProperties;

//		repositories.add(templateRepository);
//
//		repositories.add(new RunTemplateRepository());
//		repositories.add(new UsrTemplateRepository());
//		repositories.add(new AppTemplateRepository());
//		repositories.add(new MvnTemplateRepository());

		// Budowanie mapy szablonów w oparciu o pliki konfiguracyjne templates.xml
		// w katalogu konfiguracyjnym aplikacji, katalogu domowym użytkownika oraz
		// katalogu bieżącym:
		readAppTemplates(templateRepository);
		readUsrTemplates(templateRepository);
		readRunTemplates(templateRepository);
	}

	@Override
	public Template get(String key, String groupId, String templateId, String version) {
		return templateRepository.get(key, groupId, templateId, version);
	}

//	--------------------------------------------------------------------------
	/**
	 * @param xStream
	 * @param templateRepository
	 * @throws IOException
	 */
	private void readAppTemplates(final ITemplateRepository templateRepository) throws IOException {
		// Mapa szablonów globalnych (katalog konfiguracyjny aplikacji):
		String appSettings = getAppSettingsPath();
		File appSettingsFile = new File(appSettings);

		try {
			x.process(appSettingsFile, new X.IEntryProcessor() {
				@Override
				public void processObject(Object object) {
					XStreamTemplateProvider.this.processObject(Template.Scope.GLOBAL, object, templateRepository);
				}
			});
			System.out.println("Wczytano ustawienia globalne z pliku " + appSettingsFile.getCanonicalPath());
		} catch(Exception e) {
			System.err.println("Błąd wczytywania ustawień globalnych: " + e.getMessage());
			throw new RuntimeException(e);
		}
	}
	private String getAppSettingsPath() {
		String path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();

		int index = path.lastIndexOf("/repo/");
		if(index != -1) {
			path = path.substring(0, index) + "/conf/tempel.xml";
		} else {
			index = path.lastIndexOf("/target/classes/");
			path = path.substring(0, index) + "/src/test/configuration/application/tempel.xml";
		}

		return path;
	}

	/**
	 * @param xStream
	 * @param templateRepository
	 * @throws IOException
	 */
	private void readUsrTemplates(final ITemplateRepository templateRepository) throws IOException {
		// Mapa szablonów użytkownika (katalog domowy użytkownika):
		String usrSettings = getUsrSettingsPath();
		File usrSettingsFile = new File(usrSettings);

		try {
			x.process(usrSettingsFile, new X.IEntryProcessor() {
				@Override
				public void processObject(Object object) {
					XStreamTemplateProvider.this.processObject(Template.Scope.USER, object, templateRepository);
				}
			});
			System.out.println("Wczytano ustawienia użytkownika z pliku " + usrSettingsFile.getCanonicalPath());
		} catch(Exception e) {
			System.err.println("Błąd wczytywania ustawień użytkownika: " + e.getMessage());
			throw new RuntimeException(e);
		}
	}
	private String getUsrSettingsPath() {
		String path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();

		int index = path.lastIndexOf("/repo/");
		if(index != -1) {
			path = System.getProperty("user.home") + "/.tempel/tempel.xml";
		} else {
			index = path.lastIndexOf("/target/classes/");
			path = path.substring(0, index) + "/src/test/configuration/home/tempel.xml";
		}

		return path;
	}

	/**
	 * @param xStream
	 * @param templateRepository
	 * @throws IOException
	 */
	private void readRunTemplates(final ITemplateRepository templateRepository) throws IOException {
		// Mapa szablonów lokalnych (katalog bieżący projektu):
		String runSettings = getRunSettingsPath();
		File runSettingsFile = new File(runSettings);

		try {
			x.process(runSettingsFile, new X.IEntryProcessor() {
				@Override
				public void processObject(Object object) {
					XStreamTemplateProvider.this.processObject(Template.Scope.RUNTIME, object, templateRepository);
				}
			});
			System.out.println("Wczytano ustawienia lokalne z pliku " + runSettingsFile.getCanonicalPath());
		} catch(Exception e) {
			System.err.println("Błąd wczytywania ustawień lokalnych: " + e.getMessage());
			throw new RuntimeException(e);
		}
	}
	private String getRunSettingsPath() {
		String path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();

		int index = path.lastIndexOf("/repo/");
		if(index != -1) {
			path = System.getProperty("user.dir") + "/tempel.xml";
		} else {
			index = path.lastIndexOf("/target/classes/");
			path = path.substring(0, index) +"/src/test/configuration/runtime/tempel.xml";
		}

		return path;
	}

//	--------------------------------------------------------------------------
	/**
	 * Przetwarza obiekt odczytany z pliku tempel.xml
	 * 
	 * @param scope
	 * @param object
	 * @param templateRepository
	 */
	private void processObject(Template.Scope scope, Object object, ITemplateRepository templateRepository) {
		// Ścieżka repozytorium pliku tempel.xml:
		if(object instanceof Repository) {
			Repository repository = (Repository)object;

			templateRepository.setRepository(scope, repository);
			return;
		}

		// Mapa parametrów pliku tempel.xml:
		if(object instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<String, String> scopeProperties = (Map<String, String>)object;
			for(String key : scopeProperties.keySet()) {
				String value = scopeProperties.get(key);
				value = expressionEvaluator.evaluate(value, globalProperties);
				// CHECK: scopeProperties.put(key, value);	// aktualizacja wartości po rozwinięciu
				globalProperties.put(key, value);
			}
			globalProperties.put(scope.name(), Collections.unmodifiableMap(scopeProperties));
			return;
		}

		// Definicja szabloun z pliku tempel.xml:
		if(object instanceof Template) {
			Template template = (Template)object;
			template.setScope(scope);

			if(template.getResources() != null) {
				for(TemplateResource resource : template.getResources()) {
					resource.setParentTemplateReference(template);
				}
			}

			String gId = Temp.StringUtils_emptyIfBlank(template.getGroupId());
			String tId = Temp.StringUtils_emptyIfBlank(template.getTemplateId());
			String ver = Temp.StringUtils_emptyIfBlank(template.getVersion());
			templateRepository.put(null, gId, tId, ver, template);

			String key = template.getKey();
			if(!Temp.StringUtils_isBlank(key)) {
				templateRepository.put(key, null, null, null, template);
			}
			return;
		}
	}
}
