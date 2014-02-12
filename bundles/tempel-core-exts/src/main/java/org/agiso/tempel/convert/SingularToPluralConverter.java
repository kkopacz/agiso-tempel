/* org.agiso.tempel.convert.SingularToPluralConverter (05-12-2012)
 * 
 * SingularToPluralConverter.java
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

import static org.jvnet.inflector.Noun.*;

import java.util.Locale;

import org.agiso.core.lang.util.StringUtils;
import org.agiso.tempel.api.ITemplateParamConverter;

/**
 * Konwerter liczby pojedynczej na mnogą.
 * 
 * @author Karol Kopacz
 * @since 1.0
 */
public class SingularToPluralConverter implements ITemplateParamConverter<String, String> {
	@Override
	public boolean canConvert(Class<?> fromType, Class<?> toType) {
		return (fromType == null || String.class.equals(fromType))
				&& String.class.equals(toType);
	}

	@Override
	public String convert(String value) {
		if(StringUtils.isBlank(value)) {
			return value;
		}
		return pluralOf(value, Locale.ENGLISH);
	}
}
