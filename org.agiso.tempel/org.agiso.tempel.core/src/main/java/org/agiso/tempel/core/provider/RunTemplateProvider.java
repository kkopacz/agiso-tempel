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

import org.agiso.tempel.Temp;
import org.agiso.tempel.api.ITemplateSource;
import org.agiso.tempel.api.internal.ITempelScopeInfo;
import org.agiso.tempel.api.internal.ITempelEntryProcessor;
import org.agiso.tempel.api.internal.ITemplateProviderElement;
import org.agiso.tempel.api.internal.ITemplateRepository;
import org.agiso.tempel.core.TempelScopeInfo;
import org.agiso.tempel.core.model.ITemplateSourceFactory;
import org.agiso.tempel.core.model.Repository;
import org.agiso.tempel.core.model.Template;
import org.agiso.tempel.core.model.Template.Scope;
import org.agiso.tempel.core.provider.source.FileTemplateSource;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class RunTemplateProvider extends BaseTemplateProvider implements ITemplateProviderElement {
	// FIXME: Zastosować wstrzykiwanie zależności
	private ITempelScopeInfo tempelScopeInfo = new TempelScopeInfo();

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
		String runSettings = tempelScopeInfo.getSettingsPath(Scope.RUNTIME);
		File runSettingsFile = new File(runSettings);

		try {
			tempelFileProcessor.process(runSettingsFile, new ITempelEntryProcessor() {
				@Override
				public void processObject(Object object) {
					RunTemplateProvider.this.processObject(Template.Scope.RUNTIME, object, templateRepository,
							new ITemplateSourceFactory() {
								@Override
								public ITemplateSource createTemplateSource(Template template, String source) {
									try {
										return new FileTemplateSource(getTemplatePath(template), source);
									} catch(IOException e) {
										throw new RuntimeException(e);
									}
								}
							}
					);
				}
			});
			System.out.println("Wczytano ustawienia lokalne z pliku " + runSettingsFile.getCanonicalPath());
		} catch(Exception e) {
			System.err.println("Błąd wczytywania ustawień lokalnych: " + e.getMessage());
			throw new RuntimeException(e);
		}
	}

	private String getTemplatePath(Template template) {
		if(Temp.StringUtils_isEmpty(template.getGroupId())) {	// dopuszczone w repozytorium RUNTIME
			// Szablony bez określonej grupy, identyfikatora i wersji mogą generować zasoby
			// o ile nie wymagają do tego celu żadnych plików źródłowych (np. szablonów velocity).
			// Nie mają one bowiem określonej ścieżki w repozytorium.
			// Przykładem tego typu szablonów są szablony tworzące katalogi.

			return null;
		}

		Repository repository = template.getRepository();
		if(repository != null) {
			if(!repository.getValue().equals(tempelScopeInfo.getRepositoryPath(Scope.RUNTIME))) {
				throw new RuntimeException("Zmodyfikowane repozytorium szablonu!");
			}
		}

		String path = tempelScopeInfo.getRepositoryPath(Scope.RUNTIME);
		path = path + '/' + template.getGroupId().replace('.', '/');
		path = path + '/' + template.getTemplateId();
		path = path + '/' + template.getVersion();
		return path;
	}
}
