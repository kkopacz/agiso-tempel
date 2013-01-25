/* org.agiso.tempel.ITempel (25-01-2013)
 * 
 * ITempel.java
 * 
 * Copyright 2013 agiso.org
 */
package org.agiso.tempel;

import java.util.Map;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public interface ITempel {
	public void startTemplate(String name, Map<String, String> params, String workDir) throws Exception;
}
