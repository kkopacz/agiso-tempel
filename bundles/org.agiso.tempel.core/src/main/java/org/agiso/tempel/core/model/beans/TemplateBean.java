/* org.agiso.tempel.core.model.beans.TemplateBean (14-09-2012)
 * 
 * TemplateBean.java
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.agiso.tempel.api.ITemplateSource;
import org.agiso.tempel.api.ITemplateSourceFactory;
import org.agiso.tempel.api.model.Template;
import org.agiso.tempel.api.model.TemplateReference;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * 
 * 
 * @author Karol Kopacz
 * @since 1.0
 */
@XStreamAlias("template")
public class TemplateBean extends TemplateReferenceBean implements Template<TemplateEngineBean> {
	@XStreamAsAttribute
	@XStreamAlias("abstract")
	private boolean abstrct;

	private TemplateEngineBean engine;

	private List<TemplateReference> references;

	@XStreamOmitField
	private Set<String> templateClassPath;

	@XStreamOmitField
	private ITemplateSourceFactory templateSourceFactory;

//	--------------------------------------------------------------------------
	public TemplateBean() {
		super();
	}

//	--------------------------------------------------------------------------
	@Override
	public boolean isAbstract() {
		return abstrct;
	}
	public void setAbstract(boolean abstrct) {
		this.abstrct = abstrct;
	}

	@Override
	public TemplateEngineBean getEngine() {
		return engine;
	}
	public void setEngine(TemplateEngineBean engine) {
		this.engine = engine;
	}
	@SuppressWarnings("unchecked")
	public <T extends TemplateBean> T withEngine(TemplateEngineBean engine) {
		this.engine = engine;
		return (T)this;
	}

	@Override
	public List<TemplateReference> getReferences() {
		return references;
	}
	public void setReferences(List<TemplateReference> references) {
		this.references = references;
	}
	@SuppressWarnings("unchecked")
	public <T extends TemplateBean> T withTemplates(List<TemplateReference> references) {
		this.references = references;
		return (T)this;
	}
	@SuppressWarnings("unchecked")
	public <T extends TemplateBean> T withTemplates(TemplateReference[] references) {
		this.references = new ArrayList<TemplateReference>(references.length);
		for(TemplateReference reference : references) {
			this.references.add(reference);
		}
		return (T)this;
	}

	@Override
	public Set<String> getTemplateClassPath() {
		return templateClassPath;
	}
	public void setTemplateClassPath(Set<String> templateClassPath) {
		this.templateClassPath = templateClassPath;
	}
	@SuppressWarnings("unchecked")
	public <T extends TemplateBean> T withTemplateClassPath(Set<String> templateClassPath) {
		this.templateClassPath = templateClassPath;
		return (T)this;
	}

	@Override
	public void extendTemplateClassPath(Set<String> entries) {
		if(templateClassPath == null) {
			templateClassPath = new HashSet<String>(entries);
		} else {
			templateClassPath.addAll(entries);
		}
	}

//	--------------------------------------------------------------------------
	@Override
	public void setTemplateSourceFactory(ITemplateSourceFactory templateSourceFactory) {
		this.templateSourceFactory = templateSourceFactory;
	}

	@Override
	public ITemplateSource getTemplateSource(String source) {
		return templateSourceFactory.createTemplateSource(this, source);
	}

//	--------------------------------------------------------------------------
	@Override
	public TemplateBean clone() {
		return fillClone(new TemplateBean());
	}
	protected TemplateBean fillClone(TemplateBean clone) {
		super.fillClone(clone);

		if(engine != null) {
			clone.engine = engine.clone();
		}

		// TODO: Zweryfikować:
		// Z punktu widzenia aktualizacji parametrów podszablonu klonowanie
		// jego podszablonów nie jest konieczne, bo i tak bedą klonowane
		// przez kod główny. Jest wykonywane ze względu na czystość kodu
		// metody clone. Można rozważyć utworzenie wariantu tej metody z
		// parametrem sterującym klonowaniem podszablonów.
		if(references != null) {
			clone.references = new ArrayList<TemplateReference>(references.size());
			for(TemplateReference reference : references) {
				clone.references.add(reference.clone());
			}
		}

		if(templateClassPath != null) {
			clone.templateClassPath = new HashSet<String>(templateClassPath);
		}

		clone.templateSourceFactory = templateSourceFactory;

		return clone;
	}
}
