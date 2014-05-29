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
package net.morilib.parfait.misc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;

public class MyGridLayout extends Layout {

	//
	private GridLayout wrapee;
	private Method flushmethod, computemethod, layoutmethod;

	/**
	 * 
	 * @param g
	 * @param b
	 */
	public MyGridLayout(int g, boolean b) {
		wrapee = new GridLayout(g, b);
	}

	@Override
	protected boolean flushCache(Control control) {
		try {
			if(flushmethod == null) {
				flushmethod = GridLayout.class.getDeclaredMethod(
						"flushCache", Control.class);
				flushmethod.setAccessible(true);
			}
			return ((Boolean)flushmethod.invoke(wrapee,
					control)).booleanValue();
		} catch(NoSuchMethodException e) {
			throw new RuntimeException(e);
		} catch(SecurityException e) {
			throw new RuntimeException(e);
		} catch(IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch(IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch(InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected Point computeSize(Composite composite, int wHint,
			int hHint, boolean flushCache) {
		Point si, sc;

		try {
			if(computemethod == null) {
				computemethod = GridLayout.class.getDeclaredMethod(
						"computeSize", Composite.class, int.class,
						int.class, boolean.class);
				computemethod.setAccessible(true);
			}

			sc = composite.getSize();
			si = (Point)computemethod.invoke(wrapee, composite, wHint,
					hHint, flushCache);
			si.y = Math.min(sc.y, si.y);
			return si;
		} catch(NoSuchMethodException e) {
			throw new RuntimeException(e);
		} catch(SecurityException e) {
			throw new RuntimeException(e);
		} catch(IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch(IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch(InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void layout(Composite composite, boolean flushCache) {
		try {
			if(layoutmethod == null) {
				layoutmethod = GridLayout.class.getDeclaredMethod(
						"layout", Composite.class, boolean.class);
				layoutmethod.setAccessible(true);
			}
			layoutmethod.invoke(wrapee, composite, flushCache);
		} catch(NoSuchMethodException e) {
			throw new RuntimeException(e);
		} catch(SecurityException e) {
			throw new RuntimeException(e);
		} catch(IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch(IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch(InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

}
