/* org.agiso.tempel.core.processor.xstream.TemplateParamFetcherBeanFetcher (22-12-2013)
 * 
 * TemplateParamFetcherBeanFetcher.java
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

import org.agiso.tempel.core.model.beans.TemplateParamFetcherBean;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.mapper.Mapper;

/**
 * Konwerter XStream obsługujący konwersję znacznika &lt;fetcher&gt; definicji
 * dostarczyciela parametru szablonu.
 * 
 * @author Karol Kopacz
 * @since 1.0
 */
public class TemplateParamFetcherBeanConverter extends AbstractConfigurableConverter {
	public TemplateParamFetcherBeanConverter(Mapper mapper, ReflectionProvider reflectionProvider) {
		super(mapper, reflectionProvider);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class type) {
		return type.equals(TemplateParamFetcherBean.class);
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		String fetcherClassName = reader.getAttribute("class");
		if(fetcherClassName == null) {
			throw new ConversionException("Fetcher 'class' not defined");
		}
		if(fetcherClassName.isEmpty()) {
			throw new ConversionException("Empty string is invalid fetcher 'class' value");
		}

		TemplateParamFetcherBean templateParamFetcher = new TemplateParamFetcherBean();
		templateParamFetcher.setFetcherClassName(fetcherClassName);
		templateParamFetcher.setProperties(readProperties(reader));
		return templateParamFetcher;
	}
}
