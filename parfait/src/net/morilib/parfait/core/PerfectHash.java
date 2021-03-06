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

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.morilib.parfait.misc.MyChar;

public class PerfectHash {

	//
	private SortedSet<MyChar> cset;
	private KeysigStatistics<MyChar> stat;
	private PermutationInclementor perm;
	private int minWordLength;
	private int maxWordLength;
	private boolean addLength;
	private boolean ignoreCase;

	//
	private PerfectHash(SortedSet<MyChar> c,
			KeysigStatistics<MyChar> s, PermutationInclementor p,
			int max, int min, boolean add, boolean icase) {
		cset = c;
		stat = s;
		perm = p;
		maxWordLength = max;
		minWordLength = min;
		addLength = add;
		ignoreCase = icase;
	}

	/**
	 * 
	 * @return
	 */
	public KeysigStatistics<MyChar> getStatstics() {
		return stat;
	}

	/**
	 * 
	 * @return
	 */
	public PermutationInclementor getPermutation() {
		return perm;
	}

	/**
	 * 
	 * @return
	 */
	public int getTotalKeywords() {
		return stat.getTotalKeywords();
	}

	/**
	 * 
	 * @return
	 */
	public int getMinHashValue() {
		return stat.getMinHashValue();
	}

	/**
	 * 
	 * @return
	 */
	public int getMaxHashValue() {
		return stat.getMaxHashValue();
	}

	/**
	 * 
	 * @return
	 */
	public int getHashValueRange() {
		return stat.getHashValueRange();
	}

	/**
	 * 
	 * @return
	 */
	public int getDuplicates() {
		return stat.getDuplicates();
	}

	/**
	 * 
	 * @return
	 */
	public int getMinWordLength() {
		return minWordLength;
	}

	/**
	 * 
	 * @return
	 */
	public int getMaxWordLength() {
		return maxWordLength;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isAddLength() {
		return addLength;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isIgnoreCase() {
		return ignoreCase;
	}

	/**
	 * 
	 * @param x
	 * @return
	 */
	public boolean contains(int x) {
		return cset.contains(MyChar.valueOf((char)x));
	}

	/**
	 * 
	 * @param x
	 * @return
	 */
	public int getAssoValue(int x) {
		MyChar c = MyChar.valueOf((char)x);
		int y;

		return cset.contains(c) && (y = stat.getAssoValue(c)) >= 0 ?
				y : stat.getMaxHashValue() + 1;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isASCII() {
		return cset.last().isASCII();
	}

	/**
	 * 
	 * @return
	 */
	public boolean isByte() {
		return (!cset.last().isASCII() &&
				cset.last().distance(cset.first()) >= 128);
	}

	/**
	 * 
	 * @return
	 */
	public int getMinimum() {
		if(isASCII()) {
			return 0;
		} else if(isByte()) {
			return 0;
		} else {
			return cset.first().getChar();
		}
	}

	/**
	 * 
	 * @return
	 */
	public int getMaximum() {
		if(isASCII()) {
			return 127;
		} else if(isByte()) {
			return 255;
		} else {
			return cset.last().getChar();
		}
	}

	/**
	 * 
	 * @param s
	 * @return
	 */
	public int hashCode(String s) {
		Keysig<MyChar> k;

		k = isByte() ?
				perm.pickByte(s, ignoreCase) :
					perm.pickCharacter(s, ignoreCase);
		return stat.hashCode(k, addLength);
	}

	/**
	 * 
	 * @param add
	 * @param keys
	 * @return
	 */
	public static PerfectHash chooseKeys(int add, boolean ignoreCase,
			Iterable<String> keys) {
		SortedSet<MyChar> c = new TreeSet<MyChar>();
		SortedSet<MyChar> b = new TreeSet<MyChar>();
		KeysigStatistics<MyChar> r;
		MyCharacterSet<MyChar> t;
		PermutationInclementor p;
		List<Keysig<MyChar>> l;
		int m = Integer.MAX_VALUE, x = -1;
		boolean f;
		char h;

		for(String s : keys) {
			m = m > s.length() ? s.length() : m;
			x = x < s.length() ? s.length() : x;
			for(int k = 0; k < s.length(); k++) {
				h = s.charAt(k);
				h = ignoreCase ? Character.toUpperCase(h): h;
				c.add(MyChar.valueOf(h));
				b.add(MyChar.valueOf((char)(h & 0xff)));
				b.add(MyChar.valueOf((char)(h >> 8)));
			}
		}

		f = c.last().isASCII() || c.last().distance(c.first()) < 128;
		p = new PermutationInclementor(0, f ? m : m * 2);
		c = f ? c : b;
		do {
			l = new ArrayList<Keysig<MyChar>>();
			if(f) {
				for(String s : keys) {
					l.add(p.pickCharacter(s, ignoreCase));
				}
			} else {
				for(String s : keys) {
					l.add(p.pickByte(s, ignoreCase));
				}
			}
			t = new MyCharacterSet<MyChar>(l);

			r = KeysigStatistics.compute(t, l, add, false, false);
			if(r != null) {
				return new PerfectHash(c, r, p, x, m, false,
						ignoreCase);
			}

			r = KeysigStatistics.compute(t, l, add, false, true);
			if(r != null) {
				return new PerfectHash(c, r, p, x, m, true,
						ignoreCase);
			}
		} while((p = p.nextAll()) != null);
		return null;
	}

	/**
	 * 
	 * @param add
	 * @param perm
	 * @param pluslen
	 * @param keys
	 * @return
	 */
	public static PerfectHash chooseKeys(int add,
			PermutationInclementor p, boolean pluslen,
			boolean ignoreCase, Iterable<String> keys) {
		SortedSet<MyChar> c = new TreeSet<MyChar>();
		SortedSet<MyChar> b = new TreeSet<MyChar>();
		KeysigStatistics<MyChar> r;
		MyCharacterSet<MyChar> t;
		List<Keysig<MyChar>> l;
		int m = Integer.MAX_VALUE, x = -1;
		boolean f;
		char h;

		for(String s : keys) {
			m = m > s.length() ? s.length() : m;
			x = x < s.length() ? s.length() : x;
			for(int k = 0; k < s.length(); k++) {
				h = s.charAt(k);
				h = ignoreCase ? Character.toUpperCase(h): h;
				c.add(MyChar.valueOf(h));
				b.add(MyChar.valueOf((char)(h & 0xff)));
				b.add(MyChar.valueOf((char)(h >> 8)));
			}
		}

		f = c.last().isASCII();
		c = f ? c : b;
		l = new ArrayList<Keysig<MyChar>>();
		if(f) {
			for(String s : keys) {
				l.add(p.pickCharacter(s, ignoreCase));
			}
		} else {
			for(String s : keys) {
				l.add(p.pickByte(s, ignoreCase));
			}
		}

		t = new MyCharacterSet<MyChar>(l);
		r = KeysigStatistics.compute(t, l, add, false, pluslen);
		if(r != null) {
			return new PerfectHash(c, r, p, x, m, pluslen, ignoreCase);
		} else {
			return null;
		}
	}

}
