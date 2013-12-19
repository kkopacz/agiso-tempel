/* org.agiso.tempel.convert.GetterFieldTypeConverter (16-12-2013)
 * 
 * GetterFieldTypeConverter.java
 * 
 * Copyright 2013 PPW 'ARAJ' Sp. z o. o.
 */
package org.agiso.tempel.convert;

import org.agiso.tempel.Temp;
import org.agiso.tempel.api.ITemplateParamConverter;

/**
 * Konwerter zamieniający nazwę pola klasy na metodę dostępową 
 * do tego pola get lub is
 * 
 * @author <a href="mailto:mklin@agiso.org">Michał Klin</a>
 */
public class GetterFieldTypeConverter implements ITemplateParamConverter<String> {

//	--------------------------------------------------------------------------
	@Override
	public boolean canConvert(Class<?> type) {
		return String.class.equals(type);
	}

	@Override
	public String convert(String value) {
		if(value != null && value.equals("boolean")){
			return "is";
		}
		return "get";
	}
}
