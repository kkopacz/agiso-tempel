/* org.agiso.tempel.core.X (15-12-2012)
 * 
 * X.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.core;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.agiso.tempel.core.model.Template;
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
public class X {
	/**
	 * 
	 * 
	 * @author <a href="mailto:kkopacz@araj.pl">Karol Kopacz</a>
	 */
	public interface IEntryProcessor {
		public void processObject(Object object);
	}

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
	public void process(File file, IEntryProcessor entryProcessor) throws Exception {
		FileInputStream xmlStream = null;
		try {
			xmlStream = new FileInputStream(file);
			ObjectInputStream in = xStream.createObjectInputStream(new InputStreamReader(xmlStream));
			try {
				while(true) {
					entryProcessor.processObject(in.readObject());
				}
			} catch(EOFException e) {
				// Normalne zakończenie przetwarzania pliku szablonu
			}
		} finally {
			if(xmlStream != null) {
				xmlStream.close();
				xmlStream = null;
			}
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
