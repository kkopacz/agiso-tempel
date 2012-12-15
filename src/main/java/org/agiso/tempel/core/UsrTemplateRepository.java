/* org.agiso.tempel.core.UsrTemplateRepository (15-12-2012)
 * 
 * UsrTemplateRepository.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.core;

import org.agiso.tempel.core.model.Repository;
import org.agiso.tempel.core.model.Template;
import org.agiso.tempel.core.model.Template.Scope;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class UsrTemplateRepository implements ITemplateRepository {

	/* (non-Javadoc)
	 * @see org.agiso.tempel.core.ITemplateRepository#setRepository(org.agiso.tempel.core.model.Template.Scope, org.agiso.tempel.core.model.Repository)
	 */
	@Override
	public void setRepository(Scope scope, Repository repository) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.agiso.tempel.core.ITemplateRepository#put(java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.agiso.tempel.core.model.Template)
	 */
	@Override
	public void put(String key, String groupId, String templateId,
			String version, Template template) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.agiso.tempel.core.ITemplateRepository#get(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Template get(String key, String groupId, String templateId,
			String version) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.agiso.tempel.core.ITemplateRepository#contains(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean contains(String key, String groupId, String templateId,
			String version) {
		// TODO Auto-generated method stub
		return false;
	}
}
