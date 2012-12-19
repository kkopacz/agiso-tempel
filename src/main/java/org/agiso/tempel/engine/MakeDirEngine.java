/* org.agiso.tempel.engine.MakeDirEngine (15-09-2012)
 * 
 * MakeDirEngine.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.engine;

import java.io.File;
import java.util.Map;

import org.agiso.tempel.core.engine.ITempelEngine;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class MakeDirEngine implements ITempelEngine {
	@Override
	public void run(File source, Map<String, Object> params, String target) {
		new File(target).mkdir();
	}
}
