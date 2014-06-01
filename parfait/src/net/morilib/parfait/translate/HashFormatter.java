/*
 * Copyright 2014 Yuichiro Moriguchi
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
package net.morilib.parfait.translate;

import java.io.PrintWriter;
import java.util.Map;

public interface HashFormatter {

	/**
	 * 
	 * @param wr
	 * @param hf
	 * @param columns
	 * @param pluslen
	 * @param ignoreCase
	 * @param name
	 * @param map
	 * @param defaultAction
	 * @param license
	 * @param prologue
	 * @param desc
	 * @param aux
	 * @return
	 */
	public boolean print(PrintWriter wr,
			HashFormatter hf, String columns, boolean pluslen,
			boolean ignoreCase, String name, Map<String, String> map,
			String defaultAction, String license, String prologue,
			String desc, String aux);

	/**
	 * 
	 * @param s
	 * @return
	 */
	public String getTargetFilename(String s);

}
