/* org.agiso.tempel.api.ITemplateSource (19-12-2012)
 * 
 * ITemplateSource.java
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

import java.util.Collection;

/**
 * Interfej źródła szablonu.
 * </br>
 * Źródło szablonu stanowi reprezentację struktury zasobów szablonu (które są
 * przetwarzane przez silnik generacji szablonu) niezależną od typu szablonu,
 * jego lokalizacji i sposobu przechowywania w repozytorium. Umożliwia taki
 * sam sposób przetarzania szablonów ze standardowych repozytorów plikowych
 * (zwykła struktura plików i katalogów dyskowych), szablonów z repozytoriów
 * Apache Maven (archiwa jar o odpowiedniej strukturze wewnętrznej) i innych.
 * 
 * @author <a href="mailto:kkopaczz@agiso.org">Karol Kopacz</a>
 */
public interface ITemplateSource {
	public String getTemplate();

	public String getResource();

//	--------------------------------------------------------------------------
	public boolean exists();

	public boolean isFile();

	public boolean isDirectory();

//	--------------------------------------------------------------------------
	/**
	 * Zwraca kolekcję wpisów źródła szablonu. Kolejność elementów kolekcji
	 * zwracanych przez jej iterator musi być zgodna ze strukturą drzewa
	 * katalogowego zasobów źródła szablonu, tzn. wpis reprezentujący katalog
	 * musi się znaleźć przed wpisami opisującymi jego zawartość.
	 * 
	 * @return Kolekcja wpisów źródła szablonu.
	 */
	public Collection<ITemplateSourceEntry> listEntries();

	public ITemplateSourceEntry getEntry(String name);
}
