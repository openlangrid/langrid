/*
 * $Id:FileTransInfoDao.java 4384 2007-04-03 08:56:48Z nakaguchi $
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
package jp.go.nict.langrid.client.ws_1_2.management;

import jp.go.nict.langrid.client.ws_1_2.ServiceClient;
import jp.go.nict.langrid.client.ws_1_2.error.LangridException;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.Endpoint;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 370 $
 */
public interface ExternalServiceManagementClient
extends ServiceClient
{
	/**
	 * 
	 * 
	 */
	Endpoint[] listActualEndpoints(String serviceId)
		throws LangridException;

	/**
	 * 
	 * 
	 */
	void addActualEndpoint(String serviceId, Endpoint endpoint)
	throws LangridException;

	/**
	 * 
	 * 
	 */
	void deleteActualEndpoint(String serviceId, String url, String userName)
	throws LangridException;

	/**
	 * 
	 * 
	 */
	void enableActualEndpoint(
			String serviceId, String url, String userName
			)
	throws LangridException;

	/**
	 * 
	 * 
	 */
	void disableActualEndpoint(
			String serviceId, String url, String userName
			)
	throws LangridException;
}
