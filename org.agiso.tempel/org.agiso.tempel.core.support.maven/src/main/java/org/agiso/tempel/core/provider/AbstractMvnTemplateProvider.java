/* org.agiso.tempel.core.provider.AbstractMvnTemplateProvider (19-01-2013)
 * 
 * AbstractMvnTemplateProvider.java
 * 
 * Copyright 2013 agiso.org
 */
package org.agiso.tempel.core.provider;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.agiso.tempel.JarTemplateSource;
import org.agiso.tempel.api.ITemplateSource;
import org.agiso.tempel.api.internal.ITempelEntryProcessor;
import org.agiso.tempel.api.internal.ITemplateProviderElement;
import org.agiso.tempel.api.internal.ITemplateRepository;
import org.agiso.tempel.core.model.ITemplateSourceFactory;
import org.agiso.tempel.core.model.Template;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public abstract class AbstractMvnTemplateProvider extends BaseTemplateProvider implements ITemplateProviderElement {
	private Map<String, MvnTemplate> cache = new HashMap<String, MvnTemplate>();

//	--------------------------------------------------------------------------
	@Override
	public int getOrder() {
		return 40;
	}

//	--------------------------------------------------------------------------
	@Override
	public boolean contains(String key, String groupId, String templateId, String version) {
		if(!cache.containsKey(key)) {
			cache.put(key, doGet(key, groupId, templateId, version));
		}
		return cache.get(key) != null;
	}

	@Override
	public Template get(String key, String groupId, String templateId, String version) {
		if(!cache.containsKey(key)) {
			cache.put(key, doGet(key, groupId, templateId, version));
		}

		final MvnTemplate mvnTemplate = getMvnTemplate(key);
		if(mvnTemplate == null) {
			return null;
		}

		if(mvnTemplate.repository == null) {
			final ITemplateRepository templateRepository = new HashBasedTemplateRepository();

			try {
				tempelFileProcessor.process(mvnTemplate.definition, new ITempelEntryProcessor() {
					@Override
					public void processObject(Object object) {
						AbstractMvnTemplateProvider.this.processObject(Template.Scope.MAVEN, object, templateRepository,
								new ITemplateSourceFactory() {
									@Override
									public ITemplateSource createTemplateSource(Template template, String source) {
										try {
											return new JarTemplateSource(getTemplatePath(template), source);
										} catch(IOException e) {
											throw new RuntimeException(e);
										}
									}
								}
						);
					}
				});
				System.out.println("Wczytano ustawienia z biblioteki szablonu " + key);
			} catch(Exception e) {
				System.err.println("Błąd wczytywania ustawień z biblioteki szablonu '" + key + "': " + e.getMessage());
				throw new RuntimeException(e);
			}

			mvnTemplate.repository = templateRepository;
		}

		return mvnTemplate.repository.get(key, groupId, templateId, version);
	}

	protected final MvnTemplate getMvnTemplate(String key) {
		return cache.get(key);
	}

	protected abstract String getTemplatePath(Template template);

//	--------------------------------------------------------------------------
	/**
	 * @param key
	 * @param groupId
	 * @param templateId
	 * @param version
	 * @return
	 */
	protected MvnTemplate doGet(String key, String groupId, String templateId, String version) {
		if(key.indexOf(':') <= 0) {
			return null;
		}

		// Wydzielamy z klucza grupę, szablon i wersję:
		StringTokenizer tokenizer = new StringTokenizer(key, ":", false);
		groupId = tokenizer.nextToken();
		templateId = tokenizer.nextToken();
		version = tokenizer.nextToken();

		// Pobieranie i przegląd bibliotek z repozytorium maven, wyszukiwanie biblioteki
		// szablonu i sprawdzanie czy zawiera plik tempel.xml. Jeśli tak, to jego
		// odczytywanie i parsowanie w celu przygotowania obiektu Template:
		try {
			List<File> files = resolve(groupId, templateId, version);
			for(File file : files) {
				if(file.getName().equals(templateId + '-' + version + ".jar")) {
					JarEntry tempel_xml = null;
					JarFile jarFile = new JarFile(file);
					try {
						Enumeration<JarEntry> jarEntries = jarFile.entries();
						while(jarEntries.hasMoreElements()) {
							JarEntry jarEntry = jarEntries.nextElement();
							if("TEMPEL-INF/tempel.xml".equals(jarEntry.getName())) {
								tempel_xml = jarEntry;
								break;
							}
						}

						if(tempel_xml == null) {
							return null;					// nie znaleiono pliku tempel.xml
						}

						InputStream is = jarFile.getInputStream(tempel_xml);

						MvnTemplate mvnTemplate = new MvnTemplate();
						mvnTemplate.path = file.getCanonicalPath();
						mvnTemplate.definition = convertStreamToString(is);
						mvnTemplate.classpath = files;

						return mvnTemplate;
					} finally {
						if(jarFile != null) {
							jarFile.close();
						}
					}
				}
			}

		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}

		return null;
	}

	/**
	 * @param groupId
	 * @param templateId
	 * @param version
	 * @return
	 */
	protected abstract List<File> resolve(String groupId, String templateId, String version) throws Exception;

//	---------------------------------------------------------------------------
	protected static String convertStreamToString(java.io.InputStream is) {
		Scanner s = new Scanner(is, "UTF-8").useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

//	--------------------------------------------------------------------------
	public static class MvnTemplate {
		public String path;
		public String definition;
		public List<File> classpath;
		public ITemplateRepository repository;
	}
}
