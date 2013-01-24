/* org.agiso.tempel.core.model.Template (14-09-2012)
 * 
 * Template.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.core.model;

import java.util.List;

import org.agiso.tempel.api.ITempelEngine;
import org.agiso.tempel.api.ITemplateSource;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public interface Template extends TemplateReference {
	public enum Scope {
		GLOBAL, USER, RUNTIME, MAVEN
	}

//	--------------------------------------------------------------------------
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
	public Template clone();

//	--------------------------------------------------------------------------
	public void setTemplateSourceFactory(ITemplateSourceFactory templateSourceFactory);

	/**
	 * @param source
	 * @return
	 */
	public ITemplateSource getTemplateSource(String source);
}
