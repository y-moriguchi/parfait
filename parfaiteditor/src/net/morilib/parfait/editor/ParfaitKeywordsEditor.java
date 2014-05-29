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
package net.morilib.parfait.editor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import net.morilib.parfait.file.DeserializeParfaitXML;
import net.morilib.parfait.file.KeywordBean;
import net.morilib.parfait.file.KeywordsBean;
import net.morilib.parfait.file.ParfaitBean;
import net.morilib.parfait.misc.MyGridLayout;
import net.morilib.parfait.wizard.ParfaitImportWizard;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

public class ParfaitKeywordsEditor extends FormPage {

	//
	private static int count = 1;

	//
	private boolean dirty;
	ParfaitPageEditor editor;
	TableViewer tvkey;

	//
	private KeywordsBean lbean = new KeywordsBean();

	/**
	 * 
	 * @param p
	 */
	public ParfaitKeywordsEditor(ParfaitPageEditor p) {
		super(p, "keywords", "Keywords");
		editor = p;
	}

	/**
	 * 
	 * @return
	 */
	public List<KeywordBean> getKeywordList() {
		return Collections.unmodifiableList(lbean.getList());
	}

	/**
	 * 
	 * @param b
	 */
	public void addKeyword(KeywordBean b) {
		tvkey.add(b);
		lbean.add(b);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.editor.FormPage#createFormContent(org.eclipse.ui.forms.IManagedForm)
	 */
	@Override
	protected void createFormContent(IManagedForm managedForm) {
		ScrolledForm form = managedForm.getForm();
		FormToolkit tk = managedForm.getToolkit();
		GridLayout gl = new GridLayout(1, false);
		final Composite ct = form.getBody();
		TableViewerColumn c1;
		Composite cm, cn;
		CellEditor[] ea;
		GridData gd;
		Section sc;
		Button b1;
		Table tb;

		form.setText("Keywords");
		ct.setLayout(gl);

		// operators
		sc = tk.createSection(ct, Section.TITLE_BAR);
		sc.setText("Keywords");
		gd = new GridData(GridData.FILL_BOTH);
		sc.setLayoutData(gd);
		cm = tk.createComposite(sc);
		cm.setLayout(new MyGridLayout(2, false));

		b1 = tk.createButton(cm, "Import from a file", SWT.BORDER);
		gd = new GridData();
		gd.horizontalSpan = 2;
		b1.setLayoutData(gd);
		b1.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				ParfaitImportWizard iw;
				WizardDialog wd;

				iw = new ParfaitImportWizard(
						ParfaitKeywordsEditor.this);
				wd = new WizardDialog(ct.getShell(), iw);
				wd.open();
			}

		});

		tb = tk.createTable(cm, SWT.H_SCROLL | SWT.V_SCROLL |
				SWT.SINGLE | SWT.FULL_SELECTION | SWT.BORDER);
		gd = new GridData(GridData.FILL_BOTH);
		tb.setLayoutData(gd);
		tb.setHeaderVisible(true);
		tb.setLinesVisible(true);
		tvkey = new TableViewer(tb);

		c1 = new TableViewerColumn(tvkey, SWT.LEFT);
		c1.getColumn().setText("Keyword");
		c1.getColumn().setWidth(100);
		c1.getColumn().setMoveable(true);
		c1 = new TableViewerColumn(tvkey, SWT.LEFT);
		c1.getColumn().setText("Action");
		c1.getColumn().setWidth(300);
		c1.getColumn().setMoveable(true);

		tvkey.setColumnProperties(
				KeywordsCellModifier.getAllProperties());
		ea = new CellEditor[] {
				new TextCellEditor(tb),
				new TextCellEditor(tb),
		};
		tvkey.setCellEditors(ea);
		tvkey.setCellModifier(new KeywordsCellModifier(this, tvkey));

		// operations
		cn = tk.createComposite(cm);
		cn.setLayout(new GridLayout());
		b1 = tk.createButton(cn, "Add", SWT.NONE);
		b1.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				KeywordBean b;

				b = new KeywordBean();
				b.setKeyword("keyword" + (count++));
				tvkey.add(b);
				lbean.add(b);
				setDirty(true);
			}

		});
		b1 = tk.createButton(cn, "Remove", SWT.NONE);
		b1.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				KeywordBean fb;
				IStructuredSelection ss;

				ss = (IStructuredSelection)tvkey.getSelection();
				fb = (KeywordBean)ss.getFirstElement();
				tvkey.remove(fb);
				lbean.remove(fb);
				setDirty(true);
			}

		});
		b1 = tk.createButton(cn, "Clear", SWT.NONE);
		b1.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				for(int k = 0; k < lbean.size(); k++) {
					tvkey.remove(lbean.get(k));
				}
				lbean.clear();
				setDirty(true);
			}

		});

		tvkey.setContentProvider(new KeywordsContentProvider());
		tvkey.setLabelProvider(new KeywordsLabelProvider());
		tvkey.setInput(lbean.getList());
		sc.setClient(cm);
	}

	//
	void setDirty(boolean o) {
		dirty = o;
		editor.editorDirtyStateChanged();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.editor.FormPage#isDirty()
	 */
	@Override
	public boolean isDirty() {
		return dirty;
	}

	//
	private void loadFragment(KeywordsBean act) {
		lbean = act;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#setInput(org.eclipse.ui.IEditorInput)
	 */
	protected void setInput(IEditorInput input) {
		InputStream ins = null;
		ParfaitBean mb;
		IFile file;

		super.setInput(input);
		file = ((IFileEditorInput)input).getFile();
		try {
			ins = file.getContents(false);
			if((mb = DeserializeParfaitXML.read(ins)) != null) {
				loadFragment(mb.getKeywords());
			}
		} catch(IOException e) {
			e.printStackTrace();
		} catch(CoreException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(ins != null) {
				try {
					ins.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
