/* org.agiso.tempel.api.internal.ITemplateExecutor (02-10-2012)
 * 
 * ITemplateExecutor.java
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
package org.agiso.tempel.api.internal;

import java.util.Map;

/**
 * 
 * 
 * @author Karol Kopacz
 * @since 1.0
 */
public interface ITemplateExecutor {
	/**
	 * Umożliwia modyfikację standardowej implemtacji {@link IParamReader}'a
	 * odpowiedzialnego za odczyt parametrów wejściowych wymaganych do wykonania
	 * szablonu.
	 * </br>
	 * Metoda wykorzystywana głównie w kodzie testowym w celu wstrzyknięcia
	 * instancji klasy {@link IParamReader} (lub jej pozornej implementacji),
	 * która automatycznie dostarcza wartości parametrów, które przy normalnym
	 * uruchomieniu szablonu podawane są ręcznie w konsoli ekranowej.
	 * 
	 * @param paramReader {@link IParamReader} dostarczający wartości parametrów.
	 */
	public void setParamReader(IParamReader paramReader);

	public void executeTemplate(String templateName, Map<String, Object> properties, String workDir) throws Exception;
}
