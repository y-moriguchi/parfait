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

import java.io.PrintWriter;
import java.util.Map;

import net.morilib.parfait.core.PerfectHash;

public class JavaEnumHashFormatter extends JavaHashFormatter {

	/**
	 * 
	 * @param wr
	 * @param hf
	 * @param name
	 * @param map
	 * @param defaultAction
	 * @param license
	 * @param prologue
	 * @return
	 */
	public boolean print(PrintWriter wr,
			HashFormatter hf, String columns, boolean pluslen,
			String name, Map<String, String> map,
			String defaultAction, String license, String prologue,
			String desc, String aux) {
		PerfectHash p;

		if((p = JavaHashFormatterUtils.gethash(
				map.keySet(), columns, pluslen)) == null) {
			return false;
		}
		JavaHashFormatterUtils.printLicense(wr, license);
		JavaHashFormatterUtils.printPrologue(wr, prologue);
		JavaHashFormatterUtils.printDescription(wr, desc);
		JavaHashFormatterUtils.printEnumDefinition(wr, name);
		JavaHashFormatterUtils.printEnumList(wr, p, map);
		JavaHashFormatterUtils.printEnum(wr, p);
		JavaHashFormatterUtils.printAssoValues(wr, p);
		JavaHashFormatterUtils.printWordlist(wr, p, map.keySet());
		JavaHashFormatterUtils.printEnumMap(wr, p, name, map);
		JavaHashFormatterUtils.printHashFunction(wr, p);
		JavaHashFormatterUtils.printLookupFunction(wr, p);
		JavaHashFormatterUtils.printMapFunction(wr, p, name);
		JavaHashFormatterUtils.printAuxiliary(wr, aux);
		JavaHashFormatterUtils.printClassEpilogue(wr);
		return true;
	}

}
