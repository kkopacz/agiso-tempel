/* org.agiso.tempel.engine.AbstractEngineTest (16-11-2012)
 * 
 * AbstractTempelEngineTest.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.test;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.agiso.tempel.core.engine.ITempelEngine;
import org.agiso.tempel.core.model.Template.Scope;
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

// --------------------------------------------------------------------------
	@BeforeClass
	public void prepareTemplerEngine() throws Exception {
		// Przygotowanie mapy repozytoriów testowych:
		Map<Scope, String> repositories = new HashMap<Scope, String>();
		String path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		path = path.substring(0, path.lastIndexOf("/target/test-classes/")) + "/src/test/resources";
		repositories.put(Scope.RUNTIME, path);

		// Tworzenie i inicjalizacja silnika:
		engine = createTemplerEngineInstance();
		engine.initialize(repositories);
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
				+ timeInMillis + "/" + trace[depth].getMethodName();
		if(create) {
			new File(outputPath).mkdirs();
		}
		return outputPath;
	}
}
