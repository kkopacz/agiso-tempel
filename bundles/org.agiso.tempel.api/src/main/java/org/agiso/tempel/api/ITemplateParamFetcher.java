/* org.agiso.tempel.api.ITemplateParamFetcher (21-12-2013)
 * 
 * ITemplateParamFetcher.java
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
package org.agiso.tempel.api;

import org.agiso.tempel.api.internal.IParamData;
import org.agiso.tempel.api.internal.IParamReader;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public interface ITemplateParamFetcher<T> {
	/**
	 * Dostarcza wartość parametru, pobierając go z wykorzystaniem dostarczonej
	 * instancji klasy {@link IParamReader}. Opis parametru do dostarczenia
	 * znajduje się w przekazanym obiekcie {@link IParamData}.
	 * 
	 * @param paramReader Reader wykorzystywany do pobierania parametru.
	 * @param paramData Dane opisujące parametr do pobrania.
	 * @return Wartość parametru o typie właściwym dla dostarczyciela.
	 */
	public T fetch(IParamReader paramReader, IParamData paramData);
}
