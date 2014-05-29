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
package net.morilib.parfait.file;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KeywordsBean {

	//
	List<KeywordBean> keys = new ArrayList<KeywordBean>();

	/**
	 * 
	 * @return
	 */
	public List<KeywordBean> getList() {
		return Collections.unmodifiableList(keys);
	}

	/**
	 * 
	 * @param f
	 */
	public void add(KeywordBean f) {
		keys.add(f);
	}

	/**
	 * 
	 * @param f
	 */
	public void remove(KeywordBean f) {
		int x;

		if((x = keys.indexOf(f)) >= 0)  keys.remove(x);
	}

	/**
	 * 
	 * @return
	 */
	public int size() {
		return keys.size();
	}

	/**
	 * 
	 * @param f
	 * @return
	 */
	public int indexOf(KeywordBean f) {
		return keys.indexOf(f);
	}

	/**
	 * 
	 */
	public void clear() {
		keys.clear();
	}

	/**
	 * 
	 * @param k
	 * @return
	 */
	public KeywordBean get(int k) {
		return keys.get(k);
	}

}
