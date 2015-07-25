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
import org.eclipse.swt.widgets.Combo;
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
	public static final int L_JAVA = 0;
	public static final int L_CSHARP = 1;

	//
	private static final int C_EXECUTE = 0;
	private static final int C_MAP     = 1;
	private static final int C_ENUM    = 2;
	private static final int C_ONLY    = 3;
	private static final String[] C_MESG = new String[] {
		"Execute corresponding actions",
		"Return corresponding values",
		"Type-safe enum",
		"Validate only",
	};

	//
	Text pacage, returnType, defaultAction;
	Combo language, functype;
	Button inject, create;
	Button ignoreCase, cAuto;
	Text columns;
	Combo pluslen;
	Button testCase;

	//
	private ParfaitPageEditor editor;
	private boolean dirty = false;
	private String oldpac, oldlan, oldret, olddef, oldtyp, oldcol;
	private boolean oldaut, oldlen, oldinj, oldcre, oldcas, oldtes;

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
	public String getReturnType() {
		return returnType != null ? returnType.getText() : oldret;
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
	public String getPackage() {
		return pacage != null ? pacage.getText() : oldpac;
	}

	/**
	 * 
	 * @return
	 */
	public String getLanguage() {
		return language != null ? language.getText() : oldlan;
	}

	/**
	 * 
	 * @return
	 */
	public String getType() {
		if(functype == null) {
			return oldtyp;
		} else {
			switch(functype.getSelectionIndex()) {
			case C_EXECUTE:  return ParfaitBean.R_ACTION;
			case C_MAP:      return ParfaitBean.R_MAP;
			case C_ENUM:     return ParfaitBean.R_ENUM;
			case C_ONLY:     return ParfaitBean.R_LOOKUP;
			default:         return ParfaitBean.R_DEFAULT;
			}
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

	/**
	 * 
	 * @return
	 */
	public boolean isEnum() {
		return ParfaitBean.R_ENUM.equals(getType());
	}

	/**
	 * 
	 * @return
	 */
	public int getLanguageNo() {
		return language.getSelectionIndex();
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

	/**
	 * 
	 * @return
	 */
	public boolean isAutomatically() {
		return cAuto != null ? cAuto.getSelection() : oldaut;
	}

	/**
	 * 
	 * @return
	 */
	public String getColumns() {
		return columns != null ? columns.getText() : oldcol;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isPlusLength() {
		return pluslen != null ?
				pluslen.getSelectionIndex() == 1 : oldlen;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isInject() {
		return inject != null ? inject.getSelection() : oldinj;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isCreate() {
		return create != null ? create.getSelection() : oldcre;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isIgnoreCase() {
		return ignoreCase != null ? ignoreCase.getSelection() : oldcas;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isTestCase() {
		return testCase != null ? testCase.getSelection() : oldtes;
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
		Composite ct = form.getBody(), cm, cn;
		GridData gd;
		Section sc;

		form.setText("Options");
		ct.setLayout(gl);

		// options
		sc = tk.createSection(ct, Section.TITLE_BAR);
		sc.setText("Options");
		sc.setLayoutData(new GridData(GridData.FILL_BOTH));
		cm = tk.createComposite(sc);
		cm.setLayout(new GridLayout(2, false));

		tk.createLabel(cm, "Package");
		pacage = tk.createText(cm, "");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		pacage.setLayoutData(gd);
		if(oldpac != null) {
			pacage.setText(oldpac);
		} else {
			oldpac = "";
		}
		pacage.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				if(dirty) {
					// do nothing
				} else if(dirty = !oldpac.equals(e.data)) {
					editor.editorDirtyStateChanged();
				}
			}

		});

		tk.createLabel(cm, "Target language");
		language = new Combo(cm, SWT.BORDER | SWT.READ_ONLY);
		language.add("Java");
		language.add("C#");
		language.select(0);

		tk.createLabel(cm, "Function Type");
		functype = new Combo(cm, SWT.BORDER | SWT.READ_ONLY);
		functype.add(C_MESG[C_EXECUTE]);
		functype.add(C_MESG[C_MAP]);
		functype.add(C_MESG[C_ENUM]);
		functype.add(C_MESG[C_ONLY]);
		if(oldtyp == null) {
			olddef = "";
		} else if(oldtyp.equals(ParfaitBean.R_ACTION)) {
			functype.select(C_EXECUTE);
		} else if(oldtyp.equals(ParfaitBean.R_MAP)) {
			functype.select(C_MAP);
		} else if(oldtyp.equals(ParfaitBean.R_ENUM)) {
			functype.select(C_ENUM);
		} else if(oldtyp.equals(ParfaitBean.R_LOOKUP)) {
			functype.select(C_ONLY);
		}
		functype.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				if(dirty) {
					// do nothing
				} else if(dirty = !olddef.equals(getType())) {
					editor.editorDirtyStateChanged();
				}
			}

		});

		tk.createLabel(cm, "Return type of Action");
		returnType = tk.createText(cm, "");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		returnType.setLayoutData(gd);
		if(oldret != null) {
			returnType.setText(oldret);
		} else {
			oldret = "";
		}
		returnType.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				if(dirty) {
					// do nothing
				} else if(dirty = !oldret.equals(e.data)) {
					editor.editorDirtyStateChanged();
				}
			}

		});

		tk.createLabel(cm, "Default Action");
		defaultAction = tk.createText(cm, "");
		gd = new GridData(GridData.FILL_HORIZONTAL);
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

		tk.createLabel(cm, "");
		inject = tk.createButton(cm, "Inject functions", SWT.RADIO);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		inject.setLayoutData(gd);
		inject.setSelection(oldinj);
		inject.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!dirty && oldinj != inject.getSelection()) {
					dirty = true;
					editor.editorDirtyStateChanged();
				}
			}

		});

		tk.createLabel(cm, "");
		create = tk.createButton(cm, "Create new class", SWT.RADIO);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		create.setLayoutData(gd);
		create.setSelection(oldcre);
		create.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!dirty && oldcre != create.getSelection()) {
					dirty = true;
					editor.editorDirtyStateChanged();
				}
			}

		});

		tk.createLabel(cm, "");
		ignoreCase = tk.createButton(cm, "Ignore Case", SWT.CHECK);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		ignoreCase.setLayoutData(gd);
		ignoreCase.setSelection(oldcas);
		ignoreCase.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!dirty && oldcas != ignoreCase.getSelection()) {
					dirty = true;
					editor.editorDirtyStateChanged();
				}
			}

		});

		tk.createLabel(cm, "Columns");
		cAuto = tk.createButton(cm, "Decide Automatically", SWT.CHECK);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		cAuto.setLayoutData(gd);
		cAuto.setSelection(oldaut);
		cAuto.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!dirty && oldaut != cAuto.getSelection()) {
					dirty = true;
					editor.editorDirtyStateChanged();
				}
				columns.setEnabled(!cAuto.getSelection());
				pluslen.setEnabled(!cAuto.getSelection());
			}

		});

		tk.createLabel(cm, "");
		cn = tk.createComposite(cm);
		cn.setLayout(new GridLayout(4, false));
		tk.createLabel(cn, "hash = string[");
		columns = tk.createText(cn, "");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 110;
		columns.setLayoutData(gd);
		if(oldcol != null) {
			columns.setText(oldcol);
		} else {
			oldcol = "";
		}
		columns.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				if(dirty) {
					// do nothing
				} else if(dirty = !oldcol.equals(e.data)) {
					editor.editorDirtyStateChanged();
				}
			}

		});
		tk.createLabel(cn, "] +");
		pluslen = new Combo(cn, SWT.READ_ONLY | SWT.BORDER);
		pluslen.add("0");
		pluslen.add("length");
		pluslen.select(oldlen ? 1 : 0);
		pluslen.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				if(dirty) {
					// do nothing
				} else if(dirty = (oldlen !=
						(pluslen.getSelectionIndex() == 1))) {
					editor.editorDirtyStateChanged();
				}
			}

		});
		columns.setEnabled(!oldaut);
		pluslen.setEnabled(!oldaut);

		tk.createLabel(cm, "");
		testCase = tk.createButton(cm, "Create a test case", SWT.CHECK);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		testCase.setLayoutData(gd);
		testCase.setSelection(oldtes);
		testCase.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!dirty && oldtes != testCase.getSelection()) {
					dirty = true;
					editor.editorDirtyStateChanged();
				}
			}

		});
		sc.setClient(cm);
	}

	//
	private void loadFragment(ParfaitBean act) {
		oldpac = act.getPackage();
		oldlan = act.getLanguage();
		oldret = act.getReturnType();
		olddef = act.getDefaultAction();
		oldtyp = act.getFunctionType();
		oldaut = act.isAutomatically();
		oldcol = act.getColumns();
		oldlen = act.isPlusLength();
		oldinj = act.isInject();
		oldcre = act.isCreate();
		oldcas = act.isIgnoreCase();
		oldtes = act.isTestCase();
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
