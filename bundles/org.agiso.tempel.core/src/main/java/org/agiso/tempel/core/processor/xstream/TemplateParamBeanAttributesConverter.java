/* org.agiso.tempel.core.processor.xstream.TemplateParamBeanAttributesConverter (12-12-2013)
 * 
 * TemplateParamBeanAttributesConverter.java
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

import org.agiso.tempel.api.ITemplateParamConverter;
import org.agiso.tempel.api.ITemplateParamValidator;
import org.agiso.tempel.core.model.beans.TemplateParamBean;
import org.agiso.tempel.core.model.beans.TemplateParamConverterBean;
import org.agiso.tempel.core.model.beans.TemplateParamValidatorBean;
import org.agiso.tempel.core.validator.DefaultParamValidator;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.reflection.ReflectionConverter;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.mapper.Mapper;

/**
 * Kowerter XStream obsługujący atrybuty "converter" i "validator" znacznika
 * &lt;param&gt; definicji parametru szablonu.
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
class TemplateParamBeanAttributesConverter extends ReflectionConverter {
	public TemplateParamBeanAttributesConverter(Mapper mapper, ReflectionProvider reflectionProvider) {
		super(mapper, reflectionProvider);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class type) {
		return type.equals(TemplateParamBean.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		Class<?> converterClass = null;
		String converterClassName = reader.getAttribute("converter");
		if(converterClassName != null) {
			if(converterClassName.isEmpty()) {
				throw new ConversionException("Empty string is invalid 'converter' value");
			}
			try {
				converterClass = Class.forName(converterClassName);
				if(!ITemplateParamConverter.class.isAssignableFrom(converterClass)) {
					throw new ConversionException("Invalid 'converter' class value");
				}
			} catch(ClassNotFoundException e) {
				throw new ConversionException("Unknown 'converter' class", e);
			}
		}

		Class<?> validatorClass = null;
		String validatorClassName = reader.getAttribute("validator");
		if(validatorClassName != null) {
			if(validatorClassName.isEmpty()) {
				throw new ConversionException("Empty string is invalid 'validator' value");
			}
			try {
				validatorClass = Class.forName(validatorClassName);
				if(!ITemplateParamValidator.class.isAssignableFrom(validatorClass)) {
					throw new ConversionException("Invalid 'validator' class value");
				}
			} catch(ClassNotFoundException e) {
				throw new ConversionException("Unknown 'validator' class", e);
			}
		}

		TemplateParamBean templateParam = (TemplateParamBean)super.unmarshal(reader, context);
		if(templateParam.getConverter() != null && converterClass != null) {
			throw new ConversionException("Param 'converterClass' and attribute 'converter' tag defined simultaneously");
		} else if(templateParam.getConverter() == null) {
			if(converterClass == null) {
				converterClass = getDefaultConverterClass();
			}
			templateParam.setConverter(new TemplateParamConverterBean()
					.withConverterClass((Class<ITemplateParamConverter<?>>)converterClass)
			);
		}
		if(templateParam.getValidator() != null && validatorClass != null) {
			throw new ConversionException("Param 'validatorClass' and attribute 'validator' tag defined simultaneously");
		} else if(templateParam.getValidator() == null) {
			if(validatorClass == null) {
				validatorClass = getDefaultValidatorClass();
			}
			templateParam.setValidator(new TemplateParamValidatorBean()
					.withValidatorClass((Class<ITemplateParamValidator<?>>)validatorClass)
			);
		}
		return templateParam;
	}

	/**
	 * @return
	 */
	private Class<?> getDefaultValidatorClass() {
		return DefaultParamValidator.class;
	}

	/**
	 * @return
	 */
	private Class<?> getDefaultConverterClass() {
		return null;
	}
}
