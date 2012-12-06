/* org.agiso.tempel.core.lang.SimpleMapStackUTest (06-12-2012)
 * 
 * SimpleMapStackUTest.java
 * 
 * Copyright 2012 PPW 'ARAJ' Sp. z o. o.
 */
package org.agiso.tempel.core.lang;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@araj.pl">Karol Kopacz</a>
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
