/* org.agiso.tempel.core.provider.MainTemplateProvider (02-10-2012)
 * 
 * MainTemplateProvider.java
 * 
 * Copyright 2012 agiso.org
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
import org.agiso.tempel.core.model.Template;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class MainTemplateProvider implements ITemplateProvider {
	private List<ITemplateProviderElement> providers;

//	--------------------------------------------------------------------------
	@Autowired
	public void setTemplateProviderElements(List<ITemplateProviderElement> providers) {
		this.providers = new ArrayList<ITemplateProviderElement>(providers);

		Collections.sort(this.providers, new Comparator<ITemplateProviderElement>() {
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
		for(ITemplateProvider provider : providers) {
			provider.initialize(globalProperties);
		}
	}

	@Override
	public boolean contains(String key, String groupId, String templateId, String version) {
		for(ITemplateProvider provider : providers) {
			if(provider.contains(key, groupId, templateId, version)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Template get(String key, String groupId, String templateId, String version) {
		for(ITemplateProvider provider : providers) {
			if(provider.contains(key, groupId, templateId, version)) {
				return provider.get(key, groupId, templateId, version);
			}
		}
		return null;
	}
}
