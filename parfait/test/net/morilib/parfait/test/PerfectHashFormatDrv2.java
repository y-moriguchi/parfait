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
import net.morilib.parfait.translate.HashFormatter;
import net.morilib.parfait.translate.JavaHashFormatter;

public class PerfectHashFormatDrv2 {

	public static void main(String[] args) {
		PrintWriter wr;
		HashFormatter hf;
		PerfectHash p;
		Map<String, String> l;

		l = new LinkedHashMap<String, String>();
		l.put("睦月",   "return \"JAN\";");
		l.put("如月",   "return \"FEB\";");
		l.put("弥生",   "return \"MAR\";");
		l.put("卯月",   "return \"APR\";");
		l.put("皐月",   "return \"MAY\";");
		l.put("水無月", "return \"JUN\";");
		l.put("文月",   "return \"JUL\";");
		l.put("葉月",   "return \"AUG\";");
		l.put("長月",   "return \"SEP\";");
		l.put("神無月", "return \"OCT\";");
		l.put("霜月",   "return \"NOV\";");
		l.put("師走",   "return \"DEC\";");
		p = PerfectHash.chooseKeys(1, l.keySet());

		hf = new JavaHashFormatter();
		wr = new PrintWriter(new OutputStreamWriter(System.out), true);
		hf.printLicense(wr, "");
		hf.printPrologue(wr, "");
		hf.printClassDefinition(wr, "TestHash");
		hf.printEnum(wr, p);
		hf.printAssoValues(wr, p);
		hf.printWordlist(wr, p, l.keySet());
		hf.printHashFunction(wr, p);
		hf.printLookupFunction(wr, p);
		hf.printExecuteFunction(wr, p, l, null);
		hf.printClassEpilogue(wr);
	}

}