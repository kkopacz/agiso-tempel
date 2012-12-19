/* org.agiso.tempel.core.JarTemplateSource (19-12-2012)
 * 
 * JarTemplateSource.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.core;

import java.io.File;


/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class JarTemplateSource implements ITemplateSource {
	private String repository;
	private String template;
	private String resource;

	public JarTemplateSource(String repository, String template, String resource) {
		this.repository = repository;
		this.template = template;
		this.resource = resource;
	}

	@Override
	public boolean exists() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getRepository() {
		return repository;
	}

	@Override
	public String getTemplate() {
		return template;
	}

	@Override
	public String getResource() {
		return resource;
	}

	@Override
	public File getEntry() {
		// TODO Auto-generated method stub
		return null;
	}
}
