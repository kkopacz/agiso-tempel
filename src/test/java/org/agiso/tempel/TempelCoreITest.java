/* org.agiso.tempel.TempelCoreITest (14-09-2012)
 * 
 * TempelCoreITest.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel;

import java.io.File;

import org.testng.annotations.Test;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class TempelCoreITest {
	@Test
	public void testTemplerCore() throws Exception {
		File workDir = new File("./target/templer2");
		if(!workDir.exists()) {
			workDir.mkdirs();
		}

		String[] args = new String[] {
				"velocityDirTemplate",
				"-d ./target/templer2"
		};

		Bootstrap.main(args);
	}
}
