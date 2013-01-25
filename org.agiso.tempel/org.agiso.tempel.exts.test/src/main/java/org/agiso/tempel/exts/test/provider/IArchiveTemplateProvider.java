/* org.agiso.tempel.exts.test.provider.IArchiveTemplateProvider (25-01-2013)
 * 
 * IArchiveTemplateProvider.java
 * 
 * Copyright 2013 agiso.org
 */
package org.agiso.tempel.exts.test.provider;

import org.agiso.tempel.api.internal.ITemplateProvider;
import org.jboss.shrinkwrap.api.Archive;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public interface IArchiveTemplateProvider extends ITemplateProvider {
	public void addArchive(String groupId, String templateId, String version, Archive<?> archive);
}
