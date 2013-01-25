/* org.agiso.tempel.api.ITemplateRepository (29-10-2012)
 * 
 * ITemplateRepository.java
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

import org.agiso.tempel.api.model.Template;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public interface ITemplateRepository {
	/**
	 * @param key
	 * @param groupId
	 * @param templateId
	 * @param version
	 * @param template
	 */
	public void put(String key, String groupId, String templateId, String version, Template template);

	/**
	 * @param key
	 * @param groupId
	 * @param templateId
	 * @param version
	 * @return
	 */
	public boolean contains(String key, String groupId, String templateId, String version);

	/**
	 * @param key
	 * @param groupId
	 * @param templateId
	 * @param version
	 * @return
	 */
	public Template get(String key, String groupId, String templateId, String version);
}
