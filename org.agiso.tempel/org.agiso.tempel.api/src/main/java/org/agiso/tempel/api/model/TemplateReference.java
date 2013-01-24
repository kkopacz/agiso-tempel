/* org.agiso.tempel.api.model.TemplateReference (02-10-2012)
 * 
 * TemplateReference.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.api.model;

import java.util.List;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public interface TemplateReference extends Cloneable {
	/**
	 * @return Identyfikator (klucz) szablonu.
	 */
	public String getKey();

	public String getGroupId();
	public String getTemplateId();
	public String getVersion();

	/**
	 * @return Ścieżka katalogu roboczego szablonu. Relatywna w stosunku
	 *     do ścieżki nadszablonu. Dla szablonu pierwszego poziomu ścieżka
	 *     ta jest określana w stosunku do katalogu roboczego aplikacji.
	 */
	public String getWorkDir();
	public void setWorkDir(String workDir);

	/**
	 * @return Lista parametrów wymaganych do wypełnienia szablonu przez
	 *     silnik generatora.
	 */
	public List<TemplateParam> getParams();

	/**
	 * @return Lista zasobów (powiązanie szablonu i miejsca docelowego), które
	 *     będą przetwarzane przez silnik generatora.
	 */
	public List<TemplateResource> getResources();

//	--------------------------------------------------------------------------
	public TemplateReference clone();
}
