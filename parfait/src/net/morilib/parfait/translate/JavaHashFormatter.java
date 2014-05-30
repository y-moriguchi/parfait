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
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.morilib.parfait.core.PerfectHash;
import net.morilib.parfait.core.PermutationInclementor;

public class JavaHashFormatter implements HashFormatter {

	//
	private static final Pattern RET = Pattern.compile(
			"(.*\n)?[ \t]*return[^\n]+");
	private static final Pattern P1 = Pattern.compile(
			"(.+)\\.[A-Za-z0-9]+");

	/* (non-Javadoc)
	 * @see net.morilib.parfait.translate.HashFormatter#printLicense(java.io.PrintWriter)
	 */
	@Override
	public void printLicense(PrintWriter wr, String s) {
		wr.println("/*");
		for(String t : s.split("\n")) {
			wr.printf(" * %s\n", t.trim());
		}
		wr.println(" */");
	}

	/* (non-Javadoc)
	 * @see net.morilib.parfait.translate.HashFormatter#printPrologue(java.io.PrintWriter)
	 */
	@Override
	public void printPrologue(PrintWriter wr, String s) {
		wr.println(s);
	}

	/* (non-Javadoc)
	 * @see net.morilib.parfait.translate.HashFormatter#printDescription(java.io.PrintWriter, java.lang.String)
	 */
	@Override
	public void printDescription(PrintWriter wr, String s) {
		wr.println("/**");
		for(String t : s.split("\n")) {
			wr.printf(" * %s\n", t.trim());
		}
		wr.println(" */");
	}

	/* (non-Javadoc)
	 * @see net.morilib.parfait.translate.HashFormatter#printClassDefintion(java.io.PrintWriter, java.lang.String)
	 */
	@Override
	public void printClassDefinition(PrintWriter wr, String className) {
		wr.printf("public final class %s {\n", className);
		wr.println();
		wr.printf("\tprivate %s () {}\n", className);
		wr.println();
	}

