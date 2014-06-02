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
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import net.morilib.parfait.core.PerfectHash;
import net.morilib.parfait.core.PermutationInclementor;

public final class JavaHashFormatterUtils {

	//
	private static final Pattern RET = Pattern.compile(
			"(.*\n)?[ \t]*return[^\n]+");

	//
	private JavaHashFormatterUtils() {}

	//
	static PerfectHash gethash(Collection<String> it,
			String columns, boolean pluslen, boolean ic) {
		PermutationInclementor i;
		int x = -1;

		if(columns != null) {
			for(String s : it) {
				x = x < s.length() ? s.length() : x;
			}
			i = PermutationInclementor.newInstance(columns, x);
			return PerfectHash.chooseKeys(1, i, pluslen, ic, it);
		} else {
			return PerfectHash.chooseKeys(1, ic, it);
		}
	}

	public static void printLicense(PrintWriter wr, String s) {
		wr.println("/*");
		for(String t : s.split("\n")) {
			wr.printf(" * %s\n", t.trim());
		}
		wr.println(" */");
	}

	public static void printPrologue(PrintWriter wr, String s) {
		wr.println(s);
	}

	public static void printDescription(PrintWriter wr, String s) {
		wr.println("/**");
		for(String t : s.split("\n")) {
			wr.printf(" * %s\n", t.trim());
		}
		wr.println(" */");
	}

	public static void printClassDefinition(PrintWriter wr,
			String className) {
		wr.printf("public final class %s {\n", className);
		wr.println();
		wr.printf("\tprivate %s () {}\n", className);
		wr.println();
	}

	public static void printEnumDefinition(PrintWriter wr,
			String className) {
		wr.printf("public enum %s {\n", className);
		wr.println();
	}

	public static void printEnumList(PrintWriter wr, PerfectHash ph,
			Map<String, String> vs) {
		for(Map.Entry<String, String> v : vs.entrySet()) {
			wr.printf("\t%s,\n", v.getValue());
		}
		wr.println("\t;");
		wr.println();
	}

	public static void printEnum(PrintWriter wr, PerfectHash ph) {
		wr.printf("\tpublic static final int TOTAL_KEYWORDS = %d;\n",
				ph.getTotalKeywords());
		wr.printf("\tpublic static final int MIN_WORD_LENGTH = %d;\n",
				ph.getMinWordLength());
		wr.printf("\tpublic static final int MAX_WORD_LENGTH = %d;\n",
				ph.getMaxWordLength());
		wr.printf("\tpublic static final int MIN_HASH_VALUE = %d;\n",
				ph.getMinHashValue());
		wr.printf("\tpublic static final int MAX_HASH_VALUE = %d;\n",
				ph.getMaxHashValue());
		wr.printf("\tpublic static final int HASH_VALUE_RANGE = %d;\n",
				ph.getHashValueRange());
		wr.printf("\tpublic static final int DUPLICATES = %d;\n",
				ph.getDuplicates());
		wr.println();
	}

	public static void printAssoValues(PrintWriter wr,
			PerfectHash ph) {
		String d = "";

		wr.print("\tprivate static int[] asso_values = new int[] {");
		for(int k = ph.getMinimum(); k <= ph.getMaximum(); k++) {
			wr.printf(k % 10 > 0 ? "%s %3d" : "%s\n\t\t%3d", d,
					ph.getAssoValue(k));
			d = ",";
		}
		wr.println("\n\t};");
		wr.println();
	}

	//
	private static String esc(String s) {
		String t = s;

		t = t.replaceAll("\\\\", "\\\\\\\\");
		t = t.replaceAll("\"", "\\\\\"");
		return t;
	}

	public static void printWordlist(PrintWriter wr, PerfectHash ph,
			Iterable<String> vs) {
		Map<Integer, String> m = new HashMap<Integer, String>();

		for(String s : vs)  m.put(ph.hashCode(s), esc(s));
		wr.println("\tprivate static String[] wordlist = new String[] {");
		for(int k = ph.getMinHashValue(); k <= ph.getMaxHashValue(); k++) {
			if(m.containsKey(k)) {
				wr.printf("\t\t\"%s\",\n", m.get(k));
			} else {
				wr.printf("\t\tnull,\n");
			}
		}
		wr.println("\n\t};");
		wr.println();
	}

