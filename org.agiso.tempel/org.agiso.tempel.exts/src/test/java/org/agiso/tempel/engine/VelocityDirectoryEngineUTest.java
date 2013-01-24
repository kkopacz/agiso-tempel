/* org.agiso.tempel.engine.VelocityDirectoryEngineUTest (15-11-2012)
 * 
 * VelocityDirectoryEngineUTest.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.engine;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.agiso.tempel.Temp;
import org.agiso.tempel.api.ITemplateSource;
import org.agiso.tempel.exts.base.provider.source.FileTemplateSource;
import org.agiso.tempel.test.AbstractTempelEngineTest;
import org.agiso.tempel.test.annotation.TempelEngineTest;
import org.testng.annotations.Test;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
@TempelEngineTest(VelocityDirectoryEngine.class)
public class VelocityDirectoryEngineUTest extends AbstractTempelEngineTest {
	@Test
	public void testProcessDirectory1() throws Exception {
		// Wypełnianie mapy modelu dla szablonu:
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put("key1", "value1");
		modelMap.put("key2", "value2");
		modelMap.put("fileKey2", "file2");
		modelMap.put("fileKey4", "file4");
		modelMap.put("dirKey1", "dir1");
		modelMap.put("dirKey3", "ir3/dir4/dir5/dir");
		modelMap.put("dirKey7", "dir7");

		// Przygotowywanie katalogu wyjściowego i uruchamianie sinika:
		String outPath = getOutputPath(true);
		ITemplateSource templateSource = new FileTemplateSource(
				repositoryPath + "/VelocityDirectoryEngineUTest",
				"testProcessDirectory1"
		);
		engine.run(templateSource, modelMap, outPath);

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "1b88794f97f0b86ceba125ef9326af9e".equals(md5);
	}

	@Test
	public void testProcessFile1() throws Exception {
		// Wypełnianie mapy modelu dla szablonu:
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put("key1", "value1");

		// Przygotowywanie katalogu wyjściowego i uruchamianie sinika:
		String outPath = getOutputPath(true) + "/testProcessFile1.txt";
		ITemplateSource templateSource = new FileTemplateSource(
				repositoryPath + "/VelocityDirectoryEngineUTest",
				"testProcessFile1.vm"
		);
		engine.run(templateSource, modelMap, outPath);

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "c1f94f832c26b7deecf9aecb292f6b91".equals(md5);
	}
}
