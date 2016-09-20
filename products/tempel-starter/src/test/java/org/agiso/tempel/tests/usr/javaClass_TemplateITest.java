/* org.agiso.tempel.tests.usr_TemplateITest (14-09-2012)
 * 
 * javaClass_TemplateITest.java
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
package org.agiso.tempel.tests.usr;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.File;

import org.agiso.core.lang.util.DigestUtils;
import org.agiso.core.test.AbstractOutputTest;
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
 * @author Karol Kopacz
 * @since 1.0
 */
public class javaClass_TemplateITest extends AbstractOutputTest {
	protected String getOutputPathPrefix() {
		return "./target/velocity-output/";
	}

//	--------------------------------------------------------------------------
//	src/test/templates/usr/tempel.xml
//	--------------------------------------------------------------------------
	/**
	 * src/test/repository/home/
	 * org/agiso/tempel/tests/javaClass/usr
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
				"org.agiso.tempel.tests:javaClass:usr",
				"-d " + outPath,
				"-Dyear=2013"
		});

		// Weryfikacja wywołań poleceń odczytu paramtrów:
		InOrder inOrder = inOrder(paramReader);
		inOrder.verify(paramReader, times(1)).getParamValue("srcDir", "Source directory", "/src/main/java");
		inOrder.verify(paramReader, times(1)).getParamValue("package", "Package name", null);
		inOrder.verify(paramReader, times(1)).getParamValue("name", "Class name", null);
		verifyNoMoreInteractions(paramReader);

		String md5 = DigestUtils.countDigest("MD5", new File(outPath));
		assert "677a1eabb14d0bb2a0c3f108d2795ad1".equals(md5) : md5;
	}
}
