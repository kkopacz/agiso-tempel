/* org.agiso.tempel.api.internal.ITempelScopeInfo (14-01-2013)
 * 
 * ITempelScopeInfo.java
 * 
 * Copyright 2013 agiso.org
 */
package org.agiso.tempel.api.internal;

import org.agiso.tempel.core.model.Template.Scope;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public interface ITempelScopeInfo {
	public enum RepostoryType {
		FILE, JAR
	}

//	--------------------------------------------------------------------------
	/**
	 * @param scope
	 * @return
	 */
	public RepostoryType getRepositoryType(Scope scope);

	/**
	 * @param scope
	 * @return
	 */
	public String getSettingsPath(Scope scope);

	/**
	 * Zwraca pełną ścieżkę do katalogu zawierającego repozytorium
	 * dla określonego zasięgu. W tym repozytorium wyszukiwane są
	 * pliki przetwarzane przez silniki generacji.
	 * 
	 * @param scope
	 * @return
	 */
	public String getRepositoryPath(Scope scope);
}
