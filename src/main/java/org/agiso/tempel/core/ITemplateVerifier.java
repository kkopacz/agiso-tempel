/* org.agiso.tempel.core.ITemplateVerifier (02-10-2012)
 * 
 * ITemplateVerifier.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.core;

import org.agiso.tempel.core.model.Template;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public interface ITemplateVerifier {
	public void verifyTemplate(Template template, ITemplateProvider templateProvider) throws Exception;
}
