/* org.agiso.tempel.engine.EclipseProjectJavaTemplateITest (18-01-2014)
 * 
 * EclipseProjectJavaTemplateITest.java
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
import org.agiso.tempel.support.test.AbstractTemplateTest;
import org.mockito.InOrder;
import org.testng.annotations.Test;

/**
 * Testy integracyjne szablonu <code>eclipse.project.java</code>.
 * </br>
 * Wykorzystują definicję szablonu z pliku <code>TEMPEL-INF/tempel.xml</code>
 * znajdującą się w katalogu <code>src/main/resources</code> i umieszczaną w
 * pliku <code>.jar</code> szablonu oraz dodatkowo definicje testowe z pliku
 * <code>src/test/templates/run/tempel.xml</code>.
 * 
 * @author Karol Kopacz
 * @since 1.0
 */
public class EclipseProjectJavaTemplateITest extends AbstractTemplateTest {
	private static final String GROUP_ID    = "org.agiso.tempel.templates";
	private static final String TEMPLATE_ID = "eclipse-project-java";
	private static final String VERSION     = "1.0.0";

//	--------------------------------------------------------------------------
	public EclipseProjectJavaTemplateITest() {
		super(GROUP_ID, TEMPLATE_ID, VERSION);
	}

//	--------------------------------------------------------------------------
	/**
	 * Test bezpośredniego wywołania szablonu <code>eclipse.project.java</code>.
	 */
	@Test
	public void testTemplateInvocation() throws Exception {
		String outPath = getOutputPath(true);

		// Tworzenie i konfiguracja pozornej implementacji IParamReader'a:
		IParamReader paramReader = mock(IParamReader.class);
		when(paramReader.getParamValue(eq("name"), anyString(), anyString()))
			.thenReturn("TestProject");

		// Ustawienie implementacji IParamReader'a i wykonanie szablonu:
		ITempel tempel = createTempelInstance();
		tempel.setParamReader(paramReader);
		tempel.startTemplate(
				GROUP_ID + ":" + TEMPLATE_ID + ":" + VERSION,
				new HashMap<String, String>(), new File(outPath).getCanonicalPath()
		);

		// Weryfikacja wywołań poleceń odczytu paramtrów:
		InOrder inOrder = inOrder(paramReader);
		inOrder.verify(paramReader, times(1)).getParamValue("name", "Project name", null);
		verifyNoMoreInteractions(paramReader);

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "05b4c797692b88d8c6b295a91644bc5d".equals(md5) : md5;
	}

	/**
	 * Test wywołania szablonu <code>eclipse.project.java</code> z szablonu
	 * testowego <code>testReferenceTemplateInvocation_01</code> na podstawie
	 * jego STN.
	 */
	@Test
	public void testReferenceTemplateInvocation_01() throws Exception {
		String outPath = getOutputPath(true);

		// Tworzenie i konfiguracja pozornej implementacji IParamReader'a:
		IParamReader paramReader = mock(IParamReader.class);
		when(paramReader.getParamValue(eq("name"), anyString(), anyString()))
			.thenReturn("TestProject");

		// Ustawienie implementacji IParamReader'a i wykonanie szablonu:
		ITempel tempel = createTempelInstance();
		tempel.setParamReader(paramReader);
		tempel.startTemplate(
				"testReferenceTemplateInvocation_01",
				new HashMap<String, String>(), new File(outPath).getCanonicalPath()
		);

		// Weryfikacja wywołań poleceń odczytu paramtrów:
		InOrder inOrder = inOrder(paramReader);
		inOrder.verify(paramReader, times(1)).getParamValue("name", "Project name", null);
		verifyNoMoreInteractions(paramReader);

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "05b4c797692b88d8c6b295a91644bc5d".equals(md5) : md5;
	}

	/**
	 * Test wywołania szablonu <code>eclipse.project.java</code> z szablonu
	 * testowego <code>testReferenceTemplateInvocation_02</code> na podstawie
	 * jego STN.
	 */
	@Test
	public void testReferenceTemplateInvocation_02() throws Exception {
		String outPath = getOutputPath(true);

		// Tworzenie i konfiguracja pozornej implementacji IParamReader'a:
		IParamReader paramReader = mock(IParamReader.class);
		when(paramReader.getParamValue(eq("name"), anyString(), anyString()))
			.thenReturn("TestProject");

		// Ustawienie implementacji IParamReader'a i wykonanie szablonu:
		ITempel tempel = createTempelInstance();
		tempel.setParamReader(paramReader);
		tempel.startTemplate(
				"testReferenceTemplateInvocation_02",
				new HashMap<String, String>(), new File(outPath).getCanonicalPath()
		);

		// Weryfikacja wywołań poleceń odczytu paramtrów:
		InOrder inOrder = inOrder(paramReader);
		inOrder.verify(paramReader, times(1)).getParamValue("name", "Project name", null);
		verifyNoMoreInteractions(paramReader);

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "32a721621fc702a225a345e5a722160a".equals(md5) : md5;
	}
}