	/* (non-Javadoc)
	 * @see net.morilib.parfait.translate.HashFormatter#printEnum(java.io.PrintWriter, net.morilib.parfait.core.PerfectHash)
	 */
	@Override
	public void printEnum(PrintWriter wr, PerfectHash ph) {
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

	/* (non-Javadoc)
	 * @see net.morilib.parfait.translate.HashFormatter#printAssoValues(java.io.PrintWriter, net.morilib.parfait.core.PerfectHash)
	 */
	@Override
	public void printAssoValues(PrintWriter wr, PerfectHash ph) {
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

	/* (non-Javadoc)
	 * @see net.morilib.parfait.translate.HashFormatter#printWordlist(java.io.PrintWriter, net.morilib.parfait.core.PerfectHash, java.lang.Iterable)
	 */
	@Override
	public void printWordlist(PrintWriter wr, PerfectHash ph,
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

	/* (non-Javadoc)
	 * @see net.morilib.parfait.translate.HashFormatter#printMappedWordlist(java.io.PrintWriter, net.morilib.parfait.core.PerfectHash, java.util.Map)
	 */
	@Override
	public void printMappedWordlist(PrintWriter wr, PerfectHash ph,
			Map<String, String> vs) {
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

	/* (non-Javadoc)
	 * @see net.morilib.parfait.translate.HashFormatter#printHashFunction(java.io.PrintWriter, net.morilib.parfait.core.PerfectHash)
	 */
	@Override
	public void printHashFunction(PrintWriter wr, PerfectHash ph) {
		PermutationInclementor p = ph.getPermutation();
		String d = "\t\treturn (";
		int out = ph.getMaxHashValue() + 1;
		boolean uselen;

		wr.println("\tprivate static int _getasso(String s, int l) {");
		if(ph.isASCII()) {
			wr.println("\t\tint c = s.charAt(l);");
			wr.println();
			wr.printf("\t\treturn c < 0 || c > 127 ? %d : asso_values[c];\n", out);
		} else if(ph.isByte()) {
			wr.println("\t\tint c = s.charAt(l / 2);");
			wr.println();
			wr.println("\t\treturn asso_values[(l % 2 > 0 ? c & 0xff : c >> 8)];");
		} else {
			wr.println("\t\tint c = s.charAt(l);");
			wr.println();
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

	/* (non-Javadoc)
	 * @see net.morilib.parfait.translate.HashFormatter#printLookupFunction(java.io.PrintWriter, net.morilib.parfait.core.PerfectHash)
	 */
	@Override
	public void printLookupFunction(PrintWriter wr, PerfectHash ph) {
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

	/* (non-Javadoc)
	 * @see net.morilib.parfait.translate.HashFormatter#printExecuteFunction(java.io.PrintWriter, net.morilib.parfait.core.PerfectHash, java.util.Map)
	 */
	@Override
	public void printExecuteFunction(PrintWriter wr, PerfectHash ph,
			Map<String, String> vs, String def) {
		Map<Integer, String> m = new HashMap<Integer, String>();
		String d = def != null ? def : "// do nothing";

		for(Map.Entry<String, String> s : vs.entrySet()) {
			m.put(ph.hashCode(s.getKey()), s.getValue());
		}

		wr.println("\tpublic static void execute(String v) {");
		wr.println("\t\tint l;");
		wr.println();
		wr.println("\t\tif(v == null) {");
		wr.println("\t\t\t" + d);
		if(ph.isByte()) {
			wr.println("\t\t} else if((l = v.length() * 2) < MIN_WORD_LENGTH) {");
		} else {
			wr.println("\t\t} else if((l = v.length()) < MIN_WORD_LENGTH) {");
		}
		wr.println("\t\t\t" + d);
		wr.println("\t\t} else if(l > MAX_WORD_LENGTH) {");
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

	@Override
	public void printMapFunction(PrintWriter wr, PerfectHash ph) {
		wr.println("\tpublic static String lookup(String key) {");
		wr.println("\t\tint l = hashCode(key);");
		wr.println("\t\tString s;");
		wr.println();
		wr.println("\t\tif(l < MIN_HASH_VALUE || l > MAX_HASH_VALUE) {");
		wr.println("\t\t\treturn null;");
		wr.printf ("\t\t} else if((s = wordlist[l - %d]) == null) {\n",
				ph.getMinHashValue());
		wr.println("\t\t\treturn null;");
		wr.println("\t\t} else if(!s.equals(key)) {");
		wr.println("\t\t\treturn null;");
		wr.printf ("\t\t} else if((s = mapped_wordlist[l - %d]) != null) {\n",
				ph.getMinHashValue());
		wr.println("\t\t\treturn s;");
		wr.println("\t\t} else {");
		wr.println("\t\t\treturn null;");
		wr.println("\t\t}");
		wr.println("\t}");
		wr.println();
	}

	/* (non-Javadoc)
	 * @see net.morilib.parfait.translate.HashFormatter#printAuxiliary(java.io.PrintWriter, java.lang.String)
	 */
	@Override
	public void printAuxiliary(PrintWriter wr, String s) {
		for(String t : s.split("\n")) {
			wr.printf("\t%s\n", t.trim());
		}
	}

	/* (non-Javadoc)
	 * @see net.morilib.parfait.translate.HashFormatter#printClassEpilogue(java.io.PrintWriter)
	 */
	@Override
	public void printClassEpilogue(PrintWriter wr) {
		wr.println("\tpublic static boolean isVaild(String l) {");
		wr.println("\t\tString s;");
		wr.println("\t\tint x;");
		wr.println();
		wr.println("\t\treturn (l != null &&");
		wr.println("\t\t\t\t(x = hashCode(l)) <= MAX_HASH_VALUE &&");
		wr.println("\t\t\t\t(s = wordlist[x]) != null &&");
		wr.println("\t\t\t\ts.equals(l));");
		wr.println("\t}");
		wr.println();
		wr.println("}");
	}

	//
	private String cap(String s) {
		char[] a;

		a = s.toCharArray();
		a[0] = Character.toUpperCase(a[0]);
		return new String(a);
	}

	/* (non-Javadoc)
	 * @see net.morilib.parfait.translate.HashFormatter#getTargetFilename(java.lang.String)
	 */
	@Override
	public String getTargetFilename(String n) {
		Matcher m;

		if((m = P1.matcher(n)).matches()) {
			n = cap(m.group(1)) + ".java";
		} else {
			n = cap(n) + ".java";
		}
		return n;
	}

}
