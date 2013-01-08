/* org.agiso.tempel.AppConfig (07-01-2013)
 * 
 * AppConfig.java
 * 
 * Copyright 2013 agiso.org
 */
package org.agiso.tempel;

import org.agiso.tempel.api.internal.ITemplateExecutor;
import org.agiso.tempel.api.internal.ITemplateProvider;
import org.agiso.tempel.api.internal.ITemplateProviderElement;
import org.agiso.tempel.api.internal.ITemplateVerifier;
import org.agiso.tempel.core.DefaultTemplateExecutor;
import org.agiso.tempel.core.RecursiveTemplateVerifier;
import org.agiso.tempel.core.provider.AppTemplateProvider;
import org.agiso.tempel.core.provider.MainTemplateProvider;
import org.agiso.tempel.core.provider.MvnTemplateProvider;
import org.agiso.tempel.core.provider.RunTemplateProvider;
import org.agiso.tempel.core.provider.UsrTemplateProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
@Configuration
@Import(MvnConfig.class)
public class AppConfig {
	public @Bean TempelFactory tempel() {
		return new TempelFactory();
	}


	public @Bean ITemplateProvider templateProvider() {
		return new MainTemplateProvider();
	}

	public @Bean ITemplateVerifier templateVerifier() {
		return new RecursiveTemplateVerifier();
	}

	public @Bean ITemplateExecutor templateExecutor() {
		return new DefaultTemplateExecutor();
	}


	public @Bean ITemplateProviderElement runTemplateProvider() {
		return new RunTemplateProvider();
	}
	public @Bean ITemplateProviderElement usrTemplateProvider() {
		return new UsrTemplateProvider();
	}
	public @Bean ITemplateProviderElement appTemplateProvider() {
		return new AppTemplateProvider();
	}
}
