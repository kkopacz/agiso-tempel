/* org.agiso.tempel.MvnConfig (07-01-2013)
 * 
 * MvnConfig.java
 * 
 * Copyright 2013 agiso.org
 */
package org.agiso.tempel;

import org.agiso.tempel.api.internal.ITemplateProviderElement;
import org.agiso.tempel.core.provider.AetherMvnTemplateProvider;
import org.agiso.tempel.core.provider.ShinkWrapMvnTemplateProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
@Configuration
public class MvnConfig {
	public @Bean ITemplateProviderElement mvnTemplateProvider() {
		// return new AetherMvnTemplateProvider();
		return new ShinkWrapMvnTemplateProvider();
	}
}
