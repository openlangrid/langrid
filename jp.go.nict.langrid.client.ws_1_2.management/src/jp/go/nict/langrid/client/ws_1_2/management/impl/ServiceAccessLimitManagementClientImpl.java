/*
 * $Id: ServiceAccessLimitManagementClientImpl.java 370 2011-08-19 10:10:18Z t-nakaguchi $
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
import jp.go.nict.langrid.client.ws_1_2.management.ServiceAccessLimitManagementClient;
import jp.go.nict.langrid.service_1_2.foundation.serviceaccesslimitmanagement.AccessLimit;
import jp.go.nict.langrid.service_1_2.foundation.serviceaccesslimitmanagement.AccessLimitSearchResult;
import jp.go.nict.langrid.service_1_2.foundation.serviceaccesslimitmanagement.typed.LimitType;
import jp.go.nict.langrid.service_1_2.foundation.typed.Order;
import jp.go.nict.langrid.service_1_2.foundation.typed.Period;
import localhost.langrid_1_2.services.ServiceAccessLimitManagement.ServiceAccessLimitManagementServiceLocator;

import org.apache.axis.client.Stub;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 370 $
 */
public class ServiceAccessLimitManagementClientImpl
extends ServiceClientImpl
implements ServiceAccessLimitManagementClient
{
	/**
	 * 
	 * 
	 */
	public ServiceAccessLimitManagementClientImpl(URL serviceUrl){
		super(serviceUrl);
	}

	@Override
	protected Stub createStub(URL url) throws ServiceException {
		ServiceAccessLimitManagementServiceLocator locator = new ServiceAccessLimitManagementServiceLocator();
		setUpService(locator);
		return (Stub)locator.getServiceAccessLimitManagement(url);
	}

	public void clear() throws LangridException{
		invoke();
	}

	public AccessLimitSearchResult searchAccessLimits(
			int startIndex, int maxCount
			, String userId, String serviceId
			, Order[] orders
			)
		throws LangridException
	{
		return (AccessLimitSearchResult)invoke(
				startIndex, maxCount, userId, serviceId, orders);
	}

	public AccessLimit[] getMyAccessLimits(String serviceId)
			throws LangridException {
		return (AccessLimit[])invoke(serviceId);
	}

	public void setAccessLimit(String userId, String serviceId, Period period,
			LimitType limitType, int limitCount)
	throws LangridException {
		invoke(userId, serviceId, period, limitType, limitCount);
	}

	public void deleteAccessLimit(String userId, String serviceId, Period period,
			LimitType limitType) throws LangridException {
		invoke(userId, serviceId, period, limitType);
	}

	private static final long serialVersionUID = 1190682370359038106L;
}
