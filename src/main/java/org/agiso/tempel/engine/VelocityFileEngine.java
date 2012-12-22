/* org.agiso.tempel.engine.VelocityFileEngine (14-09-2012)
 * 
 * VelocityFileEngine.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.engine;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;

import org.agiso.tempel.core.ITemplateSource;
import org.agiso.tempel.core.ITemplateSourceEntry;
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
			throw new RuntimeException("Zasób "
					+ templateSource.getTemplate() + "/"+ templateSource.getResource()
					+ " nie istnieje"
			);
		}

		// Tworzenie kontekstu Velocity wspólnego w całym procesie obsługi:
		VelocityContext context = createVelocityContext(params);

		try {
			processVelocityResource(templateSource, context, target);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

//	--------------------------------------------------------------------------
	/**
	 * Szablon musi być pojedynczym plikiem. Jego przetwarzanie polega na jednorazowym
	 * wywołaniu metody {@link #processVelocityFile(ITemplateSourceEntry, VelocityContext,
	 * String)} w celu utworzenia pojedynczego zasobu.
	 */
	protected void processVelocityResource(ITemplateSource source, VelocityContext context, String target) throws Exception {
		if(!source.isFile()) {
			throw new RuntimeException("Zasób "
					+ source.getTemplate() + "/"+ source.getResource()
					+ " nie jest plikiem"
			);
		}
		processVelocityFile(source.getEntry(source.getResource()), context, target);
	}

	protected void processVelocityFile(ITemplateSourceEntry entry, VelocityContext context, String target) throws Exception {
		if(!entry.isFile()) {
			throw new RuntimeException("Element "
					+ entry.getTemplate() + "/"+ entry.getName()
					+ " nie jest plikiem"
			);
		}

		Writer writer = null;
		Reader reader = null;
		try {
			reader = new InputStreamReader(entry.getInputStream());
			writer = new FileWriter(new File(target));

			doVelocityTemplateMerge(entry.getName(), reader, context, writer);
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
