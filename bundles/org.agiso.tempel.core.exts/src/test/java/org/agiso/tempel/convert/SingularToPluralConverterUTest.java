/* org.agiso.tempel.convert.SingularToPluralConverterUTest (05-12-2012)
 * 
 * SingularToPluralConverterUTest.java
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
package org.agiso.tempel.convert;

import org.agiso.tempel.api.ITemplateParamConverter;
import org.testng.annotations.Test;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class SingularToPluralConverterUTest {
	private ITemplateParamConverter<String, String> converter = new SingularToPluralConverter();

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
		assert "phases".equals(converter.convert("phase"));
		assert "clocks".equals(converter.convert("clock"));
		assert "boys".equals(converter.convert("boy"));
		assert "potatoes".equals(converter.convert("potato"));
		assert "photos".equals(converter.convert("photo"));
		assert "cherries".equals(converter.convert("cherry"));
		assert "knives".equals(converter.convert("knife"));
		assert "sheep".equals(converter.convert("sheep"));
		assert "mice".equals(converter.convert("mouse"));

		assert "mouseKnives".equals(converter.convert("mouseKnife"));

		assert "????s".equals(converter.convert("????"));
	}
}
