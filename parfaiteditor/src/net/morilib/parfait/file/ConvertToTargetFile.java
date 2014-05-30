package net.morilib.parfait.file;

import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import net.morilib.parfait.editor.ParfaitPageEditor;
import net.morilib.parfait.translate.HashFormatter;
import net.morilib.parfait.translate.PerfectHashOutput;

public final class ConvertToTargetFile {

	//
	private ConvertToTargetFile() {}

	/**
	 * 
	 * @param hf
	 * @param wr
	 * @param name
	 * @param ed
	 * @return
	 */
	public static boolean output(HashFormatter hf, PrintWriter wr,
			String name, ParfaitPageEditor ed) {
		String license, defAction, prologue, desc, aux;
		Map<String, String> map;

		map = new LinkedHashMap<String, String>();
		for(KeywordBean k : ed.getKeywords().getKeywordList()) {
			map.put(k.getKeyword(), k.getAction());
		}
		defAction = ed.getOptions().getDefaultAction();
		license   = ed.getDescription().getLicense();
		prologue  = ed.getAuxiliary().getDefinition();
		desc      = ed.getDescription().getDescription();
		aux       = ed.getAuxiliary().getAuxiliary();

		if(ed.getOptions().isAction()) {
			return PerfectHashOutput.printExecute(wr, hf, name, map,
					defAction, license, prologue, desc, aux);
		} else if(ed.getOptions().isLookup()) {
			return PerfectHashOutput.printLookup(wr, hf, name,
					map.keySet(), license, prologue, desc, aux);
		} else if(ed.getOptions().isMap()) {
			return PerfectHashOutput.printMap(wr, hf, name, map,
					license, prologue, desc, aux);
		} else {
			throw new RuntimeException();
		}
	}

}
