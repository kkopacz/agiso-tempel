/* org.agiso.tempel.templates.test.VelocityFileTemplateITest (20-01-2013)
 * 
 * VelocityFileTemplateITest.java
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
public class VelocityFileTemplateITest extends AbstractTemplateTest {
	private static final String GROUP_ID    = "org.agiso.tempel.templates";
	private static final String TEMPLATE_ID = "test.velocityFileTemplate";
	private static final String VERSION     = "1.0.0";

//	--------------------------------------------------------------------------
	public VelocityFileTemplateITest() {
		super(GROUP_ID, TEMPLATE_ID, VERSION);
	}

//	--------------------------------------------------------------------------
	@Test
	public void testVelocityDirectoryTemplate_1_0_0() throws Exception {
		String outPath = getOutputPath(true);

		tempel.setWorkDir(new File(outPath));
		tempel.startTemplate(
				GROUP_ID + ":" + TEMPLATE_ID + ":" + VERSION,
				new HashMap<String, String>()
		);

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "1de00333e6819030285a9283f87e165c".equals(md5);
	}
}
