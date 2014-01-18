/* org.agiso.core.test.AbstractOutputTest (14-01-2013)
 * 
 * AbstractOutputTest.java
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
package org.agiso.core.test;

import java.io.File;
import java.util.Calendar;

import org.agiso.tempel.Temp;

/**
 * Klasa bazowa dla testów generujących zasoby podlegające weryfikacji.
 * Dostarcza mechanizmu tworzenia unikatowego katalogu dla tworzonych zasobów.
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public abstract class AbstractOutputTest {
	private long timeInMillis = Calendar.getInstance().getTimeInMillis();

//	--------------------------------------------------------------------------
	protected final String getOutputPath(boolean create) {
		return getOutputPath(create, null);
	}

	protected String getOutputPath(boolean create, String contentDir) {
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

		if(contentDir != null) {
			Temp.FileUtils_copyDir(contentDir, outputPath);
		}

		return outputPath;
	}
}
