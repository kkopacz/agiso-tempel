/* org.agiso.tempel.convert.GetterFieldTypeConverter (16-12-2013)
 * 
 * GetterFieldTypeConverter.java
 * 
 * Copyright 2013 agiso.org
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
 * Konwerter zamieniający nazwę pola klasy na metodę dostępową 
 * do tego pola get lub is
 * 
 * @author Michał Klin
 * @since 1.0
 */
public class GetterFieldTypeConverter implements ITemplateParamConverter<String, String> {

//	--------------------------------------------------------------------------
	@Override
	public boolean canConvert(Class<?> fromType, Class<?> toType) {
		return (fromType == null || String.class.equals(fromType))
				&& String.class.equals(toType);
	}

	@Override
	public String convert(String value) {
		if(value != null && value.equals("boolean")){
			return "is";
		}
		return "get";
	}
}
