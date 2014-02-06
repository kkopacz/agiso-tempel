/* org.agiso.tempel.api.internal.IParamReader (13-02-2013)
 * 
 * IParamReader.java
 * 
 * Copyright 2013 PPW 'ARAJ' Sp. z o. o.
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

/**
 * Obsługuje polecenia odczytu parametrów przygotowania do wywołania szablonu.
 * Parametry te znajdują się w sekcji &lt;params&gt; definicji szablonu.
 * 
 * @author Karol Kopacz
 * @since 1.0
 */
public interface IParamReader {
	/**
	 * @param key
	 * @param name
	 * @param value
	 * @return
	 */
	public String getParamValue(String key, String name, String value);
}
