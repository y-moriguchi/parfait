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

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class KeysigStatistics<T> {

	//
	private MyCharacterSet<T> keyset;
	private Map<T, Integer> assoValue = new HashMap<T, Integer>();
	private Map<Integer, Keysig<T>> range =
			new HashMap<Integer, Keysig<T>>();
	private int totalKeywords;
	private int minHashValue = Integer.MAX_VALUE;
	private int maxHashValue = -1;
	private int hashValueRange;
	private int duplicates = 0;

	//
	private KeysigStatistics() {}

	/**
	 * 
	 * @return
	 */
	public int getTotalKeywords() {
		return totalKeywords;
	}

	/**
	 * 
	 * @return
	 */
	public int getMinHashValue() {
		return minHashValue;
	}

	/**
	 * 
	 * @return
	 */
	public int getMaxHashValue() {
		return maxHashValue;
	}

	/**
	 * 
	 * @return
	 */
	public int getHashValueRange() {
		return hashValueRange;
	}

	/**
	 * 
	 * @return
	 */
	public int getDuplicates() {
		return duplicates;
	}

	/**
	 * 
	 * @param x
	 * @return
	 */
	public boolean isVaildHashCode(int x) {
		return range.containsKey(Integer.valueOf(x));
	}

	/**
	 * 
	 * @param a
	 * @return
	 */
	public static<T> KeysigStatistics<T> compute(MyCharacterSet<T> ks,
			List<Keysig<T>> a, int add, boolean pass1,
			boolean addlength) {
		KeysigStatistics<T> s = new KeysigStatistics<T>();
		boolean dirty = true;
		Set<T> v;
		int h, g;

		s.keyset = ks;
		while(dirty) {
			dirty = false;
			for(int k = 1; k < a.size(); k++) {
				h = s.hashCode(a.get(k), addlength);
				for(int m = 0; m < k; m++) {
					g = s.hashCode(a.get(m), addlength);
					if(h != g) {
						// do nothing
					} else if((v = a.get(k)
							.disjointUnion(a.get(m))).size() > 0) {
						s.addAssoValue(v, add);
						dirty = true;
					} else {
						// bad key signature
						return null;
					}
				}
			}
			if(pass1)  break;
		}

		s.totalKeywords = a.size();
		for(Keysig<T> t : a) {
			h = s.hashCode(t, addlength);
			s.range.put(h, t);
			s.maxHashValue = h < s.maxHashValue ? s.maxHashValue : h;
			s.minHashValue = h > s.minHashValue ? s.minHashValue : h;
		}
		s.hashValueRange = s.maxHashValue - s.minHashValue + 1;
		return s;
	}

	/**
	 * 
	 * @param a
	 * @return
	 */
	public int hashCode(Keysig<T> a, boolean addlength) {
		int r = addlength ? a.getLength() : 0;

		for(T t : a) {
			r += getAssoValue(t);
		}
		return r;
	}

	//
	private void addAssoValue(T x, int g) {
		int a;

		if(assoValue.containsKey(x)) {
			a = assoValue.get(x);
			assoValue.put(x, a + g);
		} else {
			assoValue.put(x, g);
		}
	}

	//
	private void addAssoValue(Set<T> x, int g) {
		int v = -1;
		T m = null;

		for(T t : x) {
			if(v < getOccurence(t)) {
				v = getOccurence(t);
				m = t;
			}
		}
		addAssoValue(m, g);
	}

	/**
	 * 
	 * @param x
	 * @return
	 */
	public int getAssoValue(T x) {
		if(keyset.contains(x)) {
			return assoValue.containsKey(x) ? assoValue.get(x) : 0;
		} else {
			return -1;
		}
	}

	/**
	 * 
	 * @param x
	 * @return
	 */
	public int getOccurence(T x) {
		return keyset.getOccurence(x);
	}

	/**
	 * 
	 * @param pw
	 */
	public void report(PrintWriter pw) {
		for(T t : keyset) {
			pw.printf("%10s %7d %7d %7d\n", t.toString(), getAssoValue(t),
					getOccurence(t));
		}
	}

	/**
	 * 
	 * @param pw
	 */
	public void report(PrintStream pw) {
		for(T t : keyset) {
			pw.printf("%10s %7d %7d\n", t.toString(), getAssoValue(t),
					getOccurence(t));
		}
	}

}
