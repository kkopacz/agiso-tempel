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
import java.util.Collections;
import java.util.Map;

import org.agiso.tempel.Temp;
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
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public abstract class BaseTemplateProviderElement implements ITemplateProviderElement {
	private boolean active;
	private Map<String, Object> globalProperties;

	@Autowired
	private IExpressionEvaluator expressionEvaluator;

	@Autowired
	protected ITempelFileProcessor tempelFileProcessor;

//	--------------------------------------------------------------------------
	@Override
	public void initialize(Map<String, Object> globalProperties) throws IOException {
		this.globalProperties = globalProperties;
	}

//	--------------------------------------------------------------------------
	@Override
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * Przetwarza obiekt odczytany z pliku tempel.xml
	 * 
	 * @param scope
	 * @param object
	 */
	protected void processObject(String scope, Object object, ITemplateRepository templateRepository, ITemplateSourceFactory templateSourceFactory) {
		// Mapa parametrów pliku tempel.xml:
		if(object instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<String, String> scopeProperties = (Map<String, String>)object;
			for(String key : scopeProperties.keySet()) {
				String value = scopeProperties.get(key);
				value = expressionEvaluator.evaluate(value, globalProperties);
				// CHECK: scopeProperties.put(key, value);	// aktualizacja wartości po rozwinięciu
				globalProperties.put(key, value);
			}
			globalProperties.put(scope, Collections.unmodifiableMap(scopeProperties));
			return;
		}

		// Definicja szabloun z pliku tempel.xml:
		if(object instanceof Template) {
			Template template = (Template)object;

			// Ustawianie referencji we wszystkich podszablonach:
			if(template.getResources() != null) {
				for(TemplateResource resource : template.getResources()) {
					resource.setParentTemplateReference(template);
				}
			}

			// Dodawanie do repozytorium (z wywołaniem template.setRepository(...))
			String gId = Temp.StringUtils_emptyIfBlank(template.getGroupId());
			String tId = Temp.StringUtils_emptyIfBlank(template.getTemplateId());
			String ver = Temp.StringUtils_emptyIfBlank(template.getVersion());
			templateRepository.put(null, gId, tId, ver, template);

			String key = template.getKey();
			if(!Temp.StringUtils_isBlank(key)) {
				templateRepository.put(key, null, null, null, template);
			}

			// Na końcu, gdy wywołano template.setRepository(...):
			template.setTemplateSourceFactory(templateSourceFactory);

			return;
		}
	}
}
