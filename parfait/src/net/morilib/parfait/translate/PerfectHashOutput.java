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
import java.util.Collection;
import java.util.Map;

import net.morilib.parfait.core.PerfectHash;
import net.morilib.parfait.core.PermutationInclementor;

public final class PerfectHashOutput {

	//
	private PerfectHashOutput() {}

	//
	private static PerfectHash gethash(Collection<String> it,
			String columns, boolean pluslen) {
		PermutationInclementor i;
		int x = -1;

		if(columns != null) {
			for(String s : it) {
				x = x < s.length() ? s.length() : x;
			}
			i = PermutationInclementor.newInstance(columns, x);
			return PerfectHash.chooseKeys(1, i, pluslen, it);
		} else {
			return PerfectHash.chooseKeys(1, it);
		}
	}

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
	public static boolean printExecute(PrintWriter wr,
			HashFormatter hf, String columns, boolean pluslen,
			String name, Map<String, String> map,
			String defaultAction, String license, String prologue,
			String desc, String aux) {
		PerfectHash p;

		if((p = gethash(map.keySet(), columns, pluslen)) == null) {
			return false;
		}
		hf.printLicense(wr, license);
		hf.printPrologue(wr, prologue);
		hf.printDescription(wr, desc);
		hf.printClassDefinition(wr, name);
		hf.printEnum(wr, p);
		hf.printAssoValues(wr, p);
		hf.printWordlist(wr, p, map.keySet());
		hf.printHashFunction(wr, p);
		hf.printLookupFunction(wr, p);
		hf.printExecuteFunction(wr, p, map, defaultAction);
		hf.printAuxiliary(wr, aux);
		hf.printClassEpilogue(wr);
		return true;
	}

	/**
	 * 
	 * @param wr
	 * @param hf
	 * @param name
	 * @param agg
	 * @param license
	 * @param prologue
	 */
	public static boolean printLookup(PrintWriter wr, HashFormatter hf,
			String columns, boolean pluslen, String name,
			Collection<String> agg, String license, String prologue,
			String desc, String aux) {
		PerfectHash p;

		if((p = gethash(agg, columns, pluslen)) == null) {
			return false;
		}
		hf.printLicense(wr, license);
		hf.printPrologue(wr, prologue);
		hf.printDescription(wr, desc);
		hf.printClassDefinition(wr, name);
		hf.printEnum(wr, p);
		hf.printAssoValues(wr, p);
		hf.printWordlist(wr, p, agg);
		hf.printHashFunction(wr, p);
		hf.printLookupFunction(wr, p);
		hf.printAuxiliary(wr, aux);
		hf.printClassEpilogue(wr);
		return true;
	}

	/**
	 * 
	 * @param wr
	 * @param hf
	 * @param name
	 * @param agg
	 * @param license
	 * @param prologue
	 */
	public static boolean printMap(PrintWriter wr, HashFormatter hf,
			String columns, boolean pluslen, String name,
			Map<String, String> map, String license, String prologue,
			String desc, String aux) {
		PerfectHash p;

		if((p = gethash(map.keySet(), columns, pluslen)) == null) {
			return false;
		}
		hf.printLicense(wr, license);
		hf.printPrologue(wr, prologue);
		hf.printDescription(wr, desc);
		hf.printClassDefinition(wr, name);
		hf.printEnum(wr, p);
		hf.printAssoValues(wr, p);
		hf.printWordlist(wr, p, map.keySet());
		hf.printMappedWordlist(wr, p, map);
		hf.printHashFunction(wr, p);
		hf.printLookupFunction(wr, p);
		hf.printMapFunction(wr, p);
		hf.printAuxiliary(wr, aux);
		hf.printClassEpilogue(wr);
		return true;
	}

}
