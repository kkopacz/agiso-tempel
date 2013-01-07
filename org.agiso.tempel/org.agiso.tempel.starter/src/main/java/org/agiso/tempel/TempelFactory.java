/* org.agiso.tempel.TempelFactory (07-01-2013)
 * 
 * TempelFactory.java
 * 
 * Copyright 2013 agiso.org
 */
package org.agiso.tempel;

import java.io.File;

import org.agiso.tempel.api.internal.ITemplateExecutor;
import org.agiso.tempel.api.internal.ITemplateProvider;
import org.agiso.tempel.api.internal.ITemplateVerifier;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class TempelFactory {
	private @Autowired ITemplateProvider templateProvider;
	private @Autowired ITemplateVerifier templateVerifier;
	private @Autowired ITemplateExecutor templateExecutor;

	/**
	 * @param workDir
	 * @param repoDir
	 * @return
	 */
	public Tempel getTempel(File workDir, File repoDir) {
		Tempel tempel = new Tempel(workDir, repoDir);

		tempel.setTemplateProvider(templateProvider);		// new MainTemplateProvider();
		tempel.setTemplateVerifier(templateVerifier);		// new RecursiveTemplateVerifier();
		tempel.setTemplateExecutor(templateExecutor);		// new DefaultTemplateExecutor();

		return tempel;
	}

}
