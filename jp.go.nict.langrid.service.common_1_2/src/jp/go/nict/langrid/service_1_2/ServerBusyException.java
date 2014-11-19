/*
 * $Id: ServerBusyException.java 490 2012-05-24 02:44:18Z t-nakaguchi $
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
 * Exception thrown when the server is unresponsive from load.
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 490 $
 */
public class ServerBusyException  extends LangridException{
	public ServerBusyException() {
	}

	/**
	 * 
	 * Constructor.
	 * @param description Explanation of exception
	 * 
	 */
	public ServerBusyException(String description){
		super(description);
	}

	/**
	 * 
	 * A constructor that takes the underlying exception as a parameter.
	 * The explanation of this exception is made up from the exception's message and stack trace.
	 * @param cause The underlying exception
	 * 
	 */
	public ServerBusyException(Throwable cause){
		super(cause);
	}

	private static final long serialVersionUID = 1188359654297779847L;
}
