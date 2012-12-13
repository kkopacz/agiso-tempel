/* org.agiso.tempel.TempelCoreITest (14-09-2012)
 * 
 * TempelCoreITest.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel;

import java.io.File;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.maven.repository.internal.DefaultServiceLocator;
import org.apache.maven.repository.internal.MavenRepositorySystemSession;
import org.apache.maven.wagon.Wagon;
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
import org.testng.annotations.Test;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class TempelCoreITest {
//	@Test
	public void testTemplerCore() throws Exception {
		File workDir = new File("./target/templer2/subdir");
		if(!workDir.exists()) {
			workDir.mkdirs();
		}

		Bootstrap.main(new String[] {
				// "org.agiso.tempel.templates.tests:velocityDirTemplate:1.0.0",
				"velocityDirTemplate1",
				"-d ./target/templer2"
		});

		Bootstrap.main(new String[] {
				"velocityDirTemplate2",
				"-d ./target/templer2"
		});

		Bootstrap.main(new String[] {
				"velocityDirTemplate3",
				"-d ./target/templer2"
		});

		Bootstrap.main(new String[] {
				"velocityDirTemplate4",
				"-d ./target/templer2"
		});

		Bootstrap.main(new String[] {
				"velocityDirTemplate5",
				"-d ./target/templer2"
		});

		Bootstrap.main(new String[] {
				"velocityDirTemplate6",
				"-d ./target/templer2"
		});

		Bootstrap.main(new String[] {
				"velocityDirTemplate7",
				"-d ./target/templer2"
		});

		Bootstrap.main(new String[] {
				"velocityDirTemplate8",
				"-d ./target/templer2"
		});
	}

//	@Test
	public void testAether() throws Exception {
		RepositorySystem repoSystem = newRepositorySystem();
		RepositorySystemSession session = newSession(repoSystem);

		String groupId = "javax.persistence";
		String artifactId = "persistence-api";
		String version = "1.0";
		
		Dependency dependency = new Dependency(new DefaultArtifact(groupId + ':' + artifactId + ':' + version), "runtime");
		RemoteRepository central = new RemoteRepository( "central", "default", "http://repo1.maven.org/maven2/" );

		CollectRequest collectRequest = new CollectRequest();
		collectRequest.setRoot(dependency);
		collectRequest.addRepository(central);
		DependencyNode node = repoSystem.collectDependencies(session, collectRequest).getRoot();

		DependencyRequest dependencyRequest = new DependencyRequest(node, null);

		repoSystem.resolveDependencies(session, dependencyRequest);

		PreorderNodeListGenerator nlg = new PreorderNodeListGenerator();
		node.accept(nlg);
		List<File> files = nlg.getFiles();
		for(File file : files) {
			if(file.getName().equals(artifactId + '-' + version + ".jar")) {
				JarFile jarFile = new JarFile(file);
				try {
					Enumeration<JarEntry> jarEntries = jarFile.entries();
					while(jarEntries.hasMoreElements()) {
						JarEntry jarEntry = jarEntries.nextElement();
						System.out.println(jarEntry.getName());
					}
				} finally {
					if(jarFile != null) {
						jarFile.close();
					}
				}
			}
		}
	}

	private static RepositorySystem newRepositorySystem() {
		DefaultServiceLocator locator = new DefaultServiceLocator();
		locator.setServices(WagonProvider.class, new ManualWagonProvider());
		locator.addService(RepositoryConnectorFactory.class, WagonRepositoryConnectorFactory.class );

		return locator.getService( RepositorySystem.class );
	}

	private static RepositorySystemSession newSession(RepositorySystem system) {
		MavenRepositorySystemSession session = new MavenRepositorySystemSession();

		LocalRepository localRepo = new LocalRepository( "target/local-repo" );
		session.setLocalRepositoryManager(system.newLocalRepositoryManager(localRepo));

		return session;
	}

	private static class ManualWagonProvider implements WagonProvider {
		@Override
		public Wagon lookup(final String roleHint) throws Exception {
//			if("file".equals(roleHint)) {
//				return new FileWagon();
//			} else
			if("http".equals(roleHint)) {
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
