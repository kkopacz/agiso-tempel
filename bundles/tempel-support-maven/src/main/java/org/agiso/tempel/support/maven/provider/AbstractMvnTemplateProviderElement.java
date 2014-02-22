/* org.agiso.tempel.support.maven.provider.AbstractMvnTemplateProviderElement (19-01-2013)
 * 
 * AbstractMvnTemplateProviderElement.java
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
package org.agiso.tempel.support.maven.provider;

import static org.agiso.core.lang.util.AnsiUtils.*;
import static org.agiso.core.lang.util.AnsiUtils.AnsiElement.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.agiso.core.lang.util.ConvertUtils;
import org.agiso.core.lang.util.ObjectUtils;
import org.agiso.core.logging.Logger;
import org.agiso.core.logging.util.LogUtils;
import org.agiso.tempel.api.ITemplateSource;
import org.agiso.tempel.api.impl.JarTemplateSource;
import org.agiso.tempel.api.model.Template;
import org.agiso.tempel.support.base.provider.CachingTemplateProviderElement;

/**
 * 
 * 
 * @author Karol Kopacz
 * @since 1.0
 */
public abstract class AbstractMvnTemplateProviderElement extends CachingTemplateProviderElement {
	private static final Logger logger = LogUtils.getLogger(AbstractMvnTemplateProviderElement.class);

//	--------------------------------------------------------------------------
	private Set<String> classPath = Collections.emptySet();

//	--------------------------------------------------------------------------
	@Override
	public int getOrder() {
		return 40;
	}

	@Override
	public String getScope() {
		return "MAVEN";
	}

//	--------------------------------------------------------------------------
	@Override
	protected String getBasePath() {
		throw new IllegalStateException("Library templates can't have relative dependencies");
	}

	@Override
	@SuppressWarnings("unchecked")
	protected MvnCacheEntry doGet(String key) {
		if(logger.isTraceEnabled()) logger.trace("Preparing cache entry for template {}",
				ansiString(GREEN, key)
		);

		if(key.indexOf(':') <= 0) {
			return null;
		}

		// Wydzielamy z klucza grupę, szablon i wersję:
//		StringTokenizer tokenizer = new StringTokenizer(key, ":", false);
//		String groupId = tokenizer.nextToken();
//		String templateId = tokenizer.nextToken();
//		String version = tokenizer.nextToken();

		// Pobieranie i przegląd bibliotek z repozytorium maven, wyszukiwanie biblioteki
		// szablonu i sprawdzanie czy zawiera plik tempel.xml. Jeśli tak, to jego
		// odczytywanie i parsowanie w celu przygotowania obiektu Template:
		try {
			List<File> files = resolve(key);
			for(File file : files) {
// W trakcie uruchamiania testów bez instalacji Maven tworzy biblioteki szablonów
// w lokalizacji tymczasowej. Nie ma wówczas zgodności nazwy pliku biblioteki z
// konwencją templateId-version.jar (zamiast wersji jest znacznik czasowy).
// (W taki sposób testy uruchamia wykorzystywany w projekcie system 'travis-ci')
//				if(file.getName().equals(templateId + '-' + version + ".jar")) {
					JarEntry tempel_xml = null;
					JarFile jarFile = new JarFile(file);
					try {
						Enumeration<JarEntry> jarEntries = jarFile.entries();
						while(jarEntries.hasMoreElements()) {
							JarEntry jarEntry = jarEntries.nextElement();
							if("TEMPEL-INF/tempel.xml".equals(jarEntry.getName())) {
								tempel_xml = jarEntry;
								break;
							}
						}

						if(tempel_xml == null) {
							return null;					// nie znaleiono pliku tempel.xml
						}

						InputStream is = jarFile.getInputStream(tempel_xml);

						MvnCacheEntry cacheEntry = new MvnCacheEntry();
						cacheEntry.definition = ConvertUtils.toString(is);
						cacheEntry.path = file.getCanonicalPath();
						cacheEntry.classpath = new HashSet<String>();
						for(File classpathFile : files) {
							cacheEntry.classpath.add(classpathFile.getCanonicalPath());
						}

						return cacheEntry;
					} finally {
						if(jarFile != null) {
							jarFile.close();
						}
					}
//				}
			}
		} catch(Exception e) {
			logger.error(e, "Cache entry preparation error for template {}",
					ansiString(GREEN, key)
			);
			return null;
		}

		return null;
	}

	@Override
	protected Set<String> getRepositoryClassPath() {
		return classPath;
	}

	/**
	 * @param fqtn
	 * @return
	 */
	protected abstract List<File> resolve(String fqtn) throws Exception;

//	--------------------------------------------------------------------------
	@Override
	public ITemplateSource createTemplateSource(Template<?> template, String source) {
		try {
			return new JarTemplateSource(getTemplatePath(template), source);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected abstract String getTemplatePath(Template<?> template);

//	--------------------------------------------------------------------------
	public static class MvnCacheEntry extends CacheEntry {
		public String path;

		@Override
		public String toString() {
			return ObjectUtils.toStringBuilder(this);
		}
	}
}
