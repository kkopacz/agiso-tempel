/* org.agiso.tempel.test.AbstractTemplateTest (22-01-2013)
 * 
 * AbstractTemplateTest.java
 * 
 * Copyright 2013 agiso.org
 */
package org.agiso.tempel.test;

import java.io.File;
import java.util.Arrays;

import org.agiso.tempel.Tempel;
import org.agiso.tempel.api.internal.ITemplateProviderElement;
import org.agiso.tempel.core.DefaultTemplateExecutor;
import org.agiso.tempel.core.RecursiveTemplateVerifier;
import org.agiso.tempel.core.provider.MainTemplateProvider;
import org.agiso.tempel.core.provider.ShrinkWrapMvnTemplateProvider;
import org.agiso.tempel.core.provider.TstTemplateProvider;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

/**
 * Klasa bazowa dla klas testujących szablony przechowywane w repozytoriach
 * Maven. Sablony te posiadają dedykowane projekty z plikami pom.xml, które
 * po kompilacji w formie plików .jar umieszczane są w repozytoriach Maven.
 * </br>
 * Przed uruchomieniem testów szablonu, jego archiwum jest przygotowywane
 * za pomocą mechanizmów biblioteki {@link ShrinkWrap} i rejestrowane w
 * provider'ze {@link TstTemplateProvider}. Szablony zależne (podszablony)
 * muszą się znajdować w repozytoriach Maven i dostarczane są poprzez
 * {@link ShrinkWrapMvnTemplateProvider};
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public abstract class AbstractTemplateTest extends AbstractOutputTest {

//	--------------------------------------------------------------------------
	private String templateId;
	private String version;

	private TstTemplateProvider tstTemplateProvider;

	protected Tempel tempel;

//	--------------------------------------------------------------------------
	public AbstractTemplateTest(String groupId, String templateId, String version) {
		this.templateId = templateId;
		this.version = version;

		File workDir = new File(".");
		File repoDir = new File("./src/test/resources/repository");

		tempel = new Tempel(workDir, repoDir);

		tstTemplateProvider = new TstTemplateProvider();

		MainTemplateProvider mainTemplateProvider = new MainTemplateProvider();
		mainTemplateProvider.setTemplateProviderElements(
				Arrays.asList(getTemplateProviders())
		);

		Archive<?> archive = createTemplateArchive();
		tstTemplateProvider.addArchive(groupId, templateId, version, archive);

		tempel.setTemplateProvider(mainTemplateProvider);
		tempel.setTemplateVerifier(new RecursiveTemplateVerifier());
		tempel.setTemplateExecutor(new DefaultTemplateExecutor());
	}

//	--------------------------------------------------------------------------
	protected ITemplateProviderElement[] getTemplateProviders() {
		return new ITemplateProviderElement[] {
				tstTemplateProvider,
				new ShrinkWrapMvnTemplateProvider()
		};
	}

	protected JavaArchive createTemplateArchive() {
		JavaArchive archive = ShrinkWrap.create(
				JavaArchive.class, templateId + ":" + version + ".jar"
		);
		archive.as(ExplodedImporter.class).importDirectory("src/main/resources");
		return archive;
	}
}
