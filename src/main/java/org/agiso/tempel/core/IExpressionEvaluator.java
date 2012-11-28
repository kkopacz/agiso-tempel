/* org.agiso.tempel.core.IExpressionEvaluator (07-11-2012)
 * 
 * IExpressionEvaluator.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.core;

import java.util.Map;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public interface IExpressionEvaluator {

	/**
	 * @param expression
	 * @param model
	 * @return
	 */
	public String evaluate(String expression, Map<String, Object> model);
}
