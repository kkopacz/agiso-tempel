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
package org.agiso.tempel.core.processor.xstream;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Map;

import org.agiso.tempel.api.internal.ITempelEntryProcessor;
import org.agiso.tempel.api.internal.ITempelFileProcessor;
import org.agiso.tempel.core.model.beans.TemplateBean;
import org.agiso.tempel.core.model.beans.TemplateEngineBean;
import org.agiso.tempel.core.model.beans.TemplateParamBean;
import org.agiso.tempel.core.model.beans.TemplateParamConverterBean;
import org.agiso.tempel.core.model.beans.TemplateParamValidatorBean;
import org.agiso.tempel.core.model.beans.TemplateReferenceBean;
import org.agiso.tempel.core.model.beans.TemplateResourceBean;
import org.springframework.stereotype.Component;

import com.thoughtworks.xstream.XStream;

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
		xStream.registerConverter(new TemplateEngineBeanConverter(
				xStream.getMapper(), xStream.getReflectionProvider()
		));
		xStream.registerConverter(new TemplateParamBeanAttributesConverter(
				xStream.getMapper(), xStream.getReflectionProvider()
		));
		xStream.registerConverter(new TemplateParamFetcherBeanConverter(
				xStream.getMapper(), xStream.getReflectionProvider()
		));
		xStream.registerConverter(new TemplateParamConverterBeanConverter(
				xStream.getMapper(), xStream.getReflectionProvider()
		));
		xStream.registerConverter(new TemplateParamValidatorBeanConverter(
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
