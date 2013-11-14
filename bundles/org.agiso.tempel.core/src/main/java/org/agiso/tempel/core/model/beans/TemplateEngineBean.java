/* org.agiso.tempel.core.model.beans.TemplateEngineBean (10-11-2013)
 * 
 * TemplateEngineBean.java
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
package org.agiso.tempel.core.model.beans;

import org.agiso.tempel.api.ITempelEngine;
import org.agiso.tempel.api.model.TemplateEngine;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
@XStreamAlias("engine")
public class TemplateEngineBean implements TemplateEngine {
	@XStreamAsAttribute
	@XStreamAlias("class")
	private Class<? extends ITempelEngine> engineClass;

//	--------------------------------------------------------------------------
	@Override
	public Class<? extends ITempelEngine> getEngineClass() {
		return engineClass;
	}
	public void setEngineClass(Class<? extends ITempelEngine> engineClass) {
		this.engineClass = engineClass;
	}
	@SuppressWarnings("unchecked")
	public <T extends TemplateEngineBean> T withEngineClass(Class<? extends ITempelEngine> engineClass) {
		this.engineClass = engineClass;
		return (T)this;
	}

//	--------------------------------------------------------------------------
	@Override
	public TemplateEngineBean clone() {
		return fillClone(new TemplateEngineBean());
	}
	protected TemplateEngineBean fillClone(TemplateEngineBean clone) {
		clone.engineClass = engineClass;

		return clone;
	}
}
