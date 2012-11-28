/* org.agiso.tempel.core.model.Template (14-09-2012)
 * 
 * Template.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.core.model;

import java.util.List;

import org.agiso.tempel.core.engine.ITempelEngine;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public interface Template extends TemplateReference {
	public enum Scope {
		GLOBAL, USER, RUNTIME
	}

//	--------------------------------------------------------------------------
	public Scope getScope();
	public void setScope(Scope scope);

	/**
	 * @return Klasa silnika generatora.
	 */
	public Class<? extends ITempelEngine> getEngine();

	/**
	 * @return Lista szablonów wykorzystywanych przez szablon bieżący (np. w
	 *     celu utworzenia katalogu dla pliku tworzonego na podstawie szablonu).
	 */
	public List<TemplateReference> getReferences();

	public void setRepository(Repository repository);
	public Repository getRepository();

//	--------------------------------------------------------------------------
	public Template clone();
}
