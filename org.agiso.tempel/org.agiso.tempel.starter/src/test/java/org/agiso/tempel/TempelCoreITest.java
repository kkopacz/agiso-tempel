/* org.agiso.tempel.TempelCoreITest (14-09-2012)
 * 
 * TempelCoreITest.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel;

import java.io.File;

import org.agiso.tempel.test.AbstractOutputTest;
import org.testng.annotations.Test;

/**
 * Testy działania szablonów testowych. Wykorzystują szablony z repozytorium
 * testowego src/test/resources/repository/runtime/ (dla standardowych szablonów
 * umieszczanych w repozytoriach katalogowych) oraz szablony z repozytorium
 * maven'owego src/test/resources/repository/maven/ (dla szablonów będących
 * zasobami w repozytoriach maven).
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class TempelCoreITest extends AbstractOutputTest {
//	@Test
//	public void test_base_mkdir_1_0_0() throws Exception {
//		String outPath = getOutputPath(true);
//		Bootstrap.main(new String[] {
//				"org.agiso.tempel.templates:base.mkdir:1.0.0",
//				"-d " + outPath
//		});
//
//		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
//		System.out.println(md5);
//		assert "31c287aec4ff50269de4523bdfc4707b".equals(md5);
//	}

//	--------------------------------------------------------------------------
//	src/test/configuration/application/tempel.xml
//	--------------------------------------------------------------------------

//	--------------------------------------------------------------------------
//	src/test/configuration/home/tempel.xml
//	--------------------------------------------------------------------------
	/**
	 * src/test/resources/repository/home/
	 * org/agiso/tempel/tests/javaClass/1.0.0
	 */
	@Test
	public void testJavaClass_1_0_0() throws Exception {
		String outPath = getOutputPath(true);
		Bootstrap.main(new String[] {
				"org.agiso.tempel.tests:javaClass:1.0.0",
				"-d " + outPath
		});

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "afc6decadb5da14f7aa120b0adf1cb96".equals(md5);
	}

//	--------------------------------------------------------------------------
//	src/test/configuration/runtime/tempel.xml
//	--------------------------------------------------------------------------
	/**
	 * Szablon grupujący org.agiso.tempel.tests:javaProject:1.0.0
	 */
	@Test
	public void testJavaProject_1_0_0() throws Exception {
		String outPath = getOutputPath(true);
		Bootstrap.main(new String[] {
				"org.agiso.tempel.tests:javaProject:1.0.0",
				"-d " + outPath
		});

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "5eab6c02e2877f31805c65e7e2aeba87".equals(md5);
	}

	/**
	 * Szablon grupujący org.agiso.tempel.tests:javaBundleProject:1.0.0
	 */
