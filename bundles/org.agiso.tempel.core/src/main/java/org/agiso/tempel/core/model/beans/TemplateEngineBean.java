/* org.agiso.tempel.core.model.beans.TemplateEngineBean (10-11-2013)
 * 
 * TemplateEngineBean.java
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
import java.util.LinkedHashMap;
import java.util.Map;

import org.agiso.tempel.api.ITempelEngine;
import org.agiso.tempel.api.model.TemplateEngine;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
@XStreamAlias("engine")
public class TemplateEngineBean implements TemplateEngine {
//	@XStreamAsAttribute
//	@XStreamAlias("class")
	private Class<? extends ITempelEngine> engineClass;

	private Map<String, String> properties;

//	--------------------------------------------------------------------------
	public Class<? extends ITempelEngine> getEngineClass() {
		return engineClass;
	}
	public void setEngineClass(Class<? extends ITempelEngine> engineClass) {
		this.engineClass = engineClass;
	}
	@SuppressWarnings("unchecked")
	public <T extends TemplateEngineBean> T withEngineClass(Class<? extends ITempelEngine> engineClass) {
		this.engineClass = engineClass;
		return (T)this;
	}

	public Map<String, String> getProperties() {
		return properties;
	}
	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
	@SuppressWarnings("unchecked")
	public <T extends TemplateEngineBean> T withProperties(Map<String, String> properties) {
		this.properties = properties;
		return (T)this;
	}

//	--------------------------------------------------------------------------
	@Override
	public ITempelEngine getInstance() {
		if(engineClass == null) {
			return null;
		} else try {
			ITempelEngine instance;
			try {
				// Instancjonowanie i inicjalizcja w oparciu o konstruktor inicjujący:
				@SuppressWarnings("unchecked")
				Constructor<ITempelEngine> constructor =
						(Constructor<ITempelEngine>)engineClass.getConstructor(Map.class);
				instance = constructor.newInstance(properties);
			} catch(NoSuchMethodException nsme) {
				// Instancjonowanie i inicjalizacja w oparciu o konstruktor bezargumentowy
				// i publiczne setter'y Java Beans:
				instance = engineClass.newInstance();

				if(properties != null && !properties.isEmpty()) {
					BeanInfo converterInfo = Introspector.getBeanInfo(engineClass);
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
									+ "in converter class " + engineClass.getCanonicalName());
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
	public TemplateEngineBean clone() {
		return fillClone(new TemplateEngineBean());
	}
	protected TemplateEngineBean fillClone(TemplateEngineBean clone) {
		clone.engineClass = engineClass;
		if(properties != null) {
			clone.properties = new LinkedHashMap<String, String>(properties);
		}

		return clone;
	}
}
