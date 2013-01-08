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
		File workDir = new File("./target/templer2/subdir");
		if(!workDir.exists()) {
			workDir.mkdirs();
		}

		Bootstrap.main(new String[] {
				"org.agiso.tempel.tests:velocityFileTemplate:1.0.0",	// "velocityFileTemplate1",
				"-d ./target/templer2"
		});

		Bootstrap.main(new String[] {
				"org.agiso.tempel.tests:velocityDirTemplate:1.0.0",		// "velocityDirTemplate1",
				"-d ./target/templer2"
		});

		Bootstrap.main(new String[] {
				"org.agiso.tempel.tests:velocityDirTemplate:2.0.0",		// "velocityDirTemplate2",
				"-d ./target/templer2"
		});

		Bootstrap.main(new String[] {
				"org.agiso.tempel.tests:velocityDirTemplate:3.0.0",		// "velocityDirTemplate3",
				"-d ./target/templer2"
		});

		Bootstrap.main(new String[] {
				"org.agiso.tempel.tests:velocityDirTemplate:4.0.0",		// "velocityDirTemplate4",
				"-d ./target/templer2"
		});

		Bootstrap.main(new String[] {
				"org.agiso.tempel.tests:velocityDirTemplate:5.0.0",		// "velocityDirTemplate5",
				"-d ./target/templer2"
		});

		Bootstrap.main(new String[] {
				"org.agiso.tempel.tests:velocityDirTemplate:6.0.0",		// "velocityDirTemplate6",
				"-d ./target/templer2"
		});

		Bootstrap.main(new String[] {
				"org.agiso.tempel.tests:velocityDirTemplate:7.0.0",		// "velocityDirTemplate7",
				"-d ./target/templer2"
		});

		Bootstrap.main(new String[] {
				"org.agiso.tempel.tests:velocityDirTemplate:8.0.0",		// "velocityDirTemplate8",
				"-d ./target/templer2"
		});

		Bootstrap.main(new String[] {
				"org.agiso.tempel.tests:velocityDirTemplate:9.0.0",		// "velocityDirTemplate9",
				"-d ./target/templer2"
		});
	}
}
