/* org.agiso.tempel.core.provider.AppTemplateProvider (15-12-2012)
 * 
 * AppTemplateProvider.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.core.provider;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.agiso.tempel.Temp;
import org.agiso.tempel.api.internal.ITempelEntryProcessor;
import org.agiso.tempel.api.internal.ITemplateRepository;
import org.agiso.tempel.core.model.Repository;
import org.agiso.tempel.core.model.Template;
import org.agiso.tempel.core.model.Template.Scope;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class AppTemplateProvider extends BaseTemplateProvider {
	private ITemplateRepository templateRepository = new HashBasedTemplateRepository();

//	--------------------------------------------------------------------------
	@Override
	public int getOrder() {
		return 30;
	}

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
		String appSettings = tempelScopeInfo.getSettingsPath(Scope.GLOBAL);
		File appSettingsFile = new File(appSettings);

		try {
			tempelFileProcessor.process(appSettingsFile, new ITempelEntryProcessor() {
				@Override
				public void processObject(Object object) {
					AppTemplateProvider.this.processObject(Scope.GLOBAL, object, templateRepository);
				}
			});
			System.out.println("Wczytano ustawienia globalne z pliku " + appSettingsFile.getCanonicalPath());
		} catch(Exception e) {
			System.err.println("Błąd wczytywania ustawień globalnych: " + e.getMessage());
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void setupTemplatePath(Template template) {
		if(Temp.StringUtils_isEmpty(template.getGroupId())) {
			throw new RuntimeException("Szablon GLOBAL bez groupId");
		}

		Repository repository = template.getRepository();
		if(repository != null) {
			if(!repository.getValue().equals(tempelScopeInfo.getRepositoryPath(Scope.GLOBAL))) {
				throw new RuntimeException("Zmodyfikowane repozytorium szablonu!");
			}
		}

		String path = tempelScopeInfo.getRepositoryPath(Scope.GLOBAL);
		path = path + '/' + template.getGroupId().replace('.', '/');
		path = path + '/' + template.getTemplateId();
		path = path + '/' + template.getVersion();
		template.setPath(path);
	}
}
