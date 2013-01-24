/* org.agiso.tempel.api.ITemplateSourceFactory (20-01-2013)
 * 
 * ITemplateSourceFactory.java
 * 
 * Copyright 2013 agiso.org
 */
package org.agiso.tempel.api;

import org.agiso.tempel.api.model.Template;

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
