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

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Map;

import net.morilib.parfait.core.PerfectHash;

public class JavaExecuteHashFormatter extends JavaHashFormatter {

	/* (non-Javadoc)
	 * @see net.morilib.parfait.translate.HashFormatter#print(java.io.PrintWriter, net.morilib.parfait.translate.HashFormatter, java.lang.String, boolean, boolean, java.lang.String, java.util.Map, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean print(PrintWriter wr,
			HashFormatter hf, String columns, boolean pluslen,
			boolean ignoreCase, String name, Map<String, String> map,
			String defaultAction, String license, String prologue,
			String desc, String aux, String type) {
		PerfectHash p;

		if((p = JavaHashFormatterUtils.gethash(
				map.keySet(), columns, pluslen, ignoreCase)) == null) {
			return false;
		}
		JavaHashFormatterUtils.printLicense(wr, license);
		JavaHashFormatterUtils.printPrologue(wr, prologue);
		JavaHashFormatterUtils.printDescription(wr, desc);
		JavaHashFormatterUtils.printClassDefinition(wr, name);
		wr.println(JavaHashFormatterUtils.REPLACE_START);
		JavaHashFormatterUtils.printEnum(wr, p);
		JavaHashFormatterUtils.printAssoValues(wr, p);
		JavaHashFormatterUtils.printWordlist(wr, p, map.keySet());
		JavaHashFormatterUtils.printHashFunction(wr, p);
		JavaHashFormatterUtils.printLookupFunction(wr, p);
		JavaHashFormatterUtils.printExecuteFunction(wr, p, type, map,
				defaultAction);
		JavaHashFormatterUtils.printValidateFunction(wr, p);
		wr.println(JavaHashFormatterUtils.REPLACE_END);
		JavaHashFormatterUtils.printAuxiliary(wr, aux);
		JavaHashFormatterUtils.printClassEpilogue(wr);
		return true;
	}

	/* (non-Javadoc)
	 * @see net.morilib.parfait.translate.HashFormatter#replace(java.io.PrintWriter, java.io.BufferedReader, net.morilib.parfait.translate.HashFormatter, java.lang.String, boolean, boolean, java.util.Map, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean replace(PrintWriter wr, BufferedReader rd,
			HashFormatter hf, String columns, boolean pluslen,
			boolean ignoreCase, String name, Map<String, String> map,
			String defaultAction, String type) {
		PerfectHash p;

		if((p = JavaHashFormatterUtils.gethash(
				map.keySet(), columns, pluslen, ignoreCase)) == null) {
			return false;
		}
		JavaHashFormatterUtils.printPrologue(wr, rd);
		JavaHashFormatterUtils.printEnum(wr, p);
		JavaHashFormatterUtils.printAssoValues(wr, p);
		JavaHashFormatterUtils.printWordlist(wr, p, map.keySet());
		JavaHashFormatterUtils.printHashFunction(wr, p);
		JavaHashFormatterUtils.printLookupFunction(wr, p);
		JavaHashFormatterUtils.printExecuteFunction(wr, p, type, map,
				defaultAction);
		JavaHashFormatterUtils.printValidateFunction(wr, p);
		JavaHashFormatterUtils.skipToReplace(wr, rd);
		JavaHashFormatterUtils.printEpilogue(wr, rd);
		return true;
	}

}
