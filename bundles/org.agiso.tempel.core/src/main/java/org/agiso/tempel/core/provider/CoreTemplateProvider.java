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

import static org.agiso.tempel.Temp.AnsiUtils.*;
import static org.agiso.tempel.Temp.AnsiUtils.AnsiElement.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.agiso.core.logging.Logger;
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
	private static final Logger logger = LogUtils.getLogger(CoreTemplateProvider.class);

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
			logger.debug("Setting up template provider {} with order {}",
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
			logger.trace("Initializing template provider {} with order {}",
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
			logger.trace("Configuring template provider {} with order {}",
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
		for(ITemplateProviderElement provider : elements) {
			if(provider.isActive() && provider.contains(key, groupId, templateId, version)) {
				return provider.get(key, groupId, templateId, version);
			}
		}
		return null;
	}
}
