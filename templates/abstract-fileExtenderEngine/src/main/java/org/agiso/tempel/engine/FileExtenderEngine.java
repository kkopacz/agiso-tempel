/* org.agiso.tempel.engine.FileExtenderEngine (22-12-2012)
 * 
 * FileExtenderEngine.java
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.agiso.tempel.api.ITempelEngine;
import org.agiso.tempel.api.ITemplateSource;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 * 
 * 
 * @author Mateusz Kołdowski
 * @since 1.0
 */
public class FileExtenderEngine implements ITempelEngine {
	private String lineSeparator;
	private int lineSeparatorLength;

//	--------------------------------------------------------------------------
	public FileExtenderEngine() {
		setLineSeparator(System.getProperty("line.separator"));
	}

//	--------------------------------------------------------------------------
	public void setLineSeparator(String lineSeparator) {
		this.lineSeparator = lineSeparator;
		this.lineSeparatorLength = lineSeparator.length();
	}

//	--------------------------------------------------------------------------
	@Override
	public void run(ITemplateSource source, Map<String, Object> params, String target) {
		// Tworzenie kontekstu Velocity wspólnego w całym procesie obsługi:
		final VelocityContext context = createVelocityContext(params);

		BufferedReader reader = null;
		try {
			//System.out.println("Otwieranie pliku template " + source.getTemplate() + "/"+ source.getResource());
			reader = new BufferedReader(new InputStreamReader(source.getEntry(source.getResource()).getInputStream()));

			boolean insideTag = false;
			String line = "", multiLine = "", tag = "";
			List<String> listAdd = new ArrayList<String>();

			// Wczytywanie linii
			while((line = reader.readLine()) != null) {
				//System.out.println("add line " + line + " długość " + line.length());

				// Wykrywanie znacznikow
				if(!insideTag && !line.trim().isEmpty()) {
					tag = line;
					//System.out.println("Znaleziono tag " + tag);
					insideTag = true;
				} else if(insideTag) {
					if(!tag.equals(line)) {
						multiLine += line + lineSeparator;
					} else {		// Jeśli znaleziono znacznik kończący
						//System.out.println("Zakończono tag " + tag);
						multiLine = processVelocityString(multiLine, multiLine, context);

						// Dodawanie przetworzonej linii do listy
						for(int s = 0, e = 0; s < multiLine.length(); s = e + 1) {
							e = multiLine.indexOf(lineSeparator, s); // Indeks znalezionej frazy
							listAdd.add(multiLine.substring(s, e));
						}

						procesFile(tag, listAdd, target);

						//System.out.println("Rozpoczęto wyszukiwanie następnego tagu");
						insideTag = false;
						multiLine = line = tag = "";
						listAdd.clear();
					}
				}
			}
		} catch(Exception e) {
			throw new RuntimeException("Wystąpił błąd podczas próby edycji pliku: " + e.getMessage());
		} finally {
			if(reader != null)try {
				reader.close();
				reader = null;
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void procesFile(String strFind, List<String> listPut, String target) throws IOException {
		// Odczyt pliku, który będzie rozbudowywany:
		String fileContent = "";

		Reader fileReader = null;
		BufferedReader bufferedReader = null;
		try {
			fileReader = new FileReader(target);
			bufferedReader = new BufferedReader(fileReader);

			String line = null;
			String ls = System.getProperty("line.separator");
			StringBuilder stringBuilder = new StringBuilder();
			boolean firstTime = true;

			// Tworzenie łańcucha znakowego z zawartością pliku
			while((line = bufferedReader.readLine()) != null) {
				if(firstTime == false) {
					stringBuilder.append(ls);
				}
				else {
					firstTime = false;
				}

				stringBuilder.append(line);
			}

			fileContent = stringBuilder.toString();
		} finally {
			if(bufferedReader != null) try {
				bufferedReader.close();
				bufferedReader = null;
			} catch(Exception e) {
				e.printStackTrace();
			}
			if(fileReader != null) try {
				fileReader.close();
				fileReader = null;
			} catch(Exception e) {
				e.printStackTrace();
			}
		}

		// Przetwarzanie i rozbudowywanie pliku:
		BufferedWriter bufferedWriter = null;
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(target));

			// Zmienne pomicnicze
			int findInd = 0, commentInd = 0, newLineInd = 0, begInd = 0;
			int endInd = fileContent.length() - 1;
			char tempChar = 0;
			boolean newLineNotFound = false;
			String whiteSpace;

			// Wyszukiwanie wszystkich wystąpień frazy (do końca pliku)
			while(newLineInd != -1) {
				newLineInd = findInd = fileContent.indexOf(strFind, begInd); // Indeks znalezionej frazy
				whiteSpace = "";

				if(newLineInd != -1) {
					// Znaleziono szukaną frazę, ustalany jest indeks dla znaku nowej linii
					while(!lineSeparator.equals(fileContent.substring(newLineInd, newLineInd + lineSeparatorLength))) {
						// Jeśli został przekroczony zakres znaków
						if(newLineInd <= begInd) {
							newLineNotFound = true;
							break;
						}
						newLineInd--;
					}

					//System.out.println("Znaleziono znak nowej linii");
					commentInd = newLineInd + 1;
					tempChar = fileContent.charAt(commentInd);

					// Ustalanie indeksu komentarza
					while((tempChar == '\t' || tempChar == ' ') && commentInd < findInd) {
						whiteSpace += tempChar;
						commentInd++;
						tempChar = fileContent.charAt(commentInd);
					}

					//System.out.println("Wykryto poprawnie znaki białe przed komentarzem");

					// Przepisywanie istniejącego fragmentu pliku
					bufferedWriter.write(fileContent.substring(begInd, newLineInd));

					for(String linePut : listPut) {
						if(newLineNotFound) {
							bufferedWriter.write(whiteSpace + linePut);			// Jedna linia wstawianego tekstu
							newLineNotFound = false;
						} else {
							bufferedWriter.write(lineSeparator);

							if(!linePut.contentEquals("")) {
								bufferedWriter.write(whiteSpace + linePut);		// Jedna linia wstawianego tekstu
							}
						}
					}
					// Reszta pliku
					bufferedWriter.write(fileContent.substring(newLineInd, findInd + strFind.length()));
					begInd = findInd + strFind.length();
				} else {
					//System.out.println("Nie znaleziono więcej pasujących wyrazów");

					// Nie znaleziono szukanej frazy, pozostała część pliku jest przepisywana
					bufferedWriter.write(fileContent.substring(begInd, endInd+1));
				}
			}
		} finally {
			if(bufferedWriter != null) {
				bufferedWriter.close();
				bufferedWriter = null;
			}
		}
	}

//	--------------------------------------------------------------------------
	public VelocityContext createVelocityContext(Map<String, Object> params) {
		VelocityContext context = new VelocityContext();
		for(String key : params.keySet()) {
			context.put(key, params.get(key));
		}
		return context;
	}

	public String processVelocityString(String logTag, String inString, VelocityContext context) {
		Writer writer = new StringWriter();
		VelocityEngine engine = new VelocityEngine();
		engine.evaluate(context, writer, logTag, inString);
		return writer.toString();
	}
}