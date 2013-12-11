/* org.agiso.tempel.core.model.beans.TemplateReferenceBean (02-10-2012)
 * 
 * TemplateReferenceBean.java
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
package org.agiso.tempel.core.model.beans;

import java.util.ArrayList;
import java.util.List;

import org.agiso.tempel.api.model.TemplateParam;
import org.agiso.tempel.api.model.TemplateReference;
import org.agiso.tempel.api.model.TemplateResource;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
@XStreamAlias("reference")
public class TemplateReferenceBean implements TemplateReference {
	@XStreamAsAttribute
	private String key;

	private String groupId;
	private String templateId;
	private String version;

	@XStreamAlias("workdir")
	private String workDir;

	private List<TemplateParam<?, ?>> params;

	@XStreamImplicit
	private List<TemplateResource> resources;

//	--------------------------------------------------------------------------
	public TemplateReferenceBean() {
		params = new ArrayList<TemplateParam<?, ?>>();
		resources = new ArrayList<TemplateResource>();
	}

//	--------------------------------------------------------------------------
	@Override
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	@SuppressWarnings("unchecked")
	public <T extends TemplateReferenceBean> T withKey(String key) {
		this.key = key;
		return (T)this;
	}

	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	@SuppressWarnings("unchecked")
	public <T extends TemplateReferenceBean> T withGroupId(String groupId) {
		this.groupId = groupId;
		return (T)this;
	}

	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	@SuppressWarnings("unchecked")
	public <T extends TemplateReferenceBean> T withTemplateId(String templateId) {
		this.templateId = templateId;
		return (T)this;
	}

	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	@SuppressWarnings("unchecked")
	public <T extends TemplateReferenceBean> T withVersion(String version) {
		this.version = version;
		return (T)this;
	}

	@Override
	public String getWorkDir() {
		return workDir;
	}
	@Override
	public void setWorkDir(String workDir) {
		this.workDir = workDir;
	}
	@SuppressWarnings("unchecked")
	public <T extends TemplateReferenceBean> T withWorkDir(String workDir) {
		this.workDir = workDir;
		return (T)this;
	}

	@Override
	public List<TemplateParam<?, ?>> getParams() {
		return params;
	}
	public void setParams(List<TemplateParam<?, ?>> params) {
		this.params = params;
	}
	@SuppressWarnings("unchecked")
	public <T extends TemplateReferenceBean> T withParams(List<TemplateParam<?, ?>> params) {
		this.params = params;
		return (T)this;
	}
	@SuppressWarnings("unchecked")
	public <T extends TemplateReferenceBean> T withParams(TemplateParam<?, ?>[] params) {
		this.params = new ArrayList<TemplateParam<?, ?>>(params.length);
		for(TemplateParam<?, ?> param : params) {
			this.params.add(param);
		}
		return (T)this;
	}

	@Override
	public List<TemplateResource> getResources() {
		return resources;
	}
	public void setResources(List<TemplateResource> resources) {
		this.resources = resources;
	}
	@SuppressWarnings("unchecked")
	public <T extends TemplateReferenceBean> T withResources(List<TemplateResource> resources) {
		this.resources = resources;
		return (T)this;
	}
	@SuppressWarnings("unchecked")
	public <T extends TemplateReferenceBean> T withResources(TemplateResource[] resources) {
		this.resources = new ArrayList<TemplateResource>(resources.length);
		for(TemplateResource resource : resources) {
			this.resources.add(resource);
		}
		return (T)this;
	}

//	--------------------------------------------------------------------------
	@Override
	public TemplateReferenceBean clone() {
		return fillClone(new TemplateReferenceBean());
	}
	protected TemplateReferenceBean fillClone(TemplateReferenceBean clone) {
		clone.key = key;

		clone.groupId = groupId;
		clone.templateId = templateId;
		clone.version = version;

		clone.workDir = workDir;

		if(params != null) {
			clone.params = new ArrayList<TemplateParam<?, ?>>(params.size());
			for(TemplateParam<?, ?> param : params) {
				clone.params.add(param.clone());
			}
		}

		if(resources != null) {
			clone.resources = new ArrayList<TemplateResource>(resources.size());
			for(TemplateResource resource : resources) {
				clone.resources.add(resource.clone());
			}
		}

		return clone;
	}
}
