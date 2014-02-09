/* org.agiso.tempel.engine.MakeDirsEngineUTest (18-01-2014)
 * 
 * MakeDirsEngineUTest.java
 * 
 * Copyright 2014 agiso.org
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
package org.agiso.tempel.engine;

import java.io.File;

import org.agiso.tempel.Temp;
import org.agiso.tempel.support.test.AbstractTempelEngineTest;
import org.agiso.tempel.support.test.annotation.TempelEngineTest;
import org.testng.annotations.Test;

/**
 * Testy jednostkowe klasy silnika {@link MakeDirsEngine}.
 * 
 * @author Karol Kopacz
 * @since 1.0
 */
@TempelEngineTest(MakeDirsEngine.class)
public class MakeDirsEngineUTest extends AbstractTempelEngineTest {
	@Test
	public void testMakeDirs() throws Exception {
		// Przygotowywanie katalogu wyjściowego i uruchamianie sinika:
		String outPath = getOutputPath(true);
		engine.run(null, null, outPath + "/parent/newDirectory");

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "30cdf9209aac5b88185f97dbe4863a5a".equals(md5) : md5;
	}
}
