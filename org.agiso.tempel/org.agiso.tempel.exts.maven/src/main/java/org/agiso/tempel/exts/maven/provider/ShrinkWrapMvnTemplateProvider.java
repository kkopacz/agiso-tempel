/* org.agiso.tempel.exts.maven.provider.ShrinkWrapMvnTemplateProvider (19-01-2013)
 * 
 * ShrinkWrapMvnTemplateProvider.java
 * 
 * Copyright 2013 agiso.org
 */
package org.agiso.tempel.exts.maven.provider;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.agiso.tempel.Temp;
import org.agiso.tempel.api.model.Template;
import org.apache.maven.settings.Settings;
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
public class ShrinkWrapMvnTemplateProvider extends AbstractMvnTemplateProvider {
	private final Settings settings;
	private final MavenResolverSystem resolver = Maven.resolver();

//	--------------------------------------------------------------------------
	/**
	 * 
	 */
	public ShrinkWrapMvnTemplateProvider() {
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

		String path = settings.getLocalRepository();
		path = path + '/' + template.getGroupId().replace('.', '/');
		path = path + '/' + template.getTemplateId();
		path = path + '/' + template.getVersion();
		path = path + '/' + template.getTemplateId() + '-' +  template.getVersion() + ".jar";
		return path;
	}
}
