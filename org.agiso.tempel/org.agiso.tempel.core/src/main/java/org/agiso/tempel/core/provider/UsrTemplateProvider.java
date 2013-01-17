/* org.agiso.tempel.core.provider.UsrTemplateProvider (15-12-2012)
 * 
 * UsrTemplateProvider.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.core.provider;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.agiso.tempel.api.internal.ITempelScopeInfo;
import org.agiso.tempel.api.internal.ITempelEntryProcessor;
import org.agiso.tempel.api.internal.ITemplateRepository;
import org.agiso.tempel.core.TempelScopeInfo;
import org.agiso.tempel.core.model.Template;
import org.agiso.tempel.core.model.Template.Scope;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class UsrTemplateProvider extends BaseTemplateProvider {
	// FIXME: Zastosować wstrzykiwanie zależności
	private ITempelScopeInfo tempelScopeInfo = new TempelScopeInfo();

	private ITemplateRepository templateRepository = new HashBasedTemplateRepository();

//	--------------------------------------------------------------------------
	@Override
	public int getOrder() {
		return 20;
	}

//	--------------------------------------------------------------------------
	@Override
	public void initialize(Map<String, Object> globalProperties) throws IOException {
		super.initialize(globalProperties);

		readUsrTemplates(templateRepository);
	}

//	--------------------------------------------------------------------------
	@Override
	public boolean contains(String key, String groupId, String templateId, String version) {
		return templateRepository.contains(key, groupId, templateId, version);
	}

	@Override
	public Template get(String key, String groupId, String templateId, String version) {
		return templateRepository.get(key, groupId, templateId, version);
	}

//	--------------------------------------------------------------------------
	/**
	 * @param xStream
	 * @param templateRepository
	 * @throws IOException
	 */
	private void readUsrTemplates(final ITemplateRepository templateRepository) throws IOException {
		// Mapa szablonów użytkownika (katalog domowy użytkownika):
		String usrSettings = tempelScopeInfo.getSettingsPath(Scope.USER);
		File usrSettingsFile = new File(usrSettings);

		try {
			tempelFileProcessor.process(usrSettingsFile, new ITempelEntryProcessor() {
				@Override
				public void processObject(Object object) {
					UsrTemplateProvider.this.processObject(Scope.USER, object, templateRepository);
				}
			});
			System.out.println("Wczytano ustawienia użytkownika z pliku " + usrSettingsFile.getCanonicalPath());
		} catch(Exception e) {
			System.err.println("Błąd wczytywania ustawień użytkownika: " + e.getMessage());
			throw new RuntimeException(e);
		}
	}
}