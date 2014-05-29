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

import java.util.regex.Pattern;

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
	Combo encoding;
	Text filename, keycolumn, mapcolumn;

	/**
	 * 
	 */
	protected ParfaitImportWizardPage() {
		super("ImportWizardPage");
		setTitle("Import keywords");
	}

	//
	private static final Pattern NUM = Pattern.compile("[0-9]+");

	//
	private void doValidate() {
		String s;

		if((s = filename.getText()) == null || s.equals("")) {
			setErrorMessage("Filename must not be empty.");
			setPageComplete(false);
		} else if((s = keycolumn.getText()) == null || s.equals("")) {
			setErrorMessage("Keyword column must not be empty.");
			setPageComplete(false);
		} else if(!NUM.matcher(s).matches()) {
			setErrorMessage("Keyword column must be positive integer.");
			setPageComplete(false);
		} else if(Integer.parseInt(s) == 0) {
			setErrorMessage("Keyword column must be positive integer.");
			setPageComplete(false);
		} else if((s = mapcolumn.getText()) != null && !s.equals("") &&
				(!NUM.matcher(s).matches() || Integer.parseInt(s) == 0)) {
			setErrorMessage("Action column must be positive integer.");
			setPageComplete(false);
		} else {
			setErrorMessage(null);
			setPageComplete(true);
		}
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

		lb = new Label(cm, SWT.NULL);
		lb.setText("Keyword column");
		keycolumn = new Text(cm, SWT.BORDER);
		keycolumn.setText("1");
		gd = new GridData();
		gd.horizontalSpan = 2;
		gd.widthHint = 50;
		keycolumn.setLayoutData(gd);
		keycolumn.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				doValidate();
			}

		});

		lb = new Label(cm, SWT.NULL);
		lb.setText("Action(Map) column");
		mapcolumn = new Text(cm, SWT.BORDER);
		gd = new GridData();
		gd.horizontalSpan = 2;
		gd.widthHint = 50;
		mapcolumn.setLayoutData(gd);
		mapcolumn.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				doValidate();
			}

		});

		doValidate();
		setControl(cm);
	}

}
