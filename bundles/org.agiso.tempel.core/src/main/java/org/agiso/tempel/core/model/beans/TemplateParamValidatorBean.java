/* org.agiso.tempel.core.model.beans.TemplateParamValidatorBean (20-11-2013)
 * 
 * TemplateParamValidatorBean.java
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

import org.agiso.tempel.api.ITemplateParamValidator;
import org.agiso.tempel.api.model.TemplateParamValidator;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
@XStreamAlias("validator")
public class TemplateParamValidatorBean extends AbstractBeanInitializer<ITemplateParamValidator<?>>
		implements TemplateParamValidator {
	public Class<ITemplateParamValidator<?>> getValidatorClass() {
		return getBeanClass();
	}
	public void setValidatorClass(Class<ITemplateParamValidator<?>> validatorClass) {
		setBeanClass(validatorClass);
	}
	public <T extends TemplateParamValidatorBean> T withValidatorClass(Class<ITemplateParamValidator<?>> validatorClass) {
		return withBeanClass(validatorClass);
	}

//	--------------------------------------------------------------------------
	@Override
	public TemplateParamValidatorBean clone() {
		return fillClone(new TemplateParamValidatorBean());
	}
}
