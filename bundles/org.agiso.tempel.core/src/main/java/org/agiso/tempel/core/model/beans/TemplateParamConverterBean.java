/* org.agiso.tempel.core.model.beans.TemplateParamConverterBean (20-11-2013)
 * 
 * TemplateParamConverterBean.java
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

import org.agiso.tempel.api.ITemplateParamConverter;
import org.agiso.tempel.api.model.TemplateParamConverter;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
@XStreamAlias("converter")
public class TemplateParamConverterBean extends AbstractBeanInitializer<ITemplateParamConverter<?, ?>>
		implements TemplateParamConverter {
	public String getConverterClassName() {
		return getBeanClassName();
	}
	public void setConverterClassName(String converterClassName) {
		setBeanClassName(converterClassName);
	}
	public <T extends TemplateParamConverterBean> T withConverterClassName(String converterClassName) {
		return withBeanClassName(converterClassName);
	}

//	--------------------------------------------------------------------------
	@Override
	public TemplateParamConverterBean clone() {
		return fillClone(new TemplateParamConverterBean());
	}
}
