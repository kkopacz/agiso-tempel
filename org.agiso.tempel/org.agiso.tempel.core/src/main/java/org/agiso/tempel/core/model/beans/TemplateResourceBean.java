/* org.agiso.tempel.core.model.beans.TemplateResourceBean (17-10-2012)
 * 
 * TemplateResourceBean.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.core.model.beans;

import org.agiso.tempel.api.model.Template;
import org.agiso.tempel.api.model.TemplateResource;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
@XStreamAlias("resource")
public class TemplateResourceBean implements TemplateResource {
	private String source;
	private String target;

	@XStreamOmitField
	private Template parentTemplateReference;

//	--------------------------------------------------------------------------
	@Override
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	@SuppressWarnings("unchecked")
	public <T extends TemplateResourceBean> T withSource(String source) {
		this.source = source;
		return (T)this;
	}

	@Override
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	@SuppressWarnings("unchecked")
	public <T extends TemplateResourceBean> T withTarget(String target) {
		this.target = target;
		return (T)this;
	}

	@Override
	public Template getParentTemplateReference() {
		return parentTemplateReference;
	}
	@Override
	public void setParentTemplateReference(Template parentTemplateReference) {
		this.parentTemplateReference = parentTemplateReference;
	}

//	--------------------------------------------------------------------------
	@Override
	public TemplateResourceBean clone() {
		return fillClone(new TemplateResourceBean());
	}
	protected TemplateResourceBean fillClone(TemplateResourceBean clone) {
		clone.source = source;
		clone.target = target;

		clone.parentTemplateReference = parentTemplateReference;

		return clone;
	}
}
