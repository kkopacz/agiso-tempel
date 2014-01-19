/* org.agiso.tempel.engine.DirectoryExtenderEngineUTest (15-11-2013)
 * 
 * DirectoryExtenderEngineUTest.java
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.agiso.tempel.api.ITemplateSource;
import org.agiso.tempel.api.impl.FileTemplateSource;
import org.agiso.tempel.test.AbstractTempelEngineTest;
import org.agiso.tempel.test.annotation.TempelEngineTest;
import org.testng.annotations.Test;

@TempelEngineTest(DirectoryExtenderEngine.class)
public class DirectoryExtenderEngineUTest extends AbstractTempelEngineTest {
	@Test
	public void testProcessDirectory1() throws Exception {
		
		System.out.println("Uruchamianie testu: testProcessDirectory1() ");
		// Wypełnianie mapy modelu dla szablonu:
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put("key_canonical", "pl.exso.core.");
		modelMap.put("key_project", "mat");
		modelMap.put("key_type", "int");
		modelMap.put("key_field", "rate");
		modelMap.put("key_upper_entity", "Film");
		
		// Ustalanie dodatkowych parametrów
		String str = (String)modelMap.get("key_field");
		modelMap.put("key_upper_field", Character.toUpperCase(str.charAt(0)) + str.substring(1));
		str = (String)modelMap.get("key_upper_entity");
		modelMap.put("key_entity", Character.toLowerCase(str.charAt(0)) + str.substring(1));
		
		// Przygotowywanie katalogu wyjściowego i uruchamianie sinika:
		String outPath = getOutputPath(true);
//		ITemplateSource templateSource = new FileTemplateSource(
//				repositoryPath + "/DirectoryExtenderEngineUTest", "resources");
		
		ITemplateSource templateSource = new FileTemplateSource(
				repositoryPath + "/DirectoryExtenderEngineUTest/resources", "");
		
		File srcFolder = new File(repositoryPath + "/DirectoryExtenderEngineUTest/bundles");
		File destFolder = new File(outPath);
		
		// Sprawdzenie czy folder źródłowy istnieje
		if(!srcFolder.exists()) {
			assert false : "Zasób " + srcFolder + " nie istnieje. Test zostaje przewany.";
		} else {
			try {
				copyFolder(srcFolder, destFolder);
				//System.out.println("Pliki niezbędne do testu zostały poprawnie skopiowane. "
				//		+ "Zostaje uruchomiony DirectoryExtenderEngineUTest\n");
				engine.run(templateSource, modelMap, outPath);
				
				//String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
				//assert "1b88794f97f0b86ceba125ef9326af9e".equals(md5) : md5;
				} catch(IOException e) {
					e.printStackTrace();
					assert false : "Pliki niezbędne do testu nie zostały skoiowane.";
					System.exit(0);
				}
		}
	}
	
	public static void copyFolder(File src, File dest) throws IOException {
		
		if(src.isDirectory()) {
			// Jeśli ścieżka nie istnieje, to zostaje ona utworzona
			if(!dest.exists()) {
				dest.mkdir();
				//System.out.println("Skopiowano folder z " + src + " do " + dest);
			}
			
			// Lista wszystkich zaweranych ścieżek
			String files[] = src.list();
			
			for(String file : files) {
				// Tworzenie zródłowej oraz docelowej struktury folderów
				File srcFile = new File(src, file);
				File destFile = new File(dest, file);
				// Kopiowanie rekurencyjne
				copyFolder(srcFile,destFile);
			}
			
		} else {
			// Jeśli przetwarzany jest plik, to zostaje on skopiowany
			InputStream in = new FileInputStream(src);
			OutputStream out = new FileOutputStream(dest); 
			
			byte[] buffer = new byte[1024];
			
			int length;
			// Kopiowanie zawartości pliku 
			while((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}
			
			in.close();
			out.close();
			//System.out.println("Skopiowano plik z " + src + " do " + dest);
		}
	}
}
