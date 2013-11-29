/* org.agiso.tempel.core.model.beans.TemplateParamConverterBean (20-11-2013)
 * 
 * TemplateParamConverterBean.java
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

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;

import org.agiso.tempel.api.ITemplateParamConverter;
import org.agiso.tempel.api.model.TemplateParamConverter;
import org.apache.velocity.util.introspection.IntrospectionUtils;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
@XStreamAlias("converter")
public class TemplateParamConverterBean implements TemplateParamConverter {
//	@XStreamAsAttribute
//	@XStreamAlias("class")
	private Class<? extends ITemplateParamConverter<?>> converterClass;

	private Map<String, String> properties;

//	--------------------------------------------------------------------------
	@Override
	public Class<? extends ITemplateParamConverter<?>> getConverterClass() {
		return converterClass;
	}
	public void setConverterClass(Class<? extends ITemplateParamConverter<?>> converterClass) {
		this.converterClass = converterClass;
	}
	@SuppressWarnings("unchecked")
	public <T extends TemplateParamConverterBean> T withConverterClass(Class<? extends ITemplateParamConverter<?>> converterClass) {
		this.converterClass = converterClass;
		return (T)this;
	}

	@Override
	public Map<String, String> getProperties() {
		return properties;
	}
	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
	@SuppressWarnings("unchecked")
	public <T extends TemplateParamConverterBean> T withProperties(Map<String, String> properties) {
		this.properties = properties;
		return (T)this;
	}

//	--------------------------------------------------------------------------
	@Override
	public ITemplateParamConverter<?> getInstance() {
		try {
			ITemplateParamConverter<?> instance;
			try {
				// Instancjonowanie i inicjalizcja w oparciu o konstruktor inicjujący:
				@SuppressWarnings("unchecked")
				Constructor<ITemplateParamConverter<?>> constructor =
						(Constructor<ITemplateParamConverter<?>>)converterClass.getConstructor(Map.class);
				instance = constructor.newInstance(properties);
			} catch(NoSuchMethodException nsme) {
				// Instancjonowanie i inicjalizacja w oparciu o konstruktor bezargumentowy
				// i publiczne setter'y Java Beans:
				instance = converterClass.newInstance();

				if(properties != null && !properties.isEmpty()) {
					BeanInfo converterInfo = Introspector.getBeanInfo(converterClass);
					PropertyDescriptor[] propertyDescriptors = converterInfo.getPropertyDescriptors();
					for(String propertyName : properties.keySet()) {
						Method writeMethod = null;
						for(PropertyDescriptor propertyDescriptor : propertyDescriptors) {
							if(propertyDescriptor.getName().equals(propertyName)) {
								writeMethod = propertyDescriptor.getWriteMethod();
								break;
							}
						}
						if(writeMethod == null) {
							throw new RuntimeException("No public setter found for property '" + propertyName + "' "
									+ "in converter class " + converterClass.getCanonicalName());
						}
						writeMethod.invoke(instance, properties.get(propertyName));
					}
				}
			}
			return instance;
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

//	--------------------------------------------------------------------------
	@Override
	public TemplateParamConverterBean clone() {
		return fillClone(new TemplateParamConverterBean());
	}
	protected TemplateParamConverterBean fillClone(TemplateParamConverterBean clone) {
		clone.converterClass = converterClass;

		return clone;
	}
}
