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

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
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
	Text filename;

	/**
	 * 
	 */
	protected ParfaitImportWizardPage() {
		super("ImportWizardPage");
		setTitle("Import keywords");
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
		b1 = new Button(cm, SWT.BORDER);
		b1.setText("Browse");
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
		encoding.setLayoutData(gd);
		setControl(cm);
	}

}
