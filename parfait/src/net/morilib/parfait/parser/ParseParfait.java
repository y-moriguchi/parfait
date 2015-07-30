package net.morilib.parfait.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.morilib.parfait.translate.CSharpPrintMethod;
import net.morilib.parfait.translate.EnumHashFormatter;
import net.morilib.parfait.translate.ExecuteHashFormatter;
import net.morilib.parfait.translate.HashFormatter;
import net.morilib.parfait.translate.JavaPrintMethod;
import net.morilib.parfait.translate.LanguagePrintMethod;
import net.morilib.parfait.translate.MapHashFormatter;
import net.morilib.parfait.translate.ValidateHashFormatter;

/**
 * 
 */
public class ParseParfait {

	//
	private static final Pattern DET = Pattern.compile(
			"^%([A-Za-z0-9]+)[\\s]+(.*)[\\s]*$");
	private static final Pattern COL = Pattern.compile(
			"^%column[\\s]+\\[(-[0-9]+(,-[0-9])*)\\](\\+length)?[\\s]*$");
	private static final Pattern PART = Pattern.compile(
			"^%([A-Za-z0-9]*)[\\s]*\\{[\\s]*$");
	private static final Pattern ACTION = Pattern.compile(
			"^([\\S]+|\"(.*)\")[\\s]+(.*)[\\s]*$");

	private LanguagePrintMethod getLanguage(String lang) {
		if(lang.equals("Java")) {
			return new JavaPrintMethod();
		} else if(lang.equals("C#")) {
			return new CSharpPrintMethod();
		} else {
			throw new ParfaitException("unknownlanguage", lang);
		}
	}

	private HashFormatter getFormat(String format) {
		if(format.equals("enum")) {
			return new EnumHashFormatter();
		} else if(format.equals("execute")) {
			return new ExecuteHashFormatter();
		} else if(format.equals("map")) {
			return new MapHashFormatter();
		} else if(format.equals("validate")) {
			return new ValidateHashFormatter();
		} else {
			throw new ParfaitException("unknownoutput", format);
		}
	}

	/**
	 * 
	 * @param rd
	 * @param pw
	 * @throws IOException 
	 */
	public void execute(BufferedReader rd,
			String fname) throws IOException {
		final int S_INIT = 0;
		final int S_PART = 1;
		HashFormatter format = new MapHashFormatter();
		LanguagePrintMethod method = new JavaPrintMethod();
		PrintWriter pw = null;
		String pkg = "";
		String columns = null;
		boolean pluslen = false;
		boolean ignoreCase = false;
		String name = null;
		Map<String, String> map = new HashMap<String, String>();
		String defaultAction = null;
		String license = "";
		String prologue = "";
		String description = "";
		StringBuilder aux = new StringBuilder();
		String type = null;
		String line;
		String word = null;
		int stat;

		stat = S_INIT;
		StringBuilder buf1 = new StringBuilder();
		String typ = "";
		outer: while(true) {
			Matcher m;

			if((line = rd.readLine()) == null) {
				throw new ParfaitException("unexpectedeof");
			}

			switch(stat) {
			case S_INIT:
				if(line.equals("%%")) {
					break outer;
				} else if(line.equals("%ignoreCase")) {
					ignoreCase = true;
				} else if((m = PART.matcher(line)).matches()) {
					typ = m.group(1);
					buf1 = new StringBuilder();
					stat = S_PART;
				} else if((m = COL.matcher(line)).matches()) {
					columns = m.group(1);
					pluslen = m.group(3) != null;
				} else if((m = DET.matcher(line)).matches()) {
					if(m.group(1).equals("language")) {
						method = getLanguage(m.group(2));
					} else if(m.group(1).equals("package")) {
						pkg = m.group(2);
					} else if(m.group(1).equals("name")) {
						name = m.group(2);
					} else if(m.group(1).equals("type")) {
						type = m.group(2);
					} else if(m.group(1).equals("output")) {
						format = getFormat(m.group(2));
					}
				}
				break;
			case S_PART:
				if(!line.equals("%}")) {
					buf1.append(line).append('\n');
				} else if(typ.equals("defaultAction")) {
					defaultAction = buf1.toString();
					stat = S_INIT;
				} else if(typ.equals("license")) {
					license = buf1.toString();
					stat = S_INIT;
				} else if(typ.equals("")) {
					prologue = buf1.toString();
					stat = S_INIT;
				} else if(typ.equals("description")) {
					description = buf1.toString();
					stat = S_INIT;
				}
				break;
			default:
				throw new RuntimeException();
			}
		}

		StringBuilder buf = new StringBuilder();
		while(!"%%".equals(line = rd.readLine())) {
			Matcher m;

			if(line == null) {
				throw new ParfaitException("unexpectedeof");
			} else if(!(m = ACTION.matcher(line)).matches()) {
				buf.append(line.trim()).append('\n');
			} else {
				if(word != null) {
					map.put(word, buf.toString());
				}
				word = m.group(1);
				buf = new StringBuilder(m.group(3).trim()).append('\n');
			}
		}

		if(word != null) {
			map.put(word, buf.toString());
		}

		while((line = rd.readLine()) != null) {
			aux.append(line).append('\n');
		}

		if(name == null) {
			throw new ParfaitException("requiredname");
		} else if(type == null) {
			if(method instanceof JavaPrintMethod) {
				type = "String";
			} else if(method instanceof CSharpPrintMethod) {
				type = "string";
			}
		}

		// restriction
		if(method instanceof CSharpPrintMethod) {
			if(format instanceof EnumHashFormatter) {
				throw new ParfaitException("unknownoutput", "enum");
			}
		}

		try {
			File fo = new File(fname);

			pw = new PrintWriter(new OutputStreamWriter(
					new FileOutputStream(method.getTargetFilename(
							fo.getName()))));
			format.print(pw, method, pkg, columns, pluslen, ignoreCase,
					name, map, defaultAction, license, prologue,
					description, aux.toString(), type);
		} finally {
			if(pw != null) {
				pw.close();
			}
		}
	}

}
