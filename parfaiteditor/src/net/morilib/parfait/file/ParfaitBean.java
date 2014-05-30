/*
 * Copyright 2009-2014 Yuichiro Moriguchi
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

public class ParfaitBean {

	/**
	 * 
	 */
	public static final String R_ACTION  = "action";

	/**
	 * 
	 */
	public static final String R_LOOKUP  = "lookup";

	/**
	 * 
	 */
	public static final String R_MAP     = "map";

	/**
	 * 
	 */
	public static final String R_DEFAULT = R_ACTION;

	//
	KeywordsBean keywords = new KeywordsBean();
	DescriptionBean description = new DescriptionBean();
	AuxiliaryCodeBean auxiliary = new AuxiliaryCodeBean();
	String functionType = R_DEFAULT, defaultAction, columns;
	boolean automatically = true, plusLength = false;

	/**
	 * 
	 * @return
	 */
	public KeywordsBean getKeywords() {
		return keywords;
	}

	/**
	 * 
	 * @return
	 */
	public DescriptionBean getDescription() {
		return description;
	}

	/**
	 * 
	 * @return
	 */
	public AuxiliaryCodeBean getAuxiliary() {
		return auxiliary;
	}

	/**
	 * 
	 * @return
	 */
	public String getFunctionType() {
		return functionType;
	}

	/**
	 * 
	 * @return
	 */
	public String getDefaultAction() {
		return defaultAction;
	}

	/**
	 * 
	 * @return
	 */
	public String getColumns() {
		return columns;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isAutomatically() {
		return automatically;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isPlusLength() {
		return plusLength;
	}

}
