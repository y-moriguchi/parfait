/*
 * Copyright 2013-2015 Yuichiro Moriguchi
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
package net.morilib.parfait.parser;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;

/**
 *
 *
 * @author MORIGUCHI, Yuichiro 2013/03/16
 */
public class RemoveCCommentReader extends FilterReader {

	private static enum S0 {
		INI, CM1, CM2, CM3, CL1, SQ1, SQ2, DQ1, DQ2
	}

	private S0 stat = S0.INI;
	private int unread = -1;

	/**
	 * @param in
	 */
	protected RemoveCCommentReader(Reader in) {
		super(in);
	}

	/* (non-Javadoc)
	 * @see java.io.FilterReader#read()
	 */
	public int read() throws IOException {
		int c;

		if(unread >= 0) {
			c = unread;  unread = -1;
			return c;
		}

		while((c = in.read()) >= 0) {
			switch(stat) {
			case INI:
				if(c == '/') {
					stat = S0.CM1;
				} else if(c == '\'') {
					stat = S0.SQ1;
					return c;
				} else if(c == '\"') {
					stat = S0.DQ1;
					return c;
				} else {
					return c;
				}
				break;
			case CM1:
				if(c == '*') {
					stat = S0.CM2;
				} else if(c == '/') {
					stat = S0.CL1;
				} else {
					stat = S0.INI;
					unread = c;
					return '/';
				}
				return ' ';
			case CM2:
				stat = c == '*' ? S0.CM3 : S0.CM2;
				if(c == '\n')  return c;
				break;
			case CM3:
				if(c == '/') {
					stat = S0.INI;
				} else if(c != '*') {
					stat = S0.CM2;
				}
				if(c == '\n')  return c;
				break;
			case CL1:
				stat = c == '\n' ? S0.INI : S0.CL1;
				if(c == '\n')  return c;
				break;
			case SQ1:
				if(c == '\\') {
					stat = S0.SQ2;
				} else if(c == '\'') {
					stat = S0.INI;
				}
				return c;
			case SQ2:
				stat = S0.SQ1;
				return c;
			case DQ1:
				if(c == '\\') {
					stat = S0.DQ2;
				} else if(c == '\"') {
					stat = S0.INI;
				}
				return c;
			case DQ2:
				stat = S0.DQ1;
				return c;
			}
		}
		return -1;
	}

	/* (non-Javadoc)
	 * @see java.io.FilterReader#read(char[], int, int)
	 */
	@Override
	public int read(char[] b, int off, int len) throws IOException {
		int c;

		for(int i = off; i < off + len; i++) {
			if((c = read()) < 0) {
				return i - off > 0 ? i - off : -1;
			} else {
				b[i] = (char)c;
			}
		}
		return len;
	}

	/* (non-Javadoc)
	 * @see java.io.FilterReader#skip(long)
	 */
	@Override
	public long skip(long n) throws IOException {
		for(long i = 0; i < n; i++) {
			if(!in.ready() || read() < 0) {
				return i;
			}
		}
		return n;
	}

	/* (non-Javadoc)
	 * @see java.io.FilterReader#markSupported()
	 */
	@Override
	public boolean markSupported() {
		return false;
	}

	/* (non-Javadoc)
	 * @see java.io.FilterReader#mark(int)
	 */
	@Override
	public void mark(int readAheadLimit) throws IOException {
		throw new IOException();
	}

	/* (non-Javadoc)
	 * @see java.io.FilterReader#reset()
	 */
	@Override
	public void reset() throws IOException {
		throw new IOException();
	}

	/* (non-Javadoc)
	 * @see java.io.FilterReader#close()
	 */
	@Override
	public void close() throws IOException {
		in.close();
	}

}
