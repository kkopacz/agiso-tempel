/* org.agiso.tempel.api.impl.NullTemplateSource (26-01-2014)
 * 
 * NullTemplateSource.java
 * 
 * Copyright 2014 agiso.org
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
package org.agiso.tempel.api.impl;

import java.util.Collection;
import java.util.Collections;

import org.agiso.tempel.api.ITemplateSource;
import org.agiso.tempel.api.ITemplateSourceEntry;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class NullTemplateSource implements ITemplateSource {

//	--------------------------------------------------------------------------
	@Override
	public String getTemplate() {
		return null;
	}

	@Override
	public String getResource() {
		return null;
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public boolean isFile() {
		return false;
	}

	@Override
	public boolean isDirectory() {
		return false;
	}

	@Override
	public ITemplateSourceEntry getEntry(String name) {
		return null;
	}

	@Override
	public Collection<ITemplateSourceEntry> listEntries() {
		return Collections.emptyList();
	}
}
