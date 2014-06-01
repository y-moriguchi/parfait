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

import java.util.Iterator;
import java.util.Set;

import net.morilib.parfait.misc.TinySortedBag;

public class Keysig<T> implements Iterable<T> {

	//
	private TinySortedBag<T> keys;
	private int length;

	//
	private Keysig(TinySortedBag<T> t, int l, boolean dum) {
		keys = t;
		length = l;
	}

	/**
	 * 
	 * @param keys
	 */
	public Keysig(Iterable<T> keys, int length) {
		this.keys = new TinySortedBag<T>(keys);
		this.length = length;
	}

	/**
	 * 
	 * @param s
	 * @return
	 */
	public static Keysig<Character> newInstance(String s, int len,
			boolean ignoreCase) {
		TinySortedBag<Character> r;
		char c;

		r = new TinySortedBag<Character>();
		for(int k = 0; k < s.length(); k++) {
			c = s.charAt(k);
			r.add(ignoreCase ? Character.toUpperCase(c) : c);
		}
		return new Keysig<Character>(r, len, false);
	}

	/**
	 * 
	 * @return
	 */
	public int getLength() {
		return length;
	}

	/**
	 * 
	 * @param a
	 * @return
	 */
	public Set<T> disjointUnion(Keysig<T> a) {
		Set<T> x = keys.toSet();
		Set<T> y = keys.toSet();
		Set<T> z = a.keys.toSet();

		x.addAll(z);
		y.retainAll(z);
		x.removeAll(y);
		return x;
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<T> iterator() {
		return keys.immutableIterator();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder b = new StringBuilder();
		String d = "";

		for(T t : keys) {
			b.append(d).append(t.toString());
			d = ",";
		}
		return b.toString();
	}

}
