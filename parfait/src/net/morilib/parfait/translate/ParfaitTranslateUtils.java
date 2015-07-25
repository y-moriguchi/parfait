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
package net.morilib.parfait.translate;

import java.util.Collection;

import net.morilib.parfait.core.PerfectHash;
import net.morilib.parfait.core.PermutationInclementor;

public class ParfaitTranslateUtils {

	//
	static PerfectHash gethash(Collection<String> it,
			String columns, boolean pluslen, boolean ic) {
		PermutationInclementor i;
		int x = -1;

		if(columns != null) {
			for(String s : it) {
				x = x < s.length() ? s.length() : x;
			}
			i = PermutationInclementor.newInstance(columns, x);
			return PerfectHash.chooseKeys(1, i, pluslen, ic, it);
		} else {
			return PerfectHash.chooseKeys(1, ic, it);
		}
	}

}
