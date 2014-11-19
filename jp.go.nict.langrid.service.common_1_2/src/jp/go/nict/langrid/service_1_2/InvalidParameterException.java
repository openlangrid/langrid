/*
 * $Id: InvalidParameterException.java 490 2012-05-24 02:44:18Z t-nakaguchi $
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
 * Exception thrown on an invalid parameter.
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 490 $
 */
public class InvalidParameterException extends LangridException {
	public InvalidParameterException() {
	}

	/**
	 * 
	 * Constructor.
	 * @param parameterName Parameter name
	 * @param description Message
	 * 
	 */
	public InvalidParameterException(String parameterName, String description) {
		super(parameterName + ": " + description);
		this.parameterName = parameterName;
	}

	/**
	 * 
	 * Gets parameter name.
	 * @return Parameter name
	 * 
	 */
	public String getParameterName(){
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	private String parameterName;

	private static final long serialVersionUID = 7993576411995482085L;
}
