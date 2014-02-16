/* org.agiso.tempel.engine.AbstractVelocityEngine (15-11-2012)
 * 
 * AbstractVelocityEngine.java
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
package org.agiso.tempel.engine;

import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import org.agiso.tempel.api.ITempelEngine;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 * 
 * 
 * @author Karol Kopacz
 * @since 1.0
 */
public abstract class AbstractVelocityEngine implements ITempelEngine {
	private VelocityEngine engine = new VelocityEngine();

//	--------------------------------------------------------------------------
	protected final String processVelocityString(String logTag, String inString, VelocityContext context) {
		Writer writer = new StringWriter();
		doVelocityTemplateMerge(logTag, inString, context, writer);
		return writer.toString();
	}

//	--------------------------------------------------------------------------
	protected VelocityContext createVelocityContext(Map<String, Object> params) {
		VelocityContext context = new VelocityContext();
		for(String key : params.keySet()) {
			context.put(key, params.get(key));
		}
		return context;
	}

	protected void doVelocityTemplateMerge(String logTag, String inString, VelocityContext context, Writer writer) {
		engine.evaluate(context, writer, logTag, inString);
	}

	protected void doVelocityTemplateMerge(String logTag, Reader reader, VelocityContext context, Writer writer) {
		engine.evaluate(context, writer, logTag, reader);
	}
}
