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
package net.morilib.parfait.wizard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import net.morilib.parfait.editor.ParfaitKeywordsEditor;
import net.morilib.parfait.file.KeywordBean;
import net.morilib.parser.csv.CSVConfig;
import net.morilib.parser.csv.CSVException;
import net.morilib.parser.csv.StringCSVPullParser;
import net.morilib.util.encoding.EncodingDetectorFactory;

import org.eclipse.jface.wizard.Wizard;

public class ParfaitImportWizard extends Wizard {

	//
	private ParfaitKeywordsEditor editor;
	private ParfaitImportWizardPage page1;

	/**
	 * 
	 */
	public ParfaitImportWizard(ParfaitKeywordsEditor ed) {
		setWindowTitle("Imports keywords");
		editor = ed;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	public void addPages() {
		addPage(page1 = new ParfaitImportWizardPage());
	}

	@Override
	public boolean performFinish() {
		BufferedReader rd = null;
		StringCSVPullParser cv;
		KeywordBean kb;
		String fn, en;
		CSVConfig cc;
		String[] ar;
		Charset cs;
		File fl;

		fn = page1.filename.getText();
		en = page1.encoding.getText();
		if(fn == null || fn.equals("")) {
			return true;
		} else {
			fl = new File(fn);
		}

		try {
			if(en == null || en.equals("") || en.charAt(0) == '(') {
				cs = EncodingDetectorFactory.getInstance().detect(fl);
			} else {
				cs = Charset.forName(en);
			}
			rd = new BufferedReader(new InputStreamReader(
					new FileInputStream(fl), cs));
			cc = new CSVConfig(",", '"', false);
			cv = new StringCSVPullParser(rd, cc);
			while(cv.next()) {
				if((ar = cv.get()).length > 0) {
					kb = new KeywordBean();
					kb.setKeyword(ar[0]);
					editor.addKeyword(kb);
				}
			}
			return true;
		} catch(IOException e) {
			return false;
		} catch(CSVException e) {
			return false;
		} finally {
			try {
				if(rd != null)  rd.close();
			} catch (IOException e) {
				return false;
			}
		}
	}

}
