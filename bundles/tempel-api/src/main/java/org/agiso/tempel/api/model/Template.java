/* org.agiso.tempel.api.model.Template (14-09-2012)
 * 
 * Template.java
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
package org.agiso.tempel.api.model;

import java.util.List;
import java.util.Set;

import org.agiso.tempel.api.ITemplateClassPathExtender;
import org.agiso.tempel.api.ITemplateSource;
import org.agiso.tempel.api.ITemplateSourceFactory;

/**
 * 
 * 
 * @author Karol Kopacz
 * @since 1.0
 */
public interface Template<E extends TemplateEngine> extends TemplateReference {
	public boolean isAbstract();

	public E getEngine();

	/**
	 * @return Lista szablonów wykorzystywanych przez szablon bieżący (np. w
	 *     celu utworzenia katalogu dla pliku tworzonego na podstawie szablonu).
	 */
	public List<TemplateReference> getReferences();

	/**
	 * @return Zbiór dodatkowych elementów ścieżki klas wymaganych do uruchomienia
	 *     szablonu.
	 */
	public Set<String> getTemplateClassPath();

	/**
	 * @param extender
	 */
	public void addTemplateClassPathExtender(ITemplateClassPathExtender extender);

//	--------------------------------------------------------------------------
	public void setTemplateSourceFactory(ITemplateSourceFactory templateSourceFactory);

	/**
	 * @param source
	 * @return
	 */
	public ITemplateSource getTemplateSource(String source);

//	--------------------------------------------------------------------------
	public Template<E> clone();
}
