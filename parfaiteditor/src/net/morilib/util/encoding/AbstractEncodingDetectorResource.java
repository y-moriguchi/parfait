/*
 * Copyright 2013 Yuichiro Moriguchi
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
package net.morilib.util.encoding;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;

/**
 * エンコード自動判別のリソースです。
 * 
 * 
 * @author MORIGUCHI, Yuichiro 2013/04/27
 */
public abstract class AbstractEncodingDetectorResource
extends ResourceBundle implements EncodingDetector {

	@Override
	public Enumeration<String> getKeys() {
		return new Enumeration<String>() {

			public boolean hasMoreElements() {
				return false;
			}

			public String nextElement() {
				throw new NoSuchElementException();
			}

		};
	}

	@Override
	protected Object handleGetObject(String key) {
		if(key == null)  throw new NullPointerException();
		return null;
	}

	public Charset detect(File f) throws IOException {
		InputStream ins = null;

		try {
			ins = new FileInputStream(f);
			return detect(ins);
		} finally {
			if(ins != null)  ins.close();
		}
	}

}
