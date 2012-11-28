/* org.agiso.tempel.core.ITemplateExecutor (02-10-2012)
 * 
 * ITemplateExecutor.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.core;

import java.util.Map;

import org.agiso.tempel.core.model.Template;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public interface ITemplateExecutor {
	public void executeTemplate(String workDir, String repoDir, Template template, ITemplateRepository repository, Map<String, Object> globalProperties) throws Exception;
}
