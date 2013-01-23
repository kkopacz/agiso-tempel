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
import org.agiso.tempel.test.AbstractTemplateTest;
import org.testng.annotations.Test;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class VelocityDirectoryTemplateITest extends AbstractTemplateTest {
	private static final String GROUP_ID    = "org.agiso.tempel.templates";
	private static final String TEMPLATE_ID = "test.velocityDirectoryTemplate";
	private static final String VERSION     = "3.0.0";

//	--------------------------------------------------------------------------
	public VelocityDirectoryTemplateITest() {
		super(GROUP_ID, TEMPLATE_ID, VERSION);
	}

//	--------------------------------------------------------------------------
	@Test
	public void testVelocityDirectoryTemplate_3_0_0() throws Exception {
		String outPath = getOutputPath(true);

		tempel.setWorkDir(new File(outPath));
		tempel.startTemplate(
				GROUP_ID + ":" + TEMPLATE_ID + ":" + VERSION,
				new HashMap<String, String>()
		);

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "554cd5d1a8d91902a6315f11f1f98887".equals(md5);
	}
}
