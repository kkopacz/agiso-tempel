/* org.agiso.tempel.engine.VelocityFileEngineUTest (15-11-2012)
 * 
 * VelocityFileEngineUTest.java
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
 * Testy jednostkowe klasy silnika {@link VelocityFileEngine}.
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
@TempelEngineTest(VelocityFileEngine.class)
public class VelocityFileEngineUTest extends AbstractTempelEngineTest {
	@Test
	public void testProcessFile1() throws Exception {
		// Wypełnianie mapy modelu dla szablonu:
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put("key1", "value1");

		// Przygotowywanie katalogu wyjściowego i uruchamianie sinika:
		String outPath = getOutputPath(true) + "/testProcessFile1.txt";
		ITemplateSource templateSource = new FileTemplateSource(
				repositoryPath + "/VelocityFileEngineUTest",
				"testProcessFile1.vm"
		);
		engine.run(templateSource, modelMap, outPath);

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "c1f94f832c26b7deecf9aecb292f6b91".equals(md5) : md5;
	}
}
