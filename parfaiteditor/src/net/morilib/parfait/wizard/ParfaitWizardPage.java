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

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import net.morilib.parfait.file.SerializeParfaitXML;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.ide.IDE;

public class ParfaitWizardPage extends WizardNewFileCreationPage {

	private static int exampleCount = 1;
	private IWorkbench workbench;

	/**
	 * 
	 */
	public ParfaitWizardPage(IWorkbench w, IStructuredSelection s) {
		super("create a new hash definition", s);
		this.setTitle("create a new hash definition");
		this.setDescription("create a new hash definition");
		this.workbench = w;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		super.createControl(parent);
		this.setFileName("keywords" + exampleCount + ".gparfait");
		setPageComplete(validatePage());
	}

	/**
	 * 
	 * @return
	 */
	protected InputStream getInitialContents() {
		ByteArrayInputStream bin = null;
		String s;

		try {
			s   = getContainerFullPath().removeFirstSegments(2).toString();
			s   = s.replaceFirst("^/", "");
			s   = s.replace('/', '.');
			s   = SerializeParfaitXML.newfile(s);
			bin = new ByteArrayInputStream(s.getBytes());
		} catch(Exception e) {
			e.printStackTrace();
		}
		return bin;
	}

	/**
	 * 
	 * @return
	 */
	public boolean finish() {
		IFile newFile = createNewFile();

		if(newFile == null)  return false;
		try {
			IWorkbenchWindow dwindow = workbench.getActiveWorkbenchWindow();
			IWorkbenchPage page = dwindow.getActivePage();

			if(page != null)  IDE.openEditor(page, newFile, true);
		} catch(org.eclipse.ui.PartInitException e) {
			e.printStackTrace();
			return false;
		}
		exampleCount++;
		return true;
	}

}
