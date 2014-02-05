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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;
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
	@Override
	public void run(ITemplateSource source, Map<String, Object> params, String target) {
		
		try {
			File fileSource = new File(source.getTemplate() + "/" + source.getResource());
			//System.out.println("Otwieranie pliku template " + source.getTemplate() + "/"+ source.getResource());
			String fileContent = new String(Files.readAllBytes(fileSource.toPath()));
			int srcInd = 0;
			int nlInd = 0;
			int srcEndInd = fileContent.length();
			String line = "", multiLine = "", tag = "";
			boolean foundTag = false;
			List<String> listAdd = new ArrayList<String>();
			VelocityContext context = createVelocityContext(params);
			
			// Wczytywanie linii
			while(nlInd != -1) {
				nlInd = fileContent.indexOf('\n', srcInd); // Indeks znalezionej frazy
				if(nlInd != -1) {
					line = fileContent.substring(srcInd, nlInd);
					srcInd = nlInd + 1;
				}
				else {
					line = fileContent.substring(srcInd, srcEndInd);
				}
				
				//System.out.println("add line " + line + " długość " + line.length());
				
				// Wykrywanie znacznikow
				if(!foundTag && line.trim().length() > 0) {
					tag = line;
					//System.out.println("Znaleziono tag " + tag);
					foundTag = true;
				} else if(foundTag) {
					// Jeśli znaleziono znacznik kończący
					if(tag.compareTo(line) == 0) {
						//System.out.println("Zakończono tag " + tag);
						multiLine = processVelocityString(multiLine, multiLine, context);
						
						 // Dodawanie przetworzonej linii do listy
						for(int s=0, e=0; s<multiLine.length(); s=e+1) {
							e = multiLine.indexOf('\n', s); // Indeks znalezionej frazy
							listAdd.add(multiLine.substring(s, e));
						}
						
						procesFile(tag, listAdd, target);
						//System.out.println("Rozpoczęto wyszukiwanie następnego tagu");
						multiLine = line = tag = "";
						listAdd.clear();
						foundTag = false;
					} else {
						multiLine += line + '\n';
					}
				}
			}
		} catch(IOException e) {
			throw new RuntimeException("Wystąpił błąd podczas próby edycji pliku");
		}
	}
	
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
	
	public void procesFile(String strFind, List<String> listPut, String target) throws IOException {
		
		// Zmienne związane z plikami
		File fileTarget = new File(target);
		String fileContent = new String(Files.readAllBytes(fileTarget.toPath()));
		FileWriter fileModified = new FileWriter(target, false);
		BufferedWriter buffModified = null;
		
		try {
			// Otwieranie pliku, do którego będzie zapisywana zmodyfikowana zawartość
			buffModified = new BufferedWriter(fileModified);
			
			// Zmienne pomicnicze
			int findInd = 0, commentInd = 0, newLineInd = 0, begInd = 0;
			int endInd = fileContent.length()-1;
			char tempChar = 0;
			boolean newLineNotFound = false;
			String whiteSpace;
			
			// Wyszukiwanie wszystkich wystąpień frazy (do końca pliku)
			while(newLineInd != -1) {
				newLineInd = findInd = fileContent.indexOf(strFind, begInd); // Indeks znalezionej frazy
				whiteSpace = "";
				
				if(newLineInd != -1) {
					// Znaleziono szukaną frazę, ustalany jest indeks dla znaku nowej linii
					while(fileContent.charAt(newLineInd) != '\n') {
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
					buffModified.write(fileContent.substring(begInd, newLineInd));
					
					for(String linePut : listPut) {
						if(newLineNotFound) {
							buffModified.write(whiteSpace + linePut); // Jedna linia wstawianego tekstu
							newLineNotFound = false;
						}
						else {
							buffModified.write("\n" + whiteSpace + linePut); // Jedna linia wstawianego tekstu
						}
					}
					// Reszta pliku
					buffModified.write(fileContent.substring(newLineInd, findInd + strFind.length()));
					begInd = findInd + strFind.length();
				}
				else {
					//System.out.println("Nie znaleziono więcej pasujących wyrazów");
					
					// Nie znaleziono szukanej frazy, pozostała część pliku jest przepisywana
					buffModified.write(fileContent.substring(begInd, endInd+1));
				}
			}
		}
		finally {
			if(buffModified != null) {
				buffModified.close();
			}
			
			//System.out.println("Plik został zakmniety");
		}
	}
}
