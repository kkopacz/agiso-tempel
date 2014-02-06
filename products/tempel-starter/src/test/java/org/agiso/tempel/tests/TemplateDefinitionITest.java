/* org.agiso.tempel.tests.TemplateDefinitionITest (20-11-2013)
 * 
 * TemplateDefinitionITest.java
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
 * @author Karol Kopacz
 * @since 1.0
 */
public class TemplateDefinitionITest extends AbstractOutputTest {
//	--------------------------------------------------------------------------
//	src/test/templates/run/tempel.xml
//	--------------------------------------------------------------------------
	/**
	 * src/test/templates/run/repo/
	 * org/agiso/tempel/tests/TemplateDefinitionITest/templateDefinitionTest01
	 */
	@Test
	public void templateDefinitionTest01() throws Exception {
		String outPath = getOutputPath(true);

		// Tworzenie i konfiguracja pozornej implementacji IParamReader'a:
		IParamReader paramReader = mock(IParamReader.class);
		when(paramReader.getParamValue(eq("name"), anyString(), anyString()))
			.thenReturn("value 1");

		// Wywołanie Bootstrap i uruchamianie szablonu:
		Bootstrap.main(paramReader, new String[] {
				"org.agiso.tempel.tests:TemplateDefinitionITest:templateDefinitionTest01",
				"-d " + outPath
		});

		// Weryfikacja wywołań poleceń odczytu paramtrów:
		InOrder inOrder = inOrder(paramReader);
		inOrder.verify(paramReader, times(1)).getParamValue("name", "Project name", null);
		verifyNoMoreInteractions(paramReader);

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "4bc922a21a32a7bdc89c1e8c715454d8".equals(md5) : md5;
	}

	/**
	 * src/test/templates/run/repo/
	 * org/agiso/tempel/tests/TemplateDefinitionITest/templateDefinitionTest02
	 */
	@Test
	public void templateDefinitionTest02() throws Exception {
		String outPath = getOutputPath(true);

		// Tworzenie i konfiguracja pozornej implementacji IParamReader'a:
		IParamReader paramReader = mock(IParamReader.class);
		when(paramReader.getParamValue(eq("name"), anyString(), anyString()))
			.thenReturn("value 1");

		// Wywołanie Bootstrap i uruchamianie szablonu:
		Bootstrap.main(paramReader, new String[] {
				"org.agiso.tempel.tests:TemplateDefinitionITest:templateDefinitionTest02",
				"-d " + outPath
		});

		// Weryfikacja wywołań poleceń odczytu paramtrów:
		InOrder inOrder = inOrder(paramReader);
		inOrder.verify(paramReader, times(1)).getParamValue("name", "Project name", null);
		verifyNoMoreInteractions(paramReader);

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "b8cbf54abdf135b7000bcff1c2e0a83d".equals(md5) : md5;
	}

	/**
	 * src/test/templates/run/repo/
	 * org/agiso/tempel/tests/TemplateDefinitionITest/templateDefinitionTest03
	 */
	@Test
	public void templateDefinitionTest03() throws Exception {
		String outPath = getOutputPath(true);

		// Tworzenie i konfiguracja pozornej implementacji IParamReader'a:
		IParamReader paramReader = mock(IParamReader.class);
		when(paramReader.getParamValue(eq("name"), anyString(), anyString()))
			.thenReturn("value 2");

		// Wywołanie Bootstrap i uruchamianie szablonu:
		Bootstrap.main(paramReader, new String[] {
				"org.agiso.tempel.tests:TemplateDefinitionITest:templateDefinitionTest03",
				"-d " + outPath
		});

		// Weryfikacja wywołań poleceń odczytu paramtrów:
		InOrder inOrder = inOrder(paramReader);
		inOrder.verify(paramReader, times(1)).getParamValue("name", "Project name", null);
		verifyNoMoreInteractions(paramReader);

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "06a1c46145a9170665c73a59d9d934b5".equals(md5) : md5;
	}

	/**
	 * src/test/templates/run/repo/
	 * org/agiso/tempel/tests/TemplateDefinitionITest/templateDefinitionTest04
	 */
	@Test
	public void templateDefinitionTest04() throws Exception {
		String outPath = getOutputPath(true);

		// Tworzenie i konfiguracja pozornej implementacji IParamReader'a:
		IParamReader paramReader = mock(IParamReader.class);
		when(paramReader.getParamValue(eq("name"), anyString(), anyString()))
			.thenReturn("value 3");

		// Wywołanie Bootstrap i uruchamianie szablonu:
		Bootstrap.main(paramReader, new String[] {
				"org.agiso.tempel.tests:TemplateDefinitionITest:templateDefinitionTest04",
				"-d " + outPath
		});

		// Weryfikacja wywołań poleceń odczytu paramtrów:
		InOrder inOrder = inOrder(paramReader);
		inOrder.verify(paramReader, times(1)).getParamValue("name", "Project name", null);
		verifyNoMoreInteractions(paramReader);

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "ec9b716697122eef04a8521d19802bc8".equals(md5) : md5;
	}

