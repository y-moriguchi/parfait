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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * 
 * 
 * @author MORIGUCHI, Yuichiro 2006/01/07
 */
public final class SimpleCSVPushParser implements CSVPushParser {

	//
	private static class Cont implements CSVParserContinuation {

		//
		private int st = BEGIN_OF_FILE;
		private int index = 1, line = 1, c;
		private BufferedReader ins;

		//
		private Cont() { }

		//
		private Cont(
				int st, int index, int line, int c,
				BufferedReader ins) {
			this.st    = st;
			this.index = index;
			this.line  = line;
			this.c     = c;
			this.ins   = ins;
		}
	}

	/**
	 * 
	 */
	public static final CSVConfig CSV_CONFIG =
		new CSVConfig(",", '\"', false);

	/**
	 * 
	 */
	public static final CSVConfig TSV_CONFIG =
		new CSVConfig("\t", '\"', false);

	//
	private static final int BEGIN_OF_FILE  =   0;
	private static final int BEGIN_OF_LINE  =   1;
	private static final int BEGIN_OF_LINE2 =   2;
	private static final int END_OF_LINE    =   3;
	private static final int END_OF_FILE    =   4;
	private static final int HALT           =   5;
	private static final int BEGIN_OF_FIELD = 400;
	private static final int ENCLOSED       = 100;
	private static final int UNENCLOSED     = 200;
	private static final int UNENCLOSED2    = 201;
	private static final int ENCLOSED_CHAR  = 300;
	private static final int COMMENT        = 500;
	private static final int PANIC_1        = 900;

	//
	private CSVHandler handler;
	private CSVConfig  config;

	/**
	 * 
	 * @param delimiters
	 * @param quote
	 * @param newline
	 */
	public SimpleCSVPushParser(
			CSVHandler handler, CSVConfig config) {
		this.handler = handler;
		this.config  = config;
	}

	/**
	 * 
	 *
	 */
	public SimpleCSVPushParser(CSVHandler handler) {
		this(handler, CSV_CONFIG);
	}

