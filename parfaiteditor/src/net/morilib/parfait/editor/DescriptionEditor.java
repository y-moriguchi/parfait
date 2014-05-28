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

import net.morilib.parfait.file.DescriptionBean;
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

public class DescriptionEditor extends FormPage {

	//
	Text license, description;

	//
	private ParfaitPageEditor editor;
	private boolean dirty = false;
	private String oldlic, olddes;

	/**
	 * 
	 * @param graph
	 */
	public DescriptionEditor(ParfaitPageEditor page) {
		super(page, "description", "Description");
		this.editor = page;
	}

	//
	private void loadFragment(DescriptionBean act) {
		oldlic = act.getLicense();
		olddes = act.getDescription();
	}

	/**
	 * 
	 * @return
	 */
	public String getLicense() {
		return license != null ? license.getText() : oldlic;
	}

	/**
	 * 
	 * @return
	 */
	public String getDescription() {
		return description != null ? description.getText() : olddes;
	}

	//
	void setDirty(boolean o) {
		dirty = o;
		editor.editorDirtyStateChanged();
		if(!o) {
			oldlic = getLicense();
			olddes = getDescription();
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

		form.setText("Descprition");
		ct.setLayout(gl);
		sc = tk.createSection(ct, Section.TITLE_BAR);
		sc.setText("License");
		sc.setLayoutData(g3);
		license = new Text(ct, SWT.MULTI | SWT.BORDER);
		license.setLayoutData(g2);
		if(oldlic != null) {
			license.setText(oldlic);
		} else {
			oldlic = "";
		}
		license.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				if(dirty) {
					// do nothing
				} else if(dirty = !oldlic.equals(e.data)) {
					editor.editorDirtyStateChanged();
				}
			}

		});

		sc = tk.createSection(ct, Section.TITLE_BAR);
		sc.setText("Descprition");
		sc.setLayoutData(g3);
		description = new Text(ct, SWT.MULTI | SWT.BORDER);
		description.setLayoutData(g2);
		if(olddes != null) {
			description.setText(olddes);
		} else {
			olddes = "";
		}
		description.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				if(dirty) {
					// do nothing
				} else if(dirty = !olddes.equals(e.data)) {
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
				loadFragment(mb.getDescription());
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
