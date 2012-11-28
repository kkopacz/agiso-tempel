/* org.agiso.tempel.core.ITemplateProvider (02-10-2012)
 * 
 * ITemplateProvider.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.core;

import java.io.IOException;
import java.util.Map;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public interface ITemplateProvider {
	public ITemplateRepository readTemplates(Map<String, Object> properties) throws IOException;
}
