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
import org.agiso.tempel.api.internal.ITemplateProvider;
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
 * Jeśli klasa testowa znajduje się w projekcie szablonu (zawierającym katalog
 * src/main/resources/TEMPEL-INF), wykorzystuje {@link TstTemplateProvider}'a
 * i dodaje do niego archiwum bieżącego szablonu przygotowywane za pomocą
 * biblioteki {@link ShrinkWrap}. Szablony zależne (podszablony) muszą się
 * znajdować w repozytoriach Maven i dostarczane są poprzez klasę {@link
 * ShrinkWrapMvnTemplateProvider}.
 * </br>
 * Jeśli klasa testowa nie znajduje się w projekcie szablonu, to wszystkie
 * testowane szablony pobiera {@link ShrinkWrapMvnTemplateProvider}.
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public abstract class AbstractTemplateTest extends AbstractOutputTest {

//	--------------------------------------------------------------------------
	private String groupId;
	private String templateId;
	private String version;

	protected Tempel tempel;

//	--------------------------------------------------------------------------
	/**
	 * Konstruktor dla klas testów umieszczonych poza projektami szablonów.
	 * Tworzy obiekt {@link Tempel} i rejestruje w nim provider'a {@link
	 * ShrinkWrapMvnTemplateProvider}, który pobiera testowane szablony z
	 * repozytorium Maven.
	 */
	public AbstractTemplateTest() {
		tempel = createTempelInstance();
	}

	/**
	 * Konstruktor dla klas testów umieszczonych wewnątrz projektów szablonów.
	 * Tworzy obiekt {@link Tempel} i rejestruje w nim provider'y {@link
	 * TstTemplateProvider} i {@link ShrinkWrapMvnTemplateProvider}. Pierwszy
	 * z nich odpowiada za dostarczenie biblioteki testowanego szablonu (która
	 * jest tworzona w metodzie {@link #createTemplateArchive()} za pomocą
	 * mechanizmów {@link ShrinkWrap}. Drugi dostarcza podszablonów testowanego
	 * szablonu, które muszą się znajdować w lokalnym repozytorium Maven.
	 * 
	 * @param groupId Grupa testowanego szablonu.
	 * @param templateId Identyfikator testowanego szablonu.
	 * @param version Wersja testowanego szablonu.
	 */
	public AbstractTemplateTest(String groupId, String templateId, String version) {
		this.groupId = groupId;
		this.templateId = templateId;
		this.version = version;

		tempel = createTempelInstance();
	}

//	--------------------------------------------------------------------------
	protected Tempel createTempelInstance() {
		File workDir = new File(".");
		File repoDir = new File("./src/test/resources/repository");

		Tempel tempel = new Tempel(workDir, repoDir);
		tempel.setTemplateProvider(getTemplateProvider());
		tempel.setTemplateVerifier(new RecursiveTemplateVerifier());
		tempel.setTemplateExecutor(new DefaultTemplateExecutor());
		return tempel;
	}

	protected ITemplateProvider getTemplateProvider() {
		MainTemplateProvider mainTemplateProvider = new MainTemplateProvider();
		mainTemplateProvider.setTemplateProviderElements(
				Arrays.asList(getTemplateProviders())
		);
		return mainTemplateProvider;
	}

	protected ITemplateProviderElement[] getTemplateProviders() {
		File tempelInf = new File("src/main/resources/TEMPEL-INF");
		if(tempelInf.exists() && tempelInf.isDirectory()) {
			Archive<?> archive = createTemplateArchive();

			TstTemplateProvider tstTemplateProvider = new TstTemplateProvider();
			tstTemplateProvider.addArchive(groupId, templateId, version, archive);

			return new ITemplateProviderElement[] {
					tstTemplateProvider,
					new ShrinkWrapMvnTemplateProvider()
			};
		}
		return new ITemplateProviderElement[] {
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
