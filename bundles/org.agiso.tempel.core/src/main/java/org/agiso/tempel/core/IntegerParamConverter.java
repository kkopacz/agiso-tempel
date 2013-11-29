/* org.agiso.tempel.core.IntegerParamConverter (28-11-2013)
 * 
 * IntegerParamConverter.java
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
package org.agiso.tempel.core;

import org.agiso.tempel.api.ITemplateParamConverter;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class IntegerParamConverter implements ITemplateParamConverter<Integer> {
	@Override
	public boolean canConvert(Class<?> type) {
		return Integer.class.equals(type);
	}

	@Override
	public Integer convert(String value) {
		return Integer.valueOf(value);
	}
}
