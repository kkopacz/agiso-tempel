/* org.agiso.tempel.core.processor.xstream.TemplateEngineBeanConverter (12-12-2013)
 * 
 * TemplateEngineBeanConverter.java
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

import org.agiso.tempel.core.model.beans.TemplateEngineBean;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.mapper.Mapper;

/**
 * Konwerter XStream obsługujący konwersję znacznika &lt;engine&gt; definicji
 * silnika szablonu.
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
class TemplateEngineBeanConverter extends AbstractConfigurableConverter {
	public TemplateEngineBeanConverter(Mapper mapper, ReflectionProvider reflectionProvider) {
		super(mapper, reflectionProvider);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class type) {
		return type.equals(TemplateEngineBean.class);
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		String engineClassName = reader.getAttribute("class");
		if(engineClassName == null) {
			throw new ConversionException("Engine 'class' not defined");
		}
		if(engineClassName.isEmpty()) {
			throw new ConversionException("Empty string is invalid 'class' value");
		}

		TemplateEngineBean templateEngine = new TemplateEngineBean();
		templateEngine.setEngineClassName(engineClassName);
		templateEngine.setProperties(readProperties(reader));
		return templateEngine;
	}
}
