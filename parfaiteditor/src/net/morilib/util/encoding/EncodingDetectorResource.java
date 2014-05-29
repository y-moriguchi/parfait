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
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * デフォルトのエンコードの自動判別器です。
 * 
 * 
 * @author MORIGUCHI, Yuichiro 2013/04/27
 */
public class EncodingDetectorResource
extends AbstractEncodingDetectorResource {

	public Charset detect(InputStream ins) {
		return Charset.defaultCharset();
	}

	public Charset detect(File f) {
		return Charset.defaultCharset();
	}

}
