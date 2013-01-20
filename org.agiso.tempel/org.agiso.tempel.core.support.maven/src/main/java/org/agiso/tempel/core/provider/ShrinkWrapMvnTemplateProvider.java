/* org.agiso.tempel.core.provider.ShrinkWrapMvnTemplateProvider (19-01-2013)
 * 
 * ShrinkWrapMvnTemplateProvider.java
 * 
 * Copyright 2013 agiso.org
 */
package org.agiso.tempel.core.provider;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.agiso.tempel.Temp;
import org.agiso.tempel.api.internal.ITempelScopeInfo;
import org.agiso.tempel.core.TempelScopeInfo;
import org.agiso.tempel.core.model.Template;
import org.agiso.tempel.core.model.Template.Scope;
import org.apache.maven.settings.Server;
import org.apache.maven.settings.Settings;
import org.apache.maven.settings.building.DefaultSettingsBuildingRequest;
import org.apache.maven.settings.building.SettingsBuildingRequest;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolverSystem;
import org.jboss.shrinkwrap.resolver.impl.maven.bootstrap.MavenSettingsBuilder;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class ShrinkWrapMvnTemplateProvider extends AbstractMvnTemplateProvider {
	// FIXME: Zastosować wstrzykiwanie zależności
	// private ITempelScopeInfo tempelScopeInfo = new TempelScopeInfo();

	private Settings settings;
	private MavenResolverSystem resolver = Maven.resolver();

//	--------------------------------------------------------------------------
	/**
	 * 
	 */
	public ShrinkWrapMvnTemplateProvider() {
//		String userSettingsPaht = System.getProperty("user.home").concat("/.m2/settings.xml");
//		String globalSettingsPath = "/work/tools/maven/maven/conf/settings.xml";
//		// String localRepositoryPath = tempelScopeInfo.getSettingsPath(Scope.MAVEN);
//
//		SettingsBuildingRequest request = new DefaultSettingsBuildingRequest();
//		request.setUserSettingsFile(new File(userSettingsPaht));
//		request.setGlobalSettingsFile(new File(globalSettingsPath));
//
//		settings = new MavenSettingsBuilder().buildSettings(request);
//		// settings.setLocalRepository(localRepositoryPath);

		settings = new MavenSettingsBuilder().buildDefaultSettings();
	}

//	--------------------------------------------------------------------------
	@Override
	protected List<File> resolve(String groupId, String templateId, String version) throws Exception {

		File[] files = resolver.resolve(
				groupId + ":" + templateId + ":" + version
		).withTransitivity().asFile();

		return Arrays.asList(files);
	}

	@Override
	protected String getTemplatePath(Template template) {
		if(Temp.StringUtils_isEmpty(template.getGroupId())) {
			throw new RuntimeException("Szablon SWRAP bez groupId");
		}

//		Repository repository = template.getRepository();
//		if(repository != null) {
//			if(!repository.getValue().equals(tempelScopeInfo.getRepositoryPath(Scope.MAVEN))) {
//				throw new RuntimeException("Zmodyfikowane repozytorium szablonu!");
//			}
//		}

		String path = settings.getLocalRepository();
		path = path + '/' + template.getGroupId().replace('.', '/');
		path = path + '/' + template.getTemplateId();
		path = path + '/' + template.getVersion();
		path = path + '/' + template.getTemplateId() + '-' +  template.getVersion() + ".jar";
		return path;
	}
}
