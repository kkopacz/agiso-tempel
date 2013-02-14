/* org.agiso.tempel.templates.test.VelocityDirectoryTemplateITest (20-01-2013)
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
package org.agiso.tempel.templates.test;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.HashMap;

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
public class VelocityDirectoryTemplateITest extends AbstractTemplateTest {
	private static final String GROUP_ID    = "org.agiso.tempel.templates";
	private static final String TEMPLATE_ID = "test.velocityDirectoryTemplate";
	private static final String VERSION     = "2.0.0";

//	--------------------------------------------------------------------------
	public VelocityDirectoryTemplateITest() {
		super(GROUP_ID, TEMPLATE_ID, VERSION);
	}

//	--------------------------------------------------------------------------
	@Test
	public void testVelocityDirectoryTemplate_2_0_0() throws Exception {
		String outPath = getOutputPath(true);

		// Tworzenie i konfiguracja pozornej implementacji IParamReader'a:
		IParamReader paramReader = mock(IParamReader.class);
		when(paramReader.getParamValue(eq("name"), anyString(), anyString()))
			.thenReturn("value");

		// Ustawienie implementacji IParamReader'a i wykonanie szablonu:
		tempel.setParamReader(paramReader);
		tempel.startTemplate(
				GROUP_ID + ":" + TEMPLATE_ID + ":" + VERSION,
				new HashMap<String, String>(), new File(outPath).getCanonicalPath()
		);

		// Weryfikacja wywołań poleceń odczytu paramtrów:
		InOrder inOrder = inOrder(paramReader);
		inOrder.verify(paramReader, times(1)).getParamValue("name", null, null);
		verifyNoMoreInteractions(paramReader);

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "8fe87b4e14b1b6ad844d2003eef134bc".equals(md5);
	}
}
