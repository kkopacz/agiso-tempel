/* org.agiso.tempel.api.ITemplateSource (19-12-2012)
 * 
 * ITemplateSource.java
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
package org.agiso.tempel.api;

import java.util.Collection;


/**
 * 
 * 
 * @author <a href="mailto:kkopaczz@agiso.org">Karol Kopacz</a>
 */
public interface ITemplateSource {
	public String getTemplate();

	public String getResource();

//	--------------------------------------------------------------------------
	public boolean exists();

	public boolean isFile();

	public boolean isDirectory();

//	--------------------------------------------------------------------------
	public Collection<ITemplateSourceEntry> listEntries();

	public ITemplateSourceEntry getEntry(String name);
}