	public static void printMappedWordlist(PrintWriter wr,
			PerfectHash ph, Map<String, String> vs) {
		Map<Integer, String> m = new HashMap<Integer, String>();

		for(Map.Entry<String, String> s : vs.entrySet()) {
			m.put(ph.hashCode(s.getKey()), esc(s.getValue()));
		}

		wr.println("\tprivate static String[] mapped_wordlist = new String[] {");
		for(int k = ph.getMinHashValue(); k <= ph.getMaxHashValue(); k++) {
			if(m.containsKey(k)) {
				wr.printf("\t\t\"%s\",\n", m.get(k));
			} else {
				wr.printf("\t\tnull,\n");
			}
		}
		wr.println("\n\t};");
		wr.println();
	}

	public static void printEnumMap(PrintWriter wr, PerfectHash ph,
			String enumName, Map<String, String> vs) {
		Map<Integer, String> m = new HashMap<Integer, String>();

		for(Map.Entry<String, String> s : vs.entrySet()) {
			m.put(ph.hashCode(s.getKey()), esc(s.getValue()));
		}

		wr.printf("\tprivate static %s[] mapped_wordlist = new %s[] {\n",
				enumName, enumName);
		for(int k = ph.getMinHashValue(); k <= ph.getMaxHashValue(); k++) {
			if(m.containsKey(k)) {
				wr.printf("\t\t%s,\n", m.get(k));
			} else {
				wr.printf("\t\tnull,\n");
			}
		}
		wr.println("\n\t};");
		wr.println();
	}

	public static void printHashFunction(PrintWriter wr,
			PerfectHash ph) {
		PermutationInclementor p = ph.getPermutation();
		String d = "\t\treturn (";
		int out = ph.getMaxHashValue() + 1;
		boolean uselen;

		wr.println("\tprivate static int _getasso(String s, int l) {");
		if(ph.isASCII()) {
			wr.println("\t\tint c = s.charAt(l);");
			wr.println();
			if(ph.isIgnoreCase()) {
				wr.println("\t\tc = Character.toUpperCase((char)c);");
			}
			wr.printf("\t\treturn c < 0 || c > 127 ? %d : asso_values[c];\n", out);
		} else if(ph.isByte()) {
			wr.println("\t\tint c = s.charAt(l / 2);");
			wr.println();
			if(ph.isIgnoreCase()) {
				wr.println("\t\tc = Character.toUpperCase((char)c);");
			}
			wr.println("\t\treturn asso_values[(l % 2 > 0 ? c & 0xff : c >> 8)];");
		} else {
			wr.println("\t\tint c = s.charAt(l);");
			wr.println();
			if(ph.isIgnoreCase()) {
				wr.println("\t\tc = Character.toUpperCase((char)c);");
			}
			wr.printf("\t\treturn c < %d || c > %d ? %d : asso_values[c - %d];\n",
					ph.getMinimum(), ph.getMaximum(), out, ph.getMinimum());
		}
		wr.println("\t}");

		wr.println("\n\tpublic static int hashCode(String s) {");
		wr.println("\t\tint l;");
		wr.println();
		wr.printf("\t\tif(s == null)  return %d;\n", out);

		uselen = ph.isAddLength();
		if(!uselen) {
			for(int k : p) {
				if(k < 0) {
					uselen = true;
					break;
				}
			}
		}

		if(!uselen) {
			// do nothing
		} else if(ph.isByte()) {
			wr.println("\t\tl = s.length() * 2;");
		} else {
			wr.println("\t\tl = s.length();");
		}

		for(int k : p) {
			if(k < 0) {
				wr.printf("%s_getasso(s, l - %d)", d, -k);
			} else {
				wr.printf("%s_getasso(s, %d)", d, k);
			}
			d = " +\n\t\t\t\t";
		}

		if(ph.isAddLength()) {
			wr.println(" + l);\n\t}");
		} else {
			wr.println(");\n\t}");
		}
		wr.println();
	}

	public static void printLookupFunction(PrintWriter wr,
			PerfectHash ph) {
		wr.println("\tpublic static String lookup(int l) {");
		wr.println("\t\tString s;");
		wr.println();
		wr.println("\t\tif(l < MIN_HASH_VALUE || l > MAX_HASH_VALUE) {");
		wr.println("\t\t\treturn null;");
		wr.printf ("\t\t} else if((s = wordlist[l - %d]) != null) {\n",
				ph.getMinHashValue());
		wr.println("\t\t\treturn s;");
		wr.println("\t\t} else {");
		wr.println("\t\t\treturn null;");
		wr.println("\t\t}");
		wr.println("\t}");
		wr.println();
	}

