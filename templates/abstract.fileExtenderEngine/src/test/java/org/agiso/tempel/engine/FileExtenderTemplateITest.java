/* org.agiso.tempel.engine.FileExtenderTemplateITest (26-01-2014)
 * 
 * FileExtenderTemplateITest.java
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
import org.agiso.tempel.core.model.exceptions.AbstractTemplateException;
import org.agiso.tempel.test.AbstractTemplateTest;
import org.mockito.InOrder;
import org.testng.annotations.Test;

/**
 * Testy integracyjne szablonu <code>abstract.fileExtenderEngine</code>.
 * </br>
 * Wykorzystują definicję szablonu z pliku <code>TEMPEL-INF/tempel.xml</code>
 * znajdującą się w katalogu <code>src/main/resources</code> i umieszczaną w
 * pliku <code>.jar</code> szablonu oraz dodatkowo definicje testowe z pliku
 * <code>src/test/templates/run/tempel.xml</code>.
 * 
 * @author Karol Kopacz
 * @since 1.0
 */
public class FileExtenderTemplateITest extends AbstractTemplateTest {
	private static final String GROUP_ID    = "org.agiso.tempel.templates";
	private static final String TEMPLATE_ID = "abstract.fileExtenderEngine";
	private static final String VERSION     = "1.0.0";

//	--------------------------------------------------------------------------
	public FileExtenderTemplateITest() {
		super(GROUP_ID, TEMPLATE_ID, VERSION);
	}

//	--------------------------------------------------------------------------
	/**
	 * Test bezpośredniego wywołania <code>abstract.fileExtenderEngine</code>.
	 * </br>
	 * Jego wywołanie kończy się błędem, ponieważ szablon jest oznaczony jako
	 * abstrakcyjny.
	 */
	@Test(expectedExceptions = AbstractTemplateException.class)
	public void testAbstractTemplateInvocation() throws Exception {
		String outPath = getOutputPath(false);

		// Próba wykonania szablonu abstrakcyjnego 'abstract.fileExtenderEngine':
		ITempel tempel = createTempelInstance();
		tempel.startTemplate(
				GROUP_ID + ":" + TEMPLATE_ID + ":" + VERSION,
				new HashMap<String, String>(), new File(outPath).getCanonicalPath()
		);
	}
}
