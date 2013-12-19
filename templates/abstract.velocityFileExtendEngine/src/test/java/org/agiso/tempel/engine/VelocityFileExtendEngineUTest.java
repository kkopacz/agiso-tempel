/* org.agiso.tempel.engine.VelocityFileExtendEngineUTest (15-11-2012)
 * 
 * VelocityFileExtendEngineUTest.java
 * 
 * Copyright 2012 agiso.org
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
import java.util.HashMap;
import java.util.Map;

import org.agiso.tempel.Temp;
import org.agiso.tempel.api.ITemplateSource;
import org.agiso.tempel.api.impl.FileTemplateSource;
import org.agiso.tempel.test.AbstractTempelEngineTest;
import org.agiso.tempel.test.annotation.TempelEngineTest;
import org.testng.annotations.Test;

/**
 * 
 * 
 * @author <a href="mailto:mklin@agiso.org">Michał Klin</a>
 */
@TempelEngineTest(VelocityFileExtendEngine.class)
public class VelocityFileExtendEngineUTest extends AbstractTempelEngineTest {
	@Test
	public void testProcessFile1() throws Exception {
		// Wypełnianie mapy modelu dla szablonu:
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put("key2", "value2");

		// Przygotowywanie katalogu wyjściowego i uruchamianie sinika:
		String outFileName = "testProcessFile1.txt";
		String outPath = getOutputPath(true) + "/" + outFileName;
		ITemplateSource templateSource = new FileTemplateSource(
				repositoryPath + "VelocityFileExtendEngineUTest",
				"testProcessFile1.txt.EXTEND_LINE.vm"
		);

		// kopia do targeta przetwarzanego pliku
		Temp.FileUtils_copyFile(repositoryPath + "VelocityFileExtendEngineUTest/" + outFileName, outPath);

		engine.run(templateSource, modelMap, outPath);

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "85a378d06fdc8f07cdd3123945ccd75e".equals(md5);
	}
}
