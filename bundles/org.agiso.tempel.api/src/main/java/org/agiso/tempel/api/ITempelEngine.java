/* org.agiso.tempel.api.ITempelEngine (14-09-2012)
 * 
 * ITempelEngine.java
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
package org.agiso.tempel.api;

import java.util.Map;


/**
 * Interfejs silnika generatora.
 * 
 * @author Karol Kopacz
 * @since 1.0
 */
public interface ITempelEngine {
	/**
	 * Uruchamia silnik generatora.
	 * 
	 * @param source Ścieżka szablonu do wykorzystania w celu generacji zasobu.
	 * @param params Mapa parametrów szablonu.
	 * @param target Ścieżka docelowa zasobu tworzonego w oparciu o szablon.
	 */
	public void run(ITemplateSource source, Map<String, Object> params, String target);
}
