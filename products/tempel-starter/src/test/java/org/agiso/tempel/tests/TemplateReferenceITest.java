/* org.agiso.tempel.tests.TemplateReferenceITest (20-11-2013)
 * 
 * TemplateReferenceITest.java
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
public class TemplateReferenceITest extends AbstractOutputTest {
//	--------------------------------------------------------------------------
//	src/test/templates/run/tempel.xml
//	--------------------------------------------------------------------------
	/**
	 * Szablon grupujący org.agiso.tempel.tests:TemplateReferenceITest:templateReferenceTest01
	 */
	@Test
	public void templateReferenceTest01() throws Exception {
		String outPath = getOutputPath(true);

		// Tworzenie i konfiguracja pozornej implementacji IParamReader'a:
		IParamReader paramReader = mock(IParamReader.class);
		when(paramReader.getParamValue(eq("name"), anyString(), anyString()))
			.thenReturn("SampleProject");
		when(paramReader.getParamValue(eq("package"), anyString(), anyString()))
			.thenReturn("org.agiso.package");

		// Wywołanie Bootstrap i uruchamianie szablonu:
		Bootstrap.main(paramReader, new String[] {
				"org.agiso.tempel.tests:TemplateReferenceITest:templateReferenceTest01",
				"-d " + outPath,
				"-Dyear=2013"
		});

		// Weryfikacja wywołań poleceń odczytu paramtrów:
		InOrder inOrder = inOrder(paramReader);
		inOrder.verify(paramReader, times(1)).getParamValue("name", "Project name", null);
		inOrder.verify(paramReader, times(1)).getParamValue("package", "Package name", null);
		verifyNoMoreInteractions(paramReader);

		String md5 = DigestUtils.countDigest("MD5", new File(outPath));
		assert "e142610395f405d6a60beae6c10474cc".equals(md5) : md5;
	}

	/**
	 * Szablon grupujący org.agiso.tempel.tests:TemplateReferenceITest:templateReferenceTest02
	 */
	@Test
	public void templateReferenceTest02() throws Exception {
		String outPath = getOutputPath(true);

		// Tworzenie i konfiguracja pozornej implementacji IParamReader'a:
		IParamReader paramReader = mock(IParamReader.class);
		when(paramReader.getParamValue(eq("name"), anyString(), anyString()))
			.thenReturn("SampleBundleProject");
		when(paramReader.getParamValue(eq("package"), anyString(), anyString()))
			.thenReturn("org.agiso.package");

		// Wywołanie Bootstrap i uruchamianie szablonu:
		Bootstrap.main(paramReader, new String[] {
				"org.agiso.tempel.tests:TemplateReferenceITest:templateReferenceTest02",
				"-d " + outPath,
				"-Dyear=2013"
		});

		// Weryfikacja wywołań poleceń odczytu paramtrów:
		InOrder inOrder = inOrder(paramReader);
		inOrder.verify(paramReader, times(1)).getParamValue("name", "Project name", null);
		inOrder.verify(paramReader, times(1)).getParamValue("package", "Package name", null);
		verifyNoMoreInteractions(paramReader);

		String md5 = DigestUtils.countDigest("MD5", new File(outPath));
		assert "e40206a73f9732699c33b623b9a2b6f5".equals(md5) : md5;
	}
}
