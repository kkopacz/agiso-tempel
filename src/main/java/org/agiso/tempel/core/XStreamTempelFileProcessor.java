/* org.agiso.tempel.core.XStreamTempelFileProcessor (15-12-2012)
 * 
 * XStreamTempelFileProcessor.java
 * 
 * Copyright 2012 agiso.org
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
import java.util.Map;
import java.util.Map.Entry;

import org.agiso.tempel.api.internal.ITempelEntryProcessor;
import org.agiso.tempel.api.internal.ITempelFileProcessor;
import org.agiso.tempel.core.model.beans.RepositoryBean;
import org.agiso.tempel.core.model.beans.TemplateBean;
import org.agiso.tempel.core.model.beans.TemplateParamBean;
import org.agiso.tempel.core.model.beans.TemplateReferenceBean;
import org.agiso.tempel.core.model.beans.TemplateResourceBean;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class XStreamTempelFileProcessor implements ITempelFileProcessor {
	private static final XStream xStream;

	static {
		// Konfiguracja XStream'a:
		// http://kickjava.com/src/com/thoughtworks/acceptance/MultipleObjectsInOneStreamTest.java.htm
		xStream = new XStream();
		xStream.alias("properties", Map.class);
		xStream.autodetectAnnotations(true);
		xStream.processAnnotations(new Class[] {
				RepositoryBean.class,
				TemplateBean.class,
				TemplateParamBean.class,
				TemplateReferenceBean.class,
				TemplateResourceBean.class
		});
		xStream.registerConverter(new MapEntryConverter());
		xStream.registerConverter(new RepositoryConverter());
		// xStream.alias("template", TemplateBean.class);
		// xStream.useAttributeFor(TemplateBean.class, "id");
		// xStream.alias("param", TemplateParamBean.class);
		// xStream.useAttributeFor(TemplateParamBean.class, "id");
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
	/**
	 * 
	 * 
	 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
	 */
	private static class MapEntryConverter implements Converter {
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
	 * 
	 * 
	 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
	 */
	private static class RepositoryConverter extends AbstractSingleValueConverter {
		@Override
		public boolean canConvert(@SuppressWarnings("rawtypes") Class type) {
			return type.equals(RepositoryBean.class);
		}

		@Override
		public Object fromString(String str) {
			RepositoryBean repository = new RepositoryBean();
			repository.setValue(str);
			return repository;
		}
	}
}
