/*
 * $Id: TemporaryUserManagementClientImpl.java 370 2011-08-19 10:10:18Z t-nakaguchi $
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
import jp.go.nict.langrid.client.ws_1_2.management.TemporaryUserManagementClient;
import jp.go.nict.langrid.service_1_2.foundation.typed.MatchingCondition;
import jp.go.nict.langrid.service_1_2.foundation.typed.Order;
import jp.go.nict.langrid.service_1_2.foundation.usermanagement.TemporaryUserEntrySearchResult;
import localhost.langrid_1_2.services.TemporaryUserManagement.TemporaryUserManagementServiceLocator;

import org.apache.axis.client.Stub;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 370 $
 */
public class TemporaryUserManagementClientImpl
extends ServiceClientImpl implements TemporaryUserManagementClient{
	/**
	 * 
	 * 
	 */
	public TemporaryUserManagementClientImpl(URL serviceUrl){
		super(serviceUrl);
	}

	@Override
	protected Stub createStub(URL url) throws ServiceException {
		TemporaryUserManagementServiceLocator locator = new TemporaryUserManagementServiceLocator();
		setUpService(locator);
		return (Stub)locator.getTemporaryUserManagement(url);
	}

	public void clear() throws LangridException{
		invoke();
	}

	public void clearExpiredUsers()	throws LangridException{
		invoke();
	}

	public TemporaryUserEntrySearchResult searchTemporaryUsers(
			int startIndex, int maxCount, MatchingCondition[] conditions
			, Order[] orders
			)
	throws LangridException{
		return (TemporaryUserEntrySearchResult)invoke(
				startIndex, maxCount, conditions, orders);
	}

	public void addTemporaryUser(
			String userId, String password
			, Calendar beginAvailableDateTime)
	throws LangridException{
		invoke(userId, password, beginAvailableDateTime);
	}

	public void deleteTemporaryUser(String userId)
	throws LangridException{
		invoke(userId);
	}

	public Calendar getBeginAvailableDateTime(String userId)
	throws LangridException{
		return (Calendar)invoke(userId);
	}

	public Calendar getEndAvailableDateTime(String userId)
	throws LangridException{
		return (Calendar)invoke(userId);
	}

	public void setAvailableDateTimes(String userId
			, Calendar beginAvailableDateTime
			, Calendar endAvailableDateTime)
	throws LangridException{
		invoke(userId, beginAvailableDateTime, endAvailableDateTime);
	}

	private static final long serialVersionUID = -8777234703676562289L;
}
