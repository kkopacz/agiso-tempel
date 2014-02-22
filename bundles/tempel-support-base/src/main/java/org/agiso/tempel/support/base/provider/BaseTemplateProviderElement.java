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

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.agiso.core.lang.util.StringUtils;
import org.agiso.tempel.api.ITemplateClassPathExtender;
import org.agiso.tempel.api.ITemplateRepository;
import org.agiso.tempel.api.ITemplateSourceFactory;
import org.agiso.tempel.api.internal.IExpressionEvaluator;
import org.agiso.tempel.api.internal.ITempelDependencyResolver;
import org.agiso.tempel.api.internal.ITempelFileProcessor;
import org.agiso.tempel.api.internal.ITemplateProviderElement;
import org.agiso.tempel.api.model.TempelDependency;
import org.agiso.tempel.api.model.Template;
import org.agiso.tempel.api.model.TemplateReference;
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
	private Set<String> templates = new HashSet<String>();
	private Map<String, String> properties = Collections.emptyMap();
	private List<TempelDependency> dependencies = Collections.emptyList();

	@Autowired
	private IExpressionEvaluator expressionEvaluator;

	@Autowired
	protected ITempelFileProcessor tempelFileProcessor;

	@Autowired(required = false)
	protected ITempelDependencyResolver tempelDependencyResolver;

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
	 * @param tempelObject Obiekt odczytany z pliku xml.
	 *     Może być listą zależności, mapą parametrów bądź definicją szablonu.
	 * @param objectClassPath Śceżka klas szablonu odczytanego z pliku xml. Ma
	 *     zastosowanie tylko dla szablonów odczytywanych z repozytoriów Maven
	 *     i zawiera zależności zdefiniowane w pliku pom.xml biblioteki szablonu.
	 * @param templateRepository Repozytorium, do którego ma być dodany szablon.
	 * @param templateSourceFactory
	 * 
	 */
	@SuppressWarnings("unchecked")
	protected void processObject(/* String scope, */ Object tempelObject, Set<String> objectClassPath,
			ITemplateRepository templateRepository, ITemplateSourceFactory templateSourceFactory) {
		// Mapa parametrów pliku tempel.xml:
		if(tempelObject instanceof Map) {
			properties = new TreeMap<String, String>();

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

		// Mapa zależności pliku tempel.xml
		if(tempelObject instanceof List) {
			dependencies = (List<TempelDependency>)tempelObject;
			return;
		}

		// Definicja szabloun z pliku tempel.xml:
		if(tempelObject instanceof Template) {
			Template<?> template = (Template<?>)tempelObject;

			// Rozbudowa ścieżki klas szablonu o elementy specyficzne dla repozytorium:
			template.addTemplateClassPathExtender(new RepositoryTemplateClassPathExtender());
			template.addTemplateClassPathExtender(new DependenciesTemplateClassPathExtender());
			// Rozbudowa ścieżki klas szablonu o zależności szablonu (dla repozytoriów typu Maven):
			template.addTemplateClassPathExtender(new FixedSetTemplateClassPathExtender(objectClassPath));

//			List<TemplateReference> referenceResources = template.getReferences();
//			if(referenceResources != null && !referenceResources.isEmpty()) {
//				template.addTemplateClassPathExtender(new ReferenceDependenciesTemplateClassPathExtender(referenceResources));
//			}

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
				templates.add(gId + ":" + tId + ":" + ver);
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

	protected abstract String getBasePath();

	/**
	 * @return
	 */
	protected abstract Set<String> getRepositoryClassPath();

//	--------------------------------------------------------------------------
	private class RepositoryTemplateClassPathExtender extends FixedSetTemplateClassPathExtender {
		@Override
		public Set<String> getClassPathEntries() {
			if(classPathEntries == null) {
				classPathEntries = getRepositoryClassPath();
			}
			return classPathEntries;
		}
	}

	private class DependenciesTemplateClassPathExtender extends FixedSetTemplateClassPathExtender {
		@Override
		public Set<String> getClassPathEntries() {
			if(classPathEntries == null) {
				classPathEntries = getDependenciesClassPath();
			}
			return classPathEntries;
		}

		private Set<String> getDependenciesClassPath() {
			Set<String> dependenciesClassPath = new LinkedHashSet<String>();
			for(TempelDependency dependency : dependencies) {
				String relativePath = dependency.getRelativePath();
				if(StringUtils.isNotEmpty(relativePath)) {
					File dependencyJar = new File(getBasePath() + "/" + relativePath);
					if(!dependencyJar.isFile()) {
						throw new RuntimeException("Error resolving dependency " +
								dependencyJar.getAbsolutePath() +"; " +
								"dependency file not found");
					}
					dependenciesClassPath.add(dependencyJar.getAbsolutePath());
				} else if(tempelDependencyResolver != null) {
					for(File dependencyJar : tempelDependencyResolver.resolve(
							dependency.getGroupId(),
							dependency.getArtifactId(),
							dependency.getVersion())) {
						if(!dependencyJar.isFile()) {
							throw new RuntimeException("Error resolving dependency " +
									dependencyJar.getAbsolutePath() +"; " +
									"dependency file not found");
						}
						dependenciesClassPath.add(dependencyJar.getAbsolutePath());
					}
				} else {
					throw new RuntimeException("Error resolving dependency " +
							dependency.getGroupId() + ":" +
							dependency.getArtifactId() + ":" + 
							dependency.getVersion() + "; " +
							"no dependency resolver defined"
					);
				}
			}
			return dependenciesClassPath;
		}
	}

	private class ReferenceDependenciesTemplateClassPathExtender extends FixedSetTemplateClassPathExtender {
		private final List<TemplateReference> references;

		public ReferenceDependenciesTemplateClassPathExtender(List<TemplateReference> references) {
			this.references = references;
		}

		@Override
		public Set<String> getClassPathEntries() {
			if(classPathEntries == null) {
				classPathEntries = getReferenceDependenciesClassPath();
			}
			return classPathEntries;
		}

		private Set<String> getReferenceDependenciesClassPath() {
			Set<String> dependenciesClassPath = new LinkedHashSet<String>();
			for(TemplateReference reference : references) {
				if(templates.contains(reference.getGroupId() + ":" +
						reference.getTemplateId() + ":" + 
						reference.getVersion())) {
					// szablon, do którego odnosi się referencja jest
					// zdefiniowany w tym samym pliku; nie dodajemy do cp:
					continue;
				}

				if(tempelDependencyResolver != null) {
					for(File dependencyJar : tempelDependencyResolver.resolve(
							reference.getGroupId(),
							reference.getTemplateId(),
							reference.getVersion())) {
						if(!dependencyJar.isFile()) {
							throw new RuntimeException("Error resolving reference dependency " +
									dependencyJar.getAbsolutePath() +"; " +
									"reference dependency file not found");
						}
						dependenciesClassPath.add(dependencyJar.getAbsolutePath());
					}
				} else {
					throw new RuntimeException("Error resolving reference dependency " +
							reference.getGroupId() + ":" +
							reference.getTemplateId() + ":" + 
							reference.getVersion() + "; " +
							"no dependency resolver defined"
					);
				}
			}
			return dependenciesClassPath;
		}
	}
}

class FixedSetTemplateClassPathExtender implements ITemplateClassPathExtender {
	protected Set<String> classPathEntries;

	protected FixedSetTemplateClassPathExtender() {
	}
	public FixedSetTemplateClassPathExtender(Set<String> classPathEntries) {
		if(classPathEntries == null) {
			this.classPathEntries = Collections.emptySet();
		} else {
			this.classPathEntries = Collections.unmodifiableSet(classPathEntries);
		}
	}

	@Override
	public Set<String> getClassPathEntries() {
		return classPathEntries;
	}
}
