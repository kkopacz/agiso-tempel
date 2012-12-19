/* org.agiso.tempel.core.FileTemplateSource (19-12-2012)
 * 
 * FileTemplateSource.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.core;

import java.io.File;

import org.agiso.tempel.Temp;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class FileTemplateSource implements ITemplateSource {
	private File sourceFile;

	private String repository;
	private String template;
	private String resource;

	public FileTemplateSource(String repository, String template, String resource) {
		this.repository = repository;
		this.template = template;
		this.resource = resource;

		if(Temp.StringUtils_isBlank(resource)) {
			sourceFile = new File(repository + "/" + template);
		} else {
			sourceFile = new File(repository + "/" + template + "/" + resource);
		}
	}

	@Override
	public boolean exists() {
		return sourceFile.exists();
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
		return sourceFile;
	}
}
