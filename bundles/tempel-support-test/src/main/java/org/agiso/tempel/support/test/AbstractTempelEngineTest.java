/* org.agiso.tempel.support.test.AbstractEngineTest (16-11-2012)
 * 
 * AbstractTempelEngineTest.java
 * 
 * Copyright 2012 agiso.org
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
package org.agiso.tempel.support.test;

import org.agiso.core.test.AbstractRepositoryTest;
import org.agiso.tempel.api.ITempelEngine;
import org.agiso.tempel.support.test.annotation.TempelEngineTest;
import org.testng.annotations.BeforeClass;

/**
 * Klasa bazowa dla klas testujących silniki generacji szablonów. Odpowiada
 * za instancjonowanie silnika i przygotowanie katalogu docelowego dla zasobów
 * generowanych przez silnik.
 * 
 * @author Karol Kopacz
 * @since 1.0
 */
public abstract class AbstractTempelEngineTest extends AbstractRepositoryTest {
	protected ITempelEngine engine;

// --------------------------------------------------------------------------
	@BeforeClass
	public void prepareTemplerEngine() throws Exception {
		// Tworzenie i inicjalizacja silnika:
		engine = createTemplerEngineInstance();
	}

// --------------------------------------------------------------------------
	protected ITempelEngine createTemplerEngineInstance() throws Exception {
		TempelEngineTest tet = this.getClass().getAnnotation(TempelEngineTest.class);
		if(tet == null) {
			throw new RuntimeException("Test class not annotated with @TempelEngineTest");
		}

		return tet.value().newInstance();
	}
}
