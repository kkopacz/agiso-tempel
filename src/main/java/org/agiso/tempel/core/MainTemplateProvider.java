/* org.agiso.tempel.core.MainTemplateProvider (02-10-2012)
 * 
 * MainTemplateProvider.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.agiso.tempel.api.internal.ITemplateProvider;
import org.agiso.tempel.core.model.Template;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class MainTemplateProvider implements ITemplateProvider {
	private List<ITemplateProvider> providers = new ArrayList<ITemplateProvider>();

//	--------------------------------------------------------------------------
	public MainTemplateProvider() {
		// Budowanie mapy szablonów w oparciu o pliki konfiguracyjne templates.xml
		// w katalogu konfiguracyjnym aplikacji, katalogu domowym użytkownika oraz
		// katalogu bieżącym (katalogu uruchomienia):
		providers.add(new RunTemplateProvider());
		providers.add(new UsrTemplateProvider());
		providers.add(new AppTemplateProvider());
		providers.add(new MvnTemplateProvider());
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
