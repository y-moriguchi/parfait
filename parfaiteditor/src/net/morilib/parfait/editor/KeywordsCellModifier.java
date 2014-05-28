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

import net.morilib.parfait.file.KeywordBean;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableItem;

public class KeywordsCellModifier implements ICellModifier {

	//
	private static final String P_KEYWORD = "keyword";
	private static final String P_ACTION  = "action";

	//
	private static final String[] PROPERTIES = new String[] {
		P_KEYWORD, P_ACTION
	};

	//
	private ParfaitKeywordsEditor editor;
	private TableViewer viewer;

	/**
	 * 
	 * @param v
	 */
	public KeywordsCellModifier(ParfaitKeywordsEditor e,
			TableViewer v) {
		editor = e;
		viewer = v;
	}

	/**
	 * 
	 * @return
	 */
	public static String[] getAllProperties() {
		return PROPERTIES;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ICellModifier#canModify(java.lang.Object, java.lang.String)
	 */
	@Override
	public boolean canModify(Object element, String property) {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ICellModifier#getValue(java.lang.Object, java.lang.String)
	 */
	@Override
	public Object getValue(Object element, String property) {
		KeywordBean p = (KeywordBean)element;

		if(P_KEYWORD.equals(property)) {
			return String.valueOf(p.getKeyword());
		} else if(P_ACTION.equals(property)) {
			return String.valueOf(p.getAction());
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object, java.lang.String, java.lang.Object)
	 */
	@Override
	public void modify(Object element, String property, Object value) {
		KeywordBean p;
		TableItem i;

		i = (TableItem)element;
		p = (KeywordBean)i.getData();
		if(P_KEYWORD.equals(property)) {
			p.setKeyword(String.valueOf(value));
		} else if(P_ACTION.equals(property)) {
			p.setAction(String.valueOf(value));
		}
		viewer.update(p, null);
		editor.setDirty(true);
	}

}
