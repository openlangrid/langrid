/*
 * $Id:ServiceActivator.java 5259 2007-09-06 10:10:27Z nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2009 NICT Language Grid Project.
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
package jp.go.nict.langrid.management.logic;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author:nakaguchi $
 * @version $Revision:5259 $
 */
public class ServiceNotDeactivatableException extends Exception {
	/**
	 * 
	 * 
	 */
	public ServiceNotDeactivatableException(String serviceId, String message){
		super(message);
		this.serviceId = serviceId;
	}

	/**
	 * 
	 * 
	 */
	public ServiceNotDeactivatableException(String serviceId, Throwable cause){
		super(cause);
		this.serviceId = serviceId;
	}

	/**
	 * 
	 * 
	 */
	public String getServiceId(){
		return serviceId;
	}

	private String serviceId;

	private static final long serialVersionUID = 1096056817205471100L;
}
