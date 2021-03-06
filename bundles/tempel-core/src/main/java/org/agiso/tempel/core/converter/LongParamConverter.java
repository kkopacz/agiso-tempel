/* org.agiso.tempel.core.LongParamConverter (28-11-2013)
 * 
 * LongParamConverter.java
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
package org.agiso.tempel.core.converter;

import org.agiso.tempel.api.ITemplateParamConverter;

/**
 * 
 * 
 * @author Karol Kopacz
 * @since 1.0
 */
public class LongParamConverter implements ITemplateParamConverter<String, Long> {
	@Override
	public boolean canConvert(Class<?> fromType, Class<?> toType) {
		return String.class.equals(fromType) && Long.class.equals(toType);
	}

	@Override
	public Long convert(String value) {
		return Long.valueOf(value);
	}
}
