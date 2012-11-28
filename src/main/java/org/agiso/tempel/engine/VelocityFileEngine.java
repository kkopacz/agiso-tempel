/* org.agiso.tempel.engine.VelocityFileEngine (14-09-2012)
 * 
 * VelocityFileEngine.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.engine;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;

import org.agiso.tempel.core.model.Template.Scope;
import org.apache.velocity.VelocityContext;

/**
 * Implementacja interfejsu silnika generatora wykorzystująca bibliotekę Velocity.
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class VelocityFileEngine extends AbstractVelocityEngine {
	@Override
	public void run(Scope scope, String source, Map<String, Object> params, String target) {
		// Wyznaczanie ścieżki zasobu docelowego i sprawdzanie jego istnienia:
		String path = getScopedSourcePath(scope, source);
		File resource = new File(path);
		if(!resource.exists()) {
			throw new RuntimeException("Zasób " + source + " nie istnieje");
		}

		// Tworzenie kontekstu Velocity wspólnego w całym procesie obsługi:
		VelocityContext context = createVelocityContext(params);

		try {
			processVelocityResource(source, resource, context, target);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}

//	--------------------------------------------------------------------------
	protected void processVelocityResource(String logTag, File resource, VelocityContext context, String target) throws IOException {
		processVelocityFile(logTag, resource, context, target);
	}

	protected final void processVelocityFile(String logTag, File resource, VelocityContext context, String target) throws IOException {
		Writer writer = null;
		Reader reader = null;
		try {
			reader = new FileReader(resource);
			writer = new FileWriter(new File(target));

			doVelocityTemplateMerge(logTag, reader, context, writer);
		} finally {
			if(reader != null) {
				try {
					reader.close();
				} catch(Exception e) {
					throw new RuntimeException(e);
				}
			}
			if(writer != null) {
				try {
					writer.flush();
					writer.close();
				} catch(Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
}
