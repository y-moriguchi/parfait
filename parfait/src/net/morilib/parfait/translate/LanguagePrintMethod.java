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

/**
 * 
 */
public interface LanguagePrintMethod {

	public String getTargetFilename(String n);

	public void printLicense(PrintWriter wr, String s);

	public void printPrologue(PrintWriter wr, String s);

	public void printDescription(PrintWriter wr, String s);

	public void printClassDefinition(PrintWriter wr,
			String className);

	public void printEnumDefinition(PrintWriter wr,
			String className);

	public void printEnumList(PrintWriter wr, PerfectHash ph,
			Map<String, String> vs);

	public void printEnum(PrintWriter wr, PerfectHash ph);

	public void printAssoValues(PrintWriter wr,
			PerfectHash ph);

	public void printWordlist(PrintWriter wr, PerfectHash ph,
			Iterable<String> vs);

	public void printMappedWordlist(PrintWriter wr,
			PerfectHash ph, Map<String, String> vs, String type);

	public void printEnumMap(PrintWriter wr, PerfectHash ph,
			String enumName, Map<String, String> vs);

	public void printHashFunction(PrintWriter wr,
			PerfectHash ph);

	public void printLookupFunction(PrintWriter wr,
			PerfectHash ph);

	public void printExecuteFunction(PrintWriter wr,
			PerfectHash ph, String type, Map<String, String> vs,
			String def);

	public void printMapFunction(PrintWriter wr, PerfectHash ph,
			String type);

	public void printAuxiliary(PrintWriter wr, String s);

	public void printValidateFunction(PrintWriter wr,
			PerfectHash ph);

	public void printClassEpilogue(PrintWriter wr);

	public void printTestCaseDefinition(PrintWriter wr,
			String className);

	public void printMapTestCase(PrintWriter wr,
			String className, Map<String, String> vs);

	public void printValidateTestCase(PrintWriter wr,
			String className, Map<String, String> vs);

	public void printTestCaseEpilogue(PrintWriter wr);

	/**
	 * 
	 * @param wr
	 * @param rd
	 */
	public void printPrologue(PrintWriter wr, BufferedReader rd);

	/**
	 * 
	 * @param wr
	 * @param rd
	 */
	public void skipToReplace(PrintWriter wr, BufferedReader rd);

	/**
	 * 
	 * @param wr
	 * @param rd
	 */
	public void printEpilogue(PrintWriter wr, BufferedReader rd);

	/**
	 * 
	 * @param wr
	 */
	public void printReplaceStart(PrintWriter wr);

	/**
	 * 
	 * @param wr
	 */
	public void printReplaceEnd(PrintWriter wr);

	/**
	 * 
	 * @param wr
	 * @param name
	 */
	public void printPackagePrologue(PrintWriter wr, String name);

	/**
	 * 
	 * @param wr
	 * @param name
	 */
	public void printPackageEpilogue(PrintWriter wr, String name);

}
