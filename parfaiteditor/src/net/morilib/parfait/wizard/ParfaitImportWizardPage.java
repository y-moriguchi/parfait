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

import net.morilib.parser.csv.CSVConfig;
import net.morilib.parser.csv.StringCSVPullParser;
import net.morilib.util.encoding.EncodingDetectorFactory;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class ParfaitImportWizardPage extends WizardPage {

	//
	ParfaitImportWizardPage2 page2;
	Combo encoding;
	Text filename;

	/**
	 * 
	 */
	protected ParfaitImportWizardPage() {
		super("ImportWizardPage");
		setTitle("Import keywords");
	}

	//
	StringCSVPullParser getCSVParser() throws IOException {
		BufferedReader rd;
		CSVConfig cc;
		String fn, en;
		Charset cs;
		File fl;

		fn = filename.getText();
		en = encoding.getText();
		if(fn != null) {
			fl = new File(fn);
		} else {
			throw new IllegalStateException();
		}

		if(en == null || en.equals("") || en.charAt(0) == '(') {
			cs = EncodingDetectorFactory.getInstance().detect(fl);
		} else {
			cs = Charset.forName(en);
		}
		rd = new BufferedReader(new InputStreamReader(
				new FileInputStream(fl), cs));
		cc = new CSVConfig(",", '"', false);
		return new StringCSVPullParser(rd, cc);
	};

	//
	private void doValidate() {
		String s;

		if((s = filename.getText()) == null || s.equals("")) {
			setErrorMessage("Filename must not be empty.");
			setPageComplete(false);
		} else if((s = page2.changeheader()) != null) {
			setErrorMessage(s);
			setPageComplete(false);
		} else {
			setErrorMessage(null);
			setPageComplete(true);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.WizardPage#canFlipToNextPage()
	 */
	@Override
	public boolean canFlipToNextPage() {
		return getErrorMessage() == null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(final Composite parent) {
		Composite cm = new Composite(parent, SWT.NULL);
		GridData gd;
		Button b1;
		Label lb;

		cm.setLayout(new GridLayout(3, false));
		lb = new Label(cm, SWT.NULL);
		lb.setText("Filename");
		filename = new Text(cm, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		filename.setLayoutData(gd);
		filename.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				doValidate();
				getWizard().getContainer().updateButtons();
			}

		});
		b1 = new Button(cm, SWT.BORDER);
		b1.setText("Browse...");
		b1.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd;
				String s;

				fd = new FileDialog(parent.getShell(), SWT.OPEN);
				if((s = fd.open()) != null) {
					filename.setText(s);
				}
				doValidate();
				getWizard().getContainer().updateButtons();
			}

		});

		lb = new Label(cm, SWT.NULL);
		lb.setText("Encoding");
		encoding = new Combo(cm, SWT.DROP_DOWN);
		encoding.setText("(auto-detect)");
		encoding.add("(auto-detect)");
		encoding.add("UTF-8");
		encoding.add("Windows-31J");
		encoding.add("EUC-JP");
		encoding.add("Shift_JIS");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		encoding.setLayoutData(gd);

		doValidate();
		setControl(cm);
	}

}
