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

import java.util.ArrayList;
import java.util.List;

import net.morilib.parfait.core.PerfectHash;

public class PerfectHashDrv {

	public static void main(String[] args) {
		PerfectHash p;
		List<String> l;

		l = new ArrayList<String>();
		l.add("january");
		l.add("february");
		l.add("march");
		l.add("april");
		l.add("may");
		l.add("june");
		l.add("july");
		l.add("august");
		l.add("september");
		l.add("october");
		l.add("november");
		l.add("december");
		p = PerfectHash.chooseKeys(1, false, l);
		System.out.println(p.getPermutation());
		for(String s : l)  System.out.println(p.hashCode(s));
	}

}
