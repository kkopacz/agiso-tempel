/* org.agiso.tempel.engine.VelocityDirectoryEngine (12-11-2012)
 * 
 * VelocityDirectoryEngine.java
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

import java.io.File;
import java.io.InputStream;

import org.agiso.tempel.Temp;
import org.agiso.tempel.api.ITemplateSource;
import org.agiso.tempel.api.ITemplateSourceEntry;
import org.apache.velocity.VelocityContext;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class VelocityDirectoryEngine extends VelocityFileEngine {
	private static final String TEMPLATE_FILE_SUFFIX = ".vm";

//	--------------------------------------------------------------------------
	/**
	 * Szablon może być pojedynczym plikiem (wówczas silnik działa tak jak silnik
	 * {@link VelocityFileEngine}, lub katalogiem. W takiej sytuacji przetwarzane
	 * są wszsytkie jego wpisy.
	 */
	@Override
	protected void processVelocityResource(ITemplateSource source, VelocityContext context, String target) throws Exception {
		if(source.isFile()) {
			super.processVelocityFile(source.getEntry(source.getResource()), context, target);
		} else if(source.isDirectory()) {
			for(ITemplateSourceEntry entry : source.listEntries()) {
				if(entry.isFile()) {
					processVelocityFile(entry, context, target);
				} else if(entry.isDirectory()) {
					processVelocityDirectory(entry, context, target);
				}
			}
		}
	}

	protected void processVelocityFile(ITemplateSourceEntry entry, VelocityContext context, String target) throws Exception {
		String resourceName = entry.getName();
		if(resourceName.endsWith(TEMPLATE_FILE_SUFFIX)) {
			resourceName = resourceName.substring(0, resourceName.lastIndexOf(TEMPLATE_FILE_SUFFIX));
			target = target + '/' + processVelocityString(resourceName, resourceName, context);
			super.processVelocityFile(entry, context, target);
		} else {
			// Kopiownaie pliku (nie jest szablonem Velocity):
			File targetFile = new File(target + '/' + processVelocityString(resourceName, resourceName, context));
			InputStream is = entry.getInputStream();
			try {
				Temp.FileUtils_copyFile(is, targetFile);
			} finally {
				if(is != null) {
					try {
						is.close();
					} catch(Exception e) {
					}
				}
			}
		}
	}

	protected final void processVelocityDirectory(ITemplateSourceEntry entry, VelocityContext context, String target) {
		String resourceName = entry.getName();
		if(!entry.isDirectory()) {
			throw new RuntimeException("Element "
					+ entry.getTemplate() + "/"+ resourceName
					+ " nie jest katalogiem"
			);
		}

		String targetPath = target + '/' + processVelocityString(resourceName, resourceName, context);
		File targetFile = new File(targetPath);
		if(!targetFile.exists()) {
			targetFile.mkdirs();
		}
	}
}
