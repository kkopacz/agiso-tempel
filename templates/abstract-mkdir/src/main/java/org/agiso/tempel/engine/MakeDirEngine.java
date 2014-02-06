/* org.agiso.tempel.engine.MakeDirEngine (15-09-2012)
 * 
 * MakeDirEngine.java
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
import java.util.Map;

import org.agiso.tempel.api.ITempelEngine;
import org.agiso.tempel.api.ITemplateSource;

/**
 * 
 * 
 * @author Karol Kopacz
 * @since 1.0
 */
public class MakeDirEngine implements ITempelEngine {
	@Override
	public void run(ITemplateSource source, Map<String, Object> params, String target) {
		new File(target).mkdir();
	}
}
