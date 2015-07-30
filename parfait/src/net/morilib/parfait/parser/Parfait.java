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
package net.morilib.parfait.parser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 *
 * @author MORIGUCHI, Yuichiro 2015/08/01
 */
public class Parfait {

	/**
	 * 
	 */
	public static final String VERSION = "0.0.2";

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		BufferedReader rd = null;
		ParseParfait m;

		try {
			if(args.length < 1) {
				ParfaitException.usage(System.err, VERSION);
				System.exit(1);
			} else {
				try {
					m = new ParseParfait();
					rd = new BufferedReader(new RemoveCCommentReader(
							new InputStreamReader(new FileInputStream(
									args[0]))));
					m.execute(rd, args[0]);
				} catch(FileNotFoundException e) {
					throw new ParfaitException("filenotfound",
							args[0]);
				} catch(IOException e) {
					throw new ParfaitException("ioexception");
				} finally {
					if(rd != null) {
						try {
							rd.close();
						} catch(IOException e) {
							throw new ParfaitException("ioexception");
						}
					}
				}
			}
		} catch(ParfaitException e) {
			e.perror(System.err);
			System.exit(2);
		}
	}

}
