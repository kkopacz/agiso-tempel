/* org.agiso.tempel.templates.test.TstTemplateProvider (20-01-2013)
 * 
 * TstTemplateProvider.java
 * 
 * Copyright 2013 agiso.org
 */
package org.agiso.tempel.core.provider;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.agiso.tempel.Temp;
import org.agiso.tempel.api.ITemplateSource;
import org.agiso.tempel.api.internal.ITemplateProviderElement;
import org.agiso.tempel.core.model.Template;
import org.agiso.tempel.core.provider.source.ArchiveTemplateSource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.Node;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class TstTemplateProvider extends CachingTemplateProvider implements ITemplateProviderElement {
	private Map<String, Archive<?>> repository;

//	--------------------------------------------------------------------------
	/**
	 * 
	 */
	public TstTemplateProvider() {
		repository = new HashMap<String, Archive<?>>();
	}

//	--------------------------------------------------------------------------
	@Override
	public int getOrder() {
		return -1;
	}

//	--------------------------------------------------------------------------
	public void addArchive(String groupId, String templateId, String version,
			Archive<?> archive) {
		repository.put(groupId + ":" + templateId + ":" + version, archive);
	}

//	--------------------------------------------------------------------------
	/* (non-Javadoc)
	 * @see org.agiso.tempel.core.provider.AbstractMvnTemplateProvider#doGet(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	protected ArchiveCacheEntry doGet(String key, String groupId, String templateId, String version) {
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
			Archive<?> archive = resolve(groupId, templateId, version);
			if(archive == null) {
				return null;
			}

			Node tempel_xml = archive.get("/TEMPEL-INF/tempel.xml");
			if(tempel_xml == null) {
				return null;					// nie znaleiono pliku tempel.xml
			}

			InputStream is = tempel_xml.getAsset().openStream();

			ArchiveCacheEntry cacheEntry = new ArchiveCacheEntry();
			cacheEntry.definition = Temp.ConvertUtils_convertStreamToString(is);
			cacheEntry.archive = archive;

			return cacheEntry;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	protected Archive<?> resolve(String groupId, String templateId, String version) throws Exception {
		return repository.get(groupId + ":" + templateId + ":" + version);
	}

//	--------------------------------------------------------------------------
	@Override
	public ITemplateSource createTemplateSource(Template template, String source) {
		ArchiveCacheEntry cacheEntry = getCacheEntry(
				template.getGroupId() + ":" + template.getTemplateId() + ":" + template.getVersion()
		);

		try {
			return new ArchiveTemplateSource(cacheEntry.archive, source);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}

//	--------------------------------------------------------------------------
	public static class ArchiveCacheEntry extends CacheEntry {
		public Archive<?> archive;
	}
}
