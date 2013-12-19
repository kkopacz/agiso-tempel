/* org.agiso.tempel.engine.VelocityFileExtendEngine (14-12-2013)
 * 
 * VelocityFileExtendEngine.java
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;

import org.agiso.tempel.api.ITemplateSource;
import org.agiso.tempel.api.ITemplateSourceEntry;
import org.agiso.tempel.api.impl.FileTemplateSource;
import org.apache.velocity.VelocityContext;

/**
 * Implementacja interfejsu silnika generatora wykorzystująca bibliotekę Velocity.
 * 
 * @author <a href="mailto:mklin@agiso.org">Michał Klin</a>
 */
public class VelocityFileExtendEngine extends AbstractVelocityEngine {
	@Override
	public void run(ITemplateSource templateSource, Map<String, Object> params, String target) {
		// Wyznaczanie ścieżki zasobu docelowego i sprawdzanie jego istnienia:
		if(!templateSource.exists()) {
			throw new RuntimeException("Zasób "
					+ templateSource.getTemplate() + "/"+ templateSource.getResource()
					+ " nie istnieje"
			);
		}

		// Tworzenie kontekstu Velocity wspólnego w całym procesie obsługi:
		VelocityContext context = createVelocityContext(params);

		try {
			processVelocityResource(templateSource, context, target);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

//	--------------------------------------------------------------------------
	/**
	 * Szablon musi być pojedynczym plikiem. Jego przetwarzanie polega na jednorazowym
	 * wywołaniu metody {@link #processVelocityFile(ITemplateSourceEntry, VelocityContext,
	 * String)} w celu utworzenia pojedynczego zasobu.
	 */
	protected void processVelocityResource(ITemplateSource source, VelocityContext context, String target) throws Exception {
		if(!source.isFile()) {
			throw new RuntimeException("Zasób "
					+ source.getTemplate() + "/"+ source.getResource()
					+ " nie jest plikiem"
			);
		}
		processVelocityFile(source.getEntry(source.getResource()), context, target);
	}

	protected void processVelocityFile(ITemplateSourceEntry entry, VelocityContext context, String target) throws Exception {
		if(!entry.isFile()) {
			throw new RuntimeException("Element "
					+ entry.getTemplate() + "/"+ entry.getName()
					+ " nie jest plikiem"
			);
		}

		// parsowanie nazwy pliku szablonu
		String fileNameExtend = entry.getName().substring(0, entry.getName().lastIndexOf("."));
		String extendPatern = fileNameExtend.substring(fileNameExtend.lastIndexOf(".")+1);

		// plik tymczasowy
		String tempDir = System.getProperty("java.io.tmpdir");
		String fileTmp = "tmp-tempel.txt";
		String fileTmpPath = tempDir + "/" + fileTmp;

		// plik do modyfikacjiczytany linia po lini
		FileInputStream fstream = new FileInputStream(target);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		StringBuilder fileContent = new StringBuilder();

		String line;
		while((line = br.readLine()) != null){
			// update rozszerzenia
			if(line.contains(extendPatern)){
				//wczytanie zawartości rozszerzenia szablonu
				FileInputStream fstream2 = new FileInputStream(entry.getTemplate() + "/"+ entry.getName());
				BufferedReader br2 = new BufferedReader(new InputStreamReader(fstream2));
				String line2;
				while((line2 = br2.readLine()) != null){
					fileContent.append(line2).append("\n");
				}
			}
			// wiersze nie zmieniane (dodatkowo escapowane znaczniki '$' i '##'
			fileContent.append(line.replace("##", "#[[##]]#").replace("$", "#[[$]]#")).append("\n");
		}
		br.close();

		// zapis do pliku tymczasowego
		FileWriter fstreamWrite = new FileWriter(fileTmpPath);
		BufferedWriter out = new BufferedWriter(fstreamWrite);
		out.write(fileContent.toString());
		out.close();

		// przetwarzanie całości przez velocity i zapis do właściwego pliku (targeta)
		Writer writer = null;
		Reader reader = null;
		ITemplateSource templateSource = new FileTemplateSource(tempDir, fileTmp);
		try {
			reader = new InputStreamReader(templateSource.getEntry(templateSource.getResource()).getInputStream());
			writer = new FileWriter(new File(target));

			doVelocityTemplateMerge(entry.getName(), reader, context, writer);
		} finally {
			if(reader != null) {
				try {
					reader.close();
				} catch(Exception e) {
					throw new RuntimeException(e);
				}
			}
			if(writer != null) {
				try {
					writer.flush();
					writer.close();
				} catch(Exception e) {
					throw new RuntimeException(e);
				}
			}
		}

		// usunięcie pliku tymczasowego
		File file = new File(fileTmpPath);
		file.delete();
	}
}
