package net.morilib.parfait.file;

import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import net.morilib.parfait.editor.ParfaitPageEditor;
import net.morilib.parfait.translate.HashFormatter;

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
		String license, defAction, prologue, desc, aux, col;
		Map<String, String> map;
		boolean plen;

		map = new LinkedHashMap<String, String>();
		for(KeywordBean k : ed.getKeywords().getKeywordList()) {
			map.put(k.getKeyword(), k.getAction());
		}
		defAction = ed.getOptions().getDefaultAction();
		license   = ed.getDescription().getLicense();
		prologue  = ed.getAuxiliary().getDefinition();
		desc      = ed.getDescription().getDescription();
		aux       = ed.getAuxiliary().getAuxiliary();
		if(ed.getOptions().isAutomatically()) {
			col  = null;
			plen = false;
		} else {
			col  = ed.getOptions().getColumns();
			plen = ed.getOptions().isPlusLength();
		}
		return hf.print(wr, hf, col, plen,
				name, map, defAction, license, prologue, desc,
				aux);
	}

}
