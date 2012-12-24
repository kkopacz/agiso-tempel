/* org.agiso.tempel.core.FileTemplateSource (19-12-2012)
 * 
 * FileTemplateSource.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.agiso.tempel.Temp;
import org.agiso.tempel.api.ITemplateSource;
import org.agiso.tempel.api.ITemplateSourceEntry;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class FileTemplateSource implements ITemplateSource {
	private static final char FILE_SEPARATOR = System.getProperty("file.separator").charAt(0);

//	--------------------------------------------------------------------------
	private String repository;
	private String template;
	private String resource;

	private int mainEntryPathLength;
	private ITemplateSourceEntry mainEntry;

	private Map<String, ITemplateSourceEntry> entries;

//	--------------------------------------------------------------------------
	public FileTemplateSource(String repository, String template, String resource) throws IOException {
		this.repository = repository;
		this.template = template;
		this.resource = resource;

		// Wyznaczamy ścieżkę i sprawdzamy istnienie katalogu szablonu:
		File mainFile = new File(repository + "/" + template);
		String mainEntryPath = mainFile.getCanonicalPath();
		mainEntryPathLength = mainEntryPath.length() + 1;
		if(!mainFile.isDirectory()) {
			throw new IllegalArgumentException("Invalid template directory: " + mainEntryPath);
		}

		entries = new LinkedHashMap<String, ITemplateSourceEntry>();

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
		} else {
			throw new IllegalArgumentException("Resource "
					+ resource + " not exists in directory " + mainEntryPath);
		}
	}

//	--------------------------------------------------------------------------
	@Override
	public String getRepository() {
		return repository;
	}

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