	public static void printExecuteFunction(PrintWriter wr,
			PerfectHash ph, String type, Map<String, String> vs,
			String def) {
		Map<Integer, String> m = new HashMap<Integer, String>();
		String d = def != null ? def : "// do nothing";

		for(Map.Entry<String, String> s : vs.entrySet()) {
			m.put(ph.hashCode(s.getKey()), s.getValue());
		}

		wr.printf ("\tpublic static %s execute(String v) {\n", type);
		wr.println("\t\tString s;");
		wr.println("\t\tint l;");
		wr.println();
		wr.println("\t\tif(v == null) {");
		wr.println("\t\t\t" + d);
		wr.println("\t\t} else if((l = v.length()) < MIN_WORD_LENGTH) {");
		wr.println("\t\t\t" + d);
		wr.println("\t\t} else if(l > MAX_WORD_LENGTH) {");
		wr.println("\t\t\t" + d);
		wr.println("\t\t} else if((l = hashCode(v)) < MIN_HASH_VALUE) {");
		wr.println("\t\t\t" + d);
		wr.println("\t\t} else if(l > MAX_HASH_VALUE) {");
		wr.println("\t\t\t" + d);
		wr.printf ("\t\t} else if((s = wordlist[l - %d]) == null) {\n",
				ph.getMinHashValue());
		wr.println("\t\t\t" + d);
		if(ph.isIgnoreCase()) {
			wr.println("\t\t} else if(!s.equalsIgnoreCase(v)) {");
		} else {
			wr.println("\t\t} else if(!s.equals(v)) {");
		}
		wr.println("\t\t\t" + d);
		wr.println("\t\t} else {");
		wr.println("\t\t\tswitch(hashCode(v)) {");
		for(int k = ph.getMinHashValue(); k <= ph.getMaxHashValue(); k++) {
			if(m.containsKey(k)) {
				wr.printf("\t\t\tcase %d:\n", k);
				wr.printf("\t\t\t\t%s\n", m.get(k));
				if(!RET.matcher(m.get(k)).matches()) {
					wr.printf("\t\t\t\tbreak;\n");
				}
			}
		}
		wr.println("\t\t\tdefault:");
		wr.println("\t\t\t\t" + d);
		if(!RET.matcher(d).matches())  wr.println("\t\t\t\tbreak;");
		wr.println("\t\t\t}");
		wr.println("\t\t}");
		wr.println("\t}");
		wr.println();
	}

	public static void printMapFunction(PrintWriter wr, PerfectHash ph,
			String type) {
		wr.printf ("\tpublic static %s map(String key) {\n", type);
		wr.println("\t\tint l = hashCode(key);");
		wr.printf ("\t\tString s;\n");
		wr.printf ("\t\t%s r;\n", type);
		wr.println();
		wr.println("\t\tif(key == null) {");
		wr.println("\t\t\treturn null;");
		wr.println("\t\t} else if((l = key.length()) < MIN_WORD_LENGTH) {");
		wr.println("\t\t\treturn null;");
		wr.println("\t\t} else if(l > MAX_WORD_LENGTH) {");
		wr.println("\t\t\treturn null;");
		wr.println("\t\t} else if((l = hashCode(key)) < MIN_HASH_VALUE) {");
		wr.println("\t\t\treturn null;");
		wr.println("\t\t} else if(l > MAX_HASH_VALUE) {");
		wr.println("\t\t\treturn null;");
		wr.printf ("\t\t} else if((s = wordlist[l - %d]) == null) {\n",
				ph.getMinHashValue());
		wr.println("\t\t\treturn null;");
		if(ph.isIgnoreCase()) {
			wr.println("\t\t} else if(!s.equalsIgnoreCase(key)) {");
		} else {
			wr.println("\t\t} else if(!s.equals(key)) {");
		}
		wr.println("\t\t\treturn null;");
		wr.printf ("\t\t} else if((r = mapped_wordlist[l - %d]) != null) {\n",
				ph.getMinHashValue());
		wr.println("\t\t\treturn r;");
		wr.println("\t\t} else {");
		wr.println("\t\t\treturn null;");
		wr.println("\t\t}");
		wr.println("\t}");
		wr.println();
	}

