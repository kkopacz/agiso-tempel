/* org.agiso.tempel.convert.PackageToPathConverterUTest (12-11-2012)
 * 
 * PackageToPathConverterUTest.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.convert;

import org.agiso.tempel.api.ITemplateParamConverter;
import org.testng.annotations.Test;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class PackageToPathConverterUTest {
	private ITemplateParamConverter<String> converter = new PackageToPathConverter();

//	--------------------------------------------------------------------------
	@Test
	public void testConvertNull() throws Exception {
		assert null == converter.convert(null);
	}

	@Test
	public void testConvertEmpty() throws Exception {
		assert "".equals(converter.convert(""));
	}

	@Test
	public void testConvertBlank() throws Exception {
		assert "  ".equals(converter.convert("  "));
	}

	@Test
	public void testConvert() throws Exception {
		assert "aa/bb/cc/dd".equals(converter.convert("aa.bb.cc.dd"));
	}
}
