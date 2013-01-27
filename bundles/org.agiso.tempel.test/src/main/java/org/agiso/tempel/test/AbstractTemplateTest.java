/* org.agiso.tempel.test.AbstractTemplateTest (22-01-2013)
 * 
 * AbstractTemplateTest.java
 * 
 * Copyright 2013 agiso.org
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
package org.agiso.tempel.test;

import java.io.File;

import org.agiso.core.test.AbstractOutputTest;
import org.agiso.tempel.ITempel;
import org.agiso.tempel.support.test.provider.IArchiveTemplateProvider;
import org.agiso.tempel.support.test.provider.ArchiveTemplateProvider;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Klasa bazowa dla klas testujących szablony przechowywane w repozytoriach
 * Maven. Sablony te posiadają dedykowane projekty z plikami pom.xml, które
 * po kompilacji w formie plików .jar umieszczane są w repozytoriach Maven.
 * </br>
 * Jeśli klasa testowa znajduje się w projekcie szablonu (zawierającym katalog
 * src/main/resources/TEMPEL-INF), wykorzystuje {@link ArchiveTemplateProvider}'a
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
	private String groupId;
	private String templateId;
	private String version;

	protected ITempel tempel;

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
	 * ArchiveTemplateProvider} i {@link ShrinkWrapMvnTemplateProvider}. Pierwszy
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
	protected ITempel createTempelInstance() {
		ClassPathXmlApplicationContext ctx = null;
		try {
			ctx = new ClassPathXmlApplicationContext(getContextConfigLocations());

			File tempelInf = new File("src/main/resources/TEMPEL-INF");
			if(tempelInf.exists() && tempelInf.isDirectory()) {
				Archive<?> archive = createTemplateArchive();

				// Mamy do czynienia z testem projektu szablonu:
				IArchiveTemplateProvider atp = ctx.getBean(IArchiveTemplateProvider.class);
				atp.addArchive(groupId, templateId, version, archive);
			}


			return ctx.getBean(ITempel.class);
		} finally {
			if(ctx != null) {
				try {
					ctx.close();
				} catch(Exception e) {
				}
			}
		}
	}

	protected String[] getContextConfigLocations() {
		return new String[] {
				"classpath*:/META-INF/spring/tempel-context.xml"
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