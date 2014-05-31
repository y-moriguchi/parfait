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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class JavaHashFormatter implements HashFormatter {

	//
	private static final Pattern P1 = Pattern.compile(
			"(.+)\\.[A-Za-z0-9]+");

	//
	private String cap(String s) {
		char[] a;

		a = s.toCharArray();
		a[0] = Character.toUpperCase(a[0]);
		return new String(a);
	}

	/* (non-Javadoc)
	 * @see net.morilib.parfait.translate.HashFormatter#getTargetFilename(java.lang.String)
	 */
	@Override
	public String getTargetFilename(String n) {
		Matcher m;

		if((m = P1.matcher(n)).matches()) {
			n = cap(m.group(1)) + ".java";
		} else {
			n = cap(n) + ".java";
		}
		return n;
	}

}
