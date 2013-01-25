/* org.agiso.core.lang.SimpleMapStack (06-12-2012)
 * 
 * SimpleMapStack.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.core.lang;

import java.util.AbstractMap;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class SimpleMapStack<K, V> extends AbstractMap<K, V> implements MapStack<K, V> {
	private /* volatile */ Stack<Map<K, V>> stack;

	public SimpleMapStack() {
		stack = new Stack<Map<K, V>>();
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		if(stack.isEmpty()) {
			throw new EmptyStackException();
		}

		Map<K, Entry<K, V>> entryMap = new HashMap<K, Entry<K, V>>();
		for(int index = stack.size() - 1; index >= 0; index--) {
			Map<K, V> map = stack.get(index);
			for(Entry<K, V> entry : map.entrySet()) {
				if(!entryMap.containsKey(entry.getKey())) {
					entryMap.put(entry.getKey(), entry);
				}
			}
		}

		return new HashSet<Entry<K, V>>(entryMap.values());
	}

	@Override
	public V put(K key, V value) {
		if(stack.isEmpty()) {
			throw new EmptyStackException();
		}

		return stack.peek().put(key, value);
	}

	@Override
	public Map<K, V> push(Map<K, V> map) {
		return stack.push(map);
	}

	@Override
	public Map<K, V> pop() {
		return stack.pop();
	}

	@Override
	public Map<K, V> peek() {
		return stack.peek();
	}
}
