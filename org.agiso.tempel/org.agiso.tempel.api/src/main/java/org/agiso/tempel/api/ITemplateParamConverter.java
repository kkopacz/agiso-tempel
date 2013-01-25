/* org.agiso.tempel.api.ITemplateParamConverter (19-09-2012)
 * 
 * ITemplateParamConverter.java
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

/**
 * Interfejs konwertera parametrów.
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public interface ITemplateParamConverter<T> {
	/**
	 * Konwertuje wartość łańcuchową parametru na typ właściwy dla implementacji
	 * konwertera.
	 * 
	 * @param value Wartość łańcuchowa parametru do konwersji.
	 * @return Wartość parametru o typie właściwym dla konwertera.
	 */
	public T convert(String value);
}
