/* org.agiso.tempel.engine.AbstractVelocityEngine (15-11-2012)
 * 
 * AbstractVelocityEngine.java
 * 
 * Copyright 2012 PPW 'ARAJ' Sp. z o. o.
 */
package org.agiso.tempel.engine;

import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import org.agiso.tempel.core.model.Template.Scope;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@araj.pl">Karol Kopacz</a>
 */
public abstract class AbstractVelocityEngine extends AbstractTempelEngine {
//	private static Map<Scope, VelocityEngine> V_MAP;
	private VelocityEngine engine = new VelocityEngine();

//	--------------------------------------------------------------------------
	@Override
	public void initialize(Map<Scope, String> repositories) {
		super.initialize(repositories);

//		if(V_MAP == null) {
//			V_MAP = new HashMap<Scope, VelocityEngine>();
//			for(Scope scope : repositories.keySet()) {
//				VelocityEngine engine = new VelocityEngine();
//				engine.setProperty(RuntimeConstants.RESOURCE_LOADER, "file");
//				engine.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_CACHE, "true");
//				engine.init();
//				V_MAP.put(scope, engine);
//			}
//		}
	}

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

//		// Tworzenie szablonu Velocity:
//		Template template = null;
//		try {
//			template = V_MAP.get(scope).getTemplate(source, "UTF-8");
//		} catch(ResourceNotFoundException rnfe) {
//			// couldn't find the template
//		} catch(ParseErrorException pee) {
//			// syntax error: problem parsing the template
//		} catch(MethodInvocationException mie) {
//			// something invoked in the template
//			// threw an exception
//		} catch (Exception e) {
//		}
//
//		template.merge(context, writer);
	}
}
