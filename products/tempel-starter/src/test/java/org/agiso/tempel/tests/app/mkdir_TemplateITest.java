/* org.agiso.tempel.tests.app.mkdir_TemplateITest (18-01-2014)
 * 
 * mkdir_TemplateITest.java
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
package org.agiso.tempel.tests.app;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.File;

import org.agiso.core.lang.util.DigestUtils;
import org.agiso.core.test.AbstractOutputTest;
import org.agiso.tempel.api.internal.IParamReader;
import org.agiso.tempel.starter.Bootstrap;
import org.mockito.InOrder;
import org.testng.annotations.Test;

/**
 * Testy integracyjne szablonu <code>abstract-mkdir</code>.
 * </br>
 * Wykorzystują definicję szablonu z pliku <code>TEMPEL-INF/tempel.xml</code>
 * znajdującą się w katalogu <code>src/main/resources</code> i umieszczaną w
 * pliku <code>.jar</code> szablonu oraz dodatkowo definicje testowe z pliku
 * <code>src/test/templates/app/tempel.xml</code>.
 * 
 * @author Karol Kopacz
 * @since 1.0
 */
public class mkdir_TemplateITest extends AbstractOutputTest {
	protected String getOutputPathPrefix() {
		return "./target/velocity-output/";
	}

//	--------------------------------------------------------------------------
//	src/test/templates/app/tempel.xml
//	--------------------------------------------------------------------------
	/**
	 * Test bezpośredniego wywołania szablonu (polecenia) <code>mkdir</code>.
	 */
	@Test
	public void testTemplateInvocation() throws Exception {
		String outPath = getOutputPath(true);

		// Tworzenie i konfiguracja pozornej implementacji IParamReader'a:
		IParamReader paramReader = mock(IParamReader.class);
		when(paramReader.getParamValue(eq("name"), anyString(), anyString()))
			.thenReturn("newDirectory");

		// Wywołanie Bootstrap i uruchamianie szablonu:
		Bootstrap.main(paramReader, new String[] {"mkdir",
				"-d " + outPath
		});

		// Weryfikacja wywołań poleceń odczytu paramtrów:
		InOrder inOrder = inOrder(paramReader);
		inOrder.verify(paramReader, times(1)).getParamValue("name", "Directory name", null);
		verifyNoMoreInteractions(paramReader);

		String md5 = DigestUtils.countDigest("MD5", new File(outPath));
		assert "ff25e77df10a16b09de1c524300051ed".equals(md5) : md5;
	}
}