	public static void printAuxiliary(PrintWriter wr, String s) {
		for(String t : s.split("\n")) {
			wr.printf("\t%s\n", t.trim());
		}
	}

	public static void printValidateFunction(PrintWriter wr,
			PerfectHash ph) {
		wr.println("\tpublic static boolean isValid(String l) {");
		wr.println("\t\tString s;");
		wr.println("\t\tint x;");
		wr.println();
		wr.println("\t\treturn (l != null &&");
		wr.println("\t\t\t\t(x = l.length()) <= MAX_WORD_LENGTH &&");
		wr.println("\t\t\t\tx >= MIN_WORD_LENGTH &&");
		wr.println("\t\t\t\t(x = hashCode(l)) <= MAX_HASH_VALUE &&");
		wr.println("\t\t\t\tx >= MIN_HASH_VALUE &&");
		wr.println("\t\t\t\t(s = wordlist[x - MIN_HASH_VALUE]) != null &&");
		if(ph.isIgnoreCase()) {
			wr.println("\t\t\t\ts.equalsIgnoreCase(l));");
		} else {
			wr.println("\t\t\t\ts.equals(l));");
		}
		wr.println("\t}");
		wr.println();
	}

	public static void printClassEpilogue(PrintWriter wr) {
		wr.println("}");
	}

	public static void printTestCaseDefinition(PrintWriter wr,
			String className) {
		wr.printf("public class %s extends junit.framework.TestCase {\n",
				className);
		wr.println();
	}

	public static void printMapTestCase(PrintWriter wr,
			String className, Map<String, String> vs) {
		String s, v;

		v = className.replaceFirst("Test$", "");
		wr.printf("\tpublic void test0001() {\n");
		for(Map.Entry<String, String> t : vs.entrySet()) {
			s = t.getKey();
			wr.printf("\t\tassertEquals(\"%s\", %s.map(\"%s\"));\n",
					t.getValue(), v, s);
			if(s.length() > 0 && !vs.containsKey(s + s.charAt(0))) {
				wr.printf("\t\tassertNull(%s.map(\"%s%c\"));\n",
						v, s, s.charAt(0));
			}

			if(s.length() > 0 && !vs.containsKey(s.charAt(0) + s)) {
				wr.printf("\t\tassertNull(%s.map(\"%c%s\"));\n",
						v, s.charAt(0), s);
			}

			if(s.length() > 0 && !vs.containsKey(
					s.charAt(0) + s + s.charAt(0))) {
				wr.printf("\t\tassertNull(%s.map(\"%c%s%c\"));\n",
						v, s.charAt(0), s, s.charAt(0));
			}

			if(s.length() > 0 && !vs.containsKey(s.substring(1))) {
				wr.printf("\t\tassertNull(%s.map(\"%s\"));\n",
						v, s.substring(1));
			}
		}
		wr.printf("\t}\n");
		wr.println();
	}

	public static void printValidateTestCase(PrintWriter wr,
			String className, Map<String, String> vs) {
		String s, v;

		v = className.replaceFirst("Test$", "");
		wr.printf("\tpublic void test0002() {\n");
		for(Map.Entry<String, String> t : vs.entrySet()) {
			s = t.getKey();
			wr.printf("\t\tassertTrue(%s.isValid(\"%s\"));\n",
					v, t.getKey());
			if(s.length() > 0 && !vs.containsKey(s + s.charAt(0))) {
				wr.printf("\t\tassertFalse(%s.isValid(\"%s%c\"));\n",
						v, s, s.charAt(0));
			}

			if(s.length() > 0 && !vs.containsKey(s.charAt(0) + s)) {
				wr.printf("\t\tassertFalse(%s.isValid(\"%c%s\"));\n",
						v, s.charAt(0), s);
			}

			if(s.length() > 0 && !vs.containsKey(
					s.charAt(0) + s + s.charAt(0))) {
				wr.printf("\t\tassertFalse(%s.isValid(\"%c%s%c\"));\n",
						v, s.charAt(0), s, s.charAt(0));
			}

			if(s.length() > 0 && !vs.containsKey(s.substring(1))) {
				wr.printf("\t\tassertFalse(%s.isValid(\"%s\"));\n",
						v, s.substring(1));
			}
		}
		wr.printf("\t}\n");
		wr.println();
	}

	public static void printTestCaseEpilogue(PrintWriter wr) {
		wr.println("}");
	}

}
