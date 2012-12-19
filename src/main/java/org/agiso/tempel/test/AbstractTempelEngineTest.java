/* org.agiso.tempel.engine.AbstractEngineTest (16-11-2012)
 * 
 * AbstractTempelEngineTest.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.test;

import java.io.File;
import java.util.Calendar;

import org.agiso.tempel.core.engine.ITempelEngine;
import org.agiso.tempel.test.annotation.TempelEngineTest;
import org.testng.annotations.BeforeClass;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public abstract class AbstractTempelEngineTest {
	private long timeInMillis = Calendar.getInstance().getTimeInMillis();

	protected ITempelEngine engine;
	protected String repositoryPath;

// --------------------------------------------------------------------------
	@BeforeClass
	public void prepareTemplerEngine() throws Exception {
		// Wyznaczanie ścieżki bazowej repozytorium testowego:
		repositoryPath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		repositoryPath = repositoryPath.substring(0, repositoryPath.lastIndexOf("/target/test-classes/"));
		repositoryPath = repositoryPath + "/src/test/resources/";

		// Tworzenie i inicjalizacja silnika:
		engine = createTemplerEngineInstance();
	}

// --------------------------------------------------------------------------
	protected ITempelEngine createTemplerEngineInstance() throws Exception {
		TempelEngineTest tet = this.getClass().getAnnotation(TempelEngineTest.class);
		if(tet == null) {
			throw new RuntimeException("Test class not annotated with @TempelEngineTest");
		}

		return tet.value().newInstance();
	}

	protected String getOutputPath(boolean create) {
		int depth = 0;
		StackTraceElement[] trace = Thread.currentThread().getStackTrace();
		for(StackTraceElement traceElement : trace) {
			if(traceElement.getClassName().equals(this.getClass().getName())) {
				break;
			}
			depth++;
		}

		String outputPath = "./target/velocity-output/"
				+ this.getClass().getSimpleName() + "/"
				+ timeInMillis + "/" + trace[depth].getMethodName() + "/";
		if(create) {
			new File(outputPath).mkdirs();
		}
		return outputPath;
	}
}
