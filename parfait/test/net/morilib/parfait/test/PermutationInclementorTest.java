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
package net.morilib.parfait.test;

import junit.framework.TestCase;
import net.morilib.parfait.core.PermutationInclementor;

public class PermutationInclementorTest extends TestCase {

	static PermutationInclementor eq(PermutationInclementor p,
			String s) {
		assertEquals(s, p.toString());
		return p.next();
	}

	static PermutationInclementor ea(PermutationInclementor p,
			String s) {
		assertEquals(s, p.toString());
		return p.nextAll();
	}

	public void test0001() {
		PermutationInclementor p;

		p = new PermutationInclementor(1, 3);
		p = eq(p, "0,-1,1");
		p = eq(p, "0,-1,-2");
		p = eq(p, "0,-1,2");
		p = eq(p, "0,-1,-3");
		p = eq(p, "0,1,-2");
		p = eq(p, "0,1,2");
		p = eq(p, "0,1,-3");
		p = eq(p, "0,-2,2");
		p = eq(p, "0,-2,-3");
		p = eq(p, "0,2,-3");
		p = eq(p, "-1,1,-2");
		p = eq(p, "-1,1,2");
		p = eq(p, "-1,1,-3");
		p = eq(p, "-1,-2,2");
		p = eq(p, "-1,-2,-3");
		p = eq(p, "-1,2,-3");
		p = eq(p, "1,-2,2");
		p = eq(p, "1,-2,-3");
		p = eq(p, "1,2,-3");
		p = eq(p, "-2,2,-3");
		assertNull(p);
	}

	public void test0002() {
		PermutationInclementor p;

		p = new PermutationInclementor(0, 3);
		p = ea(p, "0");
		p = ea(p, "-1");
		p = ea(p, "1");
		p = ea(p, "-2");
		p = ea(p, "2");
		p = ea(p, "-3");
		p = ea(p, "0,-1");
		p = ea(p, "0,1");
		p = ea(p, "0,-2");
		p = ea(p, "0,2");
		p = ea(p, "0,-3");
		p = ea(p, "-1,1");
		p = ea(p, "-1,-2");
		p = ea(p, "-1,2");
		p = ea(p, "-1,-3");
		p = ea(p, "1,-2");
		p = ea(p, "1,2");
		p = ea(p, "1,-3");
		p = ea(p, "-2,2");
		p = ea(p, "-2,-3");
		p = ea(p, "2,-3");
		p = ea(p, "0,-1,1");
		p = ea(p, "0,-1,-2");
		p = ea(p, "0,-1,2");
		p = ea(p, "0,-1,-3");
		p = ea(p, "0,1,-2");
		p = ea(p, "0,1,2");
		p = ea(p, "0,1,-3");
		p = ea(p, "0,-2,2");
		p = ea(p, "0,-2,-3");
		p = ea(p, "0,2,-3");
		p = ea(p, "-1,1,-2");
		p = ea(p, "-1,1,2");
		p = ea(p, "-1,1,-3");
		p = ea(p, "-1,-2,2");
		p = ea(p, "-1,-2,-3");
		p = ea(p, "-1,2,-3");
		p = ea(p, "1,-2,2");
		p = ea(p, "1,-2,-3");
		p = ea(p, "1,2,-3");
		p = ea(p, "-2,2,-3");
		p = ea(p, "0,-1,1,-2");
		p = ea(p, "0,-1,1,2");
		p = ea(p, "0,-1,1,-3");
		p = ea(p, "0,-1,-2,2");
		p = ea(p, "0,-1,-2,-3");
		p = ea(p, "0,-1,2,-3");
		p = ea(p, "0,1,-2,2");
		p = ea(p, "0,1,-2,-3");
		p = ea(p, "0,1,2,-3");
		p = ea(p, "0,-2,2,-3");
		p = ea(p, "-1,1,-2,2");
		p = ea(p, "-1,1,-2,-3");
		p = ea(p, "-1,1,2,-3");
		p = ea(p, "-1,-2,2,-3");
		p = ea(p, "1,-2,2,-3");
		p = ea(p, "0,-1,1,-2,2");
		p = ea(p, "0,-1,1,-2,-3");
		p = ea(p, "0,-1,1,2,-3");
		p = ea(p, "0,-1,-2,2,-3");
		p = ea(p, "0,1,-2,2,-3");
		p = ea(p, "-1,1,-2,2,-3");
		p = ea(p, "0,-1,1,-2,2,-3");
		assertNull(p);
	}

}
