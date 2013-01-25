/* org.agiso.tempel.AbstractOutputTest (24-01-2013)
 * 
 * AbstractOutputTest.java
 * 
 * Copyright 2013 agiso.org
 */
package org.agiso.tempel;

import java.io.File;
import java.util.Calendar;

/**
 * FIXME: Powtórzenie klasy z projektu .test
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class AbstractOutputTest {
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

		String outputPath = "./target/velocity-output/"
				+ this.getClass().getSimpleName() + "/"
				+ timeInMillis + "/" + trace[depth].getMethodName() + "/";
		if(create) {
			new File(outputPath).mkdirs();
		}
		return outputPath;
	}
}
