/* org.agiso.tempel.core.XStreamTempelFileProcessor (15-12-2012)
 * 
 * XStreamTempelFileProcessor.java
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
package org.agiso.tempel.core;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.agiso.tempel.api.ITempelEngine;
import org.agiso.tempel.api.ITemplateParamConverter;
import org.agiso.tempel.api.ITemplateParamValidator;
import org.agiso.tempel.api.internal.ITempelEntryProcessor;
import org.agiso.tempel.api.internal.ITempelFileProcessor;
import org.agiso.tempel.core.model.beans.TemplateBean;
import org.agiso.tempel.core.model.beans.TemplateEngineBean;
import org.agiso.tempel.core.model.beans.TemplateParamBean;
import org.agiso.tempel.core.model.beans.TemplateParamValidatorBean;
import org.agiso.tempel.core.model.beans.TemplateParamConverterBean;
import org.agiso.tempel.core.model.beans.TemplateReferenceBean;
import org.agiso.tempel.core.model.beans.TemplateResourceBean;
import org.springframework.stereotype.Component;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.reflection.ReflectionConverter;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
@Component
public class XStreamTempelFileProcessor implements ITempelFileProcessor {
	private static final XStream xStream;

	static {
		// Konfiguracja XStream'a:
		xStream = new XStream();
		xStream.registerConverter(new MapEntryConverter());
		xStream.registerConverter(new TemplateBeanAttributesConverter(
				xStream.getMapper(), xStream.getReflectionProvider()
		));
//		xStream.registerConverter(new TemplateParamBeanConverter(
//				xStream.getMapper(), xStream.getReflectionProvider()
//		));
		xStream.registerConverter(new TemplateParamBeanAttributesConverter(
				xStream.getMapper(), xStream.getReflectionProvider()
		));
		xStream.registerConverter(new TemplateParamConverterBeanConverter(
				xStream.getMapper(), xStream.getReflectionProvider()
		));
		xStream.alias("properties", Map.class);
		xStream.aliasSystemAttribute("paramClass", "class");
		xStream.autodetectAnnotations(true);
		xStream.processAnnotations(new Class[] {
				TemplateBean.class,
				TemplateEngineBean.class,
				TemplateParamBean.class,
				TemplateParamConverterBean.class,
				TemplateParamValidatorBean.class,
				TemplateReferenceBean.class,
				TemplateResourceBean.class
		});
	}

//	--------------------------------------------------------------------------
	@Override
	public void process(String xmlString, ITempelEntryProcessor entryProcessor) throws Exception {
		process(new ByteArrayInputStream(xmlString.getBytes()), entryProcessor);
	}

	@Override
	public void process(File xmlFile, ITempelEntryProcessor entryProcessor) throws Exception {
		FileInputStream xmlStream = null;
		try {
			xmlStream = new FileInputStream(xmlFile);
			process(xmlStream, entryProcessor);
		} finally {
			if(xmlStream != null) {
				xmlStream.close();
			}
		}
	}

	@Override
	public void process(InputStream xmlStream, ITempelEntryProcessor entryProcessor) throws Exception {
		ObjectInputStream in = xStream.createObjectInputStream(xmlStream);
		try {
			while(true) {
				entryProcessor.processObject(in.readObject());
			}
		} catch(EOFException e) {
			// Normalne zakończenie przetwarzania pliku szablonu
		}
	}

//	--------------------------------------------------------------------------
//	/**
//	 * 
//	 * 
//	 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
//	 */
//	private static class RepositoryConverter extends AbstractSingleValueConverter {
//		@Override
//		public boolean canConvert(@SuppressWarnings("rawtypes") Class type) {
//			return type.equals(RepositoryBean.class);
//		}
//
//		@Override
//		public Object fromString(String str) {
//			RepositoryBean repository = new RepositoryBean();
//			repository.setValue(str);
//			return repository;
//		}
//	}
}

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
class MapEntryConverter implements Converter {
	@Override
	public boolean canConvert(@SuppressWarnings("rawtypes") Class clazz) {
		return AbstractMap.class.isAssignableFrom(clazz);
	}

