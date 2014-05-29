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
 * @author MORIGUCHI, Yuichiro 2006/05/04
 */
public interface CSVHandler {

	/**
	 * 
	 * @return
	 */
	public boolean startFile() throws CSVException;

	/**
	 * 
	 * @return
	 */
	public boolean endFile() throws CSVException;

	/**
	 * 
	 * @return
	 */
	public boolean startLine(int line) throws CSVException;

	/**
	 * 
	 * @return
	 */
	public boolean endLine(int line) throws CSVException;

	/**
	 * 
	 * @param line
	 */
	public boolean emptyLine(int line) throws CSVException;

	/**
	 * 
	 * @param elm
	 * @param line
	 * @param field
	 */
	public boolean element(
			String elm, int line, int field) throws CSVException;

	/**
	 * 
	 * @param com
	 * @param line
	 */
	public boolean comment(String com, int line) throws CSVException;

	/**
	 * 
	 * @param e
	 */
	public boolean error(CSVParseException e) throws CSVException;

}
