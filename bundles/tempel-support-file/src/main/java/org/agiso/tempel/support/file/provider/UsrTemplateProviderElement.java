/* org.agiso.tempel.support.file.provider.UsrTemplateProviderElement (15-12-2012)
 * 
 * UsrTemplateProviderElement.java
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

import static org.agiso.core.lang.util.AnsiUtils.*;
import static org.agiso.core.lang.util.AnsiUtils.AnsiElement.*;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.agiso.core.lang.util.StringUtils;
import org.agiso.core.logging.Logger;
import org.agiso.core.logging.util.LogUtils;
import org.agiso.tempel.api.ITemplateRepository;
import org.agiso.tempel.api.ITemplateSource;
import org.agiso.tempel.api.ITemplateSourceFactory;
import org.agiso.tempel.api.impl.FileTemplateSource;
import org.agiso.tempel.api.impl.NullTemplateSource;
import org.agiso.tempel.api.internal.ITempelEntryProcessor;
import org.agiso.tempel.api.model.Template;
import org.agiso.tempel.support.base.provider.BaseTemplateProviderElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 * 
 * @author Karol Kopacz
 * @since 1.0
 */
@Component
public class UsrTemplateProviderElement extends BaseTemplateProviderElement {
	private static final Logger logger = LogUtils.getLogger(RunTemplateProviderElement.class);

//	--------------------------------------------------------------------------
	private boolean initialized;
	private String basePath;
	private String settingsPath;
	private String librariesPath;
	private String repositoryPath;

	@Autowired
	private ITemplateRepository templateRepository;

//	--------------------------------------------------------------------------
	@Override
	public int getOrder() {
		return 20;
	}

	@Override
	public String getScope() {
		return "USER";
	}

//	--------------------------------------------------------------------------
	@Override
	protected void doInitialize() throws IOException {
		String path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		int index = path.lastIndexOf("/lib/");
		// Inicjalizacja repozytoriów z zasobami dla poszczególnych poziomów:
		if(index != -1) {
			// Rzeczywiste środowisko uruchomieniowe (uruchomienie z linni komend):
			basePath = System.getProperty("user.home") + "/.tempel";
			settingsPath = System.getProperty("user.home") + "/.tempel/tempel.xml";
			librariesPath = System.getProperty("user.home") + "/.tempel/lib";
			repositoryPath = System.getProperty("user.home") + "/.tempel/repo";
		} else {
			// Deweloperskie środowisko uruchomieniowe (uruchomienie z eclipse'a):
			path = System.getProperty("user.dir");

			basePath = path + "/src/test/templates/usr";
			settingsPath = path + "/src/test/templates/usr/tempel.xml";
			librariesPath = path + "/src/test/templates/usr/lib";
			repositoryPath = path + "/src/test/templates/usr/repo";
		}


		initialized = readUsrTemplates(templateRepository);
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
	public Template<?> get(String key, String groupId, String templateId, String version) {
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
	private boolean readUsrTemplates(final ITemplateRepository templateRepository) throws IOException {
		// Mapa szablonów użytkownika (katalog domowy użytkownika):
		File usrSettingsFile = new File(settingsPath);
		if(usrSettingsFile.exists()) {
			try {
				tempelFileProcessor.process(usrSettingsFile, new ITempelEntryProcessor() {
					@Override
					public void processObject(Object object) {
						UsrTemplateProviderElement.this.processObject(object, null,
								templateRepository, new ITemplateSourceFactory() {
									@Override
									public ITemplateSource createTemplateSource(Template<?> template, String source) {
										String templatePath = getTemplatePath(template);
										if(templatePath == null) {
											return new NullTemplateSource();
										} else try {
											return new FileTemplateSource(templatePath, source);
										} catch(IOException e) {
											throw new RuntimeException(e);
										}
									}
								}
						);
					}
				});

				if(logger.isDebugEnabled()) logger.debug("User settings file {} processed successfully",
						ansiString(GREEN, usrSettingsFile.getCanonicalPath())
				);

				return true;
			} catch(Exception e) {
				logger.error(e, "Error processing user settings file {}",
						ansiString(GREEN, usrSettingsFile.getCanonicalPath())
				);
				throw new RuntimeException(e);
			}
		} else {
			// TODO: Tworzenie pustego repozytorium użytkownika

			return false;
		}
	}

	@Override
	protected String getBasePath() {
		return basePath;
	}

	private String getTemplatePath(Template<?> template) {
		if(StringUtils.isEmpty(template.getGroupId())) {	// dopuszczone w repozytorium USER
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

	@Override
	protected Set<String> getRepositoryClassPath() {
		File[] libraries = new File(librariesPath).listFiles();
		if(libraries != null && libraries.length > 0) {
			Set<String> classPath = new LinkedHashSet<String>(libraries.length);
			for(File library : libraries) try {
				classPath.add(library.getCanonicalPath());
			} catch(Exception e) {
				throw new RuntimeException(e);
			}
			return classPath;
		}

		return Collections.emptySet();
	}
}
