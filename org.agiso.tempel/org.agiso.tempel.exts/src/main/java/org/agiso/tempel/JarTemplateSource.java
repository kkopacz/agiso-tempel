/* org.agiso.tempel.JarTemplateSource (19-12-2012)
 * 
 * JarTemplateSource.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.agiso.tempel.Temp;
import org.agiso.tempel.api.ITemplateSource;
import org.agiso.tempel.api.ITemplateSourceEntry;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class JarTemplateSource implements ITemplateSource {
	private static final String BASE_PATH = "TEMPEL-INF/template/";

//	--------------------------------------------------------------------------
	private JarFile jarFile;
	private JarEntry baseEntry;

	private String template;
	private String resource;

	private int basePathLength;
	private ITemplateSourceEntry mainEntry;

	private Map<String, ITemplateSourceEntry> entries;

//	--------------------------------------------------------------------------
	public JarTemplateSource(String template, String resource) throws IOException {
		this.template = template;
		this.resource = resource;

		// Sprawdzamy istnienie i poprawność pliku .jar:
		File mainFile = new File(template);
		String mainEntryPath = mainFile.getCanonicalPath();
		if(!mainFile.isFile()) {
			throw new IllegalArgumentException("Invalid template jar file: " + mainEntryPath);
		}
		jarFile = new JarFile(mainFile);

		// Wyszukujemy wpis pliku .jar odpowiadający dokładnie wskazanemu zasobowi.
		String basePath = BASE_PATH + Temp.StringUtils_emptyIfBlank(resource);
		Enumeration<JarEntry> jarEntries = jarFile.entries();
		while(jarEntries.hasMoreElements()) {
			JarEntry jarEntry = jarEntries.nextElement();
			if(jarEntry.getName().equals(basePath)) {
				baseEntry = jarEntry;
				break;
			}
		}
		// Jeśli nie znaleziono, a wskazano nazwę plikową zasobu, to próbujemy
		// wyszukiać katalog o takiej nazwie jak zasób:
		if(baseEntry == null && !basePath.endsWith("/")) {
			basePath = basePath + "/";
			jarEntries = jarFile.entries();
			while(jarEntries.hasMoreElements()) {
				JarEntry jarEntry = jarEntries.nextElement();
				if(jarEntry.getName().equals(basePath)) {
					baseEntry = jarEntry;
					break;
				}
			}
		}
		if(baseEntry == null) {
			throw new IllegalArgumentException("Resource "
					+ resource + " not exists in directory " + BASE_PATH);
		}

		entries = new LinkedHashMap<String, ITemplateSourceEntry>();

		if(baseEntry.isDirectory()) {
			basePathLength = baseEntry.getName().length();

			jarEntries = jarFile.entries();
			while(jarEntries.hasMoreElements()) {
				JarEntry jarEntry = jarEntries.nextElement();
				if(jarEntry.getName().startsWith(basePath)) {
					addJarEntry(entries, jarEntry);
				}
			}

			mainEntry = entries.get("/");
		} else {
			basePathLength = BASE_PATH.length();

			mainEntry = addJarEntry(entries, baseEntry);
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
	private ITemplateSourceEntry addJarEntry(Map<String, ITemplateSourceEntry> entries, JarEntry jarEntry) {
		String name = jarEntry.getName().substring(basePathLength);
		if(name.isEmpty()) {
			name = "/";
		}

		ITemplateSourceEntry entry = new JarTemplateSourceEntry(name, jarEntry);
		entries.put(name, entry);
		return entry;
	}

//	--------------------------------------------------------------------------
	private class JarTemplateSourceEntry implements ITemplateSourceEntry {
		private String name;
		private JarEntry jarEntry;

		public JarTemplateSourceEntry(String name, JarEntry jarEntry) {
			this.name = name;
			this.jarEntry = jarEntry;
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
			return true;
		}

		@Override
		public boolean isFile() {
			return !jarEntry.isDirectory();
		}

		@Override
		public boolean isDirectory() {
			return jarEntry.isDirectory();
		}

		@Override
		public InputStream getInputStream() throws Exception {
			return jarFile.getInputStream(jarEntry);
		}
	}
}
