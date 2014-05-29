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

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * エンコードの自動判別器を取得するクラスです。
 * 
 * 
 * @author MORIGUCHI, Yuichiro 2013/04/27
 */
public final class EncodingDetectorFactory {

	private static final String BASENAME =
		"net.morilib.util.encoding.EncodingDetectorResource";

	//
	private EncodingDetectorFactory() {}

	/**
	 * エンコードの自動判別器を得ます。
	 * 
	 * @param locale ロケール
	 * @return 自動判別器
	 */
	public static EncodingDetector getInstance(Locale locale) {
		Object o;

		o = ResourceBundle.getBundle(BASENAME, locale);
		return (EncodingDetector)o;
	}

	/**
	 * デフォルトのロケールのエンコードの自動判別器を得ます。
	 * 
	 * @return 自動判別器
	 */
	public static EncodingDetector getInstance() {
		return getInstance(Locale.getDefault());
	}

}
