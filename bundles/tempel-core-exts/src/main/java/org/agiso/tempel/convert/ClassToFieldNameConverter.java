/* org.agiso.tempel.convert.ClassToFieldNameConverter (12-11-2012)
 * 
 * ClassToFieldNameConverter.java
 * 
 * Copyright 2012 agiso.org
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.agiso.tempel.convert;

import org.agiso.core.lang.util.StringUtils;
import org.agiso.tempel.api.ITemplateParamConverter;

/**
 * Konwerter zamieniający nazwę klasową w nomenklatruze Camel zaczynającą
 * się z wielkiej litery na nazwę pola. Zamienia pierwszą listerę na małą.
 * 
 * @author Karol Kopacz
 * @since 1.0
 */
public class ClassToFieldNameConverter implements ITemplateParamConverter<String, String> {
	@Override
	public boolean canConvert(Class<?> fromType, Class<?> toType) {
		return (fromType == null || String.class.equals(fromType))
				&& String.class.equals(toType);
	}

	@Override
	public String convert(String value) {
		if(!StringUtils.isEmpty(value)) {
			return value.substring(0, 1).toLowerCase() + value.substring(1);
		}
		return value;
	}
}
