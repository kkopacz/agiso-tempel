/* org.agiso.tempel.support.maven.provider.ShrinkWrapMvnTemplateProviderElement (19-01-2013)
 * 
 * ShrinkWrapMvnTemplateProviderElement.java
 * 
 * Copyright 2013 agiso.org
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
package org.agiso.tempel.support.maven.provider;

import static org.agiso.core.lang.util.AnsiUtils.*;
import static org.agiso.core.lang.util.AnsiUtils.AnsiElement.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.agiso.core.lang.util.ObjectUtils;
import org.agiso.core.lang.util.StringUtils;
import org.agiso.core.logging.Logger;
import org.agiso.core.logging.util.LogUtils;
import org.agiso.tempel.api.model.Template;
import org.apache.maven.settings.Settings;
import org.apache.maven.settings.building.DefaultSettingsBuildingRequest;
import org.apache.maven.settings.building.SettingsBuildingRequest;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.MavenArtifactInfo;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolvedArtifact;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolverSystem;
import org.jboss.shrinkwrap.resolver.impl.maven.bootstrap.MavenSettingsBuilder;
import org.springframework.stereotype.Component;

/**
 * 
 * 
 * @author Karol Kopacz
 * @since 1.0
 */
@Component
public class ShrinkWrapMvnTemplateProviderElement extends AbstractMvnTemplateProviderElement {
	private static final Logger logger = LogUtils.getLogger(ShrinkWrapMvnTemplateProviderElement.class);

	/** Nazwa zmiennej przechowującej ścieżkę do ustawień Maven'a */
	private static final String MAVEN_SETTINS_PATH_PROPERTY = "maven_settings";

	/** Domyślna lokalizacja ustawień Maven'a poziomu użytkownika */
	private static final String DEFAULT_MAVEN_SETTINGS_PATH = System.getProperty("user.home")
			.concat("/.m2/settings.xml");

//	--------------------------------------------------------------------------
	private Settings settings;
	private MavenResolverSystem resolver;

//	--------------------------------------------------------------------------
	@Override
	protected void doInitialize() throws IOException {
	}

	@Override
	protected void doConfigure(Map<String, Object> properties) throws IOException {
		SettingsBuildingRequest request = new DefaultSettingsBuildingRequest();

		File mavenSettingsFile = null;
		if(properties.containsKey(MAVEN_SETTINS_PATH_PROPERTY)) {
			mavenSettingsFile = new File(
					properties.get(MAVEN_SETTINS_PATH_PROPERTY).toString()
			);
		} else {
			mavenSettingsFile = new File(DEFAULT_MAVEN_SETTINGS_PATH);
		}
		if(mavenSettingsFile.exists()) {
			request.setUserSettingsFile(mavenSettingsFile);
			resolver = Maven.configureResolver().fromFile(mavenSettingsFile);

			if(logger.isDebugEnabled()) logger.debug("Using maven settings file {}",
					ansiString(GREEN, mavenSettingsFile.getCanonicalPath())
			);
		} else {
			resolver = Maven.resolver();

			if(logger.isWarnEnabled()) logger.warn("Maven settings file {} not found",
					ansiString(GREEN, mavenSettingsFile.getCanonicalPath())
			);
		}

		if(logger.isTraceEnabled()) logger.trace("Building Maven setting for request {}",
				ansiString(GREEN, ObjectUtils.toStringBuilder(request))
		);

		settings = new MavenSettingsBuilder().buildSettings(request);

		if(logger.isTraceEnabled()) {
			logger.trace("Using Maven settings {}",
					ansiString(GREEN, ObjectUtils.toStringBuilder(settings))
			);
		} else if(logger.isDebugEnabled()) {
			logger.trace("Using local Maven repository {}",
					ansiString(GREEN, settings.getLocalRepository())
			);
		}

		setActive(true);
	}

//	--------------------------------------------------------------------------
	@Override
	protected List<File> resolve(String fqtn) throws Exception {
		if(logger.isDebugEnabled()) logger.debug("Resolving Maven artifact for template {}",
				ansiString(GREEN, fqtn)
		);

		StringTokenizer tokenizer = new StringTokenizer(fqtn, ":", false);
		String groupId = tokenizer.nextToken();
		String templateId = tokenizer.nextToken();
		String version = tokenizer.nextToken();

		List<File> files = new ArrayList<File>();

		MavenResolvedArtifact artifact = resolver.resolve(
				groupId + ":" + templateId + ":" + version
		).withoutTransitivity().asSingle(MavenResolvedArtifact.class);
		files.add(artifact.asFile());

		StringBuilder deps = null;
		if(logger.isTraceEnabled()) {
			deps = new StringBuilder();
		} else if(logger.isDebugEnabled()) logger.debug(
				"Template {} resolved as Maven archive {}",
				ansiString(GREEN, fqtn),
				ansiString(GREEN, files.get(0).getCanonicalPath())
		);

		MavenArtifactInfo[] dependencies = artifact.getDependencies();
		for(MavenArtifactInfo dependency : dependencies) {
// TODO:			dependency.getScope();
			File[] jars = resolver.resolve(
					dependency.getCoordinate().toCanonicalForm()
			).withTransitivity().asFile();
			for(File jar : jars) {
				files.add(jar);
				if(logger.isTraceEnabled()) {
					if(deps.length() > 0) {
						deps.append(", ");
					}
					deps.append(jar.getCanonicalPath());
				}
			}
		}

		if(logger.isTraceEnabled()) logger.trace(
				"Template {} resolved as Maven archive {} with dependencies {}",
				ansiString(GREEN, fqtn),
				ansiString(GREEN, files.get(0).getCanonicalPath()),
				ansiString(GREEN, deps.toString())
		);

		return files;
	}

	@Override
	protected Set<String> getRepositoryClassPath() {
		return Collections.emptySet();
	}

	@Override
	protected String getTemplatePath(Template<?> template) {
		if(StringUtils.isEmpty(template.getGroupId())) {
			throw new RuntimeException("Szablon SWRAP bez groupId");
		}

		String path = settings.getLocalRepository();
		path = path + '/' + template.getGroupId().replace('.', '/');
		path = path + '/' + template.getTemplateId();
		path = path + '/' + template.getVersion();
		path = path + '/' + template.getTemplateId() + '-' +  template.getVersion() + ".jar";
		return path;
	}
}
