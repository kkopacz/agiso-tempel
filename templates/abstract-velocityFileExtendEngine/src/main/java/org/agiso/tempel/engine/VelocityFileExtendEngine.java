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
 * @author Michał Klin
 * @since 1.0
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
		String line1;
		BufferedReader reader1 = null;
		FileInputStream fileStream1 = null;
		StringBuilder fileContent = new StringBuilder();
		try {
			fileStream1 = new FileInputStream(target);
			reader1 = new BufferedReader(new InputStreamReader(fileStream1));
			while((line1 = reader1.readLine()) != null){
				// update rozszerzenia
				if(line1.contains(extendPatern)){
					//wczytanie zawartości rozszerzenia szablonu
					String line2;
					BufferedReader reader2 = null;
					FileInputStream fileStream2 = null;
					try{
						fileStream2 = new FileInputStream(entry.getTemplate() + "/"+ entry.getName());
						reader2 = new BufferedReader(new InputStreamReader(fileStream2));
						while((line2 = reader2.readLine()) != null){
							fileContent.append(line2).append("\n");
						}
					} finally{
						if(reader2 != null) try {
							reader2.close();
						} catch(Exception e) {
						}
						if(fileStream2 != null) try {
							fileStream2.close();
						} catch(Exception e) {
						}
					}
				}
				// wiersze nie zmieniane (dodatkowo escapowane znaczniki '$' i '##'
				fileContent.append(line1.replace("##", "#[[##]]#").replace("$", "#[[$]]#")).append("\n");
			}
		} finally {
			if(reader1 != null) try {
				reader1.close();
			} catch(Exception e) {
			}
			if(fileStream1 != null) try {
				fileStream1.close();
			} catch(Exception e) {
			}
		}

		// zapis do pliku tymczasowego
		FileWriter fstreamWrite = null;
		BufferedWriter out = null;
		try {
			fstreamWrite = new FileWriter(fileTmpPath);
			out = new BufferedWriter(fstreamWrite);

			out.write(fileContent.toString());
		} finally {
			if(out != null) try {
				out.flush();
				out.close();
			} catch (Exception e) {
			}
			if(fstreamWrite != null) try {
				fstreamWrite.close();
			} catch (Exception e) {
			}
		}

		// przetwarzanie całości przez velocity i zapis do właściwego pliku (targeta)
		Writer writer = null;
		Reader reader = null;
		ITemplateSource templateSource = new FileTemplateSource(tempDir, fileTmp);
		try {
			reader = new InputStreamReader(templateSource.getEntry(templateSource.getResource()).getInputStream());
			writer = new FileWriter(new File(target));

			doVelocityTemplateMerge(entry.getName(), reader, context, writer);
		} finally {
			if(reader != null) try {
				reader.close();
			} catch(Exception e) {
			}
			if(writer != null) try {
				writer.flush();
				writer.close();
			} catch(Exception e) {
			}
		}

		// usunięcie pliku tymczasowego
		File file = new File(fileTmpPath);
		file.delete();
	}
}
