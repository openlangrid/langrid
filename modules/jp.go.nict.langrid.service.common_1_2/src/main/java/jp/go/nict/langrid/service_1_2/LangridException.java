/*
 * $Id: LangridException.java 1329 2014-12-19 15:10:21Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2008 NICT Language Grid Project.
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
package jp.go.nict.langrid.service_1_2;

import jp.go.nict.langrid.commons.lang.ExceptionUtil;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1329 $
 */
public class LangridException extends Exception {
	public LangridException() {
	}

	/**
	 * 
	 * 
	 */
	public LangridException(String description) {
		super(description);
		this.description = description;
	}

	/**
	 * 
	 * 
	 */
	public LangridException(Throwable cause){
		this(ExceptionUtil.getMessageWithStackTrace(cause));
	}

	/**
	 * 
	 * 
	 */
	public LangridException(String message, Throwable cause){
		this(message + ": "
				+ ExceptionUtil.getMessageWithStackTrace(cause)
				);
	}

	/**
	 * 
	 * 
	 */
	public String getDescription(){
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String toString() {
		String s = getClass().getName();
		return (description != null) ? (s + ": " + description) : s;
	}

	private String description;

	private static final long serialVersionUID = 1698537889799018779L;
}
