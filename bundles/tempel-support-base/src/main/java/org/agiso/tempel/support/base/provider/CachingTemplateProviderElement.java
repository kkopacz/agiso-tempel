/* org.agiso.tempel.support.base.provider.CachingTemplateProviderElement (21-01-2013)
 * 
 * CachingTemplateProviderElement.java
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
package org.agiso.tempel.support.base.provider;

import static org.agiso.core.lang.util.AnsiUtils.*;
import static org.agiso.core.lang.util.AnsiUtils.AnsiElement.*;
import static org.agiso.tempel.ITempel.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.agiso.core.i18n.annotation.I18n;
import org.agiso.core.i18n.util.I18nUtils.I18nId;
import org.agiso.core.lang.annotation.InToString;
import org.agiso.core.logging.I18nLogger;
import org.agiso.core.logging.util.LogUtils;
import org.agiso.tempel.api.ITemplateRepository;
import org.agiso.tempel.api.ITemplateSourceFactory;
import org.agiso.tempel.api.internal.ITempelEntryProcessor;
import org.agiso.tempel.api.model.Template;
import org.agiso.tempel.support.base.repository.HashBasedTemplateRepository;

/**
 * 
 * 
 * @author Karol Kopacz
 * @since 1.0
 */
public abstract class CachingTemplateProviderElement extends BaseTemplateProviderElement implements ITemplateSourceFactory {
	private static final I18nLogger<Logs> supportLogger = LogUtils.getLogger(LOGGER_SUPPORT);
	private static enum Logs implements I18nId {
		@I18n(def = "Definition successfully processed for entry {0}")
		LOG_01,

		@I18n(def = "Error processing definition for entry {0}")
		LOG_02,

		@I18n(def = "Caching entry {0} for template {1}")
		LOG_03,
	}

//	--------------------------------------------------------------------------
	private final Map<String, CacheEntry> cache = new HashMap<String, CacheEntry>();

//	--------------------------------------------------------------------------
	@Override
	public boolean contains(String key, String groupId, String templateId, String version) {
		doChacheEntry(key);

		return null != getCacheEntry(key);
	}

	@Override
	public Template<?> get(String key, String groupId, String templateId, String version) {
		doChacheEntry(key);

		final CacheEntry cacheEntry = getCacheEntry(key);
		if(cacheEntry == null) {
			return null;
		}

		if(cacheEntry.repository == null) {
			final ITemplateRepository templateRepository = new HashBasedTemplateRepository();

			try {
				tempelFileProcessor.process(cacheEntry.definition, new ITempelEntryProcessor() {
					@Override
					public void processObject(Object object) {
						CachingTemplateProviderElement.this.processObject(object, cacheEntry.classpath,
								templateRepository, CachingTemplateProviderElement.this
						);
					}
				});
				if(supportLogger.isTraceEnabled()) supportLogger.trace(Logs.LOG_01,
						ansiString(GREEN, cacheEntry.toString())
				);
			} catch(Exception e) {
				supportLogger.error(e, Logs.LOG_02,
						ansiString(GREEN, cacheEntry.toString())
				);
				throw new RuntimeException(e);
			}

			cacheEntry.repository = templateRepository;
		}

		return cacheEntry.repository.get(key, groupId, templateId, version);
	}

	private void doChacheEntry(String key) {
		if(!cache.containsKey(key)) {
			cache.put(key, doGet(key));
			if(supportLogger.isTraceEnabled()) {
				CacheEntry entry = cache.get(key);
				supportLogger.trace(Logs.LOG_03,
						ansiString(GREEN, entry == null? "null" : entry.toString()),
						ansiString(GREEN, key)
				);
			}
		}
	}

	@SuppressWarnings("unchecked")
	protected final <T extends CacheEntry> T getCacheEntry(String key) {
		return (T)cache.get(key);
	}

//	--------------------------------------------------------------------------
	protected abstract <T extends CacheEntry> T doGet(String key);

//	--------------------------------------------------------------------------
	public static class CacheEntry {
		/**
		 * Definicja XML szablonu
		 */
		@InToString(ignore = true)
		public String definition;
		/**
		 * Repozytorium, w którym znajduje się szablon
		 */
		public ITemplateRepository repository;
		/**
		 * Dodatkowe elementy ścieżki klas szablonu
		 */
		public Set<String> classpath;
	}
}
