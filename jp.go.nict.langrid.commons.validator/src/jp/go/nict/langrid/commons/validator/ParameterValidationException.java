/*
 * $Id: ParameterValidationException.java 193 2010-10-02 11:34:46Z t-nakaguchi $
 * 
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2008 NICT Language Grid Project.
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
package jp.go.nict.langrid.commons.validator;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 193 $
 */
public class ParameterValidationException
extends ValidationException
{
	/**
	 * 
	 * 
	 */
	public ParameterValidationException(String parameterName){
		this.parameterName = parameterName;
	}

	/**
	 * 
	 * 
	 */
	public ParameterValidationException(String parameterName, String message){
		super(message);
		this.parameterName = parameterName;
	}

	/**
	 * 
	 * 
	 */
	public ParameterValidationException(String parameterName, Throwable cause){
		super(cause);
		this.parameterName = parameterName;
	}

	/**
	 * 
	 * 
	 */
	public ParameterValidationException(String parameterName, String message, Throwable cause){
		super(message, cause);
		this.parameterName = parameterName;
	}

	/**
	 * 
	 * 
	 */
	public String getParameterName(){
		return parameterName;
	}

	private String parameterName;

	private static final long serialVersionUID = -8211914037061510878L;
}
