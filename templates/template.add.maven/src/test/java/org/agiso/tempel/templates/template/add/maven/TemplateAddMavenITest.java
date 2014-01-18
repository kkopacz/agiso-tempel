/* org.agiso.tempel.templates.tempel.add.TemplateAddMavenITest (13-02-2013)
 * 
 * TemplateAddMavenITest.java
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
package org.agiso.tempel.templates.template.add.maven;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.agiso.tempel.ITempel;
import org.agiso.tempel.Temp;
import org.agiso.tempel.api.internal.IParamReader;
import org.agiso.tempel.test.AbstractTemplateTest;
import org.mockito.InOrder;
import org.testng.annotations.Test;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class TemplateAddMavenITest extends AbstractTemplateTest {
	private static final String GROUP_ID    = "org.agiso.tempel.templates";
	private static final String TEMPLATE_ID = "template.add.maven";
	private static final String VERSION     = "1.0.0";

//	--------------------------------------------------------------------------
	public TemplateAddMavenITest() {
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
		Map<String, String> cmdParams = new HashMap<String, String>();
		cmdParams.put("date_format", "yyyy-MM-dd HH:mm:ss");
		cmdParams.put("date", "2010-12-21 11:12:13");

		ITempel tempel = getTempelInstance();
		tempel.setParamReader(paramReader);
		tempel.startTemplate(
				GROUP_ID + ":" + TEMPLATE_ID + ":" + VERSION,
				cmdParams, new File(outPath).getCanonicalPath()
		);

		// Weryfikacja wywołań poleceń odczytu paramtrów:
		InOrder inOrder = inOrder(paramReader);
		inOrder.verify(paramReader, times(1)).getParamValue("groupId", "Template groupId", "org.agiso.tempel.templates");
		inOrder.verify(paramReader, times(1)).getParamValue("templateId", "Template templateId", null);
		inOrder.verify(paramReader, times(1)).getParamValue("version", "Template version", "1.0.0");
		verifyNoMoreInteractions(paramReader);

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "829d6fe8dd17be3342fe92c8ed8d7c29".equals(md5) : md5;
	}
}
