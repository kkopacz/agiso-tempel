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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.agiso.tempel.api.internal.ITemplateProvider;
import org.agiso.tempel.api.internal.ITemplateProviderElement;
import org.agiso.tempel.api.model.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
@Component
public class CoreTemplateProvider implements ITemplateProvider {
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

		for(ITemplateProviderElement provider : providers) {
			System.out.println(provider.getOrder() + ": " + provider.getClass().getSimpleName());
		}
	}

//	--------------------------------------------------------------------------
	@Override
	public void initialize(Map<String, Object> globalProperties) throws IOException {
		for(ITemplateProviderElement provider : elements) {
			provider.initialize(globalProperties);
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
	public Template get(String key, String groupId, String templateId, String version) {
		for(ITemplateProviderElement provider : elements) {
			if(provider.isActive() && provider.contains(key, groupId, templateId, version)) {
				return provider.get(key, groupId, templateId, version);
			}
		}
		return null;
	}
}
