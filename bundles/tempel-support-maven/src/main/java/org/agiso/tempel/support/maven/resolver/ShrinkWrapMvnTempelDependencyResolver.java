/* org.agiso.tempel.support.maven.resolver.ShrinkWrapMvnTempelDependencyResolver (20-02-2014)
 * 
 * ShrinkWrapMvnTempelDependencyResolver.java
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
package org.agiso.tempel.support.maven.resolver;

import static org.agiso.core.lang.util.AnsiUtils.*;
import static org.agiso.core.lang.util.AnsiUtils.AnsiElement.*;
import static org.agiso.tempel.ITempel.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.agiso.core.i18n.annotation.I18n;
import org.agiso.core.i18n.util.I18nUtils.I18nId;
import org.agiso.core.lang.util.ObjectUtils;
import org.agiso.core.logging.I18nLogger;
import org.agiso.core.logging.util.LogUtils;
import org.agiso.tempel.api.internal.ITempelDependencyResolver;
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
public class ShrinkWrapMvnTempelDependencyResolver implements ITempelDependencyResolver {
	private static final I18nLogger<Logs> supportLogger = LogUtils.getLogger(LOGGER_SUPPORT);
	private static enum Logs implements I18nId {
		@I18n(def = "Using maven settings file {0}")
		LOG_01,

		@I18n(def = "Maven settings file {0} not found")
		LOG_02,

		@I18n(def = "Building Maven setting for request {0}")
		LOG_03,

		@I18n(def = "Using Maven settings {0}")
		LOG_04,

		@I18n(def = "Using local Maven repository {0}")
		LOG_05,

		@I18n(def = "Template {0} resolved as Maven archive {1}")
		LOG_06,

		@I18n(def = "Template {0} resolved as Maven archive {1} with dependencies {2}")
		LOG_07,
	}

	/** Nazwa zmiennej przechowującej ścieżkę do ustawień Maven'a */
	private static final String MAVEN_SETTINS_PATH_PROPERTY = "maven_settings";

	/** Domyślna lokalizacja ustawień Maven'a poziomu użytkownika */
	private static final String DEFAULT_MAVEN_SETTINGS_PATH = System.getProperty("user.home")
			.concat("/.m2/settings.xml");

//	--------------------------------------------------------------------------
	private Settings settings;
	private MavenResolverSystem resolver;

//	--------------------------------------------------------------------------
	public void doConfigure(Map<String, Object> properties) throws IOException {
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

			if(supportLogger.isDebugEnabled()) supportLogger.debug(Logs.LOG_01,
					ansiString(GREEN, mavenSettingsFile.getCanonicalPath())
			);
		} else {
			resolver = Maven.resolver();

			if(supportLogger.isWarnEnabled()) supportLogger.warn(Logs.LOG_02,
					ansiString(GREEN, mavenSettingsFile.getCanonicalPath())
			);
		}

		if(supportLogger.isTraceEnabled()) supportLogger.trace(Logs.LOG_03,
				ansiString(GREEN, ObjectUtils.toStringBuilder(request))
		);

		settings = new MavenSettingsBuilder().buildSettings(request);

		if(supportLogger.isTraceEnabled()) {
			supportLogger.trace(Logs.LOG_04,
					ansiString(GREEN, ObjectUtils.toStringBuilder(settings))
			);
		} else if(supportLogger.isDebugEnabled()) {
			supportLogger.trace(Logs.LOG_05,
					ansiString(GREEN, settings.getLocalRepository())
			);
		}

//		setActive(true);
	}

	public String getLocalRepositoryPath() {
		return settings.getLocalRepository();
	}

	@Override
	public List<File> resolve(String groupId, String artifactId, String version) {
		List<File> files = new ArrayList<File>();

		MavenResolvedArtifact artifact = resolver.resolve(
				groupId + ":" + artifactId + ":" + version
		).withoutTransitivity().asSingle(MavenResolvedArtifact.class);
		files.add(artifact.asFile());

		StringBuilder deps = null;
		if(supportLogger.isTraceEnabled()) {
			deps = new StringBuilder();
		} else if(supportLogger.isDebugEnabled()) supportLogger.debug(Logs.LOG_06,
				ansiString(GREEN, groupId + ":" + artifactId + ":" + version),
				ansiString(GREEN, files.get(0).getAbsolutePath())
		);

		MavenArtifactInfo[] dependencies = artifact.getDependencies();
		for(MavenArtifactInfo dependency : dependencies) {
// TODO:			dependency.getScope();
			File[] jars = resolver.resolve(
					dependency.getCoordinate().toCanonicalForm()
			).withTransitivity().asFile();
			for(File jar : jars) {
				files.add(jar);
				if(supportLogger.isTraceEnabled()) {
					if(deps.length() > 0) {
						deps.append(", ");
					}
					deps.append(jar.getAbsolutePath());
				}
			}
		}

		if(supportLogger.isTraceEnabled()) supportLogger.trace(Logs.LOG_07,
				ansiString(GREEN, groupId + ":" + artifactId + ":" + version),
				ansiString(GREEN, files.get(0).getAbsolutePath()),
				ansiString(GREEN, deps.toString())
		);

		return files;
	}
}
