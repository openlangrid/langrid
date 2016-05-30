/*
 * $Id: ServiceAccessRightManagementClientImpl.java 370 2011-08-19 10:10:18Z t-nakaguchi $
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
package jp.go.nict.langrid.client.ws_1_2.management.impl;

import java.net.URL;

import javax.xml.rpc.ServiceException;

import jp.go.nict.langrid.client.ws_1_2.error.LangridException;
import jp.go.nict.langrid.client.ws_1_2.impl.ServiceClientImpl;
import jp.go.nict.langrid.client.ws_1_2.management.ServiceAccessRightManagementClient;
import jp.go.nict.langrid.service_1_2.foundation.serviceaccessrightmanagement.AccessRight;
import jp.go.nict.langrid.service_1_2.foundation.serviceaccessrightmanagement.AccessRightSearchResult;
import jp.go.nict.langrid.service_1_2.foundation.typed.Order;
import localhost.langrid_1_2.services.ServiceAccessRightManagement.ServiceAccessRightManagementServiceLocator;

import org.apache.axis.client.Stub;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 370 $
 */
public class ServiceAccessRightManagementClientImpl
	extends ServiceClientImpl
	implements ServiceAccessRightManagementClient
{
	/**
	 * 
	 * 
	 */
	public ServiceAccessRightManagementClientImpl(URL serviceUrl){
		super(serviceUrl);
	}

	@Override
	protected Stub createStub(URL url) throws ServiceException {
		ServiceAccessRightManagementServiceLocator locator = new ServiceAccessRightManagementServiceLocator();
		setUpService(locator);
		return (Stub)locator.getServiceAccessRightManagement(url);
	}

	public void clear() throws LangridException{
		invoke();
	}

	public AccessRightSearchResult searchAccessRights(
			int startIndex, int maxCount
			, String userId, String serviceId
			, Order[] orders)
		throws LangridException
	{
		return (AccessRightSearchResult)invoke(
				startIndex, maxCount, userId, serviceId, orders);
	}

	public AccessRight getMyAccessRight(String serviceId)
			throws LangridException {
		return (AccessRight)invoke(serviceId);
	}

	public void setAccessRight(String userId, String serviceId,
			boolean permitted)
	throws LangridException
	{
		invoke(userId, serviceId, permitted);
	}

	public void deleteAccessRight(String userId, String serviceId) throws LangridException {
		invoke(userId, serviceId);
	}

	private static final long serialVersionUID = 4496770310979165238L;
}
