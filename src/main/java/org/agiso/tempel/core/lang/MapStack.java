/* org.agiso.tempel.core.lang.MapStack (06-12-2012)
 * 
 * MapStack.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.core.lang;

import java.util.Map;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public interface MapStack<K, V> extends Map<K, V> {
	public Map<K, V> push(Map<K, V> map);

	public Map<K, V> pop();

	public Map<K, V> peek();
}
