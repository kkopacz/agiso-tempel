/* org.agiso.tempel.core.model.beans.RepositoryBean (03-11-2012)
 * 
 * RepositoryBean.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.core.model.beans;

import org.agiso.tempel.core.model.Repository;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
@XStreamAlias("repository")
public class RepositoryBean implements Repository {
	private String value;

//	--------------------------------------------------------------------------
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

//	--------------------------------------------------------------------------
	@Override
	public RepositoryBean clone() {
		return fillClone(new RepositoryBean());
	}
	protected RepositoryBean fillClone(RepositoryBean clone) {
		clone.value = value;

		return clone;
	}
}
