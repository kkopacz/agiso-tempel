/* org.agiso.tempel.convert.PackageToPathConverter (19-09-2012)
 * 
 * PackageToPathConverter.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.convert;

import org.agiso.tempel.api.ITemplateParamConverter;

/**
 * Konwerter zamieniający nazwę pakietu na ścieżkę katalogową.
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class PackageToPathConverter implements ITemplateParamConverter<String> {
	@Override
	public String convert(String value) {
		return value == null? null : value.replace('.', '/');
	}
}
