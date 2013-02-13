/* org.agiso.tempel.support.test.provider.source.ArchiveTemplateSource (21-01-2013)
 * 
 * ArchiveTemplateSource.java
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
package org.agiso.tempel.support.test.provider.source;

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
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.Node;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class ArchiveTemplateSource implements ITemplateSource {
	private static final String BASE_PATH = "/TEMPEL-INF/template";

//	--------------------------------------------------------------------------
	private Archive<?> archive;
	private Node baseEntry;

	private String resource;

	private int basePathLength;
	private ITemplateSourceEntry mainEntry;

	// TODO: Przenieść do klasy bazowej AbstractTemplateSource
	private Map<String, ITemplateSourceEntry> entries;

//	--------------------------------------------------------------------------
	public ArchiveTemplateSource(Archive<?> archive, String resource) throws IOException{
		this.archive = archive;
		this.resource = resource;

		String basePath = BASE_PATH;
		if(!Temp.StringUtils_isBlank(resource)) {
			basePath = basePath + "/" + Temp.StringUtils_emptyIfBlank(resource);
			if(basePath.endsWith("/")) {
				basePath.substring(0, basePath.length() - 1);
			}
		}

		// Wyszukujemy wpis w archiwum odpowiadający wskazanemu zasobowi:
		if(archive.contains(basePath)) {
			baseEntry = archive.get(basePath);
		}
		if(baseEntry == null) {
			if(Temp.StringUtils_isBlank(resource)) {
				// Wpis baseEntry nie istnieje. Dopuszczamy taką sytuację jeśli nie jest
				// określona wartość resource. Sytuacja ta występuje jeśli silnik nie
				// przetwarza zasobów wejściowych, tylko np. tworzy katalogi (jak silnik
				// MakeDirEngine).
				return;
			}
			throw new IllegalArgumentException("Resource "
					+ resource + " not exists in directory " + BASE_PATH);
		}

		entries = new TreeMap<String, ITemplateSourceEntry>(new Comparator<String>() {
			@Override
			public int compare(String path1, String path2) {	// mapa musi być posortowana tak, aby
				return path1.compareTo(path2);					// wpisy plików były po katalogach w
			}													// których się znajdują
		});

		if(baseEntry.getAsset() == null) {
			basePathLength = baseEntry.getPath().get().length();

			Map<ArchivePath, Node> content = archive.getContent();
			for(ArchivePath path : content.keySet()) {
				if(path.get().startsWith(basePath)) {
					addJarEntry(entries, content.get(path));
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
		return archive.getName();
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
		return entries.get("/" + name);
	}

	@Override
	public Collection<ITemplateSourceEntry> listEntries() {
		return Collections.unmodifiableCollection(entries.values());
	}

//	--------------------------------------------------------------------------
	private ITemplateSourceEntry addJarEntry(Map<String, ITemplateSourceEntry> entries, Node jarEntry) {
		String name = jarEntry.getPath().get().substring(basePathLength);
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
		private Node jarEntry;

		public JarTemplateSourceEntry(String name, Node jarEntry) {
			this.name = name;
			this.jarEntry = jarEntry;
		}


		@Override
		public String getTemplate() {
			return archive.getName();
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
			return jarEntry.getAsset() != null;
		}

		@Override
		public boolean isDirectory() {
			return jarEntry.getAsset() == null;
		}

		@Override
		public InputStream getInputStream() throws Exception {
			return jarEntry.getAsset().openStream();
		}
	}
}
