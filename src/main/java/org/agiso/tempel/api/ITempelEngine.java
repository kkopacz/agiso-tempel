/* org.agiso.tempel.core.engine.ITempelEngine (14-09-2012)
 * 
 * ITempelEngine.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.api;

import java.util.Map;

import org.agiso.tempel.api.internal.ITemplateSource;

/**
 * Interfejs silnika generatora.
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public interface ITempelEngine {
	/**
	 * Uruchamia silnik generatora.
	 * 
	 * @param source Ścieżka szablonu do wykorzystania w celu generacji zasobu.
	 * @param params Mapa parametrów szablonu.
	 * @param target Ścieżka docelowa zasobu tworzonego w oparciu o szablon.
	 */
	public void run(ITemplateSource source, Map<String, Object> params, String target);
}
