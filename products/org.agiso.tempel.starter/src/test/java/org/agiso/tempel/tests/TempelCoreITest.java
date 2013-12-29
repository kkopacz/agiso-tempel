/* org.agiso.tempel.TempelCoreITest (14-09-2012)
 * 
 * TempelCoreITest.java
 * 
 * Copyright 2012 agiso.org
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
 * Testy działania szablonów testowych. Wykorzystują szablony z repozytoriów
 * testowych src/test/repository/ (dla standardowych szablonów umieszczanych
 * w repozytoriach katalogowych) oraz szablony z repozytorium maven'owego
 * (dla szablonów będących zasobami w repozytoriach maven).
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class TempelCoreITest extends AbstractOutputTest {
//	@Test
//	public void test_base_mkdir_1_0_0() throws Exception {
//		String outPath = getOutputPath(true);
//		Bootstrap.main(new String[] {
//				"org.agiso.tempel.templates:base.mkdir:1.0.0",
//				"-d " + outPath
//		});
//
//		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
//		System.out.println(md5);
//		assert "31c287aec4ff50269de4523bdfc4707b".equals(md5);
//	}

//	--------------------------------------------------------------------------
//	src/test/templates/app/tempel.xml
//	--------------------------------------------------------------------------

//	--------------------------------------------------------------------------
//	src/test/templates/usr/tempel.xml
//	--------------------------------------------------------------------------
	/**
	 * src/test/repository/home/
	 * org/agiso/tempel/tests/javaClass/1.0.0
	 */
	@Test
	public void testJavaClass_1_0_0() throws Exception {
		String outPath = getOutputPath(true);

		// Tworzenie i konfiguracja pozornej implementacji IParamReader'a:
		IParamReader paramReader = mock(IParamReader.class);
		when(paramReader.getParamValue(eq("srcDir"), anyString(), anyString()))
			.thenReturn("/src/main/java");
		when(paramReader.getParamValue(eq("package"), anyString(), anyString()))
			.thenReturn("org.agiso.package");
		when(paramReader.getParamValue(eq("name"), anyString(), anyString()))
			.thenReturn("SampleClass");

		// Wywołanie Bootstrap i uruchamianie szablonu:
		Bootstrap.main(paramReader, new String[] {
				"org.agiso.tempel.tests:javaClass:1.0.0",
				"-d " + outPath,
				"-Dyear=2013"
		});

		// Weryfikacja wywołań poleceń odczytu paramtrów:
		InOrder inOrder = inOrder(paramReader);
		inOrder.verify(paramReader, times(1)).getParamValue("srcDir", "Source directory", "/src/main/java");
		inOrder.verify(paramReader, times(1)).getParamValue("package", "Package name", null);
		inOrder.verify(paramReader, times(1)).getParamValue("name", "Class name", null);
		verifyNoMoreInteractions(paramReader);

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "afc6decadb5da14f7aa120b0adf1cb96".equals(md5);
	}
}
