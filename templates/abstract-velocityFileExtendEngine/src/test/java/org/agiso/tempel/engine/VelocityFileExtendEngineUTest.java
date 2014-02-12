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

import org.agiso.core.lang.util.DigestUtils;
import org.agiso.tempel.api.ITemplateSource;
import org.agiso.tempel.api.impl.FileTemplateSource;
import org.agiso.tempel.support.test.AbstractTempelEngineTest;
import org.agiso.tempel.support.test.annotation.TempelEngineTest;
import org.testng.annotations.Test;

/**
 * Testy jednostkowe klasy silnika {@link VelocityFileExtendEngine}.
 * 
 * @author Michał Klin
 * @since 1.0
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
				repositoryPath + "VelocityFileExtendEngineUTest/testProcessFile1/base"
		) + "/testProcessFile1.txt";
		ITemplateSource templateSource = new FileTemplateSource(
				repositoryPath + "VelocityFileExtendEngineUTest/testProcessFile1/repo",
				"testProcessFile1.txt.EXTEND_LINE.vm"
		);
		engine.run(templateSource, modelMap, outPath);

		String md5 = DigestUtils.countDigest("MD5", new File(outPath));
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
				repositoryPath + "VelocityFileExtendEngineUTest/testProcessFile2/base"
		) + "/testProcessFile2.txt";
		ITemplateSource templateSource = new FileTemplateSource(
				repositoryPath + "VelocityFileExtendEngineUTest/testProcessFile2/repo",
				"testProcessFile2.txt.EXTEND_LINE.vm"
		);

		Object val = modelMap.get("key2");
		if(val instanceof ArrayList) {
			Map<String, Object> modelMap2 = new HashMap<String, Object>();
			modelMap2.put("key2", ((ArrayList<Object>)val).get(0));
			engine.run(templateSource, modelMap2, outPath);
		}

		String md5 = DigestUtils.countDigest("MD5", new File(outPath));
		assert "848f9d91182100184aab4e05f2e05354".equals(md5) : md5;
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
				repositoryPath + "VelocityFileExtendEngineUTest/testProcessFile3/base"
		) + "/testProcessFile3.txt";
		ITemplateSource templateSource = new FileTemplateSource(
				repositoryPath + "VelocityFileExtendEngineUTest/testProcessFile3/repo",
				"testProcessFile3.txt.EXTEND_LINE.vm"
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

		String md5 = DigestUtils.countDigest("MD5", new File(outPath));
		assert "06784b080c2787f65a9a4fba4b0097c7".equals(md5) : md5;
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
				repositoryPath + "VelocityFileExtendEngineUTest/testProcessFile4/base"
		) + "/testProcessFile4.txt";
		ITemplateSource templateSource = new FileTemplateSource(
				repositoryPath + "VelocityFileExtendEngineUTest/testProcessFile4/repo",
				"testProcessFile4.txt.EXTEND_LINE.vm"
		);
		engine.run(templateSource, modelMap, outPath);

		String md5 = DigestUtils.countDigest("MD5", new File(outPath));
		assert "adfc8ec3f763b8d44088f0327f571c79".equals(md5) : md5;
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
				repositoryPath + "VelocityFileExtendEngineUTest/testProcessFile5/base"
		) + "/testProcessFile5.txt";
		ITemplateSource templateSource = new FileTemplateSource(
				repositoryPath + "VelocityFileExtendEngineUTest/testProcessFile5/repo",
				"testProcessFile5.txt.EXTEND_LINE.vm"
		);
		engine.run(templateSource, modelMap, outPath);

		String md5 = DigestUtils.countDigest("MD5", new File(outPath));
		assert "d7d4f636abf3b73ee622c4cc254e7681".equals(md5) : md5;
	}
}
