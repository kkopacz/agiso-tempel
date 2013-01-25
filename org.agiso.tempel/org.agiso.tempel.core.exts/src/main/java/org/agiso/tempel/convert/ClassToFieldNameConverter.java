/* org.agiso.tempel.convert.ClassToFieldNameConverter (12-11-2012)
 * 
 * ClassToFieldNameConverter.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.convert;

import org.agiso.tempel.Temp;
import org.agiso.tempel.api.ITemplateParamConverter;

/**
 * Konwerter zamieniający nazwę klasową w nomenklatruze Camel zaczynającą
 * się z wielkiej litery na nazwę pola. Zamienia pierwszą listerę na małą.
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class ClassToFieldNameConverter implements ITemplateParamConverter<String> {
	@Override
	public String convert(String value) {
		if(!Temp.StringUtils_isEmpty(value)) {
			return value.substring(0, 1).toLowerCase() + value.substring(1);
		}
		return value;
	}
}
