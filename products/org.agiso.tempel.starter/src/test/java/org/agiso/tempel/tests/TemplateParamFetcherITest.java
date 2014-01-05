/* org.agiso.tempel.tests.TemplateParamFetcherITest (21-12-2013)
 * 
 * TemplateParamFetcherITest.java
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

import org.agiso.tempel.Temp;
import org.agiso.tempel.api.internal.IParamReader;
import org.agiso.tempel.starter.Bootstrap;
import org.mockito.InOrder;
import org.testng.annotations.Test;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class TemplateParamFetcherITest extends AbstractOutputTest {
//	--------------------------------------------------------------------------
//	src/test/templates/run/tempel.xml
//	--------------------------------------------------------------------------
	@Test
	public void templateParamFetcherTest01() throws Exception {
		String outPath = getOutputPath(true);

		// Tworzenie i konfiguracja pozornej implementacji IParamReader'a:
		IParamReader paramReader = mock(IParamReader.class);
		when(paramReader.getParamValue(eq("param_string1"), anyString(), anyString()))
			.thenReturn("value_string1");
		when(paramReader.getParamValue(eq("param_string2"), anyString(), anyString()))
			.thenReturn("value_string2");
		when(paramReader.getParamValue(eq("param_map.key1"), anyString(), anyString()))
			.thenReturn("value_map.key1");
		when(paramReader.getParamValue(eq("param_map.key2"), anyString(), anyString()))
			.thenReturn("value_map.key2");
		when(paramReader.getParamValue(eq("param_map.key3"), anyString(), anyString()))
			.thenReturn("value_map.key3");
		when(paramReader.getParamValue(eq("param_bean.field1"), anyString(), anyString()))
			.thenReturn("param_bean.field1");
		when(paramReader.getParamValue(eq("param_bean.field2"), anyString(), anyString()))
			.thenReturn("param_bean.field2");
		when(paramReader.getParamValue(eq("param_bean.field3"), anyString(), anyString()))
			.thenReturn("param_bean.field3");

		// Wywołanie Bootstrap i uruchamianie szablonu:
		Bootstrap.main(paramReader, new String[] {
				"org.agiso.tempel.tests:TemplateParamFetcherITest:templateParamFetcherTest01",
				"-d " + outPath
		});

		// Weryfikacja wywołań poleceń odczytu paramtrów:
		InOrder inOrder = inOrder(paramReader);
		inOrder.verify(paramReader, times(1)).getParamValue("param_string1", "String param 1", null);
		inOrder.verify(paramReader, times(1)).getParamValue("param_string2", "String param 2", "String value 2");
		inOrder.verify(paramReader, times(1)).getParamValue("param_map.key1", "Map param key 1", null);
		inOrder.verify(paramReader, times(1)).getParamValue("param_map.key2", "Map param key 2", null);
		inOrder.verify(paramReader, times(1)).getParamValue("param_map.key3", "Map param key 3", "Map param value 3");
		inOrder.verify(paramReader, times(1)).getParamValue("param_bean.field1", "Param 1", null);
		inOrder.verify(paramReader, times(1)).getParamValue("param_bean.field2", "Param 2", null);
		inOrder.verify(paramReader, times(1)).getParamValue("param_bean.field3", "Param 3", null);
		verifyNoMoreInteractions(paramReader);

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "8bb3dac758e311375d05ef0fe131e03b".equals(md5);
	}
}
