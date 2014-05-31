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

import java.io.IOException;

import net.morilib.parfait.editor.ParfaitKeywordsEditor;
import net.morilib.parfait.file.KeywordBean;
import net.morilib.parser.csv.CSVException;
import net.morilib.parser.csv.StringCSVPullParser;

import org.eclipse.jface.wizard.Wizard;

public class ParfaitImportWizard extends Wizard {

	//
	private ParfaitKeywordsEditor editor;
	private ParfaitImportWizardPage page1;
	private ParfaitImportWizardPage2 page2;

	/**
	 * 
	 */
	public ParfaitImportWizard(ParfaitKeywordsEditor ed) {
		setWindowTitle("Import keywords");
		editor = ed;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	public void addPages() {
		addPage(page1 = new ParfaitImportWizardPage());
		addPage(page2 = new ParfaitImportWizardPage2(page1));
		page1.page2 = page2;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		StringCSVPullParser cv = null;
		KeywordBean kb;
		String[] ar;
		int kw, aw;

		try {
			kw = page2.keycolumn.getSelectionIndex();
			aw = page2.mapcolumn.getSelectionIndex();
			cv = page1.getCSVParser();
			if(page2.isheader.getSelection()) {
				cv.next();
			}

			while(cv.next()) {
				ar = cv.get();
				if(ar.length > kw) {
					kb = new KeywordBean();
					kb.setKeyword(ar[kw]);
					if(page2.isusemap.getSelection() &&
							ar.length > aw) {
						kb.setAction(ar[aw]);
					}
					editor.addKeyword(kb);
				}
			}
			return true;
		} catch(IOException e) {
			return false;
		} catch(CSVException e) {
			return false;
		} finally {
			if(cv != null) {
				cv.close();
			}
		}
	}

}
