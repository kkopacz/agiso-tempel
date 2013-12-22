/* org.agiso.tempel.core.fetcher.MapParamFetcher (23-12-2013)
 * 
 * MapParamFetcher.java
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
package org.agiso.tempel.core.fetcher;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.agiso.tempel.api.ITemplateParamFetcher;
import org.agiso.tempel.api.internal.IParamData;
import org.agiso.tempel.api.internal.IParamReader;
import org.agiso.tempel.core.model.beans.BeanProperty;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class MapParamFetcher implements ITemplateParamFetcher<Map<String, Object>> {
	public Collection<BeanProperty> properties;

//	--------------------------------------------------------------------------
	public MapParamFetcher(Collection<BeanProperty> properties) {
		this.properties = properties;
	}

//	--------------------------------------------------------------------------
	@Override
	public Map<String, Object> fetch(IParamReader reader, IParamData param) {
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		for(BeanProperty property : properties) {
			String key = property.getPropertyName();
			String name = property.getAttribute("name");
			String value = property.getAttribute("value");

			params.put(key, reader.getParamValue(param.getKey() + "." + key, name, value));
		}
		return params;
	}
}
