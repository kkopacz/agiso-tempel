/* org.agiso.tempel.core.MvnTemplateProvider (15-12-2012)
 * 
 * MvnTemplateProvider.java
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
public class MvnTemplateProvider implements ITemplateProvider {
	@Override
	public void initialize(Map<String, Object> properties) throws IOException {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean contains(String key, String groupId, String templateId,String version) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Template get(String key, String groupId, String templateId, String version) {
		// TODO Auto-generated method stub
		return null;
	}
}
