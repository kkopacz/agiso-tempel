/* org.agiso.core.test.AbstractOutputTest (14-01-2013)
 * 
 * AbstractOutputTest.java
 * 
 * Copyright 2013 agiso.org
 */
package org.agiso.core.test;

import java.io.File;
import java.util.Calendar;

/**
 * Klasa bazowa dla testów generujących zasoby podlegające weryfikacji.
 * Dostarcza mechanizmu tworzenia unikatowego katalogu dla tworzonych zasobów.
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public abstract class AbstractOutputTest {
	private long timeInMillis = Calendar.getInstance().getTimeInMillis();

//	--------------------------------------------------------------------------
	protected String getOutputPath(boolean create) {
		int depth = 0;
		StackTraceElement[] trace = Thread.currentThread().getStackTrace();
		for(StackTraceElement traceElement : trace) {
			if(traceElement.getClassName().equals(this.getClass().getName())) {
				break;
			}
			depth++;
		}

		String outputPath = "./target/test-output/"
				+ this.getClass().getSimpleName() + "/"
				+ timeInMillis + "/" + trace[depth].getMethodName() + "/";
		if(create) {
			new File(outputPath).mkdirs();
		}
		return outputPath;
	}
}
