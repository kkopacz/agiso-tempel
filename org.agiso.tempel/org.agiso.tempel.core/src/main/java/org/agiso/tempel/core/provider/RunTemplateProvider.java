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
import org.agiso.tempel.api.ITemplateRepository;
import org.agiso.tempel.api.ITemplateSource;
import org.agiso.tempel.api.ITemplateSourceFactory;
import org.agiso.tempel.api.internal.ITempelEntryProcessor;
import org.agiso.tempel.api.internal.ITemplateProviderElement;
import org.agiso.tempel.api.model.Template;
import org.agiso.tempel.exts.base.provider.source.FileTemplateSource;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class RunTemplateProvider extends BaseTemplateProvider implements ITemplateProviderElement {
	private String settingsPath;
	private String repositoryPath;

	// FIXME: Zastosować wstrzykiwanie zależności
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


		String path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		int index = path.lastIndexOf("/repo/");
		// Inicjalizacja repozytoriów z zasobami dla poszczególnych poziomów:
		if(index != -1) {
			// Rzeczywiste środowisko uruchomieniowe (uruchomienie z linni komend):
			settingsPath = System.getProperty("user.dir") + "/tempel.xml";
			repositoryPath = System.getProperty("user.dir") + "/.tempel";
		} else {
			// Deweloperskie środowisko uruchomieniowe (uruchomienie z eclipse'a):
			path = System.getProperty("user.dir");					// FIXME: Rodzielić katalogi repozytoriów

			settingsPath = path + "/src/test/configuration/runtime/tempel.xml";
			repositoryPath = path + "/src/test/resources/repository/runtime";
		}


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
	 * Odczytuje szablony repozytorium uruchomienowego (katalogu, w którym zostało
	 * wywołane polecenie tpl) opisane w pliku template.xml. Jeśli pliku nie ma
	 * w bieżącym katalogu, repozytorium uruchomieniowe jest puste.
	 * 
	 * @param templateRepository
	 * @throws IOException
	 */
	private void readRunTemplates(final ITemplateRepository templateRepository) throws IOException {
		// Mapa szablonów lokalnych (katalog bieżący projektu):
		File runSettingsFile = new File(settingsPath);
		if(runSettingsFile.exists()) try {
			tempelFileProcessor.process(runSettingsFile, new ITempelEntryProcessor() {
				@Override
				public void processObject(Object object) {
					RunTemplateProvider.this.processObject("RUNTIME", object, templateRepository,
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

		String path = repositoryPath;
		path = path + '/' + template.getGroupId().replace('.', '/');
		path = path + '/' + template.getTemplateId();
		path = path + '/' + template.getVersion();
		return path;
	}
}
