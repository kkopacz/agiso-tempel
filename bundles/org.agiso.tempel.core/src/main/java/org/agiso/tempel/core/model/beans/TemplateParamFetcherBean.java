/* org.agiso.tempel.core.model.beans.TemplateParamFetcherBean (21-12-2013)
 * 
 * TemplateParamFetcherBean.java
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

import org.agiso.tempel.api.ITemplateParamFetcher;
import org.agiso.tempel.api.model.TemplateParamFetcher;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
@XStreamAlias("fetcher")
public class TemplateParamFetcherBean extends AbstractBeanInitializer<ITemplateParamFetcher<?>>
		implements TemplateParamFetcher {
	public String getFetcherClassName() {
		return getBeanClassName();
	}
	public void setFetcherClassName(String fetcherClassName) {
		setBeanClassName(fetcherClassName);
	}
	public <T extends TemplateParamFetcherBean> T withFetcherClassName(String fetcherClassName) {
		return withBeanClassName(fetcherClassName);
	}

//--------------------------------------------------------------------------
	@Override
	public TemplateParamFetcherBean clone() {
	return fillClone(new TemplateParamFetcherBean());
	}
}
