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

import net.morilib.parfait.file.DeserializeParfaitXML;
import net.morilib.parfait.file.ParfaitBean;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

public class OptionsEditor extends FormPage {

	//
	Text defaultAction;
	Button rAction, rLookup, rMap;

	//
	private ParfaitPageEditor editor;
	private boolean dirty = false;
	private String olddef, oldtyp;

	/**
	 * 
	 * @param graph
	 */
	public OptionsEditor(ParfaitPageEditor page) {
		super(page, "options", "Options");
		this.editor = page;
	}

	/**
	 * 
	 * @return
	 */
	public String getDefaultAction() {
		return defaultAction != null ? defaultAction.getText() : olddef;
	}

	/**
	 * 
	 * @return
	 */
	public String getType() {
		if(rAction == null) {
			return oldtyp;
		} else if(rAction.getSelection()) {
			return ParfaitBean.R_ACTION;
		} else if(rLookup.getSelection()) {
			return ParfaitBean.R_LOOKUP;
		} else if(rMap.getSelection()) {
			return ParfaitBean.R_MAP;
		} else {
			return ParfaitBean.R_DEFAULT;
		}
	}

	/**
	 * 
	 * @return
	 */
	public boolean isAction() {
		return ParfaitBean.R_ACTION.equals(getType());
	}

	/**
	 * 
	 * @return
	 */
	public boolean isLookup() {
		return ParfaitBean.R_LOOKUP.equals(getType());
	}

	/**
	 * 
	 * @return
	 */
	public boolean isMap() {
		return ParfaitBean.R_MAP.equals(getType());
	}

	//
	void setDirty(boolean o) {
		dirty = o;
		editor.editorDirtyStateChanged();
		if(!o) {
			olddef = getDefaultAction();
			oldtyp = getType();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.editor.FormPage#isDirty()
	 */
	@Override
	public boolean isDirty() {
		return dirty;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
	 */
	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.editor.FormPage#createFormContent(org.eclipse.ui.forms.IManagedForm)
	 */
	@Override
	protected void createFormContent(IManagedForm managedForm) {
		ScrolledForm form = managedForm.getForm();
		FormToolkit tk = managedForm.getToolkit();
		GridLayout gl = new GridLayout(1, false);
		Composite ct = form.getBody(), cm;
		SelectionAdapter ap;
		GridData gd;
		Section sc;

		form.setText("Options");
		ct.setLayout(gl);

		// options
		sc = tk.createSection(ct, Section.TITLE_BAR);
		sc.setText("Options");
		sc.setLayoutData(new GridData(GridData.FILL_BOTH));
		cm = tk.createComposite(sc);
		cm.setLayout(new GridLayout(4, false));

		tk.createLabel(cm, "Function Type");
		rAction = tk.createButton(cm, "Action", SWT.RADIO);
		rLookup = tk.createButton(cm, "Lookup", SWT.RADIO);
		rMap    = tk.createButton(cm, "Map", SWT.RADIO);
		if(oldtyp == null) {
			olddef = "";
		} else if(oldtyp.equals(ParfaitBean.R_ACTION)) {
			rAction.setSelection(true);
		} else if(oldtyp.equals(ParfaitBean.R_LOOKUP)) {
			rLookup.setSelection(true);
		} else if(oldtyp.equals(ParfaitBean.R_MAP)) {
			rMap.setSelection(true);
		}
		ap = new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if(dirty) {
					// do nothing
				} else if(dirty = !olddef.equals(getType())) {
					editor.editorDirtyStateChanged();
				}
			}

		};
		rAction.addSelectionListener(ap);
		rLookup.addSelectionListener(ap);
		rMap.addSelectionListener(ap);

		tk.createLabel(cm, "Default Action");
		defaultAction = tk.createText(cm, "");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		defaultAction.setLayoutData(gd);
		defaultAction.setLayoutData(gd);
		if(olddef != null) {
			defaultAction.setText(olddef);
		} else {
			olddef = "";
		}
		defaultAction.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				if(dirty) {
					// do nothing
				} else if(dirty = !olddef.equals(e.data)) {
					editor.editorDirtyStateChanged();
				}
			}

		});

		sc.setClient(cm);
	}

	//
	private void loadFragment(ParfaitBean act) {
		olddef = act.getDefaultAction();
		oldtyp = act.getFunctionType();
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
				loadFragment(mb);
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
