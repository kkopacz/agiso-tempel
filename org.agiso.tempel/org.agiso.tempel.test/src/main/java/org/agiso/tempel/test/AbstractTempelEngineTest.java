/* org.agiso.tempel.engine.AbstractEngineTest (16-11-2012)
 * 
 * AbstractTempelEngineTest.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.test;

import org.agiso.tempel.api.ITempelEngine;
import org.agiso.tempel.test.annotation.TempelEngineTest;
import org.testng.annotations.BeforeClass;

/**
 * Klasa bazowa dla klas testujących silniki generacji szablonów. Odpowiada
 * za instancjonowanie silnika i przygotowanie katalogu docelowego dla zasobów
 * generowanych przez silnik.
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
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
