/* org.agiso.tempel.tests.TemplateEngineITest (11-12-2013)
 * 
 * TemplateEngineITest.java
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
package org.agiso.tempel.tests;

import java.io.File;

import org.agiso.tempel.Temp;
import org.agiso.tempel.starter.Bootstrap;
import org.testng.annotations.Test;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class TemplateEngineITest extends AbstractOutputTest {
//	--------------------------------------------------------------------------
//	src/test/templates/run/tempel.xml
//	--------------------------------------------------------------------------
	@Test
	public void templateEngineTest01() throws Exception {
		String outPath = getOutputPath(true);

		// Wywołanie Bootstrap i uruchamianie szablonu:
		Bootstrap.main(/* paramReader, */ new String[] {
				"org.agiso.tempel.tests:TemplateEngineITest:templateEngineTest01",
				"-d " + outPath
		});

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "aafffc5e362b45e7c28ca3593067e17d".equals(md5);
	}

	@Test
	public void templateEngineTest02() throws Exception {
		String outPath = getOutputPath(true);

		// Wywołanie Bootstrap i uruchamianie szablonu:
		Bootstrap.main(/* paramReader, */ new String[] {
				"org.agiso.tempel.tests:TemplateEngineITest:templateEngineTest02",
				"-d " + outPath
		});

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "00040f9b635713c4eb30c910d55f8c11".equals(md5);
	}

	@Test
	public void templateEngineTest03() throws Exception {
		String outPath = getOutputPath(true);

		// Wywołanie Bootstrap i uruchamianie szablonu:
		Bootstrap.main(/* paramReader, */ new String[] {
				"org.agiso.tempel.tests:TemplateEngineITest:templateEngineTest03",
				"-d " + outPath
		});

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "3925e499a09d461df7036bb111fdce61".equals(md5);
	}
}
