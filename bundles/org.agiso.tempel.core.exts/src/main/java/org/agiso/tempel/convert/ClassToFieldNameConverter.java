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
	public boolean canConvert(Class<?> type) {
		return String.class.equals(type);
	}

	@Override
	public String convert(String value) {
		if(!Temp.StringUtils_isEmpty(value)) {
			return value.substring(0, 1).toLowerCase() + value.substring(1);
		}
		return value;
	}
}
