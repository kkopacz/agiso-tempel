/* org.agiso.tempel.templates.test.AbstractTemplateTest (22-01-2013)
 * 
 * AbstractTemplateTest.java
 * 
 * Copyright 2013 agiso.org
 */
package org.agiso.tempel.templates.test;

import java.io.File;
import java.util.Arrays;

import org.agiso.tempel.Tempel;
import org.agiso.tempel.api.internal.ITemplateProviderElement;
import org.agiso.tempel.core.DefaultTemplateExecutor;
import org.agiso.tempel.core.RecursiveTemplateVerifier;
import org.agiso.tempel.core.provider.MainTemplateProvider;
import org.agiso.tempel.core.provider.ShrinkWrapMvnTemplateProvider;
import org.agiso.tempel.core.provider.TstTemplateProvider;
import org.agiso.tempel.test.AbstractOutputTest;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public abstract class AbstractTemplateTest extends  AbstractOutputTest {

//	--------------------------------------------------------------------------
	private String templateId;
	private String version;

	protected Tempel tempel;

//	--------------------------------------------------------------------------
	public AbstractTemplateTest(String groupId, String templateId, String version) {
		this.templateId = templateId;
		this.version = version;

		File workDir = new File(".");
		File repoDir = new File("./src/test/resources/repository");

		tempel = new Tempel(workDir, repoDir);

		TstTemplateProvider tstTemplateProvider;

		MainTemplateProvider mainTemplateProvider = new MainTemplateProvider();
		mainTemplateProvider.setTemplateProviderElements(
				Arrays.asList(new ITemplateProviderElement[] {
						tstTemplateProvider = new TstTemplateProvider(),
						new ShrinkWrapMvnTemplateProvider()
				})
		);

		Archive<?> archive = createTemplateArchive();
		tstTemplateProvider.addArchive(groupId, templateId, version, archive);

		tempel.setTemplateProvider(mainTemplateProvider);
		tempel.setTemplateVerifier(new RecursiveTemplateVerifier());
		tempel.setTemplateExecutor(new DefaultTemplateExecutor());
	}

//	--------------------------------------------------------------------------
	protected JavaArchive createTemplateArchive() {
		JavaArchive archive = ShrinkWrap.create(
				JavaArchive.class, templateId + ":" + version + ".jar"
		);
		archive.as(ExplodedImporter.class).importDirectory("src/main/resources");
		return archive;
	}
}
