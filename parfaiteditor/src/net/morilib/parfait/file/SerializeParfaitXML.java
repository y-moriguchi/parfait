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
package net.morilib.parfait.file;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;

import net.morilib.parfait.editor.OptionsEditor;
import net.morilib.parfait.editor.ParfaitPageEditor;

public final class SerializeParfaitXML {

	//
	private PrintWriter pw;
	private ParfaitPageEditor editor;

	/**
	 * 
	 * @param ous
	 * @param model
	 */
	SerializeParfaitXML(PrintWriter pw, ParfaitPageEditor p) {
		this.pw = pw;
		editor = p;
	}

	//
	static String esc(String s) {
		StringBuffer b = new StringBuffer();

		for(int k = 0; k < s.length(); k++) {
			if(s.charAt(k) > 0xff) {
				b.append(String.format("&#%d;", (int)s.charAt(k)));
			} else {
				switch(s.charAt(k)) {
				case '"':  b.append("&quot;");  break;
				case '&':  b.append("&amp;");  break;
				case '<':  b.append("&lt;");  break;
				case '>':  b.append("&gt;");  break;
				default:   b.append(s.charAt(k));  break;
				}
			}
		}
		return b.toString();
	}

	/**
	 * 
	 * @param ous
	 * @param model
	 */
	public void write() {
//		DescriptionEditor de = editor.getDescription();
//		AuxiliaryCodeEditor ae = editor.getAuxiliary();
		OptionsEditor oe = editor.getOptions();

		pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		pw.println("<parfait>");

		pw.printf("<package>%s</package>\n", esc(oe.getPackage()));
		pw.printf("<target-language>%s</target-language>\n",
				esc(oe.getLanguage()));
		pw.printf("<function-type>%s</function-type>\n",
				esc(oe.getType()));
		pw.printf("<return-type>%s</return-type>\n",
				esc(oe.getReturnType()));
		pw.printf("<default-action>%s</default-action>\n",
				esc(oe.getDefaultAction()));
		pw.printf("<inject>%s</inject>\n",
				oe.isInject() ? "true" : "false");
		pw.printf("<create>%s</create>\n",
				oe.isCreate() ? "true" : "false");
		pw.printf("<ignore-case>%s</ignore-case>\n",
				oe.isIgnoreCase() ? "true" : "false");
		pw.printf("<test-case>%s</test-case>\n",
				oe.isTestCase() ? "true" : "false");

		pw.println("<compute-hash>");
		pw.printf("<auto>%s</auto>\n",
				oe.isAutomatically() ? "true" : "false");
		pw.printf("<columns>%s</columns>\n", esc(oe.getColumns()));
		pw.printf("<pluslength>%s</pluslength>\n",
				oe.isPlusLength() ? "true" : "false");
		pw.println("</compute-hash>");

		pw.println("<keywords>");
		for(KeywordBean t : editor.getKeywords().getKeywordList()) {
			pw.println("<keyword>");
			pw.printf("<name>%s</name>\n", esc(t.getKeyword()));
			pw.printf("<action>%s</action>\n", esc(t.getAction()));
			pw.println("</keyword>");
		}
		pw.println("</keywords>");

//		pw.println("<description>");
//		pw.printf("<license>%s</license>\n",
//				esc(de.getLicense()));
//		pw.printf("<description>%s</description>\n",
//				esc(de.getDescription()));
//		pw.println("</description>");

//		pw.println("<auxiliary>");
//		pw.printf("<definition>%s</definition>\n",
//				esc(ae.getDefinition()));
//		pw.printf("<auxiliary>%s</auxiliary>\n",
//				esc(ae.getAuxiliary()));
//		pw.println("</auxiliary>");

		pw.println("</parfait>");
		pw.flush();
	}

	/**
	 * 
	 * @param ous
	 * @param model
	 * @param definition
	 * @param action
	 */
	public static void write(OutputStream ous, ParfaitPageEditor p) {
		new SerializeParfaitXML(
				new PrintWriter(new OutputStreamWriter(ous)),
				p).write();
	}

	/**
	 * 
	 * @return
	 */
	public static String newfile(String s) {
		StringWriter sw = new StringWriter();
		PrintWriter  pw = new PrintWriter(sw);

		pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		pw.println("<parfait>");
		pw.printf("<package>%s</package>\n", esc(s));
		pw.println("</parfait>");
		pw.flush();
		return sw.toString();
	}

}
