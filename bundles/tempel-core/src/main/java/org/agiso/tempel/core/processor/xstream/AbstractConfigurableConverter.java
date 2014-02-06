/* org.agiso.tempel.core.processor.xstream.AbstractConfigurableConverter (12-12-2013)
 * 
 * AbstractConfigurableConverter.java
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
package org.agiso.tempel.core.processor.xstream;

import java.util.Collection;
import java.util.LinkedHashMap;

import org.agiso.tempel.core.model.beans.BeanProperty;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.reflection.ReflectionConverter;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.mapper.Mapper;

/**
 * 
 * 
 * @author Karol Kopacz
 * @since 1.0
 */
abstract class AbstractConfigurableConverter extends ReflectionConverter {
	public AbstractConfigurableConverter(Mapper mapper, ReflectionProvider reflectionProvider) {
		super(mapper, reflectionProvider);
	}

//	--------------------------------------------------------------------------
	/**
	 * @param reader
	 * @return
	 */
	protected Collection<BeanProperty> readProperties(HierarchicalStreamReader reader) {
		LinkedHashMap<String, BeanProperty> beanProperties = new LinkedHashMap<String, BeanProperty>();
		while(reader.hasMoreChildren()) {
			reader.moveDown();
			String propertyName = reader.getNodeName();
			if(beanProperties.containsKey(propertyName)) {
				throw new ConversionException("Duplicated property '" + propertyName + "'");
			}

			BeanProperty beanProperty = new BeanProperty(propertyName);
			for(int index = 0, count = reader.getAttributeCount(); index < count; index++) {
				beanProperty.addAttribute(reader.getAttributeName(index), reader.getAttribute(index));
			}
			beanProperty.setPropertyValue(reader.getValue());

			beanProperties.put(propertyName, beanProperty);
			reader.moveUp();
		}
		return beanProperties.values();
	}
}
