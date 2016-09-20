/* org.agiso.tempel.TempelParamsITest (03-04-2013)
 * 
 * InputParamsITest.java
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
package org.agiso.tempel.tests;

import java.io.File;

import org.agiso.core.lang.util.DigestUtils;
import org.agiso.core.test.AbstractOutputTest;
import org.agiso.tempel.starter.Bootstrap;
import org.testng.annotations.Test;

/**
 * Testy działania poprawności obsługi parametrów. Wykorzystują pliki
 * konfiguracyjne umieszczone w katalogu src/test/configuration/ oraz
 * szablony testowe z repozytoriów testowych src/test/repository/.
 * 
 * @author Karol Kopacz
 * @since 1.0
 */
public class InputParamsITest extends AbstractOutputTest {
	protected String getOutputPathPrefix() {
		return "./target/velocity-output/";
	}

//	--------------------------------------------------------------------------
//	src/test/templates/run/tempel.xml
//	--------------------------------------------------------------------------
	@Test
	public void inputParamsTest01() throws Exception {
		String outPath = getOutputPath(true);
		File outDir = new File(outPath);
		if(!outDir.exists()) {
			outDir.mkdirs();
		}

		System.setProperty("user.name", "sysuser");

		// Wywołanie Bootstrap i uruchamianie szablonu:
		Bootstrap.main(new String[] {
				"org.agiso.tempel.tests:InputParamsITest:inputParamsTest01",
				"-d " + outPath,
				"-Ddate_locale=de_DE",
				"-Dtime_zone=GMT-8:00",
				"-Ddate_format=yyyy-MM-dd HH:mm:ss",
				"-Ddate=2010-12-21 11:12:13",
				"-Ddate_format_short=dd/MM/yyyy",
				"-Dproperty_01=property value 01 (runtime)",
				"-Duser_name=tpluser",
				"-Dparam_01=param value 01 (command)",
				"-Dparam_02=param value 02 (command)"
		});

		String md5 = DigestUtils.countDigest("MD5", new File(outPath));
		assert "575cef4cb4e23312591f588fd2e15bd0".equals(md5) : md5;
	}
}
