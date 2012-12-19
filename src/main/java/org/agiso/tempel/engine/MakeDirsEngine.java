/* org.agiso.tempel.engine.MakeDirsEngine (02-10-2012)
 * 
 * MakeDirsEngine.java
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
public class MakeDirsEngine implements ITempelEngine {
	@Override
	public void run(File source, Map<String, Object> params, String target) {
		new File(target).mkdirs();
	}
}
