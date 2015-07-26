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

public class ExecuteHashFormatter implements HashFormatter {

	/* (non-Javadoc)
	 * @see net.morilib.parfait.translate.HashFormatter#print(java.io.PrintWriter, net.morilib.parfait.translate.HashFormatter, java.lang.String, boolean, boolean, java.lang.String, java.util.Map, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean print(PrintWriter wr, LanguagePrintMethod lang,
			String pkg, String columns, boolean pluslen,
			boolean ignoreCase, String name, Map<String, String> map,
			String defaultAction, String license, String prologue,
			String desc, String aux, String type) {
		PerfectHash p;

		if((p = ParfaitTranslateUtils.gethash(
				map.keySet(), columns, pluslen, ignoreCase)) == null) {
			return false;
		}
		lang.printLicense(wr, license);
		lang.printPackagePrologue(wr, pkg);
		lang.printPrologue(wr, prologue);
		lang.printDescription(wr, desc);
		lang.printClassDefinition(wr, name);
		lang.printReplaceStart(wr);
		lang.printEnum(wr, p);
		lang.printAssoValues(wr, p);
		lang.printWordlist(wr, p, map.keySet());
		lang.printHashFunction(wr, p);
		lang.printLookupFunction(wr, p);
		lang.printExecuteFunction(wr, p, type, map,
				defaultAction);
		lang.printValidateFunction(wr, p);
		lang.printReplaceEnd(wr);
		lang.printAuxiliary(wr, aux);
		lang.printClassEpilogue(wr);
		lang.printPackageEpilogue(wr, pkg);
		return true;
	}

	/* (non-Javadoc)
	 * @see net.morilib.parfait.translate.HashFormatter#replace(java.io.PrintWriter, java.io.BufferedReader, net.morilib.parfait.translate.HashFormatter, java.lang.String, boolean, boolean, java.util.Map, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean replace(PrintWriter wr, BufferedReader rd,
			LanguagePrintMethod lang, String columns,
			boolean pluslen, boolean ignoreCase, String name,
			Map<String, String> map, String defaultAction, String type) {
		PerfectHash p;

		if((p = ParfaitTranslateUtils.gethash(
				map.keySet(), columns, pluslen, ignoreCase)) == null) {
			return false;
		}
		lang.printPrologue(wr, rd);
		lang.printEnum(wr, p);
		lang.printAssoValues(wr, p);
		lang.printWordlist(wr, p, map.keySet());
		lang.printHashFunction(wr, p);
		lang.printLookupFunction(wr, p);
		lang.printExecuteFunction(wr, p, type, map,
				defaultAction);
		lang.printValidateFunction(wr, p);
		lang.skipToReplace(wr, rd);
		lang.printEpilogue(wr, rd);
		return true;
	}

}
