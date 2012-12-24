/* org.agiso.tempel.convert.SingularToPluralConverter (05-12-2012)
 * 
 * SingularToPluralConverter.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.convert;

import static org.jvnet.inflector.Noun.*;

import java.util.Locale;

import org.agiso.tempel.Temp;
import org.agiso.tempel.api.ITemplateParamConverter;

/**
 * Konwerter liczby pojedynczej na mnogą.
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class SingularToPluralConverter implements ITemplateParamConverter<String> {
	@Override
	public String convert(String value) {
		if(Temp.StringUtils_isBlank(value)) {
			return value;
		}
		return pluralOf(value, Locale.ENGLISH);
	}
}
