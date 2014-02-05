/* org.agiso.core.lang.SimpleMapStack (06-12-2012)
 * 
 * SimpleMapStack.java
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
 * @author Karol Kopacz
 * @since 1.0
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
