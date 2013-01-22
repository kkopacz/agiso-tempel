/* org.agiso.tempel.core.model.ITemplateSourceFactory (20-01-2013)
 * 
 * ITemplateSourceFactory.java
 * 
 * Copyright 2013 agiso.org
 */
package org.agiso.tempel.core.model;

import org.agiso.tempel.api.ITemplateSource;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public interface ITemplateSourceFactory {
	/**
	 * @param source
	 * @return
	 */
	public ITemplateSource createTemplateSource(Template template, String source);
}
