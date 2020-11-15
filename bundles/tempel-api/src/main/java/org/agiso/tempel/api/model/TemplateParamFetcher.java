/* org.agiso.tempel.api.model.TemplateParamFetcher (21-12-2013)
 * 
 * TemplateParamFetcher.java
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
package org.agiso.tempel.api.model;

import org.agiso.tempel.api.ITemplateParamFetcher;

/**
 * 
 * 
 * @author Karol Kopacz
 * @since 1.0
 */
public interface TemplateParamFetcher extends Cloneable {
	/**
	 * Tworzy i zwraca instancję dostarczyciela parametru. Jeśli do jej tworzenia
	 * wymanane jest użycie {@link ClassLoader}'a, to należy go pozyskać z kontekstu
	 * wątku poprzez metodę {@link Thread#getContextClassLoader()}.
	 * 
	 * @return
	 */
	public ITemplateParamFetcher<?> getInstance();

//	--------------------------------------------------------------------------
	public TemplateParamFetcher clone();
}
