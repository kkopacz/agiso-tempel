/* org.agiso.tempel.engine.AbstractTempelEngine (16-11-2012)
 * 
 * AbstractTempelEngine.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.engine;

import java.util.Map;

import org.agiso.tempel.core.engine.ITempelEngine;
import org.agiso.tempel.core.model.Template.Scope;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public abstract class AbstractTempelEngine implements ITempelEngine {
	private Map<Scope, String> repositories;

//	--------------------------------------------------------------------------
	@Override
	public void initialize(Map<Scope, String> repositories) {
		this.repositories = repositories;
	}

//	--------------------------------------------------------------------------
	protected String getScopedSourcePath(Scope scope, String source) {
		return repositories.get(scope) + "/" + source; 
	}
}
