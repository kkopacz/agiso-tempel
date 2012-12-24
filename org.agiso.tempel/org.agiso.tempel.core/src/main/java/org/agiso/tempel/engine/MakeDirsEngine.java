/* org.agiso.tempel.engine.MakeDirsEngine (02-10-2012)
 * 
 * MakeDirsEngine.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.engine;

import java.io.File;
import java.util.Map;

import org.agiso.tempel.api.ITempelEngine;
import org.agiso.tempel.api.internal.ITemplateSource;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class MakeDirsEngine implements ITempelEngine {
	@Override
	public void run(ITemplateSource source, Map<String, Object> params, String target) {
		new File(target).mkdirs();
	}
}
