/* org.agiso.tempel.engine.DirectoryExtenderEngine (22-12-2013)
 * 
 * DirectoryExtenderEngine.java
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.agiso.tempel.api.ITempelEngine;
import org.agiso.tempel.api.ITemplateSource;
import org.agiso.tempel.api.ITemplateSourceEntry;
import org.agiso.tempel.api.impl.FileTemplateSource;
import org.apache.velocity.VelocityContext;

public class DirectoryExtenderEngine extends AbstractVelocityEngine {
	private ITempelEngine fileExtEngine = new FileExtenderEngine();
	private List<String[]> listFilesToProces = new ArrayList<String[]>();
	private static final String TEMPLATE_FILE_SUFFIX = ".vmp";
	@Override
	public void run(ITemplateSource templateSource, Map<String, Object> params, String target) {
		// Wyznaczanie ścieżki zasobu docelowego i sprawdzanie czy istnieje
		if(!templateSource.exists() || !templateSource.isDirectory()) {
			throw new RuntimeException("Folder "
					+ templateSource.getTemplate() + "/"+ templateSource.getResource()
					+ " nie istnieje");
		}
		
//		System.out.println("Folder " + templateSource.getTemplate() + "/"+ 
//				templateSource.getResource() + " istnieje");
		
		try {
			processVelocityResource(templateSource, params, target);
			System.out.println("Struktura modyfikowanych plików odpowiada stukturze szablonu");
			
			for(String tabDirs[] : listFilesToProces) {
				System.out.println("Modyfikowanie pliku ..." + tabDirs[2].substring(tabDirs[2].length() - 35) + 
						" zgodnie z plikiem: ..." + (tabDirs[0] + tabDirs[1]).substring(tabDirs[0].length() + tabDirs[1].length() - 35));
				ITemplateSource fileExtTempSource = new FileTemplateSource(tabDirs[0], tabDirs[1]);
				fileExtEngine.run(fileExtTempSource, params, tabDirs[2]);
			}
			
			System.out.println("Operacje na plikach zakończone powodzeniem\n");
			
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

//	--------------------------------------------------------------------------
	/**
	 * Szablon może być pojedynczym katalogiem. W takiej sytuacji przetwarzane
	 * są wszsytkie jego wpisy.
	 */
	protected void processVelocityResource(ITemplateSource source, Map<String, Object> params, String target) throws Exception {
		
		for(ITemplateSourceEntry entry : source.listEntries()) {
			//System.out.println("Wykryto zasób: " + entry.getName());
			
			if(entry.isFile()) {
//				System.out.println("Element " + entry.getTemplate() + "/" + source.getResource() +
//						"/"+ entry.getName() + " jest plikiem");
				processVelocityFile(entry, source.getResource(), params, target);
			}
		}
	}

	protected void processVelocityFile(ITemplateSourceEntry entry, String resFolder, Map<String, Object> params, String target) throws Exception {
		// Tworzenie kontekstu Velocity
		VelocityContext context = createVelocityContext(params);
		String fullTargetPath = entry.getName();
		
		if(fullTargetPath.endsWith(TEMPLATE_FILE_SUFFIX)) {
			// Ustalanie pełnej ściezki dla pliku vmp
			String resourcePath = entry.getTemplate() + "/" + resFolder + "/"+ fullTargetPath.substring(0, fullTargetPath.lastIndexOf('/') + 1);
			String resourceFile = fullTargetPath.substring(fullTargetPath.lastIndexOf('/') + 1, fullTargetPath.length());
			// Ustalanie pełnej ściezki dla pliku, który ma zostać zmodyfikowany
			fullTargetPath = fullTargetPath.substring(0, fullTargetPath.lastIndexOf(TEMPLATE_FILE_SUFFIX));
			fullTargetPath = target + '/' + processVelocityString(fullTargetPath, fullTargetPath, context);
			// Sprawdzenie czy obie ścieżki są poprawne 
			File fileRes = new File(resourcePath + resourceFile);
			File fileTarg = new File(fullTargetPath);
			
			if(!fileRes.exists() || !fileTarg.exists()) {
				throw new RuntimeException("Jedna ze ścieżek, będąca parametrem FileExtenderEngine'a jest nieprawidłowa");
			}
			
			//System.out.println("Obie ścieżki są prawidłowe");
			String tabDirs[] = new String[3];
			tabDirs[0] = resourcePath;
			tabDirs[1] = resourceFile;
			tabDirs[2] = fullTargetPath;
			listFilesToProces.add(tabDirs); // Dodawanie plików do listy
		}
	}
}
