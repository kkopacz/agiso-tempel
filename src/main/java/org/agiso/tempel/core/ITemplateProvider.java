/* org.agiso.tempel.core.ITemplateProvider (02-10-2012)
 * 
 * ITemplateProvider.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.core;

import java.io.IOException;
import java.util.Map;

import org.agiso.tempel.core.model.Template;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public interface ITemplateProvider {
	public void initialize(Map<String, Object> properties) throws IOException;

	/**
	 * @param key
	 * @param groupId
	 * @param templateId
	 * @param version
	 * @return
	 */
	public boolean contains(String key, String groupId, String templateId, String version);

	/**
	 * @param key
	 * @param groupId
	 * @param templateId
	 * @param version
	 * @return
	 */
	public Template get(String key, String groupId, String templateId, String version);
}