	@Override
	public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
		@SuppressWarnings("unchecked")
		AbstractMap<String, String> map = (AbstractMap<String, String>)value;
		for(Entry<String, String> entry : map.entrySet()) {
			writer.startNode(entry.getKey().toString());
			writer.setValue(entry.getValue().toString());
			writer.endNode();
		}
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		Map<String, String> map = new HashMap<String, String>();
		while(reader.hasMoreChildren()) {
			reader.moveDown();
			map.put(reader.getNodeName(), reader.getValue());
			reader.moveUp();
		}
		return map;
	}
}

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
	@SuppressWarnings("unchecked")
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		Class<?> engineClass = null;
		String engineClassName = reader.getAttribute("engine");
		if(engineClassName != null) {
			if(engineClassName.isEmpty()) {
				throw new ConversionException("Empty string is invalid 'engine' value");
			}
			try {
				engineClass = Class.forName(engineClassName);
				if(!ITempelEngine.class.isAssignableFrom(engineClass)) {
					throw new ConversionException("Invalid 'engine' class value");
				}
			} catch(ClassNotFoundException e) {
				throw new ConversionException("Unknown 'engine' class", e);
			}
		}

		TemplateBean template = (TemplateBean)super.unmarshal(reader, context);
		template.setEngineClass((Class<? extends ITempelEngine>)engineClass);
		return template;
	}
}
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
			throw new ConversionException("Param 'converterClass' attribute and 'converter' tag defined simultaneously");
		} else if(templateParam.getConverter() == null) {
			templateParam.setConverter(new TemplateParamConverterBean()
					.withConverterClass((Class<? extends ITemplateParamConverter<?>>)converterClass)
			);
		}
		if(templateParam.getValidator() != null && validatorClass != null) {
			throw new ConversionException("Param 'validatorClass' attribute and 'validator' tag defined simultaneously");
		} else if(templateParam.getValidator() == null) {
			templateParam.setValidator(new TemplateParamValidatorBean()
					.withValidatorClass((Class<? extends ITemplateParamValidator<?>>)converterClass)
			);
		}
		return templateParam;
	}
}

/**
 * Konwerter XStream obsługujący konwersję znacznika &lt;converter&gt; definicji
 * konwertera parametru szablonu.
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
class TemplateParamConverterBeanConverter extends ReflectionConverter {
	public TemplateParamConverterBeanConverter(Mapper mapper, ReflectionProvider reflectionProvider) {
		super(mapper, reflectionProvider);
	}


	@Override
	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class type) {
		return type.equals(TemplateParamConverterBean.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		Class<?> converterClass = null;
		String converterClassName = reader.getAttribute("class");
		if(converterClassName == null) {
			throw new ConversionException("Converter 'class' not defined");
		}
		if(converterClassName.isEmpty()) {
			throw new ConversionException("Empty string is invalid 'class' value");
		}
		try {
			converterClass = Class.forName(converterClassName);
			if(!ITemplateParamConverter.class.isAssignableFrom(converterClass)) {
				throw new ConversionException("Invalid converter 'class' value");
			}
		} catch(ClassNotFoundException e) {
			throw new ConversionException("Unknown converter 'class'", e);
		}

		TemplateParamConverterBean templateParamConverter = new TemplateParamConverterBean();
		templateParamConverter.setConverterClass((Class<? extends ITemplateParamConverter<?>>)converterClass);

		LinkedHashMap<String, String> properties = new LinkedHashMap<String, String>();
		while(reader.hasMoreChildren()) {
			reader.moveDown();
			String propertyName = reader.getNodeName();
			if(properties.containsKey(propertyName)) {
				throw new ConversionException("Duplicated converter property '" + propertyName + "'");
			}
			properties.put(propertyName, reader.getValue());
			reader.moveUp();
		}
		templateParamConverter.setProperties(properties);

		return templateParamConverter;
	}
}