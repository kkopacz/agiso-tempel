/* org.agiso.tempel.core.model.beans.TemplateBean (14-09-2012)
 * 
 * TemplateBean.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.core.model.beans;

import java.util.ArrayList;
import java.util.List;

import org.agiso.tempel.api.ITempelEngine;
import org.agiso.tempel.api.ITemplateSource;
import org.agiso.tempel.api.internal.ITempelScopeInfo;
import org.agiso.tempel.core.TempelScopeInfo;
import org.agiso.tempel.core.model.ITemplateSourceFactory;
import org.agiso.tempel.core.model.Repository;
import org.agiso.tempel.core.model.Template;
import org.agiso.tempel.core.model.TemplateReference;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
@XStreamAlias("template")
public class TemplateBean extends TemplateReferenceBean implements Template {
	@XStreamOmitField
	private Scope scope;

	@XStreamAsAttribute
	private Class<? extends ITempelEngine> engine;

	private List<TemplateReference> references;

	@XStreamOmitField
	private Repository repository;

	@XStreamOmitField
	private String path;

//	--------------------------------------------------------------------------
	public TemplateBean() {
		super();
	}

//	--------------------------------------------------------------------------
	@Override
	public Scope getScope() {
		return scope;
	}
	@Override
	public void setScope(Scope scope) {
		this.scope = scope;
	}

	@Override
	public Class<? extends ITempelEngine> getEngine() {
		return engine;
	}
	public void setEngine(Class<? extends ITempelEngine> engine) {
		this.engine = engine;
	}
	@SuppressWarnings("unchecked")
	public <T extends TemplateBean> T withEngine(Class<? extends ITempelEngine> engine) {
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
	public Repository getRepository() {
		return repository;
	}
	@Override
	public void setRepository(Repository repository) {
		this.repository = repository;
	}
	@SuppressWarnings("unchecked")
	public <T extends TemplateBean> T withRepository(Repository repository) {
		this.repository = repository;
		return (T)this;
	}

	@Override
	public String getPath() {
		return path;
	}
	@Override
	public void setPath(String path) {
		this.path = path;
	}
	@SuppressWarnings("unchecked")
	public <T extends TemplateBean> T withPath(String path) {
		this.path = path;
		return (T)this;
	}

//	--------------------------------------------------------------------------
	@Override
	public TemplateBean clone() {
		return fillClone(new TemplateBean());
	}
	protected TemplateBean fillClone(TemplateBean clone) {
		super.fillClone(clone);

		clone.scope = scope;

		clone.engine = engine;

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

		clone.repository = repository == null? null : repository.clone();
		clone.path = path;
		clone.templateSourceFactory = templateSourceFactory;

		return clone;
	}

//	--------------------------------------------------------------------------
	private static final ITempelScopeInfo tempelScopeInfo = new TempelScopeInfo();

	private ITemplateSourceFactory templateSourceFactory;

	@Override
	public void setTemplateSourceFactory(ITemplateSourceFactory templateSourceFactory) {
		this.templateSourceFactory = templateSourceFactory;
	}

	@Override
	public ITemplateSource getTemplateSource(String source) {
		return templateSourceFactory.createTemplateSource(this, source);
	}
}
