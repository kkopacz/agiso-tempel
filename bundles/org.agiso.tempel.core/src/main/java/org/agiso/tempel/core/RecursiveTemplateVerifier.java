/* org.agiso.tempel.core.RecursiveTemplateVerifier (02-10-2012)
 * 
 * RecursiveTemplateVerifier.java
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
package org.agiso.tempel.core;

import java.util.Iterator;
import java.util.LinkedHashSet;

import org.agiso.tempel.api.ITempelEngine;
import org.agiso.tempel.api.internal.ITemplateProvider;
import org.agiso.tempel.api.internal.ITemplateVerifier;
import org.agiso.tempel.api.model.Template;
import org.agiso.tempel.api.model.TemplateReference;
import org.springframework.stereotype.Component;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
@Component
public class RecursiveTemplateVerifier implements ITemplateVerifier {
	@Override
	public void verifyTemplate(Template template, ITemplateProvider templateProvider) throws Exception {
		verifyTemplate(template, new LinkedHashSet<String>());
	}

	/**
	 * Weryfikuje poprawność szablonu, szablonu nadrzędnego i rekurencyjne
	 * sprawdza wszystkie szablony używane. Kontroluje, czy drzewie wywołań
	 * szablonów nie występuje zapętlenie.
	 * 
	 * @param template Szablon do weryfikacji.
	 * @param templates Zbiór identyfikatorów szablonów gałęzi. Wykorzystywany
	 *     do wykrywania zapętleń wywołań.
	 */
	private void verifyTemplate(Template template, LinkedHashSet<String> templates) {
		String id = template.getKey();

		// Sprawdzanie, czy w gałęzi wywołań szablonów nie ma zapętlenia:
		if(templates.contains(id)) {
			// Wyświetlanie gałęzi z zapętleniem i wyrzucanie wyjątku:
			Iterator<String> t = templates.iterator();
			System.out.print(t.next());
			while(t.hasNext()) {
				System.out.print("->" + t.next());
			}
			System.out.println("->" + id);

			throw new IllegalStateException("Zapętlenie wywołań szablonu '" + id + "'");
		}

		// Sprawdzanie dostępności i poprawności klasy silnika generatora szablonu:
		Class<? extends ITempelEngine> engine = template.getEngineClass();
		if(engine != null && !ITempelEngine.class.isAssignableFrom(engine)) {
			throw new IllegalStateException("Niepoprawny typ silnika generatora szablonu '" + id + "': '" + engine + "'");
		}

		// Szablon OK. Dodawanie do zbioru szablonów gałęzi:
		templates.add(id);

		// Sprawdzanie każdego z podszablonów szablonu:
		if(template.getReferences() != null) {
			for(TemplateReference reference : template.getReferences()) {
				// verifyTemplate(reference, templates);
			}
		}
	}
}
