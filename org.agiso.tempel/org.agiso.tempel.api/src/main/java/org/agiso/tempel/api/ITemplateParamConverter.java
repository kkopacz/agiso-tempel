/* org.agiso.tempel.core.convert.ITemplateParamConverter (19-09-2012)
 * 
 * ITemplateParamConverter.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.api;

/**
 * Interfejs konwertera parametrów.
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public interface ITemplateParamConverter<T> {
	/**
	 * Konwertuje wartość łańcuchową parametru na typ właściwy dla implementacji
	 * konwertera.
	 * 
	 * @param value Wartość łańcuchowa parametru do konwersji.
	 * @return Wartość parametru o typie właściwym dla konwertera.
	 */
	public T convert(String value);
}
