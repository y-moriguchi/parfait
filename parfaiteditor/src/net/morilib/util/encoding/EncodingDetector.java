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
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * エンコードを自動判別する実装のインターフェースです。
 * 
 * 
 * @author MORIGUCHI, Yuichiro 2013/04/27
 */
public interface EncodingDetector {

	/**
	 * ファイルからエンコードを自動判別します。
	 * 
	 * @param f ファイル
	 * @return Charset
	 */
	public Charset detect(File f) throws IOException;

	/**
	 * InputStreamからエンコードを自動判別します。
	 * 
	 * @param ins InputStream
	 * @return Charset
	 */
	public Charset detect(InputStream f) throws IOException;

}
