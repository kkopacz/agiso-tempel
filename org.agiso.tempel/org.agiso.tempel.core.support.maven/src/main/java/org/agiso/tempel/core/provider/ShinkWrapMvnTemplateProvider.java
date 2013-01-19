/* org.agiso.tempel.core.provider.ShinkWrapMvnTemplateProvider (19-01-2013)
 * 
 * ShinkWrapMvnTemplateProvider.java
 * 
 * Copyright 2013 agiso.org
 */
package org.agiso.tempel.core.provider;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolverSystem;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class ShinkWrapMvnTemplateProvider extends AetherMvnTemplateProvider {

//	--------------------------------------------------------------------------
	@Override
	protected List<File> resolve(String groupId, String templateId, String version) throws Exception {
		MavenResolverSystem resolver = Maven.resolver();

		File[] files = resolver.resolve(
				groupId + ":" + templateId + ":" + version
		).withTransitivity().asFile();

		return Arrays.asList(files);
	}
}
