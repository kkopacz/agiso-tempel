/* org.agiso.tempel.templates.test.VelocityDirectoryTemplateITest (20-01-2013)
 * 
 * VelocityDirectoryTemplateITest.java
 * 
 * Copyright 2013 agiso.org
 */
package org.agiso.tempel.templates.test;

import java.io.File;
import java.util.HashMap;

import org.agiso.tempel.Temp;
import org.agiso.tempel.Tempel;
import org.agiso.tempel.core.DefaultTemplateExecutor;
import org.agiso.tempel.core.RecursiveTemplateVerifier;
import org.agiso.tempel.core.provider.TstTemplateProvider;
import org.agiso.tempel.test.AbstractOutputTest;
import org.testng.annotations.Test;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class VelocityDirectoryTemplateITest extends AbstractOutputTest {
	private Tempel tempel;
	private TstTemplateProvider tstTemplateProvider;

//	--------------------------------------------------------------------------
	public VelocityDirectoryTemplateITest() {
		File workDir = new File(".");
		File repoDir = new File("./src/test/resources/repository");

		tempel = new Tempel(workDir, repoDir);

		tstTemplateProvider = new TstTemplateProvider();

		tempel.setTemplateProvider(tstTemplateProvider);
		tempel.setTemplateVerifier(new RecursiveTemplateVerifier());
		tempel.setTemplateExecutor(new DefaultTemplateExecutor());
	}

//	--------------------------------------------------------------------------
	@Test
	public void testVelocityDirectoryTemplate_7_0_0() throws Exception {
		String outPath = getOutputPath(true);

		tempel.setWorkDir(new File(outPath));
		tempel.startTemplate(
				"org.agiso.tempel.templates:test.velocityDirectoryTemplate:7.0.0",
				new HashMap<String, String>()
		);

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath + "/subdir"));
		assert "8fe87b4e14b1b6ad844d2003eef134bc".equals(md5);
	}
}
