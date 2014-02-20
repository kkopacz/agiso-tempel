/* org.agiso.tempel.support.file.provider.AppTemplateProviderElement (15-12-2012)
 * 
 * AppTemplateProviderElement.java
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
public class AppTemplateProviderElement extends BaseTemplateProviderElement {
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
		return 30;
	}

	@Override
	public String getScope() {
		return "GLOBAL";
	}

//	--------------------------------------------------------------------------
	@Override
	protected void doInitialize() throws IOException {
		String path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		int index = path.lastIndexOf("/lib/");
		// Inicjalizacja repozytoriów z zasobami dla poszczególnych poziomów:
		if(index != -1) {
			// Rzeczywiste środowisko uruchomieniowe (uruchomienie z linni komend):
			basePath = path.substring(0, index) + "/templates";
			settingsPath = path.substring(0, index) + "/conf/tempel.xml";
			librariesPath = path.substring(0, index) + "/templates/lib";
			repositoryPath = path.substring(0, index) + "/templates/repo";
		} else {
			// Deweloperskie środowisko uruchomieniowe (uruchomienie z eclipse'a):
			path = System.getProperty("user.dir");

			basePath = path + "/src/test/templates/app";
			settingsPath = path + "/src/test/templates/app/tempel.xml";
			librariesPath = path + "/src/test/templates/app/lib";
			repositoryPath = path + "/src/test/templates/app/repo";
		}


		initialized = readAppTemplates(templateRepository);
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
	 * @param xStream
	 * @param templateRepository
	 * @throws IOException
	 */
	private boolean readAppTemplates(final ITemplateRepository templateRepository) throws IOException {
		// Mapa szablonów globalnych (katalog konfiguracyjny aplikacji):
		File appSettingsFile = new File(settingsPath);
		if(appSettingsFile.exists()) try {
			tempelFileProcessor.process(appSettingsFile, new ITempelEntryProcessor() {
				@Override
				public void processObject(Object object) {
					AppTemplateProviderElement.this.processObject(object, null,
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

			if(logger.isDebugEnabled()) logger.debug("Application settings file {} processed successfully",
					ansiString(GREEN, appSettingsFile.getCanonicalPath())
			);

			return true;
		} catch(Exception e) {
			logger.error(e, "Error processing application settings file {}",
					ansiString(GREEN, appSettingsFile.getCanonicalPath())
			);
			throw new RuntimeException(e);
		}
		return false;
	}

	private String getTemplatePath(Template<?> template) {
		if(StringUtils.isEmpty(template.getGroupId())) {
//			throw new RuntimeException("Szablon GLOBAL bez groupId");
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
