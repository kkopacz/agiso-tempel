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

import org.agiso.tempel.api.ITempelEngine;
import org.agiso.tempel.api.ITemplateSource;
import org.agiso.tempel.api.ITemplateSourceFactory;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public interface Template extends TemplateReference {
	/**
	 * @return Klasa silnika generatora.
	 */
	public Class<? extends ITempelEngine> getEngine();

	/**
	 * @return Lista szablonów wykorzystywanych przez szablon bieżący (np. w
	 *     celu utworzenia katalogu dla pliku tworzonego na podstawie szablonu).
	 */
	public List<TemplateReference> getReferences();

//	--------------------------------------------------------------------------
	public void setTemplateSourceFactory(ITemplateSourceFactory templateSourceFactory);

	/**
	 * @param source
	 * @return
	 */
	public ITemplateSource getTemplateSource(String source);

//	--------------------------------------------------------------------------
	public Template clone();
}
