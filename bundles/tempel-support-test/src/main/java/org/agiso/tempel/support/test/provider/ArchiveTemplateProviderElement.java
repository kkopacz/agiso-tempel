/* org.agiso.tempel.support.test.provider.ArchiveTemplateProviderElement (20-01-2013)
 * 
 * ArchiveTemplateProviderElement.java
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
package org.agiso.tempel.support.test.provider;

import static org.agiso.core.lang.util.AnsiUtils.*;
import static org.agiso.core.lang.util.AnsiUtils.AnsiElement.*;
import static org.agiso.tempel.ITempel.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.agiso.core.i18n.annotation.I18n;
import org.agiso.core.i18n.util.I18nUtils.I18nId;
import org.agiso.core.lang.util.ConvertUtils;
import org.agiso.core.lang.util.ObjectUtils;
import org.agiso.core.logging.I18nLogger;
import org.agiso.core.logging.util.LogUtils;
import org.agiso.tempel.api.ITemplateSource;
import org.agiso.tempel.api.model.Template;
import org.agiso.tempel.support.base.provider.CachingTemplateProviderElement;
import org.agiso.tempel.support.test.provider.source.ArchiveTemplateSource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.Node;
import org.springframework.stereotype.Component;

/**
 * 
 * 
 * @author Karol Kopacz
 * @since 1.0
 */
@Component
public class ArchiveTemplateProviderElement extends CachingTemplateProviderElement
		implements IArchiveTemplateProviderElement {
	private static final I18nLogger<Logs> supportLogger = LogUtils.getLogger(LOGGER_SUPPORT);
	private static enum Logs implements I18nId {
		@I18n(def = "Putting template {0} archive {1}")
		LOG_01,

		@I18n(def = "Preparing cache entry for template {0}")
		LOG_02,

		@I18n(def = "Cache entry preparation error for template {0}")
		LOG_03,

		@I18n(def = "Resolved template {0} archive {1}")
		LOG_04,
	}

	private final Map<String, Archive<?>> repository = new HashMap<String, Archive<?>>();

//	--------------------------------------------------------------------------
	@Override
	public int getOrder() {
		return -20;
	}

	@Override
	public boolean isActive() {
		return true;
	}

	@Override
	public String getScope() {
		return "TEST";
	}

//	--------------------------------------------------------------------------
	@Override
	protected void doInitialize() throws IOException {
	}

	@Override
	protected void doConfigure(Map<String, Object> properties) throws IOException {
	}

//	--------------------------------------------------------------------------
	@Override
	public void addArchive(String key, Archive<?> archive) {
		if(supportLogger.isDebugEnabled()) supportLogger.debug(Logs.LOG_01,
				ansiString(GREEN, key),
				ansiString(GREEN, archive.toString())
		);

		repository.put(key, archive);
	}

//	--------------------------------------------------------------------------
	@Override
	protected String getBasePath() {
		throw new IllegalStateException("Archive templates can't have relative dependencies");
	}

	@Override
	@SuppressWarnings("unchecked")
	protected ArchiveCacheEntry doGet(String key) {
		if(supportLogger.isTraceEnabled()) supportLogger.trace(Logs.LOG_02,
				ansiString(GREEN, key)
		);

		if(key.indexOf(':') <= 0) {
			return null;
		}

		// Pobieranie i przegląd bibliotek z repozytorium maven, wyszukiwanie biblioteki
		// szablonu i sprawdzanie czy zawiera plik tempel.xml. Jeśli tak, to jego
		// odczytywanie i parsowanie w celu przygotowania obiektu Template:
		try {
			Archive<?> archive = resolve(key);
			if(archive == null) {
				return null;
			}

			Node tempel_xml = archive.get("/TEMPEL-INF/tempel.xml");
			if(tempel_xml == null) {
				return null;					// nie znaleiono pliku tempel.xml
			}

			InputStream is = tempel_xml.getAsset().openStream();

			ArchiveCacheEntry cacheEntry = new ArchiveCacheEntry();
			cacheEntry.definition = ConvertUtils.toString(is);
			cacheEntry.archive = archive;

			return cacheEntry;
		} catch(Exception e) {
			supportLogger.error(e, Logs.LOG_03,
					ansiString(GREEN, key)
			);
			return null;
		}
	}

	protected Archive<?> resolve(String key) throws Exception {
		Archive<?> archive = repository.get(key);
		if(supportLogger.isTraceEnabled()) supportLogger.trace(Logs.LOG_04,
				ansiString(GREEN, key),
				ansiString(GREEN, archive == null? null : archive.toString())
		);
		return archive;
	}

	@Override
	protected Set<String> getRepositoryClassPath() {
		return Collections.emptySet();
	}

//	--------------------------------------------------------------------------
	@Override
	public ITemplateSource createTemplateSource(Template<?> template, String source) {
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

		@Override
		public String toString() {
			return ObjectUtils.toStringBuilder(this);
		}
	}
}