	/**
	 * src/test/templates/run/repo/
	 * org/agiso/tempel/tests/TemplateDefinitionITest/templateDefinitionTest05
	 */
	@Test
	public void templateDefinitionTest05() throws Exception {
		String outPath = getOutputPath(true);

		// Tworzenie i konfiguracja pozornej implementacji IParamReader'a:
		IParamReader paramReader = mock(IParamReader.class);
		when(paramReader.getParamValue(eq("name"), anyString(), anyString()))
			.thenReturn("value 4");

		// Wywołanie Bootstrap i uruchamianie szablonu:
		Bootstrap.main(paramReader, new String[] {
				"org.agiso.tempel.tests:TemplateDefinitionITest:templateDefinitionTest05",
				"-d " + outPath
		});

		// Weryfikacja wywołań poleceń odczytu paramtrów:
		InOrder inOrder = inOrder(paramReader);
		inOrder.verify(paramReader, times(1)).getParamValue("name", "Project name", null);
		verifyNoMoreInteractions(paramReader);

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "baf37424f517639cac4cac38defcf028".equals(md5) : md5;
	}

	/**
	 * src/test/templates/run/repo/
	 * org/agiso/tempel/tests/TemplateDefinitionITest/templateDefinitionTest06
	 */
	@Test
	public void templateDefinitionTest06() throws Exception {
		String outPath = getOutputPath(true);

		// Tworzenie i konfiguracja pozornej implementacji IParamReader'a:
		IParamReader paramReader = mock(IParamReader.class);
		when(paramReader.getParamValue(eq("name"), anyString(), anyString()))
			.thenReturn("value 5");

		// Wywołanie Bootstrap i uruchamianie szablonu:
		Bootstrap.main(paramReader, new String[] {
				"org.agiso.tempel.tests:TemplateDefinitionITest:templateDefinitionTest06",
				"-d " + outPath
		});

		// Weryfikacja wywołań poleceń odczytu paramtrów:
		InOrder inOrder = inOrder(paramReader);
		inOrder.verify(paramReader, times(1)).getParamValue("name", "Project name", null);
		verifyNoMoreInteractions(paramReader);

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "e37f056869b7e3351fefa1c5cf43c523".equals(md5) : md5;
	}

	/**
	 * src/test/templates/run/repo/
	 * org/agiso/tempel/tests/TemplateDefinitionITest/templateDefinitionTest07
	 */
	@Test
	public void templateDefinitionTest07() throws Exception {
		String outPath = getOutputPath(true);

		// Tworzenie i konfiguracja pozornej implementacji IParamReader'a:
		IParamReader paramReader = mock(IParamReader.class);
		when(paramReader.getParamValue(eq("name"), anyString(), anyString()))
			.thenReturn("value 6");

		// Wywołanie Bootstrap i uruchamianie szablonu:
		Bootstrap.main(paramReader, new String[] {
				"org.agiso.tempel.tests:TemplateDefinitionITest:templateDefinitionTest07",
				"-d " + outPath
		});

		// Weryfikacja wywołań poleceń odczytu paramtrów:
		InOrder inOrder = inOrder(paramReader);
		inOrder.verify(paramReader, times(1)).getParamValue("name", "Project name", null);
		verifyNoMoreInteractions(paramReader);

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "4cda7a3e71085716e3f53bff93cfcc29".equals(md5) : md5;
	}

	/**
	 * src/test/templates/run/repo/
	 * org/agiso/tempel/tests/TemplateDefinitionITest/templateDefinitionTest08
	 */
	@Test
	public void templateDefinitionTest08() throws Exception {
		String outPath = getOutputPath(true);

		// Tworzenie i konfiguracja pozornej implementacji IParamReader'a:
		IParamReader paramReader = mock(IParamReader.class);
		when(paramReader.getParamValue(eq("name"), anyString(), anyString()))
			.thenReturn("value 7");

		// Wywołanie Bootstrap i uruchamianie szablonu:
		Bootstrap.main(paramReader, new String[] {
				"org.agiso.tempel.tests:TemplateDefinitionITest:templateDefinitionTest08",
				"-d " + outPath
		});

		// Weryfikacja wywołań poleceń odczytu paramtrów:
		InOrder inOrder = inOrder(paramReader);
		inOrder.verify(paramReader, times(1)).getParamValue("name", "Project name", null);
		verifyNoMoreInteractions(paramReader);

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "18454eb8cf137bfe2f05fe4b20db3e49".equals(md5) : md5;
	}

	/**
	 * src/test/templates/run/repo/
	 * org/agiso/tempel/tests/TemplateDefinitionITest/templateDefinitionTest09
	 */
	@Test
	public void templateDefinitionTest09() throws Exception {
		String outPath = getOutputPath(true);

		// Tworzenie i konfiguracja pozornej implementacji IParamReader'a:
		IParamReader paramReader = mock(IParamReader.class);
		when(paramReader.getParamValue(eq("name"), anyString(), anyString()))
			.thenReturn("value 8");

		// Wywołanie Bootstrap i uruchamianie szablonu:
		Bootstrap.main(paramReader, new String[] {
				"org.agiso.tempel.tests:TemplateDefinitionITest:templateDefinitionTest09",
				"-d " + outPath
		});

		// Weryfikacja wywołań poleceń odczytu paramtrów:
		InOrder inOrder = inOrder(paramReader);
		inOrder.verify(paramReader, times(1)).getParamValue("name", "Project name", null);
		verifyNoMoreInteractions(paramReader);

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "eabcbb3f24682664c844ed3a8031a3f5".equals(md5) : md5;
	}
}
