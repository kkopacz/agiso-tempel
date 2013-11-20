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
public class TemplateReferenceITest extends AbstractOutputTest {
//	--------------------------------------------------------------------------
//	src/test/configuration/runtime/tempel.xml
//	--------------------------------------------------------------------------
	/**
	 * Szablon grupujący org.agiso.tempel.tests:javaProject:1.0.0
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
				"-d " + outPath
		});

		// Weryfikacja wywołań poleceń odczytu paramtrów:
		InOrder inOrder = inOrder(paramReader);
		inOrder.verify(paramReader, times(1)).getParamValue("name", "Project name", null);
		inOrder.verify(paramReader, times(1)).getParamValue("package", "Package name", null);
		verifyNoMoreInteractions(paramReader);

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "5eab6c02e2877f31805c65e7e2aeba87".equals(md5);
	}

	/**
	 * Szablon grupujący org.agiso.tempel.tests:javaBundleProject:1.0.0
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
				"-d " + outPath
		});

		// Weryfikacja wywołań poleceń odczytu paramtrów:
		InOrder inOrder = inOrder(paramReader);
		inOrder.verify(paramReader, times(1)).getParamValue("name", "Project name", null);
		inOrder.verify(paramReader, times(1)).getParamValue("package", "Package name", null);
		verifyNoMoreInteractions(paramReader);

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "7adef6b68dbba75ceff3dbafc1435465".equals(md5);
	}
}
