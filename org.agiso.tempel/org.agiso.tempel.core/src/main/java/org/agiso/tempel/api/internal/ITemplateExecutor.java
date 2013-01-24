/* org.agiso.tempel.core.ITemplateExecutor (02-10-2012)
 * 
 * ITemplateExecutor.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.api.internal;

import java.util.Map;

import org.agiso.tempel.api.model.Template;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public interface ITemplateExecutor {
	public void executeTemplate(String workDir, String repoDir, Template template, ITemplateProvider templateProvider, Map<String, Object> globalProperties) throws Exception;
}
