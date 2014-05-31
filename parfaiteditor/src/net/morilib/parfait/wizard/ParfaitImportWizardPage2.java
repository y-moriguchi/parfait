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

import net.morilib.parser.csv.CSVException;
import net.morilib.parser.csv.StringCSVPullParser;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class ParfaitImportWizardPage2 extends WizardPage {

	//
	private static final int MAXLEN = 27;

	//
	ParfaitImportWizardPage page1;
	Button isheader, isusemap;
	Combo keycolumn, mapcolumn;

	/**
	 * 
	 */
	protected ParfaitImportWizardPage2(ParfaitImportWizardPage p1) {
		super("ImportWizardPage");
		setTitle("Import keywords");
		page1 = p1;
	}

	//
	private static void changeCombo(Combo c, String[] a) {
		c.removeAll();
		for(String v : a) {
			if(v.length() < MAXLEN) {
				c.add(v);
			} else {
				c.add(v.substring(0, MAXLEN) + "...");
			}
		}
		c.select(0);
	}

	//
	private static void changeCombo(Combo c, int len) {
		c.removeAll();
		for(int k = 0; k < len; k++) {
			c.add(Integer.toString(k + 1));
		}
		c.select(0);
	}

	//
	String changeheader() {
		StringCSVPullParser p = null;
		String m = null;
		String[] a;

		try {
			p = page1.getCSVParser();
			if(p.next()) {
				a = p.get();
			} else {
				setErrorMessage(m = "Empty file.");
				setPageComplete(false);
				return m;
			}

			if(isheader.getSelection()) {
				changeCombo(keycolumn, a);
				changeCombo(mapcolumn, a);
			} else {
				changeCombo(keycolumn, a.length);
				changeCombo(mapcolumn, a.length);
			}
			setPageComplete(true);
		} catch(IOException e) {
			setErrorMessage(m = "I/O error occurred.");
			setPageComplete(false);
		} catch(CSVException e) {
			setErrorMessage(m = "Invalid CSV File.");
			setPageComplete(false);
		} finally {
			if(p != null) {
				p.close();
			}
		}
		return m;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(final Composite parent) {
		Composite cm = new Composite(parent, SWT.NULL);
		GridData gd;
		Label lb;

		cm.setLayout(new GridLayout(2, false));
		lb = new Label(cm, SWT.NULL);
		lb.setText("");
		isheader = new Button(cm, SWT.CHECK);
		isheader.setText("Include header");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		isheader.setLayoutData(gd);
		isheader.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				changeheader();
			}

		});

		lb = new Label(cm, SWT.NULL);
		lb.setText("Keyword column");
		keycolumn = new Combo(cm, SWT.DROP_DOWN);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		keycolumn.setLayoutData(gd);

		lb = new Label(cm, SWT.NULL);
		lb.setText("Action(Map) column");
		isusemap = new Button(cm, SWT.CHECK);
		isusemap.setText("Use Action(Map) Column");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		isusemap.setLayoutData(gd);
		isusemap.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				mapcolumn.setEnabled(isusemap.getSelection());
			}

		});

		lb = new Label(cm, SWT.NULL);
		lb.setText("");
		mapcolumn = new Combo(cm, SWT.DROP_DOWN);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		mapcolumn.setLayoutData(gd);
		mapcolumn.setEnabled(isusemap.getSelection());
		setControl(cm);
	}

}
