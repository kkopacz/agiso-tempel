/* org.agiso.tempel.core.ITemplateRepository (29-10-2012)
 * 
 * ITemplateRepository.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.api.internal;

import org.agiso.tempel.core.model.Template;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public interface ITemplateRepository {
	/**
	 * @param scope
	 * @param repository
	 */
	public void setRepository(String scope, String repository);

	/**
	 * @param key
	 * @param groupId
	 * @param templateId
	 * @param version
	 * @param template
	 */
	public void put(String key, String groupId, String templateId, String version, Template template);

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
