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

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

/**
 * 日本語エンコードの自動判別器です。
 * 
 * 
 * @author MORIGUCHI, Yuichiro 2013/04/27
 */
public class EncodingDetectorResource_ja
extends AbstractEncodingDetectorResource {

	private static final int SIZE = 1024;
	private static final Charset JIS = Charset.forName("ISO-2022-JP");

	// order of the array is significant
	private static final String[] CODES = new String[] {
		"UTF-8", "Windows-31J", "EUC-JP", "UTF-16BE", "UTF-16LE"
	};

	static boolean detectJIS(byte[] a) {
		int stat = 1000;

		for(int i = 0; i < a.length; i++) {
			if(a[i] < 0)  return false;
			switch(stat) {
			case 1000:
				stat = a[i] == 0x1b ? 1010 : 1000;
				break;
			case 1010:
				stat = a[i] == '$' ? 1020 : a[i] == 0x1b ? 1010 : 1000;
				break;
			case 1020:
				if(a[i] == 'B')  return true;
				stat = a[i] == 0x1b ? 1010 : 1000;
				break;
			}
		}
		return false;
	}

	public Charset detect(InputStream ins) throws IOException {
		byte[] a = new byte[SIZE];
		CharsetDecoder cd;
		CoderResult cr;
		ByteBuffer bb;
		CharBuffer cb;
		Charset ch;
		int l;

		cb  = CharBuffer.allocate(SIZE);
		l   = ins.read(a);
		if(detectJIS(a))  return JIS;

		for(String s : CODES) {
			ch = Charset.forName(s);
			cd = ch.newDecoder();
			cd.onMalformedInput(CodingErrorAction.REPORT);
			bb = ByteBuffer.wrap(a, 0, l);
			cr = cd.decode(bb, cb, false);
			if(!cr.isError())  return ch;
		}
		return Charset.defaultCharset();
	}

}
