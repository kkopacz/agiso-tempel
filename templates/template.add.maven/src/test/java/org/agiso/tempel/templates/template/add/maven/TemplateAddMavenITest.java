/* org.agiso.tempel.templates.tempel.add.TemplateAddMavenITest (13-02-2013)
 * 
 * TemplateAddMavenITest.java
 * 
 * Copyright 2013 agiso.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.agiso.tempel.templates.template.add.maven;

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
public class TemplateAddMavenITest extends AbstractTemplateTest {
	private static final String GROUP_ID    = "org.agiso.tempel.templates";
	private static final String TEMPLATE_ID = "template.add.maven";
	private static final String VERSION     = "1.0.0";

//	--------------------------------------------------------------------------
	public TemplateAddMavenITest() {
		super(GROUP_ID, TEMPLATE_ID, VERSION);
	}

//	--------------------------------------------------------------------------
	@Test
	public void testTemplateAddLocal() throws Exception {
		String outPath = getOutputPath(true);

		tempel.startTemplate(
				GROUP_ID + ":" + TEMPLATE_ID + ":" + VERSION,
				new HashMap<String, String>(), new File(outPath).getCanonicalPath()
		);

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		System.out.println(md5);
		// assert "e81e17876652c7875b538f9db7157c1e".equals(md5);
	}
}
