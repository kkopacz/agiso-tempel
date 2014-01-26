/* org.agiso.tempel.engine.VelocityDirectoryTemplateITest (20-01-2013)
 * 
 * VelocityDirectoryTemplateITest.java
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
package org.agiso.tempel.engine;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.HashMap;

import org.agiso.tempel.ITempel;
import org.agiso.tempel.Temp;
import org.agiso.tempel.api.internal.IParamReader;
import org.agiso.tempel.core.model.exceptions.AbstractTemplateException;
import org.agiso.tempel.test.AbstractTemplateTest;
import org.mockito.InOrder;
import org.testng.annotations.Test;

/**
 * Testy integracyjne szablonu <code>abstract.velocityDirectoryEngine</code>.
 * </br>
 * Wykorzystują definicję szablonu z pliku <code>TEMPEL-INF/tempel.xml</code>
 * znajdującą się w katalogu <code>src/main/resources</code> i umieszczaną w
 * pliku <code>.jar</code> szablonu oraz dodatkowo definicje testowe z pliku
 * <code>src/test/templates/run/tempel.xml</code>.
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class VelocityDirectoryTemplateITest extends AbstractTemplateTest {
	private static final String GROUP_ID    = "org.agiso.tempel.templates";
	private static final String TEMPLATE_ID = "abstract.velocityDirectoryEngine";
	private static final String VERSION     = "1.0.0";

//	--------------------------------------------------------------------------
	public VelocityDirectoryTemplateITest() {
		super(GROUP_ID, TEMPLATE_ID, VERSION);
	}

//	--------------------------------------------------------------------------
	/**
	 * Test bezpośredniego wywołania <code>abstract.velocityDirectoryEngine</code>.
	 * </br>
	 * Jego wywołanie kończy się błędem, ponieważ szablon jest oznaczony jako
	 * abstrakcyjny.
	 */
	@Test(expectedExceptions = AbstractTemplateException.class)
	public void testAbstractTemplateInvocation() throws Exception {
		String outPath = getOutputPath(false);

		// Próba wykonania szablonu abstrakcyjnego 'abstract.velocityDirectoryEngine':
		ITempel tempel = createTempelInstance();
		tempel.startTemplate(
				GROUP_ID + ":" + TEMPLATE_ID + ":" + VERSION,
				new HashMap<String, String>(), new File(outPath).getCanonicalPath()
		);
	}

	/**
	 * Test minimalistycznej definicji szablonu {@link VelocityDirectoryEngine}.
	 */
	@Test
	public void testEngineInvocation_01() throws Exception {
		String outPath = getOutputPath(true);

		// Tworzenie i konfiguracja pozornej implementacji IParamReader'a:
		IParamReader paramReader = mock(IParamReader.class);
		when(paramReader.getParamValue(eq("name"), anyString(), anyString()))
			.thenReturn("value");

		// Ustawienie implementacji IParamReader'a i wykonanie szablonu:
		ITempel tempel = createTempelInstance();
		tempel.setParamReader(paramReader);
		tempel.startTemplate(
				"org.agiso.tempel.engine:VelocityDirectoryTemplateITest:testEngineInvocation_01",
				new HashMap<String, String>(), new File(outPath).getCanonicalPath()
		);

		// Weryfikacja wywołań poleceń odczytu paramtrów:
		InOrder inOrder = inOrder(paramReader);
		inOrder.verify(paramReader, times(1)).getParamValue("name", null, null);
		verifyNoMoreInteractions(paramReader);

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "8fe87b4e14b1b6ad844d2003eef134bc".equals(md5) : md5;
	}

	@Test
	public void testEngineInvocation_02() throws Exception {
		String outPath = getOutputPath(true);

		// Tworzenie i konfiguracja pozornej implementacji IParamReader'a:
		IParamReader paramReader = mock(IParamReader.class);
		when(paramReader.getParamValue(eq("name"), anyString(), anyString()))
			.thenReturn("value");

		// Ustawienie implementacji IParamReader'a i wykonanie szablonu:
		ITempel tempel = createTempelInstance();
		tempel.setParamReader(paramReader);
		tempel.startTemplate(
				"org.agiso.tempel.engine:VelocityDirectoryTemplateITest:testEngineInvocation_02",
				new HashMap<String, String>(), new File(outPath).getCanonicalPath()
		);

		// Weryfikacja wywołań poleceń odczytu paramtrów:
		InOrder inOrder = inOrder(paramReader);
		inOrder.verify(paramReader, times(1)).getParamValue("name", null, null);
		verifyNoMoreInteractions(paramReader);

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "8fe87b4e14b1b6ad844d2003eef134bc".equals(md5) : md5;
	}

	@Test
	public void testEngineInvocation_03() throws Exception {
		String outPath = getOutputPath(true);

		// Tworzenie i konfiguracja pozornej implementacji IParamReader'a:
		IParamReader paramReader = mock(IParamReader.class);
		when(paramReader.getParamValue(eq("name"), anyString(), anyString()))
			.thenReturn("value");

		// Ustawienie implementacji IParamReader'a i wykonanie szablonu:
		ITempel tempel = createTempelInstance();
		tempel.setParamReader(paramReader);
		tempel.startTemplate(
				"org.agiso.tempel.engine:VelocityDirectoryTemplateITest:testEngineInvocation_03",
				new HashMap<String, String>(), new File(outPath).getCanonicalPath()
		);

		// Weryfikacja wywołań poleceń odczytu paramtrów:
		InOrder inOrder = inOrder(paramReader);
		inOrder.verify(paramReader, times(1)).getParamValue("name", null, null);
		verifyNoMoreInteractions(paramReader);

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "554cd5d1a8d91902a6315f11f1f98887".equals(md5) : md5;
	}

	@Test
	public void testEngineInvocation_04() throws Exception {
		String outPath = getOutputPath(true);

		// Tworzenie i konfiguracja pozornej implementacji IParamReader'a:
		IParamReader paramReader = mock(IParamReader.class);
		when(paramReader.getParamValue(eq("name"), anyString(), anyString()))
			.thenReturn("value");

		// Ustawienie implementacji IParamReader'a i wykonanie szablonu:
		ITempel tempel = createTempelInstance();
		tempel.setParamReader(paramReader);
		tempel.startTemplate(
				"org.agiso.tempel.engine:VelocityDirectoryTemplateITest:testEngineInvocation_04",
				new HashMap<String, String>(), new File(outPath).getCanonicalPath()
		);

		// Weryfikacja wywołań poleceń odczytu paramtrów:
		InOrder inOrder = inOrder(paramReader);
		inOrder.verify(paramReader, times(1)).getParamValue("name", null, null);
		verifyNoMoreInteractions(paramReader);

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "554cd5d1a8d91902a6315f11f1f98887".equals(md5) : md5;
	}

	@Test
	public void testReferenceTemplateInvocation_01() throws Exception {
		String outPath = getOutputPath(true);

		// Tworzenie i konfiguracja pozornej implementacji IParamReader'a:
		IParamReader paramReader = mock(IParamReader.class);
		when(paramReader.getParamValue(eq("name"), anyString(), anyString()))
			.thenReturn("value");

		// Ustawienie implementacji IParamReader'a i wykonanie szablonu:
		ITempel tempel = createTempelInstance();
		tempel.setParamReader(paramReader);
		tempel.startTemplate(
				"org.agiso.tempel.engine:VelocityDirectoryTemplateITest:testReferenceTemplateInvocation_01",
				new HashMap<String, String>(), new File(outPath).getCanonicalPath()
		);

		// Weryfikacja wywołań poleceń odczytu paramtrów:
		InOrder inOrder = inOrder(paramReader);
		inOrder.verify(paramReader, times(1)).getParamValue("name", null, null);
		verifyNoMoreInteractions(paramReader);

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "8fe87b4e14b1b6ad844d2003eef134bc".equals(md5) : md5;
	}

	@Test
	public void testReferenceTemplateInvocation_02() throws Exception {
		String outPath = getOutputPath(true);

		// Tworzenie i konfiguracja pozornej implementacji IParamReader'a:
		IParamReader paramReader = mock(IParamReader.class);
		when(paramReader.getParamValue(eq("name"), anyString(), anyString()))
			.thenReturn("value");

		// Ustawienie implementacji IParamReader'a i wykonanie szablonu:
		ITempel tempel = createTempelInstance();
		tempel.setParamReader(paramReader);
		tempel.startTemplate(
				"org.agiso.tempel.engine:VelocityDirectoryTemplateITest:testReferenceTemplateInvocation_02",
				new HashMap<String, String>(), new File(outPath).getCanonicalPath()
		);

		// Weryfikacja wywołań poleceń odczytu paramtrów:
		InOrder inOrder = inOrder(paramReader);
		inOrder.verify(paramReader, times(1)).getParamValue("name", null, null);
		verifyNoMoreInteractions(paramReader);

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "554cd5d1a8d91902a6315f11f1f98887".equals(md5) : md5;
	}

	@Test
	public void testReferenceTemplateInvocation_03() throws Exception {
		String outPath = getOutputPath(true);

		// Tworzenie i konfiguracja pozornej implementacji IParamReader'a:
		IParamReader paramReader = mock(IParamReader.class);
		when(paramReader.getParamValue(eq("name"), anyString(), anyString()))
			.thenReturn("value");

		// Ustawienie implementacji IParamReader'a i wykonanie szablonu:
		ITempel tempel = createTempelInstance();
		tempel.setParamReader(paramReader);
		tempel.startTemplate(
				"org.agiso.tempel.engine:VelocityDirectoryTemplateITest:testReferenceTemplateInvocation_03",
				new HashMap<String, String>(), new File(outPath).getCanonicalPath()
		);

		// Weryfikacja wywołań poleceń odczytu paramtrów:
		InOrder inOrder = inOrder(paramReader);
		inOrder.verify(paramReader, times(1)).getParamValue("name", null, null);
		verifyNoMoreInteractions(paramReader);

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "554cd5d1a8d91902a6315f11f1f98887".equals(md5) : md5;
	}

	@Test
	public void testReferenceTemplateInvocation_04() throws Exception {
		String outPath = getOutputPath(true);

		// Tworzenie i konfiguracja pozornej implementacji IParamReader'a:
		IParamReader paramReader = mock(IParamReader.class);
		when(paramReader.getParamValue(eq("name"), anyString(), anyString()))
			.thenReturn("value");

		// Ustawienie implementacji IParamReader'a i wykonanie szablonu:
		ITempel tempel = createTempelInstance();
		tempel.setParamReader(paramReader);
		tempel.startTemplate(
				"org.agiso.tempel.engine:VelocityDirectoryTemplateITest:testReferenceTemplateInvocation_04",
				new HashMap<String, String>(), new File(outPath).getCanonicalPath()
		);

		// Weryfikacja wywołań poleceń odczytu paramtrów:
		InOrder inOrder = inOrder(paramReader);
		inOrder.verify(paramReader, times(1)).getParamValue("name", null, null);
		verifyNoMoreInteractions(paramReader);

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "554cd5d1a8d91902a6315f11f1f98887".equals(md5) : md5;
	}
}
