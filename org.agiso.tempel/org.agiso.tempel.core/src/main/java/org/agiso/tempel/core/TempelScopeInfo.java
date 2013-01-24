/* org.agiso.tempel.core.TempelScopeInfo (14-01-2013)
 * 
 * TempelScopeInfo.java
 * 
 * Copyright 2013 agiso.org
 */
package org.agiso.tempel.core;

import java.util.HashMap;
import java.util.Map;

import org.agiso.tempel.api.internal.ITempelScopeInfo;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class TempelScopeInfo implements ITempelScopeInfo {
	private Map<String, String> settings = new HashMap<String, String>();;
	private Map<String, String> repositories = new HashMap<String, String>();

//	--------------------------------------------------------------------------
	public TempelScopeInfo() {
		String path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		int index = path.lastIndexOf("/repo/");

		// Inicjalizacja repozytoriów z zasobami dla poszczególnych poziomów:
		if(index != -1) {
			// Rzeczywiste środowisko uruchomieniowe (uruchomienie z linni komend):
			settings.put("GLOBAL", path.substring(0, index) + "/conf/tempel.xml");
			settings.put("USER", System.getProperty("user.home") + "/.tempel/tempel.xml");
			settings.put("RUNTIME", System.getProperty("user.dir") + "/tempel.xml");
			settings.put("MAVEN", System.getProperty("user.home") + "/.m2/repository/");

			repositories.put("GLOBAL", path.substring(0, index) + "/repository");
			repositories.put("USER", System.getProperty("user.home") + "/.tempel/repository");
			repositories.put("RUNTIME", System.getProperty("user.dir") + "/.tempel");
			repositories.put("MAVEN", path + System.getProperty("user.dir") + "/.tempel/maven");
		} else {
			// Deweloperskie środowisko uruchomieniowe (uruchomienie z eclipse'a):
			path = System.getProperty("user.dir");					// FIXME: Rodzielić katalogi repozytoriów

			settings.put("GLOBAL", path + "/src/test/configuration/application/tempel.xml");
			settings.put("USER", path + "/src/test/configuration/home/tempel.xml");
			settings.put("RUNTIME", path + "/src/test/configuration/runtime/tempel.xml");
			settings.put("MAVEN", path + "/src/test/resources/repository/maven");

			repositories.put("GLOBAL", path + "/src/test/resources/repository/application");
			repositories.put("USER", path + "/src/test/resources/repository/home");
			repositories.put("RUNTIME", path + "/src/test/resources/repository/runtime");
			repositories.put("MAVEN", path + "/target/local-repo");
		}
	}

//	--------------------------------------------------------------------------
	@Override
	public RepostoryType getRepositoryType(String scope) {
		return "MAVEN".equals(scope)? RepostoryType.JAR : RepostoryType.FILE;
	}

	@Override
	public String getSettingsPath(String scope) {
		return settings.get(scope);
	}

	@Override
	public String getRepositoryPath(String scope) {
		return repositories.get(scope);
	}
}
