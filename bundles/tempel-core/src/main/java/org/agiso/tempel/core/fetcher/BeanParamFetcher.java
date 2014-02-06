/* org.agiso.tempel.core.fetcher.BeanParamFetcher (29-12-2013)
 * 
 * BeanParamFetcher.java
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

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Collection;

import org.agiso.tempel.api.ITemplateParamFetcher;
import org.agiso.tempel.api.internal.IParamData;
import org.agiso.tempel.api.internal.IParamReader;
import org.agiso.tempel.core.model.beans.BeanProperty;

/**
 * 
 * 
 * @author Karol Kopacz
 * @since 1.0
 */
public class BeanParamFetcher implements ITemplateParamFetcher<Object> {
	public Collection<BeanProperty> properties;

//	--------------------------------------------------------------------------
	public BeanParamFetcher(Collection<BeanProperty> properties) {
		this.properties = properties;
	}

//	--------------------------------------------------------------------------
	@Override
	public Object fetch(IParamReader reader, IParamData param) {
		Class<?> beanClass = null;
		BeanInfo beanInfo = null;
		Object beanInstance = null;
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			beanClass = Class.forName(param.getType(), true, classLoader);
			beanInfo = Introspector.getBeanInfo(beanClass);
			beanInstance = beanClass.newInstance();

			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for(BeanProperty beanProperty : properties) {
				// Odczytujemy klucz, nazwę i wartość domyślną parametru:
				String propertyName = beanProperty.getPropertyName();
				String attributeName = beanProperty.getAttribute("name");
				String attributeValue = beanProperty.getAttribute("value");

				String propertyValue = reader.getParamValue(param.getKey() + "." + propertyName, attributeName, attributeValue);

				// TODO: W oparciu o dodatkowe atrybuty przeprowadzamy walidację wartości:

				// Po walidacji wartości wyszukujemy i wywołujemy setter'a:
				Method writeMethod = null;
				for(PropertyDescriptor propertyDescriptor : propertyDescriptors) {
					if(propertyDescriptor.getName().equals(propertyName)) {
						writeMethod = propertyDescriptor.getWriteMethod();
						break;
					}
				}
				if(writeMethod == null) {
					throw new RuntimeException("No public setter found for property '" + propertyName + "' "
							+ "in bean class " + beanClass.getCanonicalName());
				}
				writeMethod.invoke(beanInstance, propertyValue);
			}
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
		return beanInstance;
	}
}
