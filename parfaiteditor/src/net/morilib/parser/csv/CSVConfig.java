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

/**
 *
 *
 * @author MORIGUCHI, Yuichiro 2010/09/18
 */
public final class CSVConfig {

	//
	String  delimiters, charTrimed = "";
	int     quote;
	int     newline = '\n';
	boolean countinuous;
	boolean commentable = false;
	int     comment = -1;

	/**
	 * 
	 * @param delimiters
	 * @param quote
	 * @param newline
	 */
	public CSVConfig(
			String delimiters,
			int quote,
			boolean countinuous,
			int comment,
			String charTrimed,
			int newline,
			boolean commentable) {
		this.delimiters  = delimiters;
		this.quote       = quote;
		this.countinuous = countinuous;
		this.comment     = comment;
		this.charTrimed  = charTrimed;
		this.newline     = newline;
		this.commentable = commentable;
	}

	/**
	 * 
	 * @param delimiters
	 * @param quote
	 * @param newline
	 */
	public CSVConfig(
			CSVHandler handler,
			String delimiters,
			int quote,
			boolean countinuous,
			int comment) {
		this.delimiters  = delimiters;
		this.quote       = quote;
		this.countinuous = countinuous;
		this.comment     = comment;
	}

	/**
	 * 
	 * @param delimiters
	 * @param quote
	 * @param newline
	 */
	public CSVConfig(
			String delimiters,
			int quote,
			boolean countinuous) {
		this.delimiters  = delimiters;
		this.quote       = quote;
		this.countinuous = countinuous;
	}

}
