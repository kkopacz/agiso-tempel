/* org.agiso.tempel.core.model.beans.TemplateParamBean (15-09-2012)
 * 
 * TemplateParamBean.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.core.model.beans;

import org.agiso.tempel.api.ITemplateParamConverter;
import org.agiso.tempel.api.model.TemplateParam;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
@XStreamAlias("param")
public class TemplateParamBean implements TemplateParam {
	@XStreamAsAttribute
	private String key;
	@XStreamAsAttribute
	private String name;
	@XStreamAsAttribute
	private String type;

	private Boolean fixed;
	private String value;
	private Class<? extends ITemplateParamConverter<?>> converter;

//	--------------------------------------------------------------------------
	@Override
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	@SuppressWarnings("unchecked")
	public <T extends TemplateParamBean> T withKey(String key) {
		this.key = key;
		return (T)this;
	}

	@Override
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@SuppressWarnings("unchecked")
	public <T extends TemplateParamBean> T withName(String name) {
		this.name = name;
		return (T)this;
	}

	@Override
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@SuppressWarnings("unchecked")
	public <T extends TemplateParamBean> T withType(String type) {
		this.type = type;
		return (T)this;
	}

	@Override
	public Boolean getFixed() {
		return fixed;
	}
	public void setFixed(Boolean fixed) {
		this.fixed = fixed;
	}
	@SuppressWarnings("unchecked")
	public <T extends TemplateParamBean> T withFixed(Boolean fixed) {
		this.fixed = fixed;
		return (T)this;
	}

	@Override
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@SuppressWarnings("unchecked")
	public <T extends TemplateParamBean> T withValue(String value) {
		this.value = value;
		return (T)this;
	}

	@Override
	public Class<? extends ITemplateParamConverter<?>> getConverter() {
		return converter;
	}
	public void setConverter(Class<? extends ITemplateParamConverter<?>> converter) {
		this.converter = converter;
	}
	@SuppressWarnings("unchecked")
	public <T extends TemplateParamBean> T withConverter(Class<? extends ITemplateParamConverter<?>> converter) {
		this.converter = converter;
		return (T)this;
	}

//	--------------------------------------------------------------------------
	@Override
	public TemplateParamBean clone() {
		return fillClone(new TemplateParamBean());
	}
	protected TemplateParamBean fillClone(TemplateParamBean clone) {
		clone.key = key;
		clone.name = name;
		clone.type = type;

		clone.fixed = fixed;
		clone.value = value;
		clone.converter = converter;

		return clone;
	}
}
