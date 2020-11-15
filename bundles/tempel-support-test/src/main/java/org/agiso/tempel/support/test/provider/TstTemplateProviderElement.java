/* org.agiso.tempel.support.file.provider.RunTemplateProviderElement (26-01-2014)
 * 
 * TstTemplateProviderElement.java
 * 
 * Copyright 2014 agiso.org
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
package org.agiso.tempel.support.test.provider;

import static org.agiso.core.lang.util.AnsiUtils.*;
import static org.agiso.core.lang.util.AnsiUtils.AnsiElement.*;
import static org.agiso.tempel.ITempel.*;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.agiso.core.i18n.annotation.I18n;
import org.agiso.core.i18n.util.I18nUtils.I18nId;
import org.agiso.core.lang.util.StringUtils;
import org.agiso.core.logging.I18nLogger;
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
public class TstTemplateProviderElement extends BaseTemplateProviderElement {
	private static final I18nLogger<Logs> supportLogger = LogUtils.getLogger(LOGGER_SUPPORT);
	private static enum Logs implements I18nId {
		@I18n(def = "Test settings file {0} processed successfully")
		LOG_01,

		@I18n(def = "Error processing test settings file {0}")
		LOG_02,
	}

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
		return -10;
	}

	@Override
	public String getScope() {
		return "TST";
	}

//	--------------------------------------------------------------------------
	@Override
	protected void doInitialize() throws IOException {
		String path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		int index = path.lastIndexOf("/lib/");
		// Inicjalizacja repozytoriów z zasobami dla poszczególnych poziomów:
		if(index != -1) {
			throw new IllegalStateException("TstTemplateProvider can be used only in test environment");
		} else {
			// Deweloperskie środowisko uruchomieniowe (uruchomienie z eclipse'a):
			path = System.getProperty("user.dir");

			basePath = path + "/src/test/templates/tst";
			settingsPath = path + "/src/test/templates/tst/tempel.xml";
			librariesPath = path + "/src/test/templates/tst/lib";
			repositoryPath = path + "/src/test/templates/tst/repo";
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
	public Template<?> get(String key, String groupId, String templateId, String version) {
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
		File tstSettingsFile = new File(settingsPath);
		if(tstSettingsFile.exists()) try {
			tempelFileProcessor.process(tstSettingsFile, new ITempelEntryProcessor() {
				@Override
				public void processObject(Object object) {
					TstTemplateProviderElement.this.processObject(object, null,
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

			if(supportLogger.isDebugEnabled()) supportLogger.debug(Logs.LOG_01,
					ansiString(GREEN, tstSettingsFile.getCanonicalPath())
			);

			return true;
		} catch(Exception e) {
			supportLogger.error(e, Logs.LOG_02,
					ansiString(GREEN, tstSettingsFile.getCanonicalPath())
			);
			throw new RuntimeException(e);
		}
		return false;
	}

	private String getTemplatePath(Template<?> template) {
		if(StringUtils.isEmpty(template.getGroupId())) {
			return null;
		}

		String path = repositoryPath;
		path = path + '/' + template.getGroupId().replace('.', '/');
		path = path + '/' + template.getTemplateId();
		path = path + '/' + template.getVersion();
		return path;
	}

	@Override
	protected String getBasePath() {
		return basePath;
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