//	@Test
	public void testJavaBundleProject_1_0_0() throws Exception {
		String outPath = getOutputPath(true);
		Bootstrap.main(new String[] {
				"org.agiso.tempel.tests:javaBundleProject:1.0.0",
				"-d " + outPath
		});

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "4370e708b6f5b640e90a5cde835aeb8b".equals(md5);
	}

	/**
	 * src/test/resources/repository/runtime/
	 * org/agiso/tempel/tests/velocityFileTemplate/1.0.0
	 */
	@Test
	public void testVelocityFileTemplate_1_0_0_old() throws Exception {
		String outPath = getOutputPath(true);
		Bootstrap.main(new String[] {
				"org.agiso.tempel.tests:velocityFileTemplate:1.0.0",	// "velocityFileTemplate1",
				"-d " + outPath
		});

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "4bc922a21a32a7bdc89c1e8c715454d8".equals(md5);
	}

	/**
	 * src/test/resources/repository/runtime/
	 * org/agiso/tempel/tests/velocityDirTemplate/1.0.0
	 */
	@Test
	public void testVelocityDirTemplate_1_0_0() throws Exception {
		String outPath = getOutputPath(true);
		Bootstrap.main(new String[] {
				"org.agiso.tempel.tests:velocityDirTemplate:1.0.0",		// "velocityDirTemplate1",
				"-d " + outPath
		});

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "b8cbf54abdf135b7000bcff1c2e0a83d".equals(md5);
	}

	/**
	 * src/test/resources/repository/runtime/
	 * org/agiso/tempel/tests/velocityDirTemplate/2.0.0
	 */
	@Test
	public void testVelocityDirTemplate_2_0_0() throws Exception {
		String outPath = getOutputPath(true);
		Bootstrap.main(new String[] {
				"org.agiso.tempel.tests:velocityDirTemplate:2.0.0",		// "velocityDirTemplate2",
				"-d " + outPath
		});

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "06a1c46145a9170665c73a59d9d934b5".equals(md5);
	}

	/**
	 * src/test/resources/repository/runtime/
	 * org/agiso/tempel/tests/velocityDirTemplate/3.0.0
	 */
	@Test
	public void testVelocityDirTemplate_3_0_0() throws Exception {
		String outPath = getOutputPath(true);
		Bootstrap.main(new String[] {
				"org.agiso.tempel.tests:velocityDirTemplate:3.0.0",		// "velocityDirTemplate3",
				"-d " + outPath
		});

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "ec9b716697122eef04a8521d19802bc8".equals(md5);
	}

	/**
	 * src/test/resources/repository/runtime/
	 * org/agiso/tempel/tests/velocityDirTemplate/4.0.0
	 */
	@Test
	public void testVelocityDirTemplate_4_0_0() throws Exception {
		String outPath = getOutputPath(true);
		Bootstrap.main(new String[] {
				"org.agiso.tempel.tests:velocityDirTemplate:4.0.0",		// "velocityDirTemplate4",
				"-d " + outPath
		});

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "baf37424f517639cac4cac38defcf028".equals(md5);
	}

	/**
	 * src/test/resources/repository/runtime/
	 * org/agiso/tempel/tests/velocityDirTemplate/5.0.0
	 */
	@Test
	public void testVelocityDirTemplate_5_0_0() throws Exception {
		String outPath = getOutputPath(true);
		Bootstrap.main(new String[] {
				"org.agiso.tempel.tests:velocityDirTemplate:5.0.0",		// "velocityDirTemplate5",
				"-d " + outPath
		});

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "e37f056869b7e3351fefa1c5cf43c523".equals(md5);
	}

	/**
	 * src/test/resources/repository/runtime/
	 * org/agiso/tempel/tests/velocityDirTemplate/6.0.0
	 */
	@Test
	public void testVelocityDirTemplate_6_0_0() throws Exception {
		String outPath = getOutputPath(true);
		Bootstrap.main(new String[] {
				"org.agiso.tempel.tests:velocityDirTemplate:6.0.0",		// "velocityDirTemplate6",
				"-d " + outPath
		});

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "4cda7a3e71085716e3f53bff93cfcc29".equals(md5);
	}

	/**
	 * src/test/resources/repository/runtime/
	 * org/agiso/tempel/tests/velocityDirTemplate/7.0.0
	 */
	@Test
	public void testVelocityDirTemplate_7_0_0() throws Exception {
		String outPath = getOutputPath(true);
		Bootstrap.main(new String[] {
				"org.agiso.tempel.tests:velocityDirTemplate:7.0.0",		// "velocityDirTemplate7",
				"-d " + outPath
		});

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "18454eb8cf137bfe2f05fe4b20db3e49".equals(md5);
	}

	/**
	 * src/test/resources/repository/runtime/
	 * org/agiso/tempel/tests/velocityDirTemplate/8.0.0
	 */
	@Test
	public void testVelocityDirTemplate_8_0_0() throws Exception {
		String outPath = getOutputPath(true);
		Bootstrap.main(new String[] {
				"org.agiso.tempel.tests:velocityDirTemplate:8.0.0",		// "velocityDirTemplate8",
				"-d " + outPath
		});

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "eabcbb3f24682664c844ed3a8031a3f5".equals(md5);
	}
}
