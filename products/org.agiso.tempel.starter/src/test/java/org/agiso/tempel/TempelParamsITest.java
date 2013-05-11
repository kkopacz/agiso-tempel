/* org.agiso.tempel.TempelParamsITest (03-04-2013)
 * 
 * TempelParamsITest.java
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
package org.agiso.tempel;

import java.io.File;

import org.agiso.tempel.starter.Bootstrap;
import org.testng.annotations.Test;

/**
 * Testy działania poprawności obsługi parametrów. Wykorzystują pliki
 * konfiguracyjne umieszczone w katalogu src/test/configuration/ oraz
 * szablony testowe z repozytoriów testowych src/test/repository/.
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class TempelParamsITest extends AbstractOutputTest {
	@Test
	public void testTempelParams_01() throws Exception {
		String outPath = "target/velocity-output/TempelParamsITest/0000000000000"; // getOutputPath(true);
		File outDir = new File(outPath);
		if(!outDir.exists()) {
			outDir.mkdirs();
		}

		// Wywołanie Bootstrap i uruchamianie szablonu:
		Bootstrap.main(new String[] {
				"org.agiso.tempel.tests:paramsTestTemplate:1.0.0",
				"-d " + outPath,
				"-Ddate_format=yyyy-MM-dd HH:mm:ss",
				"-Ddate=2010-12-21 11:12:13",
				"-Dproperty_01=value 01 (runtime)",
				"-Ddate_locale=fr_FR"
		});

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		System.out.println(md5);
		// assert "eabcbb3f24682664c844ed3a8031a3f5".equals(md5);
	}
}
