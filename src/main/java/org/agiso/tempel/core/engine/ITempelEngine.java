/* org.agiso.tempel.core.engine.ITempelEngine (14-09-2012)
 * 
 * ITempelEngine.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.core.engine;

import java.util.Map;

import org.agiso.tempel.core.model.Template;

/**
 * Interfejs silnika generatora.
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public interface ITempelEngine {

	/**
	 * Pozwala na ustawienie ścieżek bazowych do repozytoriów przechowujących
	 * zasoby dla poszczgólnych zasięgów (uruchomienia, użytkownika i instancji).
	 * 
	 * @param repositories
	 */
	public void initialize(Map<Template.Scope, String> repositories);

	/**
	 * Uruchamia silnik generatora.
	 * 
	 * @param scope Obszar działania silnika (GLOBAL, USER, RUNTIME). Określa
	 *     repozytorium, w którym będą wyszukiwane przetwarzane zasoby.
	 * @param source Ścieżka szablonu do wykorzystania w celu generacji zasobu.
	 * @param params Mapa parametrów szablonu.
	 * @param target Ścieżka docelowa zasobu tworzonego w oparciu o szablon.
	 */
	public void run(Template.Scope scope, String source, Map<String, Object> params, String target);
}
