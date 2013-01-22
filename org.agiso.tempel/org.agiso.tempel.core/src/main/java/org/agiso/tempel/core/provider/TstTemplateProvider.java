/* org.agiso.tempel.templates.test.TstTemplateProvider (20-01-2013)
 * 
 * TstTemplateProvider.java
 * 
 * Copyright 2013 agiso.org
 */
package org.agiso.tempel.core.provider;

import java.io.IOException;
import java.io.InputStream;
import java.util.StringTokenizer;

import org.agiso.tempel.Temp;
import org.agiso.tempel.api.ITemplateSource;
import org.agiso.tempel.core.model.Template;
import org.agiso.tempel.core.provider.source.JarArchiveTemplateSource;
import org.jboss.shrinkwrap.api.Node;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class TstTemplateProvider extends CachingTemplateProvider {
	// FIXME: Zastosować wstrzykiwanie zależności
	// private ITempelScopeInfo tempelScopeInfo = new TempelScopeInfo();

//	private Settings settings;
//	private MavenResolverSystem resolver = Maven.resolver();

//	--------------------------------------------------------------------------
	/**
	 * 
	 */
	public TstTemplateProvider() {
//		String userSettingsPaht = System.getProperty("user.home").concat("/.m2/settings.xml");
//		String globalSettingsPath = "/work/tools/maven/maven/conf/settings.xml";
//		// String localRepositoryPath = tempelScopeInfo.getSettingsPath(Scope.MAVEN);
//
//		SettingsBuildingRequest request = new DefaultSettingsBuildingRequest();
//		request.setUserSettingsFile(new File(userSettingsPaht));
//		request.setGlobalSettingsFile(new File(globalSettingsPath));
//
//		settings = new MavenSettingsBuilder().buildSettings(request);
//		// settings.setLocalRepository(localRepositoryPath);
//
//		settings = new MavenSettingsBuilder().buildDefaultSettings();
	}

//	--------------------------------------------------------------------------
	/* (non-Javadoc)
	 * @see org.agiso.tempel.core.provider.AbstractMvnTemplateProvider#doGet(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	protected JavaArchiveCacheEntry doGet(String key, String groupId, String templateId, String version) {
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
			JavaArchive archive = resolve(groupId, templateId, version);

			Node tempel_xml = archive.get("/TEMPEL-INF/tempel.xml");
			if(tempel_xml == null) {
				return null;					// nie znaleiono pliku tempel.xml
			}

			InputStream is = tempel_xml.getAsset().openStream();

			JavaArchiveCacheEntry cacheEntry = new JavaArchiveCacheEntry();
			cacheEntry.definition = Temp.ConvertUtils_convertStreamToString(is);
			cacheEntry.archive = archive;

			return cacheEntry;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	protected JavaArchive resolve(String groupId, String templateId, String version) throws Exception {
		JavaArchive archive = ShrinkWrap.create(JavaArchive.class, templateId + ":" + version + ".jar");
		archive.as(ExplodedImporter.class).importDirectory("src/main/resources");

		return archive;

//		File[] files = resolver.resolve(
//				groupId + ":" + templateId + ":" + version
//		).withTransitivity().asFile();
//
//		return Arrays.asList(files);
	}

//	--------------------------------------------------------------------------
	@Override
	public ITemplateSource createTemplateSource(Template template, String source) {
		JavaArchiveCacheEntry cacheEntry = getCacheEntry(
				template.getGroupId() + ":" + template.getTemplateId() + ":" + template.getVersion()
		);

		try {
			return new JarArchiveTemplateSource(cacheEntry.archive, source);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}

//	--------------------------------------------------------------------------
	public static class JavaArchiveCacheEntry extends CacheEntry {
		public JavaArchive archive;
	}
}
