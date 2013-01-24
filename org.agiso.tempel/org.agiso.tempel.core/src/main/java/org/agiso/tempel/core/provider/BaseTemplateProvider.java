/* org.agiso.tempel.core.provider.BaseTemplateProvider (15-12-2012)
 * 
 * BaseTemplateProvider.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.core.provider;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.agiso.tempel.Temp;
import org.agiso.tempel.api.internal.IExpressionEvaluator;
import org.agiso.tempel.api.internal.ITempelFileProcessor;
import org.agiso.tempel.api.internal.ITempelScopeInfo;
import org.agiso.tempel.api.internal.ITemplateProvider;
import org.agiso.tempel.api.internal.ITemplateRepository;
import org.agiso.tempel.core.TempelScopeInfo;
import org.agiso.tempel.core.VelocityExpressionEvaluator;
import org.agiso.tempel.core.XStreamTempelFileProcessor;
import org.agiso.tempel.core.model.ITemplateSourceFactory;
import org.agiso.tempel.core.model.Repository;
import org.agiso.tempel.core.model.Template;
import org.agiso.tempel.core.model.TemplateResource;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public abstract class BaseTemplateProvider implements ITemplateProvider {
	protected String repository;

	// FIXME: Zastosować wstrzykiwanie zależności
	protected ITempelScopeInfo tempelScopeInfo = new TempelScopeInfo();
	protected ITempelFileProcessor tempelFileProcessor = new XStreamTempelFileProcessor();

	private Map<String, Object> globalProperties;
	private IExpressionEvaluator expressionEvaluator = new VelocityExpressionEvaluator();

//	--------------------------------------------------------------------------
	@Override
	public void initialize(Map<String, Object> globalProperties) throws IOException {
		this.globalProperties = globalProperties;
	}

//	--------------------------------------------------------------------------
	/**
	 * Przetwarza obiekt odczytany z pliku tempel.xml
	 * 
	 * @param scope
	 * @param object
	 */
	protected void processObject(String scope, Object object, ITemplateRepository templateRepository, ITemplateSourceFactory templateSourceFactory) {
		// Ścieżka repozytorium pliku tempel.xml:
		if(object instanceof Repository) {
			Repository repository = (Repository)object;

			this.repository = repository.getValue();
			return;
		}

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
