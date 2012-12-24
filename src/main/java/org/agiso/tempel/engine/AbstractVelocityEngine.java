/* org.agiso.tempel.engine.AbstractVelocityEngine (15-11-2012)
 * 
 * AbstractVelocityEngine.java
 * 
 * Copyright 2012 agiso.org
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
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
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
