/* org.agiso.tempel.tests.TemplateParamConverterITest (20-11-2013)
 * 
 * TemplateParamConverterITest.java
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

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.FileInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.agiso.core.lang.util.ConvertUtils;
import org.agiso.core.lang.util.DigestUtils;
import org.agiso.tempel.api.internal.IParamReader;
import org.agiso.tempel.starter.Bootstrap;
import org.mockito.InOrder;
import org.testng.annotations.Test;

/**
 * 
 * 
 * @author Karol Kopacz
 * @since 1.0
 */
public class TemplateParamConverterITest extends AbstractOutputTest {
//	--------------------------------------------------------------------------
//	src/test/templates/run/tempel.xml
//	--------------------------------------------------------------------------
	@Test
	public void templateParamConverterTest01() throws Exception {
		Logger.getLogger("").setLevel(Level.FINE);

		String outPath = getOutputPath(true);

		// Tworzenie i konfiguracja pozornej implementacji IParamReader'a:
		IParamReader paramReader = mock(IParamReader.class);
		when(paramReader.getParamValue(eq("param_integer"), anyString(), anyString()))
			.thenReturn("12");
		when(paramReader.getParamValue(eq("param_long"), anyString(), anyString()))
			.thenReturn("3456");
		when(paramReader.getParamValue(eq("param_date"), anyString(), anyString()))
			.thenReturn("2013-01-02");

		// Wywołanie Bootstrap i uruchamianie szablonu:
		Bootstrap.main(paramReader, new String[] {
				"org.agiso.tempel.tests:TemplateParamConverterITest:templateParamConverterTest01",
				"-d " + outPath,
				"-Dtime_zone=GMT-3:00"
		});

		// Weryfikacja wywołań poleceń odczytu paramtrów:
		InOrder inOrder = inOrder(paramReader);
		inOrder.verify(paramReader, times(1)).getParamValue("param_integer", null, null);
		inOrder.verify(paramReader, times(1)).getParamValue("param_long", null, null);
		inOrder.verify(paramReader, times(1)).getParamValue("param_date", null, null);
		verifyNoMoreInteractions(paramReader);


		System.out.println("!!!!!!!!!!" + ConvertUtils.toString(new FileInputStream(outPath + "/templateFile1.txt")));

		String md5 = DigestUtils.countDigest("MD5", new File(outPath));
		assert "af89b01c75f70d5ff05d9b70c7fe639e".equals(md5) : md5;
	}

//	public static class TestParamConverter implements ITemplateParamConverter<String> {
//		@Override
//		public boolean canConvert(Class<?> type) {
//			return false;
//		}
//	
//		@Override
//		public String convert(String value) {
//			return value;
//		}
//	}
}
