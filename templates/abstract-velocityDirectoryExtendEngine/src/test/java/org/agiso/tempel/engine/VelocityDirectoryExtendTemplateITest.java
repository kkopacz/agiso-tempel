/* org.agiso.tempel.engine.VelocityDirectoryExtendTemplateITest (18-01-2014)
 * 
 * VelocityDirectoryExtendTemplateITest.java
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

import org.agiso.core.lang.util.DigestUtils;
import org.agiso.tempel.ITempel;
import org.agiso.tempel.api.internal.IParamReader;
import org.agiso.tempel.core.model.exceptions.AbstractTemplateException;
import org.agiso.tempel.support.test.AbstractTemplateTest;
import org.mockito.InOrder;
import org.testng.annotations.Test;

/**
 * Testy integracyjne szablonu <code>abstract.velocityDirectoryExtendEngine</code>.
 * </br>
 * Wykorzystują definicję szablonu z pliku <code>TEMPEL-INF/tempel.xml</code>
 * znajdującą się w katalogu <code>src/main/resources</code> i umieszczaną w
 * pliku <code>.jar</code> szablonu oraz dodatkowo definicje testowe z pliku
 * <code>src/test/templates/run/tempel.xml</code>.
 * 
 * @author Karol Kopacz
 * @since 1.0
 */
public class VelocityDirectoryExtendTemplateITest extends AbstractTemplateTest {
	private static final String GROUP_ID    = "org.agiso.tempel.templates";
	private static final String TEMPLATE_ID = "abstract-velocityDirectoryExtendEngine";
	private static final String VERSION     = "0.0.2.BUILD-SNAPSHOT";

//	--------------------------------------------------------------------------
	public VelocityDirectoryExtendTemplateITest() {
		super(GROUP_ID, TEMPLATE_ID, VERSION);
	}

//	--------------------------------------------------------------------------
	/**
	 * Test bezpośredniego wywołania <code>abstract.velocityDirectoryExtendEngine</code>.
	 * </br>
	 * Jego wywołanie kończy się błędem, ponieważ szablon jest oznaczony jako
	 * abstrakcyjny.
	 */
	@Test(expectedExceptions = AbstractTemplateException.class)
	public void testAbstractTemplateInvocation() throws Exception {
		String outPath = getOutputPath(false);

		// Próba wykonania szablonu abstrakcyjnego 'abstract.velocityDirectoryExtendEngine':
		ITempel tempel = createTempelInstance();
		tempel.startTemplate(
				GROUP_ID + ":" + TEMPLATE_ID + ":" + VERSION,
				new HashMap<String, String>(), new File(outPath).getCanonicalPath()
		);
	}

	@Test
	public void testEngineInvocation_01() throws Exception {
		String outPath = getOutputPath(true,
				"src/test/templates/tst/base/VelocityDirectoryExtendTemplateITest"
		);

		// Tworzenie i konfiguracja pozornej implementacji IParamReader'a:
		IParamReader paramReader = mock(IParamReader.class);
		when(paramReader.getParamValue(eq("key1"), anyString(), anyString()))
			.thenReturn("value1");
		when(paramReader.getParamValue(eq("key2"), anyString(), anyString()))
			.thenReturn("value2");
		when(paramReader.getParamValue(eq("fileKey2"), anyString(), anyString()))
			.thenReturn("file2");
		when(paramReader.getParamValue(eq("dirKey1"), anyString(), anyString()))
			.thenReturn("dir1");
		when(paramReader.getParamValue(eq("dirKey2"), anyString(), anyString()))
			.thenReturn("ir2");

		// Ustawienie implementacji IParamReader'a i wykonanie szablonu:
		ITempel tempel = createTempelInstance();
		tempel.setParamReader(paramReader);
		tempel.startTemplate(
				"org.agiso.tempel.engine:VelocityDirectoryExtendTemplateITest:testEngineInvocation_01",
				new HashMap<String, String>(), new File(outPath).getCanonicalPath()
		);

		// Weryfikacja wywołań poleceń odczytu paramtrów:
		InOrder inOrder = inOrder(paramReader);
		inOrder.verify(paramReader, times(1)).getParamValue("key1", null, null);
		inOrder.verify(paramReader, times(1)).getParamValue("key2", null, null);
		inOrder.verify(paramReader, times(1)).getParamValue("fileKey2", null, null);
		inOrder.verify(paramReader, times(1)).getParamValue("dirKey1", null, null);
		inOrder.verify(paramReader, times(1)).getParamValue("dirKey2", null, null);
		verifyNoMoreInteractions(paramReader);

		String md5 = DigestUtils.countDigest("MD5", new File(outPath));
		assert "8d76301a49fbf4cd38a32bf4059875ad".equals(md5) : md5;
	}

	@Test
	public void testReferenceTemplateInvocation_01() throws Exception {
		String outPath = getOutputPath(true,
				"src/test/templates/tst/base/VelocityDirectoryExtendTemplateITest"
		);

		// Tworzenie i konfiguracja pozornej implementacji IParamReader'a:
		IParamReader paramReader = mock(IParamReader.class);
		when(paramReader.getParamValue(eq("key1"), anyString(), anyString()))
			.thenReturn("value1");
		when(paramReader.getParamValue(eq("key2"), anyString(), anyString()))
			.thenReturn("value2");
		when(paramReader.getParamValue(eq("fileKey2"), anyString(), anyString()))
			.thenReturn("file2");
		when(paramReader.getParamValue(eq("dirKey1"), anyString(), anyString()))
			.thenReturn("dir1");
		when(paramReader.getParamValue(eq("dirKey2"), anyString(), anyString()))
			.thenReturn("ir2");

		// Ustawienie implementacji IParamReader'a i wykonanie szablonu:
		ITempel tempel = createTempelInstance();
		tempel.setParamReader(paramReader);
		tempel.startTemplate(
				"org.agiso.tempel.engine:VelocityDirectoryExtendTemplateITest:testReferenceTemplateInvocation_01",
				new HashMap<String, String>(), new File(outPath).getCanonicalPath()
		);

		// Weryfikacja wywołań poleceń odczytu paramtrów:
		InOrder inOrder = inOrder(paramReader);
		inOrder.verify(paramReader, times(1)).getParamValue("key1", null, null);
		inOrder.verify(paramReader, times(1)).getParamValue("key2", null, null);
		inOrder.verify(paramReader, times(1)).getParamValue("fileKey2", null, null);
		inOrder.verify(paramReader, times(1)).getParamValue("dirKey1", null, null);
		inOrder.verify(paramReader, times(1)).getParamValue("dirKey2", null, null);
		verifyNoMoreInteractions(paramReader);

		String md5 = DigestUtils.countDigest("MD5", new File(outPath));
		assert "8d76301a49fbf4cd38a32bf4059875ad".equals(md5) : md5;
	}
}
