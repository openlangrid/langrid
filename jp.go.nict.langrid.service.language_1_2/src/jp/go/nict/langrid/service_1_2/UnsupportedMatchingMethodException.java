/*
 * $Id: UnsupportedMatchingMethodException.java 567 2012-08-06 11:37:14Z t-nakaguchi $
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
 * Exception thrown when the specified matching method is unsupported.
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 567 $
 */
public class UnsupportedMatchingMethodException
extends InvalidParameterException
{
	public UnsupportedMatchingMethodException() {
	}

	/**
	 * 
	 * Constructor.
	 * @param parameterName Parameter name
	 * @param method Specified matching method
	 * @param validMethods Supported matching methods
	 * 
	 */
	public UnsupportedMatchingMethodException(
			String parameterName, String method
			, String[] validMethods)
	{
		super(
				parameterName
				, "matching method \"" + method + "\" is not supported."
				);
		this.method = method;
		this.validMethods = validMethods;
	}

	/**
	 * 
	 * Gets the specified matching method.
	 * @return Specified matching method
	 * 
	 */
	public String getMatchingMethod(){
		return method;
	}

	public void setMatchingMethod(String method) {
		this.method = method;
	}

	/**
	 * 
	 * Returns supported matching methods.
	 * @return Supported matching methods
	 * 
	 */
	public String[] getValidMethods(){
		return validMethods;
	}

	public void setValidMethods(String[] validMethods) {
		this.validMethods = validMethods;
	}

	@Field(order=1)
	private String method;
	@Field(order=2)
	private String[] validMethods;

	private static final long serialVersionUID = 5703950337140918989L;
}
