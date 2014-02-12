/* org.agiso.tempel.core.VelocityExpressionEvaluator (07-11-2012)
 * 
 * VelocityExpressionEvaluator.java
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
package org.agiso.tempel.core;

import java.io.StringWriter;
import java.util.Map;

import org.agiso.core.lang.util.StringUtils;
import org.agiso.tempel.api.internal.IExpressionEvaluator;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.stereotype.Component;

/**
 * 
 * 
 * @author Karol Kopacz
 * @since 1.0
 */
@Component
public class VelocityExpressionEvaluator implements IExpressionEvaluator {
	@Override
	public String evaluate(String expression, Map<String, Object> model) {
		if(StringUtils.isBlank(expression)) {
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
