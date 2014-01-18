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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.agiso.tempel.Temp;
import org.agiso.tempel.api.ITemplateSource;
import org.agiso.tempel.api.impl.FileTemplateSource;
import org.agiso.tempel.test.AbstractTempelEngineTest;
import org.agiso.tempel.test.annotation.TempelEngineTest;
import org.testng.annotations.Test;

/**
 * Testy jednostkowe klasy silnika {@link VelocityFileExtendEngine}.
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
		String outPath = getOutputPath(true,
				repositoryPath + "VelocityFileExtendEngineUTest") +
				"/testProcessFile1.txt";
		ITemplateSource templateSource = new FileTemplateSource(
				repositoryPath + "VelocityFileExtendEngineUTest",
				"testProcessFile1.txt.EXTEND_LINE.vm"
		);
		engine.run(templateSource, modelMap, outPath);

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "85a378d06fdc8f07cdd3123945ccd75e".equals(md5) : md5;
	}

	@Test
	public void testProcessFile2() throws Exception {
		// Wypełnianie mapy modelu dla szablonu:
		Map<String, Object> modelMap = new HashMap<String, Object>();
		List<Object> list = new ArrayList<Object>();
		list.add(new String("value2"));
		modelMap.put("key2", list);

		// Przygotowywanie katalogu wyjściowego i uruchamianie sinika:
		String outPath = getOutputPath(true,
				repositoryPath + "VelocityFileExtendEngineUTest") +
				"/testProcessFile1.txt";
		ITemplateSource templateSource = new FileTemplateSource(
				repositoryPath + "VelocityFileExtendEngineUTest",
				"testProcessFile1.txt.EXTEND_LINE.vm"
		);

		Object val = modelMap.get("key2");
		if(val instanceof ArrayList) {
			Map<String, Object> modelMap2 = new HashMap<String, Object>();
			modelMap2.put("key2", ((ArrayList<Object>)val).get(0));
			engine.run(templateSource, modelMap2, outPath);
		}

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "85a378d06fdc8f07cdd3123945ccd75e".equals(md5) : md5;
	}

	@Test
	public void testProcessFile3() throws Exception {
		// Wypełnianie mapy modelu dla szablonu:
		Map<String, Object> modelMap = new HashMap<String, Object>();
		List<Object> list = new ArrayList<Object>();
		list.add(new String("value2"));
		list.add(new String("value3"));
		modelMap.put("key2", list);

		// Przygotowywanie katalogu wyjściowego i uruchamianie sinika:
		String outPath = getOutputPath(true,
				repositoryPath + "VelocityFileExtendEngineUTest") +
				"/testProcessFile2.txt";
		ITemplateSource templateSource = new FileTemplateSource(
				repositoryPath + "VelocityFileExtendEngineUTest",
				"testProcessFile2.txt.EXTEND_LINE.vm"
		);

		Object val = modelMap.get("key2");
		if(val instanceof ArrayList) {
			List<Object> listVal = (ArrayList<Object>)val;
			for(int idx = 0; idx < listVal.size(); idx++) {
				Map<String, Object> modelMap2 = new HashMap<String, Object>();
				modelMap2.put("key2", listVal.get(idx));
				engine.run(templateSource, modelMap2, outPath);
			}
		}

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "16da266e446a2f65d9acdf40a7934225".equals(md5) : md5;
	}

	@Test
	public void testProcessFile4() throws Exception {
		// Wypełnianie mapy modelu dla szablonu:
		Map<String, Object> modelMap = new HashMap<String, Object>();
		List<Object> list = new ArrayList<Object>();
		list.add(new String("value2"));
		list.add(new String("value3"));
		modelMap.put("key2", list);

		// Przygotowywanie katalogu wyjściowego i uruchamianie sinika:
		String outPath = getOutputPath(true,
				repositoryPath + "VelocityFileExtendEngineUTest") +
				"/testProcessFile3.txt";
		ITemplateSource templateSource = new FileTemplateSource(
				repositoryPath + "VelocityFileExtendEngineUTest",
				"testProcessFile3.txt.EXTEND_LINE.vm"
		);
		engine.run(templateSource, modelMap, outPath);

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "577334b8abc8ca3e517ac65150170932".equals(md5) : md5;
	}

	@Test
	public void testProcessFile5() throws Exception {
		// Wypełnianie mapy modelu dla szablonu:
		Map<String, Object> modelMap = new HashMap<String, Object>();
		List<Object> list = new ArrayList<Object>();
		list.add(new String("arg2"));
		list.add(new String("arg3"));
		modelMap.put("key2", list);
		List<Object> list2 = new ArrayList<Object>();
		list2.add(new String("value2"));
		list2.add(new String("value3"));
		modelMap.put("key3", list2);

		// Przygotowywanie katalogu wyjściowego i uruchamianie sinika:
		String outPath = getOutputPath(true,
				repositoryPath + "VelocityFileExtendEngineUTest") +
				"/testProcessFile4.txt";
		ITemplateSource templateSource = new FileTemplateSource(
				repositoryPath + "VelocityFileExtendEngineUTest",
				"testProcessFile4.txt.EXTEND_LINE.vm"
		);
		engine.run(templateSource, modelMap, outPath);

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "4ee2590760b85ff39246cf1f0c846391".equals(md5) : md5;
	}
}
