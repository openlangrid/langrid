/*
 * $Id: InvocationNotFoundException.java 563 2012-08-06 11:18:55Z t-nakaguchi $
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
package jp.go.nict.langrid.dao;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 563 $
 */
public class InvocationNotFoundException extends DaoException{
	/**
	 * 
	 * 
	 */
	public InvocationNotFoundException(
			String gridId, String serviceId, String invocationName) 
	{
		super("gridId: " + gridId + " serviceId: " + serviceId + " invocationName: " + invocationName);
		this.gridId = gridId;
	}

	/**
	 * 
	 * 
	 */
	public String getGridId() {
		return gridId;
	}
	
	public String getServiceId() {
		return serviceId;
	}
	
	public String getInvocationName() {
		return invocationName;
	}

	private String gridId;
	private String serviceId;
	private String invocationName;

	private static final long serialVersionUID = -5389549284569535958L;
}
