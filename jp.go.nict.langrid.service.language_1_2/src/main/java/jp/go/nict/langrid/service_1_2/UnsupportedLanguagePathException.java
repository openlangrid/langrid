/*
 * $Id: UnsupportedLanguagePathException.java 567 2012-08-06 11:37:14Z t-nakaguchi $
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

import jp.go.nict.langrid.commons.lang.StringUtil;
import jp.go.nict.langrid.commons.rpc.intf.Field;

/**
 * 
 * Expression thrown when an unsupported language path is specified.
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 567 $
 */
public class UnsupportedLanguagePathException extends InvalidParameterException {
	public UnsupportedLanguagePathException() {
	}

	/**
	 * 
	 * Constructor.
	 * @param parameterNames Array of parameter names
	 * @param languagePath The specified language path
	 * 
	 */
	public UnsupportedLanguagePathException(
			String[] parameterNames, LanguagePath languagePath
			){
		super(
				StringUtil.join(parameterNames, ",")
				, "(" + StringUtil.join(languagePath.getLanguages(), ",")
				+ ") is not supported.");
		this.parameterNames = parameterNames;
		this.languagePath = languagePath;
	}

	/**
	 * 
	 * Returns an array of the parameter names making up the language path.
	 * @return Array of parameter names
	 * 
	 */
	public String[] getParameterNames(){
		return parameterNames;
	}

	public void setParameterNames(String[] parameterNames) {
		this.parameterNames = parameterNames;
	}

	/**
	 * 
	 * Gets the specified language path.
	 * @return Language path candidates
	 * 
	 */
	public LanguagePath getLanguagePath(){
		return languagePath;
	}

	public void setLanguagePath(LanguagePath languagePath) {
		this.languagePath = languagePath;
	}

	@Field(order=1)
	private String[] parameterNames;
	@Field(order=2)
	private LanguagePath languagePath;

	private static final long serialVersionUID = -6666258761111283762L;
}
