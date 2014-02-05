/* org.agiso.tempel.api.internal.ITemplateProvider (02-10-2012)
 * 
 * ITemplateProvider.java
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

import java.io.IOException;
import java.util.Map;

import org.agiso.tempel.ITempel;
import org.agiso.tempel.api.model.Template;

/**
 * Interfejs dostarczyciela szablonów. Jego implementacja odpowiada za wyszukiwanie
 * i dostarczanie szablonów do wykonania.
 * 
 * @author Karol Kopacz
 * @since 1.0
 */
public interface ITemplateProvider {
	/**
	 * Inicjalizuje dostarczyciela szablonów przed pobraniem i uruchomieniem pierwszego
	 * szablonu do wykonania (każdorazowo w metodzie {@link ITempel#startTemplate(String,
	 * Map, String)}. W trakcie inicjalizacji może korzystać z przekazanej mapy parametrów,
	 * może też do niej dodawać własne parametry (np. odczytane z pliku konfiguracyjnego
	 * dostarczyciela).
	 * 
	 * @param properties Mapa parametrów środowiska/uruchomienia narzędzia Tempel.
	 * @throws IOException W razie błędu przetwarzania zasobów wymaganych do inicjalizacji.
	 */
	public void initialize(Map<String, Object> properties) throws IOException;

	/**
	 * Inicjalizuje dostarczyciela szablonów przed pobraniem i uruchomieniem pierwszego
	 * szablonu do wykonania (każdorazowo w metodzie {@link ITempel#startTemplate(String,
	 * Map, String)}. W trakcie inicjalizacji może korzystać z przekazanej mapy parametrów,
	 * może też do niej dodawać własne parametry (np. odczytane z pliku konfiguracyjnego
	 * dostarczyciela).
	 * 
	 * @param properties Mapa parametrów środowiska/uruchomienia narzędzia Tempel.
	 * @throws IOException W razie błędu przetwarzania zasobów wymaganych do inicjalizacji.
	 */
	public void configure(Map<String, Object> properties) throws IOException;

	/**
	 * @param key
	 * @param groupId
	 * @param templateId
	 * @param version
	 * @return
	 */
	public boolean contains(String key, String groupId, String templateId, String version);

	/**
	 * @param key
	 * @param groupId
	 * @param templateId
	 * @param version
	 * @return
	 */
	public Template<?> get(String key, String groupId, String templateId, String version);
}
