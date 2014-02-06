/* org.agiso.core.lang.SimpleMapStackUTest (06-12-2012)
 * 
 * SimpleMapStackUTest.java
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
package org.agiso.core.lang;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

/**
 * 
 * 
 * @author Karol Kopacz
 * @since 1.0
 */
public class SimpleMapStackUTest {
	@Test
	public void testSimpleMapStack() throws Exception {
		Object value1 = new BigDecimal("1.0");

		MapStack<String, Object> mapStack = new SimpleMapStack<String, Object>();
		Map<String, Object> map = mapStack.push(new HashMap<String, Object>());

		map.put("key1", value1);

		assert value1 == map.get("key1");
		assert value1 == mapStack.get("key1");
	}
}
