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

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.agiso.tempel.Temp;
import org.agiso.tempel.api.model.Template;
import org.apache.maven.settings.Settings;
import org.apache.maven.settings.building.DefaultSettingsBuildingRequest;
import org.apache.maven.settings.building.SettingsBuildingRequest;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolverSystem;
import org.jboss.shrinkwrap.resolver.impl.maven.bootstrap.MavenSettingsBuilder;
import org.springframework.stereotype.Component;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
@Component
public class ShrinkWrapMvnTemplateProviderElement extends AbstractMvnTemplateProviderElement {
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
	protected void doInitialize(Map<String, Object> properties) throws IOException {
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

			System.out.println("Using Maven settings file " + mavenSettingsFile.getCanonicalPath());
		} else {
			resolver = Maven.resolver();

			System.out.println("!!!! Maven settings file " + mavenSettingsFile.getCanonicalPath() + " not found !!!!");
		}

		settings = new MavenSettingsBuilder().buildSettings(request);

		System.out.println("Maven local repository: " + settings.getLocalRepository());

		setActive(true);
	}

//	--------------------------------------------------------------------------
	@Override
	protected List<File> resolve(String groupId, String templateId, String version) throws Exception {
		File[] files = resolver.resolve(
				groupId + ":" + templateId + ":" + version
		).withoutTransitivity().asFile();

		return Arrays.asList(files);
	}

	@Override
	protected String getTemplatePath(Template template) {
		if(Temp.StringUtils_isEmpty(template.getGroupId())) {
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
