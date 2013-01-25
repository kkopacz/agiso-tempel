/* org.agiso.tempel.api.ITemplateSource (19-12-2012)
 * 
 * ITemplateSource.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.api;

import java.util.Collection;


/**
 * 
 * 
 * @author <a href="mailto:kkopaczz@agiso.org">Karol Kopacz</a>
 */
public interface ITemplateSource {
	public String getTemplate();

	public String getResource();

//	--------------------------------------------------------------------------
	public boolean exists();

	public boolean isFile();

	public boolean isDirectory();

//	--------------------------------------------------------------------------
	public Collection<ITemplateSourceEntry> listEntries();

	public ITemplateSourceEntry getEntry(String name);
}
