/*
 * $Id: OverUseMonitorClientImpl.java 370 2011-08-19 10:10:18Z t-nakaguchi $
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
import jp.go.nict.langrid.client.ws_1_2.management.OverUseMonitorClient;
import jp.go.nict.langrid.service_1_2.foundation.overusemonitoring.OverUseLimit;
import jp.go.nict.langrid.service_1_2.foundation.overusemonitoring.OverUseStateSearchResult;
import jp.go.nict.langrid.service_1_2.foundation.serviceaccesslimitmanagement.typed.LimitType;
import jp.go.nict.langrid.service_1_2.foundation.typed.Order;
import jp.go.nict.langrid.service_1_2.foundation.typed.Period;
import localhost.langrid_1_2.services.OverUseMonitoring.OverUseMonitoringServiceLocator;

import org.apache.axis.client.Stub;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 370 $
 */
public class OverUseMonitorClientImpl
extends ServiceClientImpl
implements OverUseMonitorClient
{
	/**
	 * 
	 * 
	 */
	public OverUseMonitorClientImpl(URL serviceUrl){
		super(serviceUrl);
	}

	@Override
	protected Stub createStub(URL url) throws ServiceException {
		OverUseMonitoringServiceLocator locator = new OverUseMonitoringServiceLocator();
		setUpService(locator);
		return (Stub)locator.getOverUseMonitoring(url);
	}

	public void clearOverUseLimits() throws LangridException {
		invoke();
	}

	public OverUseLimit[] listOverUseLimits(Order[] orders)
			throws LangridException {
		return (OverUseLimit[])invoke((Object)orders);
	}

	public void setOverUseLimit(Period period, LimitType limitType,
			int limitValue) throws LangridException {
		invoke(period, limitType, limitValue);
	}

	public void deleteOverUseUseLimit(Period period, LimitType limitType)
	throws LangridException {
		invoke(period, limitType);
	}

	public OverUseStateSearchResult searchOverUseState(int startIndex,
			int maxCount, Calendar startDateTime, Calendar endDateTime,
			Order[] orders) throws LangridException {
		return (OverUseStateSearchResult)invoke(
				startIndex, maxCount, startDateTime, endDateTime
				, orders
				);
	}

	private static final long serialVersionUID = 3117002344724126423L;
}
