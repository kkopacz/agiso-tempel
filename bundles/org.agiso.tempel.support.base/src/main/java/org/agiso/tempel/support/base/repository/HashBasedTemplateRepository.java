/* org.agiso.tempel.support.base.repository.HashBasedTemplateRepository (29-10-2012)
 * 
 * HashBasedTemplateRepository.java
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
package org.agiso.tempel.support.base.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.agiso.tempel.api.ITemplateRepository;
import org.agiso.tempel.api.model.Template;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
@Component
@Scope("prototype")
public class HashBasedTemplateRepository implements ITemplateRepository {
	// key -> template
	private final Map<String, Template<?>> kMap;

	// groupId, templateId, version -> Template
	private final Table<String, String, Map<String, Template<?>>> gtvTable;

//	--------------------------------------------------------------------------
	public HashBasedTemplateRepository() {
		kMap = new HashMap<String, Template<?>>();
		gtvTable = HashBasedTable.create();
	}

//	--------------------------------------------------------------------------
	// FIXME: Kod metod 'put' 'contains' i 'get' do optymalizacji!!!
	@Override
	public void put(String key, String groupId, String templateId, String version, Template<?> template) {
		if(key == null) {
			Map<String, Template<?>> vMap = gtvTable.get(groupId, templateId);
			if(vMap == null) {
				vMap = new HashMap<String, Template<?>>();
				gtvTable.put(groupId, templateId, vMap);
			} else if(vMap.containsKey(version)) {
				throw new IllegalStateException("Powtórzona definicja szablonu " + groupId + ":" + templateId + ":" + version + " (" + key + ")");
			}
			vMap.put(version, template);
		} else if(key.indexOf(':') > 0) {
			StringTokenizer tokenizer = new StringTokenizer(key, ":", false);
			groupId = tokenizer.nextToken();
			templateId = tokenizer.nextToken();
			version = tokenizer.nextToken();

			Map<String, Template<?>> vMap = gtvTable.get(groupId, templateId);
			if(vMap == null) {
				vMap = new HashMap<String, Template<?>>();
				gtvTable.put(groupId, templateId, vMap);
			} else if(vMap.containsKey(version)) {
				throw new IllegalStateException("Powtórzona definicja szablonu " + groupId + ":" + templateId + ":" + version + " (" + key + ")");
			}
			vMap.put(version, template);
		} else {
			if(kMap.containsKey(key)) {
				throw new IllegalStateException("Powtórzony klucz szablonu: " + key);
			}
			kMap.put(key, template);
		}
	}

	@Override
	public boolean contains(String key, String groupId, String templateId, String version) {
		if(key == null) {
			Map<String, Template<?>> vMap = gtvTable.get(groupId, templateId);
			return vMap == null? false : vMap.containsKey(version);
		} else if(key.indexOf(':') > 0) {
			StringTokenizer tokenizer = new StringTokenizer(key, ":", false);
			groupId = tokenizer.nextToken();
			templateId = tokenizer.nextToken();
			version = tokenizer.nextToken();

			Map<String, Template<?>> vMap = gtvTable.get(groupId, templateId);
			return vMap == null? false : vMap.containsKey(version);
		} else {
			return kMap.containsKey(key);
		}
	}

	@Override
	public Template<?> get(String key, String groupId, String templateId, String version) {
		if(key == null) {
			Map<String, Template<?>> vMap = gtvTable.get(groupId, templateId);
			return vMap == null? null : vMap.get(version);
		} else if(key.indexOf(':') > 0) {
			StringTokenizer tokenizer = new StringTokenizer(key, ":", false);
			groupId = tokenizer.nextToken();
			templateId = tokenizer.nextToken();
			version = tokenizer.nextToken();

			Map<String, Template<?>> vMap = gtvTable.get(groupId, templateId);
			return vMap == null? null : vMap.get(version);
		} else {
			return kMap.get(key);
		}
	}
}
