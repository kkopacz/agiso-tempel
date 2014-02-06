/* org.agiso.tempel.api.impl.FileTemplateSource (19-12-2012)
 * 
 * FileTemplateSource.java
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
package org.agiso.tempel.api.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import org.agiso.tempel.Temp;
import org.agiso.tempel.api.ITemplateSource;
import org.agiso.tempel.api.ITemplateSourceEntry;

/**
 * 
 * 
 * @author Karol Kopacz
 * @since 1.0
 */
public class FileTemplateSource implements ITemplateSource {
	private static final char FILE_SEPARATOR = System.getProperty("file.separator").charAt(0);

//	--------------------------------------------------------------------------
	private String template;
	private String resource;

	private int mainEntryPathLength;
	private ITemplateSourceEntry mainEntry;

	// TODO: Przenieść do klasy bazowej AbstractTemplateSource
	private Map<String, ITemplateSourceEntry> entries;

//	--------------------------------------------------------------------------
	public FileTemplateSource(String template, String resource) throws IOException {
		this.template = template;
		this.resource = resource;

		// Wyznaczamy ścieżkę i sprawdzamy istnienie katalogu szablonu:
		File mainFile = new File(template);
		String mainEntryPath = mainFile.getCanonicalPath();
		mainEntryPathLength = mainEntryPath.length() + 1;
		if(mainFile.exists() && !mainFile.isDirectory()) {
			throw new IllegalArgumentException("Invalid template directory: " + mainEntryPath);
		}

		entries = new TreeMap<String, ITemplateSourceEntry>(new Comparator<String>() {
			@Override
			public int compare(String path1, String path2) {	// mapa musi być posortowana tak, aby
				return path1.compareTo(path2);					// wpisy plików były po katalogach w
			}													// których się znajdują
		});

		// Pobieramy plik odpowiadający wskazanemu zasobowi szablonu i na jego
		// podstawie budujemy listę wszystkich wpsiów szablonu:
		if(!Temp.StringUtils_isBlank(resource)) {
			mainFile = new File(mainEntryPath + "/" + resource);
		}
		if(mainFile.isFile()) {
			mainEntry = addFileEntry(entries, mainFile);
		} else if(mainFile.isDirectory()) {
			mainEntryPath = mainFile.getCanonicalPath();
			mainEntryPathLength = mainEntryPath.length() + 1;
			mainEntry = addDirectoryEntry(entries, mainFile);
		} else if(!Temp.StringUtils_isBlank(resource)) {
			// Plik mainFile nie jest ani plikiem, ani katalogiem więc nie istnieje.
			// Dopuszczamy taką sytuację jeśli nie jest określona wartość resource.
			// Sytuacja ta występuje jeśli silnik nie przetwarza zasobów wejściowych,
			// tylko np. tworzy katalogi (jak silnik MakeDirEngine).
			throw new IllegalArgumentException("Resource "
					+ resource + " not exists in directory " + mainEntryPath);
		}
	}

//	--------------------------------------------------------------------------
	@Override
	public String getTemplate() {
		return template;
	}

	@Override
	public String getResource() {
		return resource;
	}

	@Override
	public boolean exists() {
		return mainEntry.exists();
	}

	@Override
	public boolean isFile() {
		return mainEntry.isFile();
	}

	@Override
	public boolean isDirectory() {
		return mainEntry.isDirectory();
	}

	@Override
	public ITemplateSourceEntry getEntry(String name) {
		return entries.get(name);
	}

	@Override
	public Collection<ITemplateSourceEntry> listEntries() {
		return Collections.unmodifiableCollection(entries.values());
	}

//	--------------------------------------------------------------------------
	private ITemplateSourceEntry addFileEntry(Map<String, ITemplateSourceEntry> entries, final File file) throws IOException {
		String name = file.getCanonicalPath().substring(mainEntryPathLength).replace(FILE_SEPARATOR, '/');

		ITemplateSourceEntry entry = new FileTemplateSourceEntry(name, file);
		entries.put(name, entry);
		return entry;
	}

	private ITemplateSourceEntry addDirectoryEntry(Map<String, ITemplateSourceEntry> entries, File directory) throws IOException {
		String name = directory.getCanonicalPath() + "/";
		if(name.length() > mainEntryPathLength) {
			name = name.substring(mainEntryPathLength).replace(FILE_SEPARATOR, '/');
		} else {
			name = "/";
		}

		ITemplateSourceEntry entry = new FileTemplateSourceEntry(name, directory);
		entries.put(name, entry);

		for(File file : directory.listFiles()) {
			if(file.isFile()) {
				addFileEntry(entries, file);
			} else if(file.isDirectory()) {
				addDirectoryEntry(entries, file);
			} else {
				throw new IllegalStateException("Unknown resource " + file.getCanonicalPath());
			}
		}

		return entry;
	}

//	--------------------------------------------------------------------------
	private class FileTemplateSourceEntry implements ITemplateSourceEntry {
		private String name;
		private File file;

		public FileTemplateSourceEntry(String name, File file) {
			this.name = name;
			this.file = file;
		}

		@Override
		public String getTemplate() {
			return template;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public boolean exists() {
			return file.exists();
		}

		@Override
		public boolean isFile() {
			return file.isFile();
		}

		@Override
		public boolean isDirectory() {
			return file.isDirectory();
		}

		@Override
		public InputStream getInputStream() throws Exception {
			return new FileInputStream(file);
		}
	}
}
