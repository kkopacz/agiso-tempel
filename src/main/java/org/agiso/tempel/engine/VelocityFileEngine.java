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
import java.util.zip.ZipFile;

import org.agiso.tempel.core.ITemplateSource;
import org.apache.velocity.VelocityContext;

/**
 * Implementacja interfejsu silnika generatora wykorzystująca bibliotekę Velocity.
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class VelocityFileEngine extends AbstractVelocityEngine {
	@Override
	public void run(ITemplateSource templateSource, Map<String, Object> params, String target) {
		// Wyznaczanie ścieżki zasobu docelowego i sprawdzanie jego istnienia:
		if(!templateSource.exists()) {
			throw new RuntimeException("Zasób " + templateSource.getResource() + " nie istnieje");
		}

		// Tworzenie kontekstu Velocity wspólnego w całym procesie obsługi:
		VelocityContext context = createVelocityContext(params);

		try {
			processVelocityResource(templateSource.getEntry(), context, target);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}

//	--------------------------------------------------------------------------
	protected void processVelocityResource(File resource, VelocityContext context, String target) throws IOException {
		processVelocityFile(resource, context, target);
	}

	protected final void processVelocityFile(File resource, VelocityContext context, String target) throws IOException {
		Writer writer = null;
		Reader reader = null;
		try {
			reader = new FileReader(resource);
			writer = new FileWriter(new File(target));

			doVelocityTemplateMerge(resource.getPath(), reader, context, writer);
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