	/**
	 * 
	 */
	public CSVParserContinuation parse(
			CSVParserContinuation cont
			) throws IOException, CSVException {
		Cont c2 = (Cont)cont;
		int st = c2.st;
		int index = c2.index, line = c2.line;
		int c = c2.c;
		BufferedReader ins = c2.ins;
		int backto = -1;

		//
		StringBuffer b = new StringBuffer();
		while(true) {
			switch(st) {
			case BEGIN_OF_FILE:
				st = BEGIN_OF_LINE;
				if(handler.startFile()) {
					return new Cont(st, index, line, c, ins);
				}
				break;
			case BEGIN_OF_LINE:
				st = BEGIN_OF_LINE2;
				if(handler.startLine(line)) {
					return new Cont(st, index, line, c, ins);
				}
				break;
			case BEGIN_OF_LINE2:     // beginning of lines
				b = new StringBuffer();
				index = 1;
				c = ins.read();

				if(config.delimiters.indexOf(c) >= 0) {
					// an empty field
					st = BEGIN_OF_FIELD;
					if(handler.element(b.toString(), line, index++)) {
						return new Cont(st, index, line, c, ins);
					}
				} else if(c < 0) {
					st = END_OF_FILE;
				} else if(c == config.newline) {
					// empty line - do nothing
				} else if(config.commentable && c == config.comment) {
					// comment
					st = COMMENT;
				} else if(c == config.quote) {
					// enclosed field
					st = ENCLOSED;
					if(handler.startLine(line)) {
						return new Cont(st, index, line, c, ins);
					}
				} else {
					// not enclosed field
					b.append((char)c);
					st = UNENCLOSED;
					if(handler.startLine(line)) {
						return new Cont(st, index, line, c, ins);
					}
				}
				break;
			case BEGIN_OF_FIELD:     // beginning of fields
				b = new StringBuffer();
				c = ins.read();

				if(!config.countinuous &&
						config.delimiters.indexOf(c) >= 0) {
					// an empty field
					if(handler.element(b.toString(), line, index++)) {
						return new Cont(st, index, line, c, ins);
					}
				} else if(c < 0 || c == config.newline) {
					// an empty field and end of line
					st = END_OF_LINE;
					if(handler.element(b.toString(), line, index++)) {
						return new Cont(st, index, line, c, ins);
					}
				} else if(c == config.quote) {
					// enclosed field
					st = ENCLOSED;
				} else if(config.charTrimed.indexOf(c) >= 0) {
					st = UNENCLOSED;
				} else {
					// unenclosed field
					b.append((char)c);
					st = UNENCLOSED;
				}
				break;
			case END_OF_LINE:
				st = (c < 0) ? END_OF_FILE : BEGIN_OF_LINE;
				if(handler.endLine(line++)) {
					return new Cont(st, index, line, c, ins);
				}
				break;
			case ENCLOSED:     // beginning char is quote
				c = ins.read();
				if(c == config.quote) {
					// quote character is appeared
					st = ENCLOSED_CHAR;
				} else if(c >= 0) {
					// enclosed sequence
					b.append((char)c);
				} else {
					st = PANIC_1;
					if(handler.error(new CSVParseException())) {
						return new Cont(st, index, line, c, ins);
					}
				}
				break;
			case UNENCLOSED:
				if(config.charTrimed.indexOf(c) >= 0) {
					break;
				}
				st = UNENCLOSED2;
				/* continue */
			case UNENCLOSED2:   // beginning char is not quoted
				c = ins.read();
				if(config.delimiters.indexOf(c) >= 0) {
					String s = b.substring(
							0, (backto < 0) ? b.length() : backto);

					// field separator
					st = BEGIN_OF_FIELD;
					if(handler.element(s, line, index++)) {
						return new Cont(st, index, line, c, ins);
					}
				} else if(c < 0 || c == config.newline) {
					String s = b.substring(
							0, (backto < 0) ? b.length() : backto);

					// end-of-line
					st = END_OF_LINE;
					if(handler.element(s, line, index++)) {
						return new Cont(st, index, line, c, ins);
					}
				} else if(config.charTrimed.indexOf(c) >= 0) {
					if(backto < 0) {
						backto = b.length();
					}
					b.append((char)c);
				} else {
					backto = -1;
					b.append((char)c);
				}
				break;
			case ENCLOSED_CHAR:   // quote in enclosed field
				c = ins.read();
				if(config.delimiters.indexOf(c) >= 0) {
					// field separator
					st = BEGIN_OF_FIELD;
					if(handler.element(b.toString(), line, index++)) {
						return new Cont(st, index, line, c, ins);
					}
				} else if(c < 0 || c == config.newline) {
					// end-of-line
					st = END_OF_LINE;
					if(handler.element(b.toString(), line, index++)) {
						return new Cont(st, index, line, c, ins);
					}
				} else if(c == config.quote) {
					// quote character itself
					b.append((char)config.quote);
					st = ENCLOSED;
				} else {
					st = PANIC_1;
					if(handler.error(new CSVParseException())) {
						return new Cont(st, index, line, c, ins);
					}
				}
				break;
			case COMMENT:
				c = ins.read();
				if(c < 0 || c == config.newline) {
					st = END_OF_LINE;
					if(handler.comment(b.toString(), line++)) {
						return new Cont(st, index, line, c, ins);
					}
				} else {
					b.append((char)c);
				}
				break;
			case END_OF_FILE:
				st = HALT;
				if(handler.endFile()) {
					return new Cont(st, index, line, c, ins);
				}
				break;
			case PANIC_1:   // 
				c = ins.read();
				if(config.delimiters.indexOf(c) >= 0) {
					index++;
					st = BEGIN_OF_FIELD;
				} else if(c < 0 || c == config.newline) {
					st = BEGIN_OF_LINE;
					if(handler.endLine(line++)) {
						return new Cont(st, index, line, c, ins);
					}
				}
				break;
			case HALT:
				return null;
			default:
				throw new RuntimeException("illegal state " + st);
			}
		}
	}

	/* (non-Javadoc)
	 * @see net.morilib.csv.CSVPushParser#parse(java.io.Reader)
	 */
	public CSVParserContinuation parse(
			Reader stream
			) throws IOException, CSVException {
		Cont cont = new Cont();

		cont.ins = new BufferedReader(stream);
		cont.c   = 0;
		return parse(cont);
	}

	/* (non-Javadoc)
	 * @see org.usei.csv.CSVParser#getSeparator()
	 */
	public String getSeparator() {
		return config.delimiters;
	}

	/* (non-Javadoc)
	 * @see org.usei.csv.CSVParser#getEncloser()
	 */
	public String getEncloser() {
		return new String(new char[] { (char)config.quote });
	}

	/**
	 * @return the countinuous
	 */
	public boolean isCountinuous() {
		return config.countinuous;
	}

	/* (non-Javadoc)
	 * @see org.usei.csv.CSVParser#getHandler()
	 */
	public CSVHandler getHandler() {
		return handler;
	}

}
