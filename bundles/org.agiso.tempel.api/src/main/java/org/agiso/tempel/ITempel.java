/* org.agiso.tempel.ITempel (25-01-2013)
 * 
 * ITempel.java
 * 
 * Copyright 2013 agiso.org
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
package org.agiso.tempel;

import java.util.Map;

import org.agiso.tempel.api.internal.IParamReader;
import org.agiso.tempel.api.internal.ITemplateExecutor;

/**
 * 
 * 
 * @author Karol Kopacz
 * @since 1.0
 */
public interface ITempel {
	/**
	 * @see ITemplateExecutor#setParamReader(IParamReader)
	 */
	public void setParamReader(IParamReader paramReader);

	public void startTemplate(String name, Map<String, String> params, String workDir) throws Exception;
}
