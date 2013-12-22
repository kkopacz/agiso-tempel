/* org.agiso.tempel.core.processor.xstream.TemplateBeanAttributesConverter (12-12-2013)
 * 
 * TemplateBeanAttributesConverter.java
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

import org.agiso.tempel.core.model.beans.TemplateBean;
import org.agiso.tempel.core.model.beans.TemplateEngineBean;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.reflection.ReflectionConverter;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.mapper.Mapper;

/**
 * Kowerter XStream obsługujący atrybut "engine" znacznika &lt;template&gt;
 * definicji szablonu.
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
class TemplateBeanAttributesConverter extends ReflectionConverter {
	public TemplateBeanAttributesConverter(Mapper mapper, ReflectionProvider reflectionProvider) {
		super(mapper, reflectionProvider);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class type) {
		return type.equals(TemplateBean.class);
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		String engineClassName = reader.getAttribute("engine");
		if(engineClassName != null) {
			if(engineClassName.isEmpty()) {
				throw new ConversionException("Empty string is invalid 'engine' value");
			}
		}

		TemplateBean template = (TemplateBean)super.unmarshal(reader, context);
		if(template.getEngine() != null && engineClassName != null) {
			throw new ConversionException("Param and attribute 'engine' tag defined simultaneously");
		} else if(template.getEngine() == null) {
			if(engineClassName == null) {
				engineClassName = getDefaultEngineClassName();
			}
			template.setEngine(new TemplateEngineBean()
					.withEngineClassName(engineClassName)
			);
		}

		return template;
	}

	/**
	 * @return
	 */
	private String getDefaultEngineClassName() {
		return null;
	}
}
