/* org.agiso.tempel.core.provider.HashBasedTemplateRepository (29-10-2012)
 * 
 * HashBasedTemplateRepository.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.core.provider;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.agiso.tempel.api.internal.ITemplateRepository;
import org.agiso.tempel.core.model.Template;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class HashBasedTemplateRepository implements ITemplateRepository {
	// key -> template
	private Map<String, Template> kMap;

	// groupId, templateId, version -> Template
	private Table<String, String, Map<String, Template>> gtvTable;

//	--------------------------------------------------------------------------
	public HashBasedTemplateRepository() {
		kMap = new HashMap<String, Template>();
		gtvTable = HashBasedTable.create();
	}

//	--------------------------------------------------------------------------
	// FIXME: Kod metod 'put' 'contains' i 'get' do optymalizacji!!!
	@Override
	public void put(String key, String groupId, String templateId, String version, Template template) {
		if(key == null) {
			Map<String, Template> vMap = gtvTable.get(groupId, templateId);
			if(vMap == null) {
				vMap = new HashMap<String, Template>();
				gtvTable.put(groupId, templateId, vMap);
			} else if(vMap.containsKey(version)) {
				throw new IllegalStateException("Powtórzona definicja szablonu " + groupId + ":" + templateId + ":" + version);
			}
			vMap.put(version, template);
		} else if(key.indexOf(':') > 0) {
			StringTokenizer tokenizer = new StringTokenizer(key, ":", false);
			groupId = tokenizer.nextToken();
			templateId = tokenizer.nextToken();
			version = tokenizer.nextToken();

			Map<String, Template> vMap = gtvTable.get(groupId, templateId);
			if(vMap == null) {
				vMap = new HashMap<String, Template>();
				gtvTable.put(groupId, templateId, vMap);
			} else if(vMap.containsKey(version)) {
				throw new IllegalStateException("Powtórzona definicja szablonu " + groupId + ":" + templateId + ":" + version);
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
			Map<String, Template> vMap = gtvTable.get(groupId, templateId);
			return vMap == null? false : vMap.containsKey(version);
		} else if(key.indexOf(':') > 0) {
			StringTokenizer tokenizer = new StringTokenizer(key, ":", false);
			groupId = tokenizer.nextToken();
			templateId = tokenizer.nextToken();
			version = tokenizer.nextToken();

			Map<String, Template> vMap = gtvTable.get(groupId, templateId);
			return vMap == null? false : vMap.containsKey(version);
		} else {
			return kMap.containsKey(key);
		}
	}

	@Override
	public Template get(String key, String groupId, String templateId, String version) {
		if(key == null) {
			Map<String, Template> vMap = gtvTable.get(groupId, templateId);
			return vMap == null? null : vMap.get(version);
		} else if(key.indexOf(':') > 0) {
			StringTokenizer tokenizer = new StringTokenizer(key, ":", false);
			groupId = tokenizer.nextToken();
			templateId = tokenizer.nextToken();
			version = tokenizer.nextToken();

			Map<String, Template> vMap = gtvTable.get(groupId, templateId);
			return vMap == null? null : vMap.get(version);
		} else {
			return kMap.get(key);
		}
	}
}
