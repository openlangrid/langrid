/*
 * $Id: ServiceMonitorClientImpl.java 370 2011-08-19 10:10:18Z t-nakaguchi $
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
import java.util.Calendar;

import javax.xml.rpc.ServiceException;

import jp.go.nict.langrid.client.ws_1_2.error.LangridException;
import jp.go.nict.langrid.client.ws_1_2.impl.ServiceClientImpl;
import jp.go.nict.langrid.client.ws_1_2.management.ServiceMonitorClient;
import jp.go.nict.langrid.service_1_2.foundation.servicemonitor.AccessLogSearchResult;
import jp.go.nict.langrid.service_1_2.foundation.servicemonitor.UserAccessEntrySearchResult;
import jp.go.nict.langrid.service_1_2.foundation.typed.Order;
import jp.go.nict.langrid.service_1_2.foundation.typed.Period;
import localhost.langrid_1_2.services.ServiceMonitor.ServiceMonitorServiceLocator;

import org.apache.axis.client.Stub;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 370 $
 */
public class ServiceMonitorClientImpl
extends ServiceClientImpl
implements ServiceMonitorClient
{
	/**
	 * 
	 * 
	 */
	public ServiceMonitorClientImpl(URL serviceUrl){
		super(serviceUrl);
	}

	@Override
	protected Stub createStub(URL url) throws ServiceException {
		ServiceMonitorServiceLocator locator = new ServiceMonitorServiceLocator();
		setUpService(locator);
		return (Stub)locator.getServiceMonitor(url);
	}

	public void clear() throws LangridException {
		invoke();
	}

	public AccessLogSearchResult searchAccessLogs(
			int startIndex, int maxCount
			, String userId, String serviceId
			, Calendar startDateTime, Calendar endDateTime
			, Order[] orders
			)
			throws LangridException
	{
		return (AccessLogSearchResult)invoke(
				startIndex, maxCount, userId, serviceId
				, startDateTime, endDateTime, orders
				);
	}

	public int[] getAccessCounts(String userId, String serviceId,
			Calendar baseDateTime, Period period) throws LangridException
	{
		return (int[])invoke(userId, serviceId, baseDateTime, period);
	}

	public UserAccessEntrySearchResult sumUpUserAccess(
			int startIndex, int maxCount
			, String serviceId
			, Calendar startDateTime
			, Calendar endDateTime
			, Period period
			, Order[] orders
			)
	throws LangridException
	{
		return (UserAccessEntrySearchResult)invoke(
				startIndex, maxCount, serviceId
				, startDateTime, endDateTime, period
				, orders
				);
	}

	private static final long serialVersionUID = 2712817164363471998L;
}
