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

import net.morilib.parfait.core.Keysig;
import net.morilib.parfait.core.KeysigStatistics;
import net.morilib.parfait.core.MyCharacterSet;

public class KeysigDrv {

	public static void main(String[] args) {
		KeysigStatistics<Character> s;
		MyCharacterSet<Character> m;
		List<Keysig<Character>> l;

		l = new ArrayList<Keysig<Character>>();
		l.add(Keysig.newInstance("an", 0, false));
		l.add(Keysig.newInstance("eb", 0, false));
		l.add(Keysig.newInstance("ar", 0, false));
		l.add(Keysig.newInstance("pr", 0, false));
		l.add(Keysig.newInstance("ay", 0, false));
		l.add(Keysig.newInstance("un", 0, false));
		l.add(Keysig.newInstance("ul", 0, false));
		l.add(Keysig.newInstance("ug", 0, false));
		l.add(Keysig.newInstance("ep", 0, false));
		l.add(Keysig.newInstance("ct", 0, false));
		l.add(Keysig.newInstance("ov", 0, false));
		l.add(Keysig.newInstance("ec", 0, false));
		m = new MyCharacterSet<Character>(l);
		s = KeysigStatistics.compute(m, l, 1, false, false);
		s.report(System.out);

		for(Keysig<Character> k : l) {
			System.out.printf("%10s %7d\n", k, s.hashCode(k, false));
		}
	}

}
