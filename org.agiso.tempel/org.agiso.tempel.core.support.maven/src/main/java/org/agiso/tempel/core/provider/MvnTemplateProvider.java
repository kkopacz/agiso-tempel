/* org.agiso.tempel.core.provider.MvnTemplateProvider (15-12-2012)
 * 
 * MvnTemplateProvider.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.core.provider;

import java.io.File;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.agiso.tempel.api.internal.ITempelEntryProcessor;
import org.agiso.tempel.api.internal.ITemplateRepository;
import org.agiso.tempel.core.model.Template;
import org.apache.maven.repository.internal.DefaultServiceLocator;
import org.apache.maven.repository.internal.MavenRepositorySystemSession;
import org.apache.maven.wagon.Wagon;
import org.apache.maven.wagon.providers.file.FileWagon;
import org.apache.maven.wagon.providers.http.LightweightHttpWagon;
import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.RepositorySystemSession;
import org.sonatype.aether.collection.CollectRequest;
import org.sonatype.aether.connector.wagon.WagonProvider;
import org.sonatype.aether.connector.wagon.WagonRepositoryConnectorFactory;
import org.sonatype.aether.graph.Dependency;
import org.sonatype.aether.graph.DependencyNode;
import org.sonatype.aether.repository.LocalRepository;
import org.sonatype.aether.repository.RemoteRepository;
import org.sonatype.aether.resolution.DependencyRequest;
import org.sonatype.aether.spi.connector.RepositoryConnectorFactory;
import org.sonatype.aether.util.artifact.DefaultArtifact;
import org.sonatype.aether.util.graph.PreorderNodeListGenerator;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class MvnTemplateProvider extends BaseTemplateProvider {
	private RepositorySystem repoSystem;

	private RemoteRepository local;
	private RemoteRepository central;

	private Map<String, MvnTemplate> cache = new HashMap<String, MvnTemplate>();

//	--------------------------------------------------------------------------
	public MvnTemplateProvider() {
		repoSystem = newRepositorySystem();

		String path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		int index = path.lastIndexOf("/repo/");
		if(index != -1) {
			path = System.getProperty("user.home") + "/.m2/repository/";
		} else {
			path = System.getProperty("user.dir");
			path = path + "/src/test/resources/repository/maven";
		}
		local = new RemoteRepository("local", "default", "file://" + path);
		central = new RemoteRepository("central", "default", "http://repo1.maven.org/maven2/");
	}

//	--------------------------------------------------------------------------
	@Override
	public int getOrder() {
		return 40;
	}

//	--------------------------------------------------------------------------
	@Override
	public boolean contains(String key, String groupId, String templateId, String version) {
		if(!cache.containsKey(key)) {
			cache.put(key, xxxx(key, groupId, templateId, version));
		}
		return cache.get(key) != null;
	}

	@Override
	public Template get(String key, String groupId, String templateId, String version) {
		if(!cache.containsKey(key)) {
			cache.put(key, xxxx(key, groupId, templateId, version));
		}

		final MvnTemplate mvnTemplate = cache.get(key);
		if(mvnTemplate == null) {
			return null;
		}

		if(mvnTemplate.repository == null) {
			final ITemplateRepository templateRepository = new HashBasedTemplateRepository();

			try {
				tempelFileProcessor.process(mvnTemplate.definition, new ITempelEntryProcessor() {
					@Override
					public void processObject(Object object) {
						MvnTemplateProvider.this.processObject(Template.Scope.MAVEN, object, templateRepository);
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

//	--------------------------------------------------------------------------
	/**
	 * @param key
	 * @param groupId
	 * @param templateId
	 * @param version
	 * @return
	 */
	private MvnTemplate xxxx(String key, String groupId, String templateId, String version) {
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
			List<File> files = yyyy(groupId, templateId, version);
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
	private List<File> yyyy(String groupId, String templateId, String version) throws Exception {
		Dependency dependency = new Dependency(new DefaultArtifact(groupId + ':' + templateId + ':' + version), null);

		CollectRequest collectRequest = new CollectRequest();
		collectRequest.setRoot(dependency);
		collectRequest.addRepository(local);
		collectRequest.addRepository(central);

		RepositorySystemSession session = newSession(repoSystem);

		DependencyNode node = repoSystem.collectDependencies(session, collectRequest).getRoot();
		DependencyRequest dependencyRequest = new DependencyRequest(node, null);

		repoSystem.resolveDependencies(session, dependencyRequest);

		PreorderNodeListGenerator nlg = new PreorderNodeListGenerator();
		node.accept(nlg);
		return nlg.getFiles();
	}

	private static RepositorySystem newRepositorySystem() {
		DefaultServiceLocator locator = new DefaultServiceLocator();
		locator.setServices(WagonProvider.class, new ManualWagonProvider());
		locator.addService(RepositoryConnectorFactory.class, WagonRepositoryConnectorFactory.class);

		return locator.getService(RepositorySystem.class);
	}

	private static RepositorySystemSession newSession(RepositorySystem system) {
		MavenRepositorySystemSession session = new MavenRepositorySystemSession();

		LocalRepository localRepo = new LocalRepository( "target/local-repo" );
		session.setLocalRepositoryManager(system.newLocalRepositoryManager(localRepo));

		return session;
	}

	private static String convertStreamToString(java.io.InputStream is) {
		Scanner s = new Scanner(is, "UTF-8").useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

//	--------------------------------------------------------------------------
	static class MvnTemplate {
		String definition;
		List<File> classpath;
		ITemplateRepository repository;
	}

	static class ManualWagonProvider implements WagonProvider {
		@Override
		public Wagon lookup(final String roleHint) throws Exception {
			if("file".equals(roleHint)) {
				return new FileWagon();
			} else if("http".equals(roleHint)) {
				return new LightweightHttpWagon();
			}
//			else if(roleHint != null && roleHint.startsWith("http")) { // http and https
//				return new HttpWagon();
//			}
			return null;
		}
		@Override
		public void release(final Wagon wagon) {
			// intentionally empty
		}
	}
}
