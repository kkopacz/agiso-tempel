/* org.agiso.tempel.test.AbstractRepositoryTest (21-12-2012)
 * 
 * AbstractRepositoryTest.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.test;

import org.testng.annotations.BeforeClass;

/**
 * Klasa bazowa dla testów opartych o repozytorium plików. Wyznacza kanoniczną
 * ścieżkę repozytorium w środowisku uruchomienia testów. Jest to katalog
 * '/src/test/resources/' w bieżącym katalogu projektu.
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class AbstractRepositoryTest {
	protected String repositoryPath;

//	--------------------------------------------------------------------------
	@BeforeClass
	public void determineRepositoryPath() throws Exception {
		// Wyznaczanie ścieżki bazowej repozytorium testowego:
		repositoryPath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		repositoryPath = repositoryPath.substring(0, repositoryPath.lastIndexOf("/target/test-classes/"));
		repositoryPath = repositoryPath + "/src/test/resources/";
	}
}
