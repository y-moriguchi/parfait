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

import net.morilib.parfait.file.AuxiliaryCodeBean;
import net.morilib.parfait.file.DeserializeParfaitXML;
import net.morilib.parfait.file.ParfaitBean;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

public class AuxiliaryCodeEditor extends FormPage {

	//
	Text definition, auxiliary;

	//
	private ParfaitPageEditor editor;
	private boolean dirty = false;
	private String olddef, oldaux;

	/**
	 * 
	 * @param graph
	 */
	public AuxiliaryCodeEditor(ParfaitPageEditor page) {
		super(page, "auxiliary", "Auxiliary");
		this.editor = page;
	}

	//
	private void loadFragment(AuxiliaryCodeBean act) {
		olddef = act.getDefinition();
		oldaux = act.getAuxiliary();
	}

	/**
	 * 
	 * @return
	 */
	public String getDefinition() {
		return definition != null ? definition.getText() : olddef;
	}

	/**
	 * 
	 * @return
	 */
	public String getAuxiliary() {
		return auxiliary != null ? auxiliary.getText() : oldaux;
	}

	//
	void setDirty(boolean o) {
		dirty = o;
		editor.editorDirtyStateChanged();
		if(!o) {
			olddef = getDefinition();
			oldaux = getAuxiliary();
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
		GridData g2 = new GridData(GridData.FILL_BOTH);
		GridData g3 = new GridData(GridData.FILL_HORIZONTAL);
		Composite ct = form.getBody();
		Section sc;

		form.setText("Auxiliary Codes");
		ct.setLayout(gl);
		sc = tk.createSection(ct, Section.TITLE_BAR);
		sc.setText("Definition");
		sc.setLayoutData(g3);
		definition = new Text(ct, SWT.MULTI | SWT.BORDER);
		definition.setLayoutData(g2);
		if(olddef != null) {
			definition.setText(olddef);
		} else {
			olddef = "";
		}
		definition.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				if(dirty) {
					// do nothing
				} else if(dirty = !olddef.equals(e.data)) {
					editor.editorDirtyStateChanged();
				}
			}

		});

		sc = tk.createSection(ct, Section.TITLE_BAR);
		sc.setText("Auxiliary");
		sc.setLayoutData(g3);
		auxiliary = new Text(ct, SWT.MULTI | SWT.BORDER);
		auxiliary.setLayoutData(g2);
		if(oldaux != null) {
			auxiliary.setText(oldaux);
		} else {
			oldaux = "";
		}
		auxiliary.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				if(dirty) {
					// do nothing
				} else if(dirty = !oldaux.equals(e.data)) {
					editor.editorDirtyStateChanged();
				}
			}

		});
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
				loadFragment(mb.getAuxiliary());
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
