/* org.agiso.tempel.core.engine.ITemplateSource (19-12-2012)
 * 
 * ITemplateSource.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.core;

import java.io.File;

/**
 * 
 * 
 * @author <a href="mailto:kkopaczz@agiso.org">Karol Kopacz</a>
 */
public interface ITemplateSource {
	public boolean exists();


	public String getRepository();

	public String getTemplate();

	public String getResource();


	/**
	 * FIXME: Do zastąpienia getEntry(String resource);
	 */
	public File getEntry();
}
