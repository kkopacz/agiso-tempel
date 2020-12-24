/* org.agiso.tempel.engine.FileExtenderEngineUTest (22-12-2013)
 * 
 * FileExtenderEngineUTest.java
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
package org.agiso.tempel.engine;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.agiso.core.lang.util.DigestUtils;
import org.agiso.tempel.api.ITemplateSource;
import org.agiso.tempel.api.impl.FileTemplateSource;
import org.agiso.tempel.support.test.AbstractTempelEngineTest;
import org.agiso.tempel.support.test.annotation.TempelEngineTest;
import org.testng.annotations.Test;

/**
 * 
 * 
 * @author Mateusz Kołdowski
 * @since 1.0
 */
@TempelEngineTest(FileExtenderEngine.class)
public class FileExtenderEngineUTest extends AbstractTempelEngineTest {
	@Test
	// FIXME: Test nie działa poprawnie, ponieważ generowane pliki mają inne
	// zakończenia linii w systemie Windows i inne w systemie Linux.
	public void testModifyFile1() throws Exception {
		// Wypełnianie mapy modelu dla szablonu:
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put("key_type", "int");
		modelMap.put("key_field", "rate");
		modelMap.put("key_upper_entity", "Film");

		// Ustalanie dodatkowych parametrów
		String str = (String)modelMap.get("key_field");
		modelMap.put("key_upper_field", Character.toUpperCase(str.charAt(0)) + str.substring(1));
		str = (String)modelMap.get("key_upper_entity");
		modelMap.put("key_entity", Character.toLowerCase(str.charAt(0)) + str.substring(1));

		// Przygotowywanie katalogu wyjściowego i uruchamianie silnika:
		String outPath = getOutputPath(true,
				repositoryPath + "FileExtenderEngineUTest/testModifyFile1/base"
		) + "/testBean.java";
		ITemplateSource templateSource = new FileTemplateSource(
				repositoryPath + "FileExtenderEngineUTest/testModifyFile1/repo",
				"testBean.java.vm"
		);
		engine.run(templateSource, modelMap, outPath);

		String md5 = DigestUtils.countDigest("MD5", new File(outPath));
		assert "a3bf0b4a378274432e8d230214058375".equals(md5) : md5;
	}

	// @Test
	// FIXME: Test nie działa poprawnie, ponieważ generowane pliki mają inne
	// zakończenia linii w systemie Windows i inne w systemie Linux.
	public void testModifyFile2() throws Exception {
		// Wypełnianie mapy modelu dla szablonu:
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put("key_type", "sss");
		modelMap.put("key_field", "rate");
		modelMap.put("key_upper_entity", "Film");

		// Ustalanie dodatkowych parametrów
		String str = (String)modelMap.get("key_field");
		modelMap.put("key_upper_field", Character.toUpperCase(str.charAt(0)) + str.substring(1));
		str = (String)modelMap.get("key_upper_entity");
		modelMap.put("key_entity", Character.toLowerCase(str.charAt(0)) + str.substring(1));

		// Przygotowywanie katalogu wyjściowego i uruchamianie silnika:
		String outPath = getOutputPath(true,
				repositoryPath + "FileExtenderEngineUTest/testModifyFile2/base"
		) + "/testBean2.java";
		ITemplateSource templateSource = new FileTemplateSource(
				repositoryPath + "FileExtenderEngineUTest/testModifyFile2/repo",
				"testBean2.java.vm"
		);
		engine.run(templateSource, modelMap, outPath);

		String md5 = DigestUtils.countDigest("MD5", new File(outPath));
		assert "18135fe77be2ddefc8b01b02bf565ccc".equals(md5) : md5;
	}
}
