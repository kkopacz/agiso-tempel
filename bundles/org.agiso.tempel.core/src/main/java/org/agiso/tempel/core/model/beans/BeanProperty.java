/* org.agiso.tempel.core.processor.xstream.BeanProperty (12-12-2013)
 * 
 * BeanProperty.java
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
package org.agiso.tempel.core.model.beans;

import java.util.LinkedHashMap;
import java.util.Map;
import com.thoughtworks.xstream.converters.ConversionException;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class BeanProperty {
	private String propertyName;
	private String propertyValue;

	private Map<String, String> attributes;

//	--------------------------------------------------------------------------
	/**
	 * @param propertyName
	 * @param propertyValue
	 */
	public BeanProperty(String propertyName) {
		this.propertyName = propertyName;

		attributes = new LinkedHashMap<String, String>();
	}

//	--------------------------------------------------------------------------
	public String getPropertyName() {
		return propertyName;
	}

	public String getPropertyValue() {
		return propertyValue;
	}
	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}

//	--------------------------------------------------------------------------
	/**
	 * @param name
	 * @param value
	 */
	public void addAttribute(String name, String value) {
		if(attributes.containsKey(name)) {
			throw new ConversionException("Duplicated attribute '"
					+ name + "' in property '" + propertyName + "'");
		}
		attributes.put(name, value);
	}

	/**
	 * @return
	 */
	public boolean hasAttributes() {
		return !attributes.isEmpty();
	}

	/**
	 * @param name
	 * @return
	 */
	public String getAttribute(String name) {
		return attributes.get(name);
	}
}
