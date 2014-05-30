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
package net.morilib.parfait.misc;

public class MyChar implements Comparable<MyChar> {

	//
	private char value;

	//
	private MyChar(char v) {
		value = v;
	}

	/**
	 * 
	 * @param c
	 * @return
	 */
	public static MyChar valueOf(char c) {
		return new MyChar(c);
	}

	/**
	 * 
	 * @return
	 */
	public char getChar() {
		return value;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isASCII() {
		return value < 128;
	}

	/**
	 * 
	 * @param a
	 * @return
	 */
	public int distance(MyChar a) {
		return Math.abs(value - a.value);
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(MyChar o) {
		return value > o.value ? 1 : value < o.value ? -1 : 0;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return (int)value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		return o instanceof MyChar && value == ((MyChar)o).value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return value + "(" + (int)value + ")";
	}

}
