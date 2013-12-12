/* org.agiso.tempel.core.model.beans.AbstractBeanInitializer (12-12-2013)
 * 
 * AbstractBeanInitializer.java
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
import java.util.ArrayList;
import java.util.Collection;

import org.omg.Dynamic.Parameter;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public abstract class AbstractBeanInitializer<T> implements Cloneable {
	protected Class<T> beanClass;

	protected Collection<BeanProperty> properties;

//	--------------------------------------------------------------------------
	public Class<T> getBeanClass() {
		return beanClass;
	}
	public void setBeanClass(Class<T> beanClass) {
		this.beanClass = beanClass;
	}
	@SuppressWarnings("unchecked")
	public <C extends AbstractBeanInitializer<T>> C withBeanClass(Class<T> beanClass) {
		this.beanClass = beanClass;
		return (C)this;
	}

	public Collection<BeanProperty> getProperties() {
		return properties;
	}
	public void setProperties(Collection<BeanProperty> properties) {
		this.properties = properties;
	}
	@SuppressWarnings("unchecked")
	public <C extends AbstractBeanInitializer<T>> C withProperties(Collection<BeanProperty> properties) {
		this.properties = properties;
		return (C)this;
	}

//	--------------------------------------------------------------------------
	public T getInstance() {
		if(beanClass == null) {
			return null;
		} else try {
			T instance;
			try {
				// Instancjonowanie i inicjalizcja w oparciu o konstruktor inicjujący:
				Constructor<T> constructor = (Constructor<T>)beanClass.getConstructor(Collection.class);
				instance = constructor.newInstance(properties);
			} catch(NoSuchMethodException nsme) {
				// Instancjonowanie i inicjalizacja w oparciu o konstruktor bezargumentowy
				// i publiczne setter'y Java Beans:
				instance = beanClass.newInstance();

				if(properties != null && !properties.isEmpty()) {
					BeanInfo validatorInfo = Introspector.getBeanInfo(beanClass);
					PropertyDescriptor[] propertyDescriptors = validatorInfo.getPropertyDescriptors();
					for(BeanProperty beanProperty : properties) {
						String propertyName = beanProperty.getPropertyName();
						Method writeMethod = null;
						for(PropertyDescriptor propertyDescriptor : propertyDescriptors) {
							if(propertyDescriptor.getName().equals(propertyName)) {
								writeMethod = propertyDescriptor.getWriteMethod();
								break;
							}
						}
						if(writeMethod == null) {
							throw new RuntimeException("No public setter found for property '" + propertyName + "' "
									+ "in validator class " + beanClass.getCanonicalName());
						}
						Class<?> parameterType = writeMethod.getParameterTypes()[0];
						if(BeanProperty.class.equals(parameterType)) {
							writeMethod.invoke(instance, beanProperty);
						} else if(String.class.equals(parameterType)) {
							if(beanProperty.hasAttributes()) {
								throw new RuntimeException("BeanProperty '" + propertyName + "' setter should has "
										+ Parameter.class.getSimpleName() + " type parameter");
							}
							writeMethod.invoke(instance, beanProperty.getPropertyValue());
						}
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
	public abstract AbstractBeanInitializer<T> clone();

	protected <C extends AbstractBeanInitializer<T>> C fillClone(C clone) {
		clone.beanClass = beanClass;

		if(properties != null) {
			clone.properties = new ArrayList<BeanProperty>(properties);
		}

		return clone;
	}
}
