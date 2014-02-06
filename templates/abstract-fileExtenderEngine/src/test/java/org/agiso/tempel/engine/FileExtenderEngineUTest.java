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
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.agiso.tempel.api.ITemplateSource;
import org.agiso.tempel.api.impl.FileTemplateSource;
import org.agiso.tempel.test.AbstractTempelEngineTest;
import org.agiso.tempel.test.annotation.TempelEngineTest;
import org.testng.annotations.Test;

import static java.nio.file.StandardCopyOption.*;

/**
 * 
 * 
 * @author Mateusz Kołdowski
 * @since 1.0
 */
@TempelEngineTest(FileExtenderEngine.class)
public class FileExtenderEngineUTest extends AbstractTempelEngineTest {

	@Test
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
		
		String fileName = "testBean.java";
		
		// Przygotowywanie katalogu wyjściowego i uruchamianie silnika:
		String outPath = getOutputPath(true) + "/" + fileName;
		ITemplateSource templateSource = new FileTemplateSource(
				repositoryPath + "/FileExtenderEngineUTest/resources", "testBean.java.vm");
		String path = repositoryPath + "FileExtenderEngineUTest/" + fileName;
		File testBeanFile = new File(path);
		
		File outBeanFile = new File(outPath);
		Files.copy(testBeanFile.toPath(), outBeanFile.toPath(), REPLACE_EXISTING);
		
		engine.run(templateSource, modelMap, outPath);
	}
	
	@Test
	public void testModifyFile2() throws Exception {
		// Wypełnianie mapy modelu dla szablonu:
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put("key_type", "sss");
		modelMap.put("key_field", "rate");
		modelMap.put("key_upper_entity", "Film");
		String fileName = "testBean2.java";
		
		// Ustalanie dodatkowych parametrów
		String str = (String)modelMap.get("key_field");
		modelMap.put("key_upper_field", Character.toUpperCase(str.charAt(0)) + str.substring(1));
		str = (String)modelMap.get("key_upper_entity");
		modelMap.put("key_entity", Character.toLowerCase(str.charAt(0)) + str.substring(1));

		// Przygotowywanie katalogu wyjściowego i uruchamianie silnika:
		String outPath = getOutputPath(true) + "/" + fileName;
		ITemplateSource templateSource = new FileTemplateSource(
				repositoryPath + "/FileExtenderEngineUTest/resources",
				"testBean2.java.vm");
		String path = repositoryPath + "FileExtenderEngineUTest/" + fileName;
		File testBeanFile = new File(path);
		
		File outBeanFile = new File(outPath);
		Files.copy(testBeanFile.toPath(), outBeanFile.toPath(), REPLACE_EXISTING);
		
		engine.run(templateSource, modelMap, outPath);
	}
}
