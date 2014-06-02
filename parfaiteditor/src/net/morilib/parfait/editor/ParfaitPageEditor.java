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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import net.morilib.parfait.file.ConvertToTargetFile;
import net.morilib.parfait.file.SerializeParfaitXML;
import net.morilib.parfait.translate.HashFormatter;
import net.morilib.parfait.translate.JavaEnumHashFormatter;
import net.morilib.parfait.translate.JavaExecuteHashFormatter;
import net.morilib.parfait.translate.JavaMapHashFormatter;
import net.morilib.parfait.translate.JavaMapTestFormatter;
import net.morilib.parfait.translate.JavaVaildateTestFormatter;
import net.morilib.parfait.translate.JavaValidateHashFormatter;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.part.FileEditorInput;

public class ParfaitPageEditor extends FormEditor {

	//
	private ParfaitKeywordsEditor keywords;
	private DescriptionEditor description;
	private AuxiliaryCodeEditor auxiliary;
	private OptionsEditor options;

	/**
	 * 
	 * @return
	 */
	public ParfaitKeywordsEditor getKeywords() {
		return keywords;
	}

	/**
	 * 
	 * @return
	 */
	public DescriptionEditor getDescription() {
		return description;
	}

	/**
	 * 
	 * @return
	 */
	public AuxiliaryCodeEditor getAuxiliary() {
		return auxiliary;
	}

	/**
	 * 
	 * @return
	 */
	public OptionsEditor getOptions() {
		return options;
	}

	@Override
	protected void addPages() {
		try {
			keywords = new ParfaitKeywordsEditor(this);
			description = new DescriptionEditor(this);
			auxiliary = new AuxiliaryCodeEditor(this);
			options = new OptionsEditor(this);

			addPage(keywords);
			addPage(options);
			addPage(description);
			addPage(auxiliary);
		} catch(PartInitException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param ext
	 * @return
	 */
	public IFile getFile(IFile f, HashFormatter hf) {
		String n;

		n = hf.getTargetFilename(f.getName());
		return f.getParent().getFile(new Path(n));
	}

	/**
	 * 
	 * @param ext
	 * @return
	 */
	public IFile getFile(HashFormatter hf) {
		IFile f;

		f = ((IFileEditorInput)getEditorInput()).getFile();
		return getFile(f, hf);
	}

	//
	private void convert(IFile f, IFile g, HashFormatter hf,
			IProgressMonitor mon) throws CoreException {
		ByteArrayOutputStream bot = new ByteArrayOutputStream();
		ByteArrayInputStream bin;
		PrintWriter p;
		IMarker m;
		String n;

		try {
			p = new PrintWriter(new OutputStreamWriter(bot), true);
			n = g.getName().replaceFirst("\\.[^\\.]+$", "");
			if(keywords.getKeywordList().size() == 0) {
				m = f.createMarker(IMarker.PROBLEM);
				m.setAttribute(IMarker.SEVERITY,
						IMarker.SEVERITY_ERROR);
				m.setAttribute(IMarker.MESSAGE,
						"There are no keywords");
			} else if(ConvertToTargetFile.output(hf, p, n, this)) {
				p.close();
				bin = new ByteArrayInputStream(bot.toByteArray());
				if(g.exists()) {
					g.setContents(bin, true, false, mon);
				} else {
					g.create(bin, true, mon);
				}
			} else {
				p.close();
				m = f.createMarker(IMarker.PROBLEM);
				m.setAttribute(IMarker.SEVERITY,
						IMarker.SEVERITY_ERROR);
				m.setAttribute(IMarker.MESSAGE,
						"Collision of the hash value has occured");
			}
		} catch(IllegalArgumentException e) {
			m = f.createMarker(IMarker.PROBLEM);
			m.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
			m.setAttribute(IMarker.MESSAGE, "Invalid hash comptation");
		}
	}

	HashFormatter getHashFormatter() {
		if(options.isAction()) {
			return new JavaExecuteHashFormatter();
		} else if(options.isLookup()) {
			return new JavaValidateHashFormatter();
		} else if(options.isEnum()) {
			return new JavaEnumHashFormatter();
		} else if(options.isMap()) {
			return new JavaMapHashFormatter();
		} else {
			throw new RuntimeException();
		}
	}

	HashFormatter getTestFormatter() {
		if(options.isAction()) {
			return new JavaVaildateTestFormatter();
		} else if(options.isLookup()) {
			return new JavaVaildateTestFormatter();
		} else if(options.isEnum()) {
			return new JavaVaildateTestFormatter();
		} else if(options.isMap()) {
			return new JavaMapTestFormatter();
		} else {
			throw new RuntimeException();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor mon) {
		ByteArrayOutputStream bot = new ByteArrayOutputStream();
		HashFormatter hf;
		IFile f = null;

		try {
			SerializeParfaitXML.write(bot, this);
			f = ((IFileEditorInput)getEditorInput()).getFile();
			f.setContents(new ByteArrayInputStream(bot.toByteArray()),
					true, false, mon);
			f.deleteMarkers(IMarker.PROBLEM, true,
					IResource.DEPTH_INFINITE);
			hf = getHashFormatter();
			convert(f, getFile(hf), hf, mon);
			if(options.isTestCase()) {
				hf = getTestFormatter();
				convert(f, getFile(hf), hf, mon);
			}

			keywords.setDirty(false);
			description.setDirty(false);
			auxiliary.setDirty(false);
			options.setDirty(false);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#doSaveAs()
	 */
	@Override
	public void doSaveAs() {
		WorkspaceModifyOperation op;
		IWorkspace workspace;
		SaveAsDialog dialog;
		final IFile f;
		IPath path;
		Shell sh;

		dialog = new SaveAsDialog(
				getSite().getWorkbenchWindow().getShell());
		dialog.setOriginalFile(
				((IFileEditorInput)getEditorInput()).getFile());
		dialog.open();
		if((path = dialog.getResult()) == null)  return;
		workspace = ResourcesPlugin.getWorkspace();
		f  = workspace.getRoot().getFile(path);
		op = new WorkspaceModifyOperation() {

			public void execute(
					final IProgressMonitor mon) throws CoreException {
				ByteArrayOutputStream b;
				HashFormatter hf;

				try {
					b = new ByteArrayOutputStream();
					SerializeParfaitXML.write(b,
							ParfaitPageEditor.this);
					f.create(new ByteArrayInputStream(b.toByteArray()),
							true, mon);
					hf = getHashFormatter();
					convert(f, getFile(f, hf), hf, mon);
					if(options.isTestCase()) {
						hf = getTestFormatter();
						convert(f, getFile(hf), hf, mon);
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
			}

		};

		try {
			sh = getSite().getWorkbenchWindow().getShell();
			new ProgressMonitorDialog(sh).run(false, true, op);
			setInput(new FileEditorInput((IFile)f));

			keywords.setDirty(false);
			description.setDirty(false);
			auxiliary.setDirty(false);
			options.setDirty(false);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	@Override
	protected void setInput(IEditorInput input) {
		IFile file;

		super.setInput(input);
		file = ((IFileEditorInput)input).getFile();
		setPartName(file.getName());
	}

}
