/* org.agiso.tempel.support.base.provider.BaseTemplateProviderElement (15-12-2012)
 * 
 * BaseTemplateProviderElement.java
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
package org.agiso.tempel.support.base.provider;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.agiso.core.lang.util.StringUtils;
import org.agiso.tempel.api.ITemplateRepository;
import org.agiso.tempel.api.ITemplateSourceFactory;
import org.agiso.tempel.api.internal.IExpressionEvaluator;
import org.agiso.tempel.api.internal.ITempelFileProcessor;
import org.agiso.tempel.api.internal.ITemplateProviderElement;
import org.agiso.tempel.api.model.Template;
import org.agiso.tempel.api.model.TemplateResource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * 
 * @author Karol Kopacz
 * @since 1.0
 */
public abstract class BaseTemplateProviderElement implements ITemplateProviderElement {
	private boolean active;
	private Map<String, String> properties = new TreeMap<String, String>();

	@Autowired
	private IExpressionEvaluator expressionEvaluator;

	@Autowired
	protected ITempelFileProcessor tempelFileProcessor;

//	--------------------------------------------------------------------------
	@Override
	public Map<String, String> initialize() throws IOException {
		doInitialize();
		return properties;
	}

	@Override
	public void configure(Map<String, Object> properties) throws IOException {
		doConfigure(properties);
	}

	protected abstract void doInitialize() throws IOException;

	protected abstract void doConfigure(Map<String, Object> properties) throws IOException;

//	--------------------------------------------------------------------------
	@Override
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * Przetwarza obiekt odczytany z pliku tempel.xml.
	 * 
	 * @param scope
	 * @param tempelObject Obiekt odczytany z pliku xml. Może być mapą parametrów
	 *     bądź definicją szablonu.
	 * @param objectClassPath Śceżka klas szablonu odczytanego z pliku xml. Ma
	 *     zastosowanie tylko dla szablonów odczytywanych z repozytoriów Maven
	 *     i zawiera zależności zdefiniowane w pliku pom.xml biblioteki szablonu.
	 * @param templateRepository Repozytorium, do którego ma być dodany szablon.
	 * @param templateSourceFactory
	 * 
	 */
	protected void processObject(/* String scope, */ Object tempelObject, Set<String> objectClassPath,
			ITemplateRepository templateRepository, ITemplateSourceFactory templateSourceFactory) {
		// Mapa parametrów pliku tempel.xml:
		if(tempelObject instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<String, String> scopeProperties = (Map<String, String>)tempelObject;
			for(String key : scopeProperties.keySet()) {
				String value = scopeProperties.get(key);
//				value = expressionEvaluator.evaluate(value, properties);
				// CHECK: scopeProperties.put(key, value);	// aktualizacja wartości po rozwinięciu
				properties.put(key, value);
			}
//			properties.put(scope, Collections.unmodifiableMap(scopeProperties));
			return;
		}

		// Definicja szabloun z pliku tempel.xml:
		if(tempelObject instanceof Template) {
			Template<?> template = (Template<?>)tempelObject;

			// Rozbudowa ścieżki klas szablonu o elementy specyficzne dla repozytorium:
			template.extendTemplateClassPath(getRepositoryClassPath());
			// Rozbudowa ścieżki klas szablonu o zależności szablonu (dla repozytoriów typu Maven):
			if(objectClassPath != null) {
				template.extendTemplateClassPath(objectClassPath);
			}

			// Ustawianie referencji we wszystkich podszablonach:
			if(template.getResources() != null) {
				for(TemplateResource resource : template.getResources()) {
					resource.setParentTemplateReference(template);
				}
			}

			// Dodawanie do repozytorium (z wywołaniem template.setRepository(...))
			String gId = StringUtils.emptyIfBlank(template.getGroupId());
			String tId = StringUtils.emptyIfBlank(template.getTemplateId());
			String ver = StringUtils.emptyIfBlank(template.getVersion());
			if(!StringUtils.isBlank(gId)) {
				templateRepository.put(null, gId, tId, ver, template);
			}

			String key = template.getKey();
			if(!StringUtils.isBlank(key)) {
				templateRepository.put(key, null, null, null, template);
			}

			// Na końcu, gdy wywołano template.setRepository(...):
			template.setTemplateSourceFactory(templateSourceFactory);

			return;
		}
	}

	/**
	 * @return
	 */
	protected abstract Set<String> getRepositoryClassPath();
}
