/* org.agiso.tempel.templates.test.VelocityFileTemplateITest (20-01-2013)
 * 
 * VelocityFileTemplateITest.java
 * 
 * Copyright 2013 agiso.org
 */
package org.agiso.tempel.templates.tests;

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
	@Test
	public void testVelocityDirectoryTemplate_1_0_0() throws Exception {
		String outPath = getOutputPath(true);

		tempel.setWorkDir(new File(outPath));
		tempel.startTemplate(
				"org.agiso.tempel.templates:test.velocityFileTemplate:1.0.0",
				new HashMap<String, String>()
		);

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "1de00333e6819030285a9283f87e165c".equals(md5);
	}
}
