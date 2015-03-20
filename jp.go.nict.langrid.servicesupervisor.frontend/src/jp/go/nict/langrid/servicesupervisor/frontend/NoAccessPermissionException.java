/*
 * $Id:Log.java 5456 2007-10-01 07:53:07Z nakaguchi $
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
package jp.go.nict.langrid.servicesupervisor.frontend;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 247 $
 */
public class NoAccessPermissionException extends Exception {
	/**
	 * 
	 * 
	 */
	public NoAccessPermissionException(String userGridId, String userId){
		this.userGridId = userGridId;
		this.userId = userId;
	}

	/**
	 * 
	 * 
	 */
	public NoAccessPermissionException(String userGridId, String userId, String message){
		super(message);
		this.userGridId = userGridId;
		this.userId = userId;
	}

	/**
	 * 
	 * 
	 */
	public String getUserGridId(){
		return userGridId;
	}

	/**
	 * 
	 * 
	 */
	public String getUserId(){
		return userId;
	}

	private String userGridId;
	private String userId;

	private static final long serialVersionUID = 7477420805076896187L;
}
