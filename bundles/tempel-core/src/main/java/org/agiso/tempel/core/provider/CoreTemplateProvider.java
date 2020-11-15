/* org.agiso.tempel.core.provider.CoreTemplateProvider (02-10-2012)
 * 
 * CoreTemplateProvider.java
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
package org.agiso.tempel.core.provider;

import static org.agiso.core.lang.util.AnsiUtils.*;
import static org.agiso.core.lang.util.AnsiUtils.AnsiElement.*;
import static org.agiso.tempel.ITempel.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.agiso.core.i18n.annotation.I18n;
import org.agiso.core.i18n.util.I18nUtils.I18nId;
import org.agiso.core.logging.I18nLogger;
import org.agiso.core.logging.util.LogUtils;
import org.agiso.tempel.api.internal.ITemplateProvider;
import org.agiso.tempel.api.internal.ITemplateProviderElement;
import org.agiso.tempel.api.model.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 * 
 * @author Karol Kopacz
 * @since 1.0
 */
@Component
public class CoreTemplateProvider implements ITemplateProvider {
	private static final I18nLogger<Logs> coreLogger = LogUtils.getLogger(LOGGER_CORE);
	private static enum Logs implements I18nId {
		@I18n(def = "Setting up template provider {0} with order {1}")
		LOG_01,

		@I18n(def = "Initializing template provider {0} with order {1}")
		LOG_02,

		@I18n(def = "Configuring template provider {0} with order {1}")
		LOG_03,

		@I18n(def = "Looking for template {0}")
		LOG_04,
	}

	@SuppressWarnings("unchecked")
	private List<ITemplateProviderElement> elements = Collections.EMPTY_LIST;

//	--------------------------------------------------------------------------
	@Autowired(required = false)
	public void setTemplateProviderElements(List<ITemplateProviderElement> providers) {
		this.elements = new ArrayList<ITemplateProviderElement>(providers);

		Collections.sort(this.elements, new Comparator<ITemplateProviderElement>() {
			@Override
			public int compare(ITemplateProviderElement e1, ITemplateProviderElement e2) {
				int o1 = e1.getOrder(), o2 = e2.getOrder();
				return ((o1 < o2)? -1 : ((o1 == o2)? 0 : 1));
			}
		});

		for(ITemplateProviderElement provider : elements) {
			if(coreLogger.isDebugEnabled()) coreLogger.debug(Logs.LOG_01,
					ansiString(GREEN, provider.getClass().getSimpleName()),
					ansiString(GREEN, provider.getOrder())
			);
		}
	}

//	--------------------------------------------------------------------------
	@Override
	public void initialize(Map<String, Object> properties) throws IOException {
		for(int index = elements.size() - 1; index >= 0; index--) {
			ITemplateProviderElement provider = elements.get(index);
			if(coreLogger.isTraceEnabled()) coreLogger.trace(Logs.LOG_02,
					ansiString(GREEN, provider.getClass().getSimpleName()),
					ansiString(GREEN, provider.getOrder())
			);
			Map<String, String> providerProperties = provider.initialize();
			if(providerProperties != null) {
				properties.putAll(providerProperties);
				properties.put(provider.getScope(), Collections.unmodifiableMap(providerProperties));
			}
		}
	}

	@Override
	public void configure(Map<String, Object> properties) throws IOException {
		for(ITemplateProviderElement provider : elements) {
			if(coreLogger.isTraceEnabled()) coreLogger.trace(Logs.LOG_03,
					ansiString(GREEN, provider.getClass().getSimpleName()),
					ansiString(GREEN, provider.getOrder())
			);
			provider.configure(properties);
		}
	}

	@Override
	public boolean contains(String key, String groupId, String templateId, String version) {
		for(ITemplateProviderElement provider : elements) {
			if(provider.isActive() && provider.contains(key, groupId, templateId, version)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Template<?> get(String key, String groupId, String templateId, String version) {
		if(coreLogger.isDebugEnabled()) coreLogger.debug(Logs.LOG_04,
				ansiString(GREEN, key + ": " + groupId +":" + templateId + ":" + version)
		);

		for(ITemplateProviderElement provider : elements) {
			if(provider.isActive() && provider.contains(key, groupId, templateId, version)) {
				return provider.get(key, groupId, templateId, version);
			}
		}
		return null;
	}
}
