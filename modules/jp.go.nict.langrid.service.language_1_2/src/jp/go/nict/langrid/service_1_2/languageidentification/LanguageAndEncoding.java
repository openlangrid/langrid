/*
 * $Id: LanguageAndEncoding.java 567 2012-08-06 11:37:14Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2010 NICT Language Grid Project.
 *
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by 
 * the Free Software Foundation, either version 2.1 of the License, or (at 
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser 
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License 
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package jp.go.nict.langrid.service_1_2.languageidentification;

import jp.go.nict.langrid.commons.rpc.intf.Field;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 567 $
 */
public class LanguageAndEncoding {
	/**
	 * 
	 * 
	 */
	public LanguageAndEncoding(String language, String encoding) {
		this.language = language;
		this.encoding = encoding;
	}

	/**
	 * 
	 * 
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * 
	 * 
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * 
	 * 
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * 
	 * 
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	@Field(order=1)
	private String encoding;
	@Field(order=2)
	private String language;
}
