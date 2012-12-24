/* org.agiso.tempel.core.model.TemplateResource (17-10-2012)
 * 
 * TemplateResource.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.core.model;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public interface TemplateResource extends Cloneable {
	/**
	 * @return Plik szablonu przetwarzany przez silnik generatora.
	 */
	public String getSource();
//	public void setSource(String source);

	/**
	 * @return Zasób tworzony w wyniku wypełnienia szablonu przez silnik
	 *     generatora.
	 */
	public String getTarget();
	public void setTarget(String target);

	/**
	 * @param template
	 */
	public void setParentTemplateReference(Template parentTemplateReference);
	public Template getParentTemplateReference();

//	--------------------------------------------------------------------------
	public TemplateResource clone();
}
