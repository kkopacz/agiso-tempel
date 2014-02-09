/* org.agiso.tempel.engine.MakeDirEngineUTest (18-01-2014)
 * 
 * MakeDirEngineUTest.java
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
 * Testy jednostkowe klasy silnika {@link MakeDirEngine}.
 * 
 * @author Karol Kopacz
 * @since 1.0
 */
@TempelEngineTest(MakeDirEngine.class)
public class MakeDirEngineUTest extends AbstractTempelEngineTest {
	@Test
	public void testMakeDir() throws Exception {
		// Przygotowywanie katalogu wyjściowego i uruchamianie sinika:
		String outPath = getOutputPath(true);
		engine.run(null, null, outPath + "/newDirectory");

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "ff25e77df10a16b09de1c524300051ed".equals(md5) : md5;
	}
}
