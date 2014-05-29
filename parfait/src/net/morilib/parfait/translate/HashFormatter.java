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

public interface HashFormatter {

	/**
	 * 
	 * @param wr
	 * @param s
	 */
	public void printLicense(PrintWriter wr, String s);

	/**
	 * 
	 * @param wr
	 * @param s
	 */
	public void printPrologue(PrintWriter wr, String s);

	/**
	 * 
	 * @param wr
	 * @param s
	 */
	public void printDescription(PrintWriter wr, String s);

	/**
	 * 
	 * @param wr
	 * @param className
	 */
	public void printClassDefinition(PrintWriter wr, String className);

	/**
	 * 
	 * @param wr
	 * @param ph
	 */
	public void printEnum(PrintWriter wr, PerfectHash ph);

	/**
	 * 
	 * @param wr
	 * @param ph
	 */
	public void printAssoValues(PrintWriter wr, PerfectHash ph);

	/**
	 * 
	 * @param wr
	 * @param ph
	 * @param vs
	 */
	public void printWordlist(PrintWriter wr, PerfectHash ph,
			Iterable<String> vs);

	/**
	 * 
	 * @param wr
	 * @param ph
	 * @param vs
	 */
	public void printMappedWordlist(PrintWriter wr, PerfectHash ph,
			Map<String, String> vs);

	/**
	 * 
	 * @param wr
	 * @param ph
	 */
	public void printHashFunction(PrintWriter wr, PerfectHash ph);

	/**
	 * 
	 * @param wr
	 * @param ph
	 */
	public void printLookupFunction(PrintWriter wr, PerfectHash ph);

	/**
	 * 
	 * @param wr
	 * @param ph
	 * @param vs
	 * @param def
	 */
	public void printExecuteFunction(PrintWriter wr, PerfectHash ph,
			Map<String, String> vs, String def);

	/**
	 * 
	 * @param wr
	 */
	public void printMapFunction(PrintWriter wr, PerfectHash ph);

	/**
	 * 
	 * @param wr
	 * @param s
	 */
	public void printAuxiliary(PrintWriter wr, String s);

	/**
	 * 
	 * @param wr
	 */
	public void printClassEpilogue(PrintWriter wr);

	/**
	 * 
	 * @param s
	 * @return
	 */
	public String getTargetFilename(String s);

}
