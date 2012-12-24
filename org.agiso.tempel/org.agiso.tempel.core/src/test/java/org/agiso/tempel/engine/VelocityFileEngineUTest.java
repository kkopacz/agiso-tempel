/* org.agiso.tempel.engine.VelocityFileEngineUTest (15-11-2012)
 * 
 * VelocityFileEngineUTest.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.engine;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.agiso.tempel.Temp;
import org.agiso.tempel.api.internal.ITemplateSource;
import org.agiso.tempel.core.FileTemplateSource;
import org.agiso.tempel.test.AbstractTempelEngineTest;
import org.agiso.tempel.test.annotation.TempelEngineTest;
import org.testng.annotations.Test;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
@TempelEngineTest(VelocityFileEngine.class)
public class VelocityFileEngineUTest extends AbstractTempelEngineTest {
	@Test
	public void testProcessFile1() throws Exception {
		// Wypełnianie mapy modelu dla szablonu:
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put("key1", "value1");

		// Przygotowywanie katalogu wyjściowego i uruchamianie sinika:
		String outPath = getOutputPath(true) + "/testProcessFile1.txt";
		ITemplateSource templateSource = new FileTemplateSource(repositoryPath,
				"VelocityFileEngineUTest", "testProcessFile1.vm"
		);
		engine.run(templateSource, modelMap, outPath);

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "a974d0957b52ed6c80be61bbae0e2069".equals(md5);
	}
}
