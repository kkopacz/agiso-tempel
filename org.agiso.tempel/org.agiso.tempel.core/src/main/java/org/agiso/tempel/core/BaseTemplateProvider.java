/* org.agiso.tempel.core.BaseTemplateProvider (15-12-2012)
 * 
 * BaseTemplateProvider.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.core;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.agiso.tempel.Temp;
import org.agiso.tempel.api.internal.IExpressionEvaluator;
import org.agiso.tempel.api.internal.ITempelFileProcessor;
import org.agiso.tempel.api.internal.ITemplateProvider;
import org.agiso.tempel.api.internal.ITemplateRepository;
import org.agiso.tempel.core.model.Repository;
import org.agiso.tempel.core.model.Template;
import org.agiso.tempel.core.model.TemplateResource;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public abstract class BaseTemplateProvider implements ITemplateProvider {
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
	 * @param templateRepository
	 */
	protected void processObject(Template.Scope scope, Object object, ITemplateRepository templateRepository) {
		// Ścieżka repozytorium pliku tempel.xml:
		if(object instanceof Repository) {
			Repository repository = (Repository)object;

			templateRepository.setRepository(scope, repository);
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
			globalProperties.put(scope.name(), Collections.unmodifiableMap(scopeProperties));
			return;
		}

		// Definicja szabloun z pliku tempel.xml:
		if(object instanceof Template) {
			Template template = (Template)object;
			template.setScope(scope);

			if(template.getResources() != null) {
				for(TemplateResource resource : template.getResources()) {
					resource.setParentTemplateReference(template);
				}
			}

			String gId = Temp.StringUtils_emptyIfBlank(template.getGroupId());
			String tId = Temp.StringUtils_emptyIfBlank(template.getTemplateId());
			String ver = Temp.StringUtils_emptyIfBlank(template.getVersion());
			templateRepository.put(null, gId, tId, ver, template);

			String key = template.getKey();
			if(!Temp.StringUtils_isBlank(key)) {
				templateRepository.put(key, null, null, null, template);
			}
			return;
		}
	}
}
