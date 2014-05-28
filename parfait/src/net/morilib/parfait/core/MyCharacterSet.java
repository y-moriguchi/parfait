/*
 * Copyright 2014 Yuichiro Moriguchi
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
package net.morilib.parfait.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class MyCharacterSet<T> implements Iterable<T> {

	//
	private SortedSet<T> keyset;
	private Map<T, Integer> occurence = new HashMap<T, Integer>();

	/**
	 * 
	 * @param a
	 */
	public MyCharacterSet(List<Keysig<T>> a) {
		keyset = new TreeSet<T>();
		for(Keysig<T> x : a) {
			for(T t : x) {
				keyset.add(t);
				countOccurence(t);
			}
		}
	}

	//
	private void countOccurence(T x) {
		int a;

		if(occurence.containsKey(x)) {
			a = occurence.get(x);
			occurence.put(x, a + 1);
		} else {
			occurence.put(x, 1);
		}
	}

	/**
	 * 
	 * @param x
	 * @return
	 */
	public int getOccurence(T x) {
		return occurence.containsKey(x) ? occurence.get(x) : 0;
	}

	/**
	 * 
	 * @param x
	 * @return
	 */
	public boolean contains(T x) {
		return keyset.contains(x);
	}

	/**
	 * 
	 * @return
	 */
	public T getMininum() {
		return keyset.first();
	}

	/**
	 * 
	 * @return
	 */
	public T getMaximum() {
		return keyset.last();
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<T> iterator() {
		final Iterator<T> i = keyset.iterator();

		return new Iterator<T>() {

			@Override
			public boolean hasNext() {
				return i.hasNext();
			}

			@Override
			public T next() {
				return i.next();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}

		};
	}

}
