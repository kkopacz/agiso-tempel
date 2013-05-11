/* org.agiso.tempel.support.file.provider.RunTemplateProviderElement (15-12-2012)
 * 
 * RunTemplateProviderElement.java
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
package org.agiso.tempel.support.file.provider;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.agiso.tempel.Temp;
import org.agiso.tempel.api.ITemplateRepository;
import org.agiso.tempel.api.ITemplateSource;
import org.agiso.tempel.api.ITemplateSourceFactory;
import org.agiso.tempel.api.impl.FileTemplateSource;
import org.agiso.tempel.api.internal.ITempelEntryProcessor;
import org.agiso.tempel.api.model.Template;
import org.agiso.tempel.support.base.provider.BaseTemplateProviderElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
@Component
public class RunTemplateProviderElement extends BaseTemplateProviderElement {
	private boolean initialized;
	private String settingsPath;
	private String repositoryPath;

	@Autowired
	private ITemplateRepository templateRepository;

//	--------------------------------------------------------------------------
	@Override
	public int getOrder() {
		return 10;
	}

	@Override
	public String getScope() {
		return "RUNTIME";
	}

//	--------------------------------------------------------------------------
	@Override
	protected void doInitialize() throws IOException {
		String path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		int index = path.lastIndexOf("/repo/");
		// Inicjalizacja repozytoriów z zasobami dla poszczególnych poziomów:
		if(index != -1) {
			// Rzeczywiste środowisko uruchomieniowe (uruchomienie z linni komend):
			settingsPath = System.getProperty("user.dir") + "/tempel.xml";
			repositoryPath = System.getProperty("user.dir") + "/.tempel";
		} else {
			// Deweloperskie środowisko uruchomieniowe (uruchomienie z eclipse'a):
			path = System.getProperty("user.dir");

			settingsPath = path + "/src/test/configuration/runtime/tempel.xml";
			repositoryPath = path + "/src/test/repository/runtime";
		}


		initialized = readRunTemplates(templateRepository);
	}

	@Override
	protected void doConfigure(Map<String, Object> properties) throws IOException {
		setActive(initialized);
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
	private boolean readRunTemplates(final ITemplateRepository templateRepository) throws IOException {
		// Mapa szablonów lokalnych (katalog bieżący projektu):
		File runSettingsFile = new File(settingsPath);
		if(runSettingsFile.exists()) try {
			tempelFileProcessor.process(runSettingsFile, new ITempelEntryProcessor() {
				@Override
				public void processObject(Object object) {
					RunTemplateProviderElement.this.processObject(object, templateRepository,
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

			return true;
		} catch(Exception e) {
			System.err.println("Błąd wczytywania ustawień lokalnych: " + e.getMessage());
			throw new RuntimeException(e);
		}
		return false;
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
