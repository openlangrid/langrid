/*
 * $Id: ServiceNotFoundException.java 490 2012-05-24 02:44:18Z t-nakaguchi $
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
 * Exception thrown when the specified service is not found.
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 490 $
 */
public class ServiceNotFoundException extends LangridException {
	public ServiceNotFoundException() {
	}

	/**
	 * 
	 * Constructor.
	 * @param serviceId Service ID
	 * 
	 */
	public ServiceNotFoundException(String serviceId) {
		super("Service " + serviceId + " not found.");
		this.serviceId = serviceId;
	}

	/**
	 * 
	 * Constructor.
	 * @param serviceId Service ID
	 * @param description Message
	 * 
	 */
	public ServiceNotFoundException(String serviceId, String description) {
		super(description);
		this.serviceId = serviceId;
	}

	/**
	 * 
	 * Gets service ID.
	 * @return Service ID
	 * 
	 */
	public String getServiceId(){
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	private String serviceId;

	private static final long serialVersionUID = -8648344753287008478L;
}
