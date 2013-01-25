/* org.agiso.tempel.support.file.provider.UsrTemplateProvider (15-12-2012)
 * 
 * UsrTemplateProvider.java
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
import org.agiso.tempel.api.internal.ITemplateProviderElement;
import org.agiso.tempel.api.model.Template;
import org.agiso.tempel.support.base.provider.BaseTemplateProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
@Component
public class UsrTemplateProvider extends BaseTemplateProvider implements ITemplateProviderElement {
	private String settingsPath;
	private String repositoryPath;

	@Autowired
	private ITemplateRepository templateRepository;

//	--------------------------------------------------------------------------
	@Override
	public int getOrder() {
		return 20;
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
			settingsPath = System.getProperty("user.home") + "/.tempel/tempel.xml";
			repositoryPath = System.getProperty("user.home") + "/.tempel/repository";
		} else {
			// Deweloperskie środowisko uruchomieniowe (uruchomienie z eclipse'a):
			path = System.getProperty("user.dir");

			settingsPath = path + "/src/test/configuration/home/tempel.xml";
			repositoryPath = path + "/src/test/repository/home";
		}


		readUsrTemplates(templateRepository);
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
	 * Odczytuje szablony repozytorium użytkownika (katalogu .tempel w lokalizacji
	 * domowej użytkownika) opisane w pliku .tempel/template.xml. Jeśli pliku nie
	 * ma zakłada odpowiednie struktury w katalogu domowym.
	 * 
	 * @param templateRepository
	 * @throws IOException
	 */
	private void readUsrTemplates(final ITemplateRepository templateRepository) throws IOException {
		// Mapa szablonów użytkownika (katalog domowy użytkownika):
		File usrSettingsFile = new File(settingsPath);
		if(!usrSettingsFile.exists()) {
			// TODO Tworzenie pustego repozytorium użytkownika:
			
		}

		try {
			tempelFileProcessor.process(usrSettingsFile, new ITempelEntryProcessor() {
				@Override
				public void processObject(Object object) {
					UsrTemplateProvider.this.processObject("USER", object, templateRepository,
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
			System.out.println("Wczytano ustawienia użytkownika z pliku " + usrSettingsFile.getCanonicalPath());
		} catch(Exception e) {
			System.err.println("Błąd wczytywania ustawień użytkownika: " + e.getMessage());
			throw new RuntimeException(e);
		}
	}

	private String getTemplatePath(Template template) {
		if(Temp.StringUtils_isEmpty(template.getGroupId())) {	// dopuszczone w repozytorium USER
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
