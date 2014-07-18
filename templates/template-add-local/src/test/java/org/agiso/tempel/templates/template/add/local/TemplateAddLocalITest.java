/* org.agiso.tempel.templates.tempel.add.TemplateAddLocalITest (13-02-2013)
 * 
 * TemplateAddLocalITest.java
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
package org.agiso.tempel.templates.template.add.local;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.HashMap;

import org.agiso.core.lang.util.DigestUtils;
import org.agiso.tempel.ITempel;
import org.agiso.tempel.api.internal.IParamReader;
import org.agiso.tempel.support.test.AbstractTemplateTest;
import org.mockito.InOrder;
import org.testng.annotations.Test;

/**
 * 
 * 
 * @author Karol Kopacz
 * @since 1.0
 */
public class TemplateAddLocalITest extends AbstractTemplateTest {
	private static final String GROUP_ID    = "org.agiso.tempel.templates";
	private static final String TEMPLATE_ID = "template-add-local";
	private static final String VERSION     = "0.0.2.BUILD-SNAPSHOT";

//	--------------------------------------------------------------------------
	public TemplateAddLocalITest() {
		super(GROUP_ID, TEMPLATE_ID, VERSION);
	}

//	--------------------------------------------------------------------------
	@Test
	public void testTemplateAddLocal() throws Exception {
		String outPath = getOutputPath(true);

		// Tworzenie i konfiguracja pozornej implementacji IParamReader'a:
		IParamReader paramReader = mock(IParamReader.class);
		when(paramReader.getParamValue(eq("groupId"), anyString(), anyString()))
			.thenReturn("aaa.bbb.ccc");
		when(paramReader.getParamValue(eq("templateId"), anyString(), anyString()))
			.thenReturn("xxx.yyy.zzz");
		when(paramReader.getParamValue(eq("version"), anyString(), anyString()))
			.thenReturn("1.0.0");

		// Ustawienie implementacji IParamReader'a i wykonanie szablonu:
		ITempel tempel = getTempelInstance();
		tempel.setParamReader(paramReader);
		tempel.startTemplate(
				GROUP_ID + ":" + TEMPLATE_ID + ":" + VERSION,
				new HashMap<String, String>(), new File(outPath).getCanonicalPath()
		);

		// Weryfikacja wywołań poleceń odczytu paramtrów:
		InOrder inOrder = inOrder(paramReader);
		inOrder.verify(paramReader, times(1)).getParamValue("groupId", "Template groupId", "org.agiso.tempel.templates");
		inOrder.verify(paramReader, times(1)).getParamValue("templateId", "Template templateId", null);
		inOrder.verify(paramReader, times(1)).getParamValue("version", "Template version", "1.0.0");
		verifyNoMoreInteractions(paramReader);

		// Wyliczenie i sprawdzenie skrótu MD5 utworzonego zasobu:
		String md5 = DigestUtils.countDigest("MD5", new File(outPath));
		assert "f69e866f6c3c2939b8109085b7f0b77f".equals(md5) : md5;
	}
}
