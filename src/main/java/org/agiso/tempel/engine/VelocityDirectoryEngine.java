/* org.agiso.tempel.engine.VelocityDirectoryEngine (12-11-2012)
 * 
 * VelocityDirectoryEngine.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.engine;

import java.io.File;
import java.io.IOException;

import org.agiso.tempel.Temp;
import org.apache.velocity.VelocityContext;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class VelocityDirectoryEngine extends VelocityFileEngine {
	private static final String TEMPLATE_FILE_SUFFIX = ".vm";

//	--------------------------------------------------------------------------
	@Override
	protected void processVelocityResource(File resource, VelocityContext context, String target) throws IOException {
		if(resource.isDirectory()) {
			for(File subFile : resource.listFiles()) {
				processVelocitySubResource(subFile, context, target);
			}
		} else {
			processVelocityFile(resource, context, target);
		}
	}

//	--------------------------------------------------------------------------
	protected final void processVelocitySubResource(File resource, VelocityContext context, String target) throws IOException {
		String resourceName = resource.getName();

		if(resource.isDirectory()) {
			// Tworzenie katalogu:
			String targetPath = target + '/' + processVelocityString(resourceName, resourceName, context);
			File targetFile = new File(targetPath);
			if(!targetFile.exists()) {
				targetFile.mkdirs();
			}

			for(File subFile : resource.listFiles()) {
				processVelocitySubResource(subFile, context, targetPath);
			}
		} else {
			if(resource.getName().endsWith(TEMPLATE_FILE_SUFFIX)) {
				// Tworzenie pliku na podstawie szablonu Velocity:
				resourceName = resourceName.substring(0, resourceName.lastIndexOf(TEMPLATE_FILE_SUFFIX));
				String targetPath = target + '/' + processVelocityString(resourceName, resourceName, context);
				processVelocityFile(resource, context, targetPath);
			} else {
				// Kopiownaie pliku (nie jest szablonem Velocity):
				File targetFile = new File(target + '/' + processVelocityString(resourceName, resourceName, context));
				Temp.FileUtils_copyFile(resource, targetFile);
			}
		}
	}
}
