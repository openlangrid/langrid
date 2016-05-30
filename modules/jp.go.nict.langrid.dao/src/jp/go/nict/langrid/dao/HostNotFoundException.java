/*
 * $Id: HostNotFoundException.java 298 2010-12-01 01:37:10Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2009 NICT Language Grid Project.
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
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 298 $
 */
public class HostNotFoundException extends DaoException{
	/**
	 * 
	 * 
	 */
	public HostNotFoundException(String hostId) {
		super(toMessage(hostId));
		this.hostId = hostId;
	}

	/**
	 * 
	 * 
	 */
	public HostNotFoundException(String hostId, Throwable cause){
		super(toMessage(hostId), cause);
		this.hostId = hostId;
	}

	/**
	 * 
	 * 
	 */
	public String getHostId(){
		return hostId;
	}

	private String hostId;

	private static String toMessage(String hostId){
		return "host \"" + hostId + "\" is not found.";
	}
	private static final long serialVersionUID = -5819337668097502754L;
}
