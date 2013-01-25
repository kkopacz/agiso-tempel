/* org.agiso.tempel.api.impl.JarTemplateSourceUTest (21-12-2012)
 * 
 * JarTemplateSourceUTest.java
 * 
 * Copyright 2012 agiso.org
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
package org.agiso.tempel.api.impl;

import org.agiso.tempel.api.ITemplateSource;
import org.agiso.tempel.api.ITemplateSourceEntry;
import org.agiso.tempel.api.impl.JarTemplateSource;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class JarTemplateSourceUTest {
	protected String repositoryPath;

//	--------------------------------------------------------------------------
	@BeforeClass
	public void determineRepositoryPath() throws Exception {
		// Wyznaczanie ścieżki bazowej repozytorium testowego:
		repositoryPath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		repositoryPath = repositoryPath.substring(0, repositoryPath.lastIndexOf("/target/test-classes/"));
		repositoryPath = repositoryPath + "/src/test/resources/";
	}

//	--------------------------------------------------------------------------
	@Test
	public void testFileSource() throws Exception {
		final String templatePath = repositoryPath + "/" + "JarTemplateSourceUTest/testFileSource.jar";
		final String resourceName = /* TEMPEL-INF/template/ */ "testSourceFile1.txt";

		ITemplateSource templateSource = new JarTemplateSource(templatePath, resourceName);

		assert templatePath.equals(templateSource.getTemplate());
		assert resourceName.equals(templateSource.getResource());
		assert true == templateSource.exists();
		assert true == templateSource.isFile();
		assert false == templateSource.isDirectory();
		assert 1 == templateSource.listEntries().size();

		ITemplateSourceEntry entry = templateSource.getEntry(resourceName);
		assert null != entry;
		assert templatePath.equals(entry.getTemplate());
		assert "testSourceFile1.txt".equals(entry.getName());
		assert true == entry.exists();
		assert true == entry.isFile();
		assert false == entry.isDirectory();
	}

	@Test
	public void testDirectorySource() throws Exception {
		final String templatePath = repositoryPath + "/" + "JarTemplateSourceUTest/testDirectorySource.jar";

		ITemplateSource templateSource = new JarTemplateSource(templatePath, null);

		assert templatePath.equals(templateSource.getTemplate());
		assert null == templateSource.getResource();
		assert true == templateSource.exists();
		assert false == templateSource.isFile();
		assert true == templateSource.isDirectory();
		assert 4 == templateSource.listEntries().size();

		ITemplateSourceEntry entry = templateSource.getEntry("/");
		assert null != entry;
		assert templatePath.equals(entry.getTemplate());
		assert "/".equals(entry.getName());
		assert true == entry.exists();
		assert false == entry.isFile();
		assert true == entry.isDirectory();

		entry = templateSource.getEntry("testSourceFile1.txt");
		assert null != entry;
		assert templatePath.equals(entry.getTemplate());
		assert "testSourceFile1.txt".equals(entry.getName());
		assert true == entry.exists();
		assert true == entry.isFile();
		assert false == entry.isDirectory();

		entry = templateSource.getEntry("subdirectory/");
		assert null != entry;
		assert templatePath.equals(entry.getTemplate());
		assert "subdirectory/".equals(entry.getName());
		assert true == entry.exists();
		assert false == entry.isFile();
		assert true == entry.isDirectory();

		entry = templateSource.getEntry("subdirectory/testSourceFile2.txt");
		assert null != entry;
		assert templatePath.equals(entry.getTemplate());
		assert "subdirectory/testSourceFile2.txt".equals(entry.getName());
		assert true == entry.exists();
		assert true == entry.isFile();
		assert false == entry.isDirectory();
	}

	@Test
	public void testDirectorySourceSubdirectory() throws Exception {
		final String templatePath = repositoryPath + "/" + "JarTemplateSourceUTest/testDirectorySource.jar";

		ITemplateSource templateSource = new JarTemplateSource(templatePath, "subdirectory");

		assert templatePath.equals(templateSource.getTemplate());
		assert "subdirectory".equals(templateSource.getResource());
		assert true == templateSource.exists();
		assert false == templateSource.isFile();
		assert true == templateSource.isDirectory();
		assert 2 == templateSource.listEntries().size();

		ITemplateSourceEntry entry = templateSource.getEntry("/");
		assert null != entry;
		assert templatePath.equals(entry.getTemplate());
		assert "/".equals(entry.getName());
		assert true == entry.exists();
		assert false == entry.isFile();
		assert true == entry.isDirectory();

		entry = templateSource.getEntry("testSourceFile2.txt");
		assert null != entry;
		assert templatePath.equals(entry.getTemplate());
		assert "testSourceFile2.txt".equals(entry.getName());
		assert true == entry.exists();
		assert true == entry.isFile();
		assert false == entry.isDirectory();
	}
}
