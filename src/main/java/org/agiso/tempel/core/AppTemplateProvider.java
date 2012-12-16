/* org.agiso.tempel.core.AppTemplateProvider (15-12-2012)
 * 
 * AppTemplateProvider.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.core;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.agiso.tempel.core.model.Template;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class AppTemplateProvider extends BaseTemplateProvider {
	private ITemplateRepository templateRepository = new HashBasedTemplateRepository();

//	--------------------------------------------------------------------------
	@Override
	public void initialize(Map<String, Object> globalProperties) throws IOException {
		super.initialize(globalProperties);

		readAppTemplates(templateRepository);
	}

//	--------------------------------------------------------------------------
	@Override
	public boolean contains(String key, String groupId, String templateId, String version) {
		return templateRepository.contains(key, groupId, templateId, version);
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
			tempelFileProcessor.process(appSettingsFile, new ITempelEntryProcessor() {
				@Override
				public void processObject(Object object) {
					AppTemplateProvider.this.processObject(Template.Scope.GLOBAL, object, templateRepository);
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
}
