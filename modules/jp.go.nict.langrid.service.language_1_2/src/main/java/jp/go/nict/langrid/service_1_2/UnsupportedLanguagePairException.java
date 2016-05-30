/*
 * $Id: UnsupportedLanguagePairException.java 567 2012-08-06 11:37:14Z t-nakaguchi $
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

import jp.go.nict.langrid.commons.rpc.intf.Field;

/**
 * 
 * Expression thrown when an unsupported language pair is specified.
 * 
 * @author  $Author: t-nakaguchi $
 * @version  $Revision: 567 $
 */
public class UnsupportedLanguagePairException extends InvalidParameterException{
	public UnsupportedLanguagePairException() {
	}

	/**
	 * 
	 * Constructor.
	 * @param parameterName1 Name of parameter that took the first language
	 * @param parameterName2 Name of parameter that took the second language
	 * @param language1 First language
	 * @param language2 Second language
	 * 
	 */
	public UnsupportedLanguagePairException(
			String parameterName1, String parameterName2
			, String language1, String language2)
	{
		super(parameterName1 + "," + parameterName2
				, "(" + language1 + "," + language2 + ") is not supported.");
		this.parameterName1 = parameterName1;
		this.parameterName2 = parameterName2;
		this.language1 = language1;
		this.language2 = language2;
	}

	/**
	 * 
	 * Returns the first parameter name.
	 * @return Name of first parameter
	 * 
	 */
	public String getParameterName1(){
		return parameterName1;
	}

	public void setParameterName1(String parameterName1) {
		this.parameterName1 = parameterName1;
	}

	/**
	 * 
	 * Returns the first parameter name.
	 * @return Name of first parameter
	 * 
	 */
	public String getParameterName2(){
		return parameterName2;
	}

	public void setParameterName2(String parameterName2) {
		this.parameterName2 = parameterName2;
	}

	/**
	 * 
	 * Gets the first language.
	 * @return First language
	 * 
	 */
	public String getLanguage1(){
		return language1;
	}

	public void setLanguage1(String language1) {
		this.language1 = language1;
	}

	/**
	 * 
	 * Gets the second language.
	 * @return Second language
	 * 
	 */
	public String getLanguage2(){
		return language2;
	}

	public void setLanguage2(String language2) {
		this.language2 = language2;
	}

	@Field(order=1)
	private String parameterName1;
	@Field(order=2)
	private String parameterName2;
	@Field(order=3)
	private String language1;
	@Field(order=4)
	private String language2;

	private static final long serialVersionUID = -6517548706875544091L;
}
