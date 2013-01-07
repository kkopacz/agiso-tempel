/* org.agiso.tempel.core.provider.RunTemplateProvider (15-12-2012)
 * 
 * RunTemplateProvider.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.core.provider;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.agiso.tempel.api.internal.ITempelEntryProcessor;
import org.agiso.tempel.api.internal.ITemplateRepository;
import org.agiso.tempel.core.model.Template;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class RunTemplateProvider extends BaseTemplateProvider {
	private ITemplateRepository templateRepository = new HashBasedTemplateRepository();

//	--------------------------------------------------------------------------
	@Override
	public int getOrder() {
		return 10;
	}

//	--------------------------------------------------------------------------
	@Override
	public void initialize(Map<String, Object> globalProperties) throws IOException {
		super.initialize(globalProperties);

		readRunTemplates(templateRepository);
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
	private void readRunTemplates(final ITemplateRepository templateRepository) throws IOException {
		// Mapa szablonów lokalnych (katalog bieżący projektu):
		String runSettings = getRunSettingsPath();
		File runSettingsFile = new File(runSettings);

		try {
			tempelFileProcessor.process(runSettingsFile, new ITempelEntryProcessor() {
				@Override
				public void processObject(Object object) {
					RunTemplateProvider.this.processObject(Template.Scope.RUNTIME, object, templateRepository);
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
			path = System.getProperty("user.dir");
			path = path + "/src/test/configuration/runtime/tempel.xml";
		}

		return path;
	}
}
