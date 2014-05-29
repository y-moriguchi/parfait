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
import java.util.Stack;

import net.morilib.parfait.misc.MyChar;
import net.morilib.parfait.misc.TinySortedBag;

public class PermutationInclementor
implements Iterable<Integer>, Cloneable {

	//
	private PermutationInclementor basis;
	private int value;
	private int length;

	//
	private PermutationInclementor() { }

	/**
	 * 
	 * @param max
	 */
	public PermutationInclementor(int max) {
		if(max < 0)  throw new IllegalArgumentException();
		basis  = null;
		length = max;
	}

	/**
	 * 
	 * @param len
	 * @param max
	 * @return
	 */
	public PermutationInclementor(int len, int max) {
		if(compare(len, max) > 0) {
			throw new IllegalArgumentException();
		} else if(len == 0) {
			basis = new PermutationInclementor(max);
		} else {
			basis = new PermutationInclementor(prevvalue(len), max);
		}
		length = max;
		value  = len;
	}

	//
	private static int nextvalue(int x) {
		return x < 0 ? (~x) + 1 : ~x;
	}

	//
	private static int prevvalue(int x) {
		return x < 0 ? ~x : x > 0 ? ~(x - 1) : 0;
	}

	//
	private static int tovalue(int x) {
		return x % 2 == 0 ? x / 2 : ~(x / 2);
	}

	//
	private static int compare(int x, int y) {
		int a, b;

		a = x < 0 ? ~x : x;
		b = y < 0 ? ~y : y;
		if(a == b) {
			return x < 0 ? y < 0 ? 0 : 1 : y < 0 ? -1 : 0;
		} else {
			return a < b ? -1 : 1;
		}
	}

	/**
	 * 
	 * @return
	 */
	public int getMaxValue() {
		return value;
	}

	/**
	 * 
	 * @return
	 */
	public int getMaxLength() {
		return length;
	}

	/**
	 * 
	 * @return
	 */
	public int getLength() {
		return basis == null ? 0 : basis.getLength() + 1;
	}

	/**
	 * 
	 * @return
	 */
	public int getAllMaxLength() {
		return length * 2 - 1;
	}

	/**
	 * 
	 * @return
	 */
	public PermutationInclementor next() {
		PermutationInclementor p = clone();

		if(basis == null)  return null;
		while(compare(p.value = nextvalue(p.value), length) >= 0) {
			if((p.basis = p.basis.next()) == null)  return null;
			p.value = p.basis.getMaxValue();
		}
		return p;
	}

	/**
	 * 
	 * @return
	 */
	public PermutationInclementor nextAll() {
		PermutationInclementor p;
		int l;

		if((p = next()) != null) {
			return p;
		} else if((l = basis.getLength()) < basis.getAllMaxLength()) {
			return new PermutationInclementor(tovalue(l + 1),
					basis.getMaxLength());
		} else {
			return null;
		}
	}

	//
	private void _pick(TinySortedBag<MyChar> b, String s) {
		if(basis != null) {
			if(value < 0) {
				b.add(MyChar.valueOf(s.charAt(s.length() + value)));
			} else {
				b.add(MyChar.valueOf(s.charAt(value)));
			}
			basis._pick(b, s);
		}
	}

	/**
	 * 
	 * @param s
	 * @return
	 */
	public Keysig<MyChar> pickCharacter(String s) {
		TinySortedBag<MyChar> b = new TinySortedBag<MyChar>();

		_pick(b, s);
		return new Keysig<MyChar>(b, s.length());
	}

	//
	private MyChar _getb(String s, int l) {
		int x;

		x = s.charAt(l >> 1);
		x = l % 2 == 0 ? x >> 8 : x & 0xff; 
		return MyChar.valueOf((char)x);
	}

	//
	private void _pickbyte(TinySortedBag<MyChar> b, String s) {
		if(basis != null) {
			if(value < 0) {
				b.add(_getb(s, s.length() + value));
			} else {
				b.add(_getb(s, s.charAt(value)));
			}
			basis._pick(b, s);
		}
	}

	/**
	 * 
	 * @param s
	 * @return
	 */
	public Keysig<MyChar> pickByte(String s) {
		TinySortedBag<MyChar> b = new TinySortedBag<MyChar>();

		_pickbyte(b, s);
		return new Keysig<MyChar>(b, s.length() * 2);
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Integer> iterator() {
		final Stack<Integer> st = new Stack<Integer>();
		PermutationInclementor p = this;

		for(; p.basis != null; p = p.basis)  st.push(p.value);
		return new Iterator<Integer>() {

			@Override
			public boolean hasNext() {
				return st.size() > 0;
			}

			@Override
			public Integer next() {
				return st.pop();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}

		};
	}

	/**
	 * 
	 */
	public PermutationInclementor clone() {
		PermutationInclementor p;

		p = new PermutationInclementor();
		p.basis  = basis;
		p.length = length;
		p.value  = value;
		return p;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String s;

		if(basis == null) {
			return "";
		} else if((s = basis.toString()).length() > 0) {
			return s + "," + value;
		} else {
			return s + value;
		}
	}

}
