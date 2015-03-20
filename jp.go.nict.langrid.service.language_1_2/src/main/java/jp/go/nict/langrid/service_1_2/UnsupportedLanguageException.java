/*
 * $Id: UnsupportedLanguageException.java 491 2012-05-24 02:50:11Z t-nakaguchi $
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

/**
 * 
 * Expression thrown when an unsupported language is specified.
 * 
 * @author  $Author: t-nakaguchi $
 * @version  $Revision: 491 $
 */
public class UnsupportedLanguageException extends InvalidParameterException{
	public UnsupportedLanguageException() {
	}

	/**
	 * 
	 * Constructor.
	 * @param parameterName Name of the parameter that took the language expression
	 * @param language Language
	 * 
	 */
	public UnsupportedLanguageException(String parameterName, String language){
		super(parameterName, language + " is not supported.");
		this.language = language;
	}

	/**
	 * 
	 * Gets language.
	 * @return Language
	 * 
	 */
	public String getLanguage(){
		return language;
	}
	
	public void setLanguage(String language) {
		this.language = language;
	}

	private String language;

	private static final long serialVersionUID = 6116264313890243928L;
}
