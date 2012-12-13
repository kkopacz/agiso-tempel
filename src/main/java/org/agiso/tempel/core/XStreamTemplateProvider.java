/* org.agiso.tempel.core.XStreamTemplateProvider (02-10-2012)
 * 
 * XStreamTemplateProvider.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.core;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.agiso.tempel.Temp;
import org.agiso.tempel.core.model.Repository;
import org.agiso.tempel.core.model.Template;
import org.agiso.tempel.core.model.TemplateResource;
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
public class XStreamTemplateProvider implements ITemplateProvider {
	private Map<String, Object> globalProperties;

	// FIXME: Zastosować wstrzykiwanie zależności
	private IExpressionEvaluator expressionEvaluator = new VelocityExpressionEvaluator();

//	--------------------------------------------------------------------------
	/**
	 * 
	 * 
	 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
	 */
	public class RepositoryConverter extends AbstractSingleValueConverter {
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

	/**
	 * 
	 * 
	 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
	 */
	public class MapEntryConverter implements Converter {
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

//	--------------------------------------------------------------------------
	@Override
	public ITemplateRepository readTemplates(Map<String, Object> globalProperties) throws IOException {
		this.globalProperties = globalProperties;

		// Budowanie mapy szablonów w oparciu o pliki konfiguracyjne templates.xml
		// w katalogu konfiguracyjnym aplikacji, katalogu domowym użytkownika oraz
		// katalogu bieżącym:
		ITemplateRepository templateRepository = new HashBasedTemplateRepository();

		// Przygotowanie parsera plików tempel.xml:
		XStream xStream = prepareXStream();

		FileInputStream xmlStream = null;

		// Mapa szablonów globalnych (katalog konfiguracyjny aplikacji):
		String appSettings = getApplicationSettingsPath();
		File appSettingsFile = new File(appSettings);
		try {
			xmlStream = new FileInputStream(appSettingsFile);
			ObjectInputStream in = xStream.createObjectInputStream(new InputStreamReader(xmlStream));
			try {
				while(true) {
					processObject(Template.Scope.GLOBAL, in.readObject(), templateRepository);
				}
			} catch(EOFException e) {
				System.out.println("Wczytano ustawienia globalne z pliku " + appSettingsFile.getCanonicalPath());
			}
		} catch(Exception e) {
			System.err.println("Błąd wczytywania ustawień globalnych: " + e.getMessage());
			throw new RuntimeException(e);
		} finally {
			if(xmlStream != null) {
				xmlStream.close();
				xmlStream = null;
			}
		}

		// Mapa szablonów użytkownika (katalog domowy użytkownika):
		String usrSettings = getUserSettingsPath();
		File usrSettingsFile = new File(usrSettings);
		if(usrSettingsFile.exists() && usrSettingsFile.isFile()) {
			try {
				xmlStream = new FileInputStream(usrSettingsFile);
				ObjectInputStream in = xStream.createObjectInputStream(new InputStreamReader(xmlStream));
				try {
					while(true) {
						processObject(Template.Scope.USER, in.readObject(), templateRepository);
					}
				} catch(EOFException e) {
					System.out.println("Wczytano ustawienia użytkownika z pliku " + usrSettingsFile.getCanonicalPath());
				}
			} catch(Exception e) {
				System.err.println("Błąd wczytywania ustawień użytkownika: " + e.getMessage());
				throw new RuntimeException(e);
			} finally {
				if(xmlStream != null) {
					xmlStream.close();
					xmlStream = null;
				}
			}
		}

		// Mapa szablonów lokalnych (katalog bieżący projektu):
		String runSettings = getRunningSettings();
		File runSettingsFile = new File(runSettings);
		if(runSettingsFile.exists() && runSettingsFile.isFile()) {
			try {
				xmlStream = new FileInputStream(runSettingsFile);
				ObjectInputStream in = xStream.createObjectInputStream(new InputStreamReader(xmlStream));
				try {
					while(true) {
						processObject(Template.Scope.RUNTIME, in.readObject(), templateRepository);
					}
				} catch(EOFException e) {
					System.out.println("Wczytano ustawienia lokalne z pliku " + runSettingsFile.getCanonicalPath());
				}
			} catch(Exception e) {
				System.err.println("Błąd wczytywania ustawień lokalnych: " + e.getMessage());
				throw new RuntimeException(e);
			} finally {
				if(xmlStream != null) {
					xmlStream.close();
					xmlStream = null;
				}
			}
		}

		return templateRepository;
	}

//	--------------------------------------------------------------------------
	/**
	 * @return
	 */
	private XStream prepareXStream() {
		// Konfiguracja XStream'a:
		// http://kickjava.com/src/com/thoughtworks/acceptance/MultipleObjectsInOneStreamTest.java.htm
		XStream xStream = new XStream();
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

		return xStream;
	}

	/**
	 * @return
	 */
	private String getApplicationSettingsPath() {
		String path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();

		int index = path.lastIndexOf("/repo/");
		if(index != -1) {
			path = path.substring(0, index) + "/conf/tempel.xml";
		} else {
			index = path.lastIndexOf("/target/classes/");
			path = path.substring(0, index) + "/src/test/configuration/application/tempel.xml";
		}

		return path;
	}

	/**
	 * @return
	 */
	private String getUserSettingsPath() {
		String path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();

		int index = path.lastIndexOf("/repo/");
		if(index != -1) {
			path = System.getProperty("user.home") + "/.tempel/tempel.xml";
		} else {
			index = path.lastIndexOf("/target/classes/");
			path = path.substring(0, index) + "/src/test/configuration/home/tempel.xml";
		}

		return path;
	}

	/**
	 * @return
	 */
	private String getRunningSettings() {
		String path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();

		int index = path.lastIndexOf("/repo/");
		if(index != -1) {
			path = System.getProperty("user.dir") + "/tempel.xml";
		} else {
			index = path.lastIndexOf("/target/classes/");
			path = path.substring(0, index) +"/src/test/configuration/runtime/tempel.xml";
		}

		return path;
	}

//	--------------------------------------------------------------------------
	/**
	 * Przetwarza obiekt odczytany z pliku tempel.xml
	 * 
	 * @param scope
	 * @param object
	 * @param templateRepository
	 */
	private void processObject(Template.Scope scope, Object object, ITemplateRepository templateRepository) {
		if(object instanceof Repository) {
			Repository repository = (Repository)object;

			templateRepository.setRepository(scope, repository);
		} else if(object instanceof Template) {
			Template template = (Template)object;
			template.setScope(scope);

			if(template.getResources() != null) {
				for(TemplateResource resource : template.getResources()) {
					resource.setParentTemplateReference(template);
				}
			}

			String gId = Temp.StringUtils_emptyIfBlank(template.getGroupId());
			String tId = Temp.StringUtils_emptyIfBlank(template.getTemplateId());
			String ver = Temp.StringUtils_emptyIfBlank(template.getVersion());
			templateRepository.put(gId, tId, ver, template);

			String key = template.getKey();
			if(!Temp.StringUtils_isBlank(key)) {
				templateRepository.put(key, template);
			}
		} else if(object instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<String, String> scopeProperties = (Map<String, String>)object;
			for(String key : scopeProperties.keySet()) {
				String value = scopeProperties.get(key);
				value = expressionEvaluator.evaluate(value, globalProperties);
				// CHECK: scopeProperties.put(key, value);	// aktualizacja wartości po rozwinięciu
				globalProperties.put(key, value);
			}
			globalProperties.put(scope.name(), Collections.unmodifiableMap(scopeProperties));
		}
	}
}
