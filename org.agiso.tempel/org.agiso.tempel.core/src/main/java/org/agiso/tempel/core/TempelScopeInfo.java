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
import org.agiso.tempel.core.model.Template.Scope;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class TempelScopeInfo implements ITempelScopeInfo {
	private Map<Scope, String> settings = new HashMap<Scope, String>();;
	private Map<Scope, String> repositories = new HashMap<Scope, String>();

//	--------------------------------------------------------------------------
	public TempelScopeInfo() {
		String path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		int index = path.lastIndexOf("/repo/");

		// Inicjalizacja repozytoriów z zasobami dla poszczególnych poziomów:
		if(index != -1) {
			// Rzeczywiste środowisko uruchomieniowe (uruchomienie z linni komend):
			settings.put(Scope.GLOBAL, path.substring(0, index) + "/conf/tempel.xml");
			settings.put(Scope.USER, System.getProperty("user.home") + "/.tempel/tempel.xml");
			settings.put(Scope.RUNTIME, System.getProperty("user.dir") + "/tempel.xml");
			settings.put(Scope.MAVEN, System.getProperty("user.home") + "/.m2/repository/");

			repositories.put(Scope.GLOBAL, path.substring(0, index) + "/repository");
			repositories.put(Scope.USER, System.getProperty("user.home") + "/.tempel/repository");
			repositories.put(Scope.RUNTIME, System.getProperty("user.dir") + "/.tempel");
			repositories.put(Scope.MAVEN, path + System.getProperty("user.dir") + "/.tempel/maven");
		} else {
			// Deweloperskie środowisko uruchomieniowe (uruchomienie z eclipse'a):
			path = System.getProperty("user.dir");					// FIXME: Rodzielić katalogi repozytoriów

			settings.put(Scope.GLOBAL, path + "/src/test/configuration/application/tempel.xml");
			settings.put(Scope.USER, path + "/src/test/configuration/home/tempel.xml");
			settings.put(Scope.RUNTIME, path + "/src/test/configuration/runtime/tempel.xml");
			settings.put(Scope.MAVEN, path + "/src/test/resources/repository/maven");

			repositories.put(Scope.GLOBAL, path + "/src/test/resources/repository/application");
			repositories.put(Scope.USER, path + "/src/test/resources/repository/home");
			repositories.put(Scope.RUNTIME, path + "/src/test/resources/repository/runtime");
			repositories.put(Scope.MAVEN, path + "/target/local-repo");
		}
	}

//	--------------------------------------------------------------------------
	@Override
	public RepostoryType getRepositoryType(Scope scope) {
		return Scope.MAVEN.equals(scope)? RepostoryType.JAR : RepostoryType.FILE;
	}

	@Override
	public String getSettingsPath(Scope scope) {
		return settings.get(scope);
	}

	@Override
	public String getRepositoryPath(Scope scope) {
		return repositories.get(scope);
	}
}
