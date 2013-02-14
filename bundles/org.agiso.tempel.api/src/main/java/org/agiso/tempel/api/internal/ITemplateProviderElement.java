/* org.agiso.tempel.api.internal.ITemplateProviderElement (07-01-2013)
 * 
 * ITemplateProviderElement.java
 * 
 * Copyright 2013 agiso.org
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
package org.agiso.tempel.api.internal;

import java.io.IOException;
import java.util.Map;

import org.agiso.tempel.api.model.Template;

/**
 * Interfejs elementu dostarczyciela szablonów.
 * </br>
 * Implementacje tego interfejsu odpowiadają za dostarczanie obiektów szablonów
 * z repozytoriów które obsługują (globalnego, użytkownika, uruchomieniowego i
 * repozytorium Apache Maven). Jedna implementacja obsługuje jedno repozytorium.
 * </br>
 * Wykorzystywane są przez globalnego dostarczyciela szablonów, który odpowiada
 * za wyszukiwanie i pobieranie szablonów. Wywołuje on aktywne (te, dla których
 * metoda {@link #isActive()} zwraca wartość <code>true</code>) instancje elemenów
 * w kolejności określonej na podstawie wartości zwracanej przez metodę {@link
 * #getOrder()}. Zwraca pierwszy ze znalezionych szablonów odpowiadający zadanym
 * kryteriom (groupId, templateId, version).
 * 
 * @see ITemplateProvider
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public interface ITemplateProviderElement {
	public int getOrder();

	public boolean isActive();

	/**
	 * @see org.agiso.tempel.api.internal.ITemplateProvider#initialize(Map)
	 */
	public void initialize(Map<String, Object> properties) throws IOException;

	/**
	 * @see org.agiso.tempel.api.internal.ITemplateProvider#contains(String, String, String, String)
	 */
	public boolean contains(String key, String groupId, String templateId, String version);

	/**
	 * @see org.agiso.tempel.api.internal.ITemplateProvider#get(String, String, String, String)
	 */
	public Template get(String key, String groupId, String templateId, String version);
}
