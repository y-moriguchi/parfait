/*
 * Copyright 2009-2010 Yuichiro Moriguchi
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
package net.morilib.parser.csv;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 *
 *
 * @author MORIGUCHI, Yuichiro 2010/09/18
 */
public final class StringCSVPullParser
implements CSVPullParser<String[]>, Closeable {

	//
	private class Hndl implements CSVHandler {

		/* (non-Javadoc)
		 * @see net.morilib.csv.CSVHandler#startFile()
		 */
		public boolean startFile() throws CSVException {
			return false;
		}

		/* (non-Javadoc)
		 * @see net.morilib.csv.CSVHandler#endFile()
		 */
		public boolean endFile() throws CSVException {
			return false;
		}

		/* (non-Javadoc)
		 * @see net.morilib.csv.CSVHandler#startLine(int)
		 */
		public boolean startLine(int line) throws CSVException {
			toreturn = new ArrayList<String>();
			return false;
		}

		/* (non-Javadoc)
		 * @see net.morilib.csv.CSVHandler#endLine(int)
		 */
		public boolean endLine(int line) throws CSVException {
			return true;
		}

		/* (non-Javadoc)
		 * @see net.morilib.csv.CSVHandler#emptyLine(int)
		 */
		public boolean emptyLine(int line) throws CSVException {
			return false;
		}

		/* (non-Javadoc)
		 * @see net.morilib.csv.CSVHandler#element(java.lang.String, int, int)
		 */
		public boolean element(
				String elm, int line, int field) throws CSVException {
			toreturn.add(elm);
			return false;
		}

		/* (non-Javadoc)
		 * @see net.morilib.csv.CSVHandler#comment(java.lang.String, int)
		 */
		public boolean comment(
				String com, int line) throws CSVException {
			return false;
		}

		/* (non-Javadoc)
		 * @see net.morilib.csv.CSVHandler#error(net.morilib.csv.CSVParseException)
		 */
		public boolean error(CSVParseException e) throws CSVException {
			throw e;
		}

	}

	//
	private static final CSVParserContinuation MARKER =
		new CSVParserContinuation() {};

	//
	private final Hndl handler = new Hndl();
	private CSVPushParser parser;
	private CSVParserContinuation cont = MARKER;
	private List<String> toreturn;
	private Reader stream;

	/**
	 * 
	 * @param rd
	 * @param f
	 */
	public StringCSVPullParser(Reader rd, CSVConfig f) {
		parser = new SimpleCSVPushParser(handler, f);
		stream = rd;
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#hasNext()
	 */
	public String[] get() {
		return toreturn.toArray(new String[0]);
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	public boolean next() throws CSVException, IOException {
		if(cont == null) {
			throw new NoSuchElementException();
		} else if(cont == MARKER) {
			if((cont = parser.parse(stream)) == null) {
				stream = null;
				return false;
			} else {
				return true;
			}
		} else {
			if((cont = parser.parse(cont)) == null) {
				stream = null;
				return false;
			} else {
				return true;
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.io.Closeable#close()
	 */
	@Override
	public void close() {
		if(stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

}
