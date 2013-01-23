/* org.agiso.tempel.core.provider.AbstractMvnTemplateProvider (19-01-2013)
 * 
 * AbstractMvnTemplateProvider.java
 * 
 * Copyright 2013 agiso.org
 */
package org.agiso.tempel.core.provider;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.agiso.tempel.Temp;
import org.agiso.tempel.api.ITemplateSource;
import org.agiso.tempel.api.internal.ITemplateProviderElement;
import org.agiso.tempel.core.model.Template;
import org.agiso.tempel.core.provider.source.JarTemplateSource;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public abstract class AbstractMvnTemplateProvider extends CachingTemplateProvider
		implements ITemplateProviderElement {

//	--------------------------------------------------------------------------
	@Override
	public int getOrder() {
		return 40;
	}

//	--------------------------------------------------------------------------
	@Override
	@SuppressWarnings("unchecked")
	protected MvnCacheEntry doGet(String key, String groupId, String templateId, String version) {
		if(key.indexOf(':') <= 0) {
			return null;
		}

		// Wydzielamy z klucza grupę, szablon i wersję:
		StringTokenizer tokenizer = new StringTokenizer(key, ":", false);
		groupId = tokenizer.nextToken();
		templateId = tokenizer.nextToken();
		version = tokenizer.nextToken();

		// Pobieranie i przegląd bibliotek z repozytorium maven, wyszukiwanie biblioteki
		// szablonu i sprawdzanie czy zawiera plik tempel.xml. Jeśli tak, to jego
		// odczytywanie i parsowanie w celu przygotowania obiektu Template:
		try {
			List<File> files = resolve(groupId, templateId, version);
			for(File file : files) {
				if(file.getName().equals(templateId + '-' + version + ".jar")) {
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
						cacheEntry.definition = Temp.ConvertUtils_convertStreamToString(is);
						cacheEntry.path = file.getCanonicalPath();
						cacheEntry.classpath = files;

						return cacheEntry;
					} finally {
						if(jarFile != null) {
							jarFile.close();
						}
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}

		return null;
	}

	/**
	 * @param groupId
	 * @param templateId
	 * @param version
	 * @return
	 */
	protected abstract List<File> resolve(String groupId, String templateId, String version) throws Exception;

//	--------------------------------------------------------------------------
	@Override
	public ITemplateSource createTemplateSource(Template template, String source) {
		try {
			return new JarTemplateSource(getTemplatePath(template), source);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected abstract String getTemplatePath(Template template);

//	--------------------------------------------------------------------------
	public static class MvnCacheEntry extends CacheEntry {
		public String path;
		public List<File> classpath;
	}
}
