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

public class JavaVaildateTestFormatter extends JavaTestFormatter {

	/* (non-Javadoc)
	 * @see net.morilib.parfait.translate.HashFormatter#print(java.io.PrintWriter, net.morilib.parfait.translate.HashFormatter, java.lang.String, boolean, boolean, java.lang.String, java.util.Map, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean print(PrintWriter wr,
			HashFormatter hf, String columns, boolean pluslen,
			boolean ignoreCase, String name, Map<String, String> map,
			String defaultAction, String license, String prologue,
			String desc, String aux, String type) {
		JavaHashFormatterUtils.printLicense(wr, license);
		JavaHashFormatterUtils.printPrologue(wr, prologue);
		JavaHashFormatterUtils.printTestCaseDefinition(wr, name);
		JavaHashFormatterUtils.printValidateTestCase(wr, name, map);
		JavaHashFormatterUtils.printTestCaseEpilogue(wr);
		return true;
	}

}
