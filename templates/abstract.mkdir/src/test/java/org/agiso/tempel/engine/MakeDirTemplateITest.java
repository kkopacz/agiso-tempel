/* org.agiso.tempel.engine.MakeDirTemplateITest (18-01-2014)
 * 
 * MakeDirTemplateITest.java
 * 
 * Copyright 2014 agiso.org
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
import org.agiso.tempel.test.AbstractTemplateTest;
import org.mockito.InOrder;
import org.testng.annotations.Test;

/**
 * Testy integracyjne szablonu <code>abstract.mkdir</code>.
 * </br>
 * Wykorzystują definicję szablonu z pliku <code>TEMPEL-INF/tempel.xml</code>
 * znajdującą się w katalogu <code>src/main/resources</code> i umieszczaną w
 * pliku <code>.jar</code> szablonu oraz dodatkowo definicje testowe z pliku
 * <code>src/test/templates/run/tempel.xml</code>.
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class MakeDirTemplateITest extends AbstractTemplateTest {
	private static final String GROUP_ID    = "org.agiso.tempel.templates";
	private static final String TEMPLATE_ID = "abstract.mkdir";
	private static final String VERSION     = "1.0.0";

//	--------------------------------------------------------------------------
	public MakeDirTemplateITest() {
		super(GROUP_ID, TEMPLATE_ID, VERSION);
	}

//	--------------------------------------------------------------------------
	/**
	 * Test bezpośredniego wywołania szablonu <code>abstract.mkdir</code>.
	 * </br>
	 * Jego wywołanie kończy się błędem, ponieważ szablon jest oznaczony jako
	 * abstrakcyjny.
	 * 
	 * FIXME: Poprawić po implementacji obsługi atrybutu 'abstract'
	 * 
	 * @throws Exception
	 */
	@Test(/* FIXME: expectedExceptions = ... */)
	public void testAbstractTemplateInvocation() throws Exception {
		String outPath = getOutputPath(true);

		// Tworzenie i konfiguracja pozornej implementacji IParamReader'a:
		IParamReader paramReader = mock(IParamReader.class);

		// Ustawienie implementacji IParamReader'a i wykonanie szablonu:
		ITempel tempel = createTempelInstance();
		tempel.setParamReader(paramReader);
		tempel.startTemplate(
				GROUP_ID + ":" + TEMPLATE_ID + ":" + VERSION,
				new HashMap<String, String>(), new File(outPath).getCanonicalPath()
		);

		// Weryfikacja wywołań poleceń odczytu paramtrów:
		verifyNoMoreInteractions(paramReader);

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "d41d8cd98f00b204e9800998ecf8427e".equals(md5) : md5;
	}

	/**
	 * Test wywołania szablonu z ręcznie podpiętym silnikiem {@link MakeDirEngine}
	 * z szablonu <code>testEngineInvocation_01</code> na podstawie jego STN.
	 */
	@Test
	public void testEngineInvocation_01() throws Exception {
		String outPath = getOutputPath(true);

		// Tworzenie i konfiguracja pozornej implementacji IParamReader'a:
		IParamReader paramReader = mock(IParamReader.class);
		when(paramReader.getParamValue(eq("name"), anyString(), anyString()))
			.thenReturn("newDirectory");

		// Ustawienie implementacji IParamReader'a i wykonanie szablonu:
		ITempel tempel = createTempelInstance();
		tempel.setParamReader(paramReader);
		tempel.startTemplate(
				"testEngineInvocation_01",
				new HashMap<String, String>(), new File(outPath).getCanonicalPath()
		);

		// Weryfikacja wywołań poleceń odczytu paramtrów:
		InOrder inOrder = inOrder(paramReader);
		inOrder.verify(paramReader, times(1)).getParamValue("name", "Directory name", null);
		verifyNoMoreInteractions(paramReader);

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "ff25e77df10a16b09de1c524300051ed".equals(md5) : md5;
	}

	/**
	 * Test wywołania szablonu z ręcznie podpiętym silnikiem {@link MakeDirEngine}
	 * z szablonu <code>testEngineInvocation_02</code> na podstawie jego FQTN.
	 */
	@Test
	public void testEngineInvocation_02() throws Exception {
		String outPath = getOutputPath(true);

		// Tworzenie i konfiguracja pozornej implementacji IParamReader'a:
		IParamReader paramReader = mock(IParamReader.class);
		when(paramReader.getParamValue(eq("name"), anyString(), anyString()))
			.thenReturn("newDirectory");

		// Ustawienie implementacji IParamReader'a i wykonanie szablonu:
		ITempel tempel = createTempelInstance();
		tempel.setParamReader(paramReader);
		tempel.startTemplate(
				"org.agiso.tempel.engine:MakeDirTemplateITest:testEngineInvocation_02",
				new HashMap<String, String>(), new File(outPath).getCanonicalPath()
		);

		// Weryfikacja wywołań poleceń odczytu paramtrów:
		InOrder inOrder = inOrder(paramReader);
		inOrder.verify(paramReader, times(1)).getParamValue("name", "Directory name", null);
		verifyNoMoreInteractions(paramReader);

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "ff25e77df10a16b09de1c524300051ed".equals(md5) : md5;
	}

	/**
	 * Test wywołania szablonu <code>abstract.mkdir</code> z szablonu testowego
	 * <code>testReferenceTemplateInvocation_01</code> na podstawie jego STN.
	 */
	@Test
	public void testReferenceTemplateInvocation_01() throws Exception {
		String outPath = getOutputPath(true);

		// Tworzenie i konfiguracja pozornej implementacji IParamReader'a:
		IParamReader paramReader = mock(IParamReader.class);
		when(paramReader.getParamValue(eq("name"), anyString(), anyString()))
			.thenReturn("newDirectory");

		// Ustawienie implementacji IParamReader'a i wykonanie szablonu:
		ITempel tempel = createTempelInstance();
		tempel.setParamReader(paramReader);
		tempel.startTemplate(
				"testReferenceTemplateInvocation_01",
				new HashMap<String, String>(), new File(outPath).getCanonicalPath()
		);

		// Weryfikacja wywołań poleceń odczytu paramtrów:
		InOrder inOrder = inOrder(paramReader);
		inOrder.verify(paramReader, times(1)).getParamValue("name", "Directory name", null);
		verifyNoMoreInteractions(paramReader);

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "ff25e77df10a16b09de1c524300051ed".equals(md5) : md5;
	}

	/**
	 * Test wywołania szablonu <code>abstract.mkdir</code> z szablonu testowego
	 * <code>testReferenceTemplateInvocation_02</code> na podstawie jego w
	 * pełni kwalifikowanej nazwy (FQTN).
	 */
	@Test
	public void testReferenceTemplateInvocation_02() throws Exception {
		String outPath = getOutputPath(true);

		// Tworzenie i konfiguracja pozornej implementacji IParamReader'a:
		IParamReader paramReader = mock(IParamReader.class);
		when(paramReader.getParamValue(eq("name"), anyString(), anyString()))
			.thenReturn("newDirectory");

		// Ustawienie implementacji IParamReader'a i wykonanie szablonu:
		ITempel tempel = createTempelInstance();
		tempel.setParamReader(paramReader);
		tempel.startTemplate(
				"org.agiso.tempel.engine:MakeDirTemplateITest:testReferenceTemplateInvocation_02",
				new HashMap<String, String>(), new File(outPath).getCanonicalPath()
		);

		// Weryfikacja wywołań poleceń odczytu paramtrów:
		InOrder inOrder = inOrder(paramReader);
		inOrder.verify(paramReader, times(1)).getParamValue("name", "Directory name", null);
		verifyNoMoreInteractions(paramReader);

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "ff25e77df10a16b09de1c524300051ed".equals(md5) : md5;
	}
}
