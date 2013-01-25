/* org.agiso.tempel.api.ITemplateSourceEntry (21-12-2012)
 * 
 * ITemplateSourceEntry.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.api;

import java.io.InputStream;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public interface ITemplateSourceEntry {
	public String getTemplate();

	public String getName();

//	--------------------------------------------------------------------------
	public boolean exists();

	public boolean isFile();

	public boolean isDirectory();

//	--------------------------------------------------------------------------
	public InputStream getInputStream() throws Exception;
}
