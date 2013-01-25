/* org.agiso.tempel.core.VelocityExpressionEvaluator (07-11-2012)
 * 
 * VelocityExpressionEvaluator.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.core;

import java.io.StringWriter;
import java.util.Map;

import org.agiso.tempel.Temp;
import org.agiso.tempel.api.internal.IExpressionEvaluator;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.stereotype.Component;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
@Component
public class VelocityExpressionEvaluator implements IExpressionEvaluator {
	@Override
	public String evaluate(String expression, Map<String, Object> model) {
		if(Temp.StringUtils_isBlank(expression)) {
			return expression;
		}

		try {
			VelocityContext context = new VelocityContext();
			for(String key : model.keySet()) {
				context.put(key, model.get(key));
			}

			StringWriter writer = new StringWriter();
			Velocity.evaluate(context, writer, expression, expression);
			return writer.toString();
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
}
