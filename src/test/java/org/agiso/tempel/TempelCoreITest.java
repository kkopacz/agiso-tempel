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
				"velocityDirTemplate1",
				"-d ./target/templer2"
		});

		Bootstrap.main(new String[] {
				"velocityDirTemplate2",
				"-d ./target/templer2"
		});

		Bootstrap.main(new String[] {
				"velocityDirTemplate3",
				"-d ./target/templer2"
		});

		Bootstrap.main(new String[] {
				"velocityDirTemplate4",
				"-d ./target/templer2"
		});

		Bootstrap.main(new String[] {
				"velocityDirTemplate5",
				"-d ./target/templer2"
		});

		Bootstrap.main(new String[] {
				"velocityDirTemplate6",
				"-d ./target/templer2"
		});

		Bootstrap.main(new String[] {
				"velocityDirTemplate7",
				"-d ./target/templer2"
		});

		Bootstrap.main(new String[] {
				"velocityDirTemplate8",
				"-d ./target/templer2"
		});
	}
}
