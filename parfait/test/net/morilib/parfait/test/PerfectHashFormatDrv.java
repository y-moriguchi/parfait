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

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import net.morilib.parfait.core.PerfectHash;
import net.morilib.parfait.translate.JavaHashFormatterUtils;

public class PerfectHashFormatDrv {

	public static void main(String[] args) {
		PrintWriter wr;
		PerfectHash p;
		Map<String, String> l;

		l = new LinkedHashMap<String, String>();
		l.put("january",   "return \"JAN\";");
		l.put("february",  "return \"FEB\";");
		l.put("march",     "return \"MAR\";");
		l.put("april",     "return \"APR\";");
		l.put("may",       "return \"MAY\";");
		l.put("june",      "return \"JUN\";");
		l.put("july",      "return \"JUL\";");
		l.put("august",    "return \"AUG\";");
		l.put("september", "return \"SEP\";");
		l.put("october",   "return \"OCT\";");
		l.put("november",  "return \"NOV\";");
		l.put("december",  "return \"DEC\";");
		p = PerfectHash.chooseKeys(1, l.keySet());

		wr = new PrintWriter(new OutputStreamWriter(System.out), true);
		JavaHashFormatterUtils.printLicense(wr, "");
		JavaHashFormatterUtils.printPrologue(wr, "");
		JavaHashFormatterUtils.printClassDefinition(wr, "TestHash");
		JavaHashFormatterUtils.printEnum(wr, p);
		JavaHashFormatterUtils.printAssoValues(wr, p);
		JavaHashFormatterUtils.printWordlist(wr, p, l.keySet());
		JavaHashFormatterUtils.printHashFunction(wr, p);
		JavaHashFormatterUtils.printLookupFunction(wr, p);
		JavaHashFormatterUtils.printExecuteFunction(wr, p, l, null);
		JavaHashFormatterUtils.printClassEpilogue(wr);
	}

}
