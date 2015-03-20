/*
 * $Id: UserManagementClientImpl.java 370 2011-08-19 10:10:18Z t-nakaguchi $
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
import jp.go.nict.langrid.client.ws_1_2.management.UserManagementClient;
import jp.go.nict.langrid.service_1_2.foundation.Attribute;
import jp.go.nict.langrid.service_1_2.foundation.typed.MatchingCondition;
import jp.go.nict.langrid.service_1_2.foundation.typed.Order;
import jp.go.nict.langrid.service_1_2.foundation.usermanagement.UserEntrySearchResult;
import jp.go.nict.langrid.service_1_2.foundation.usermanagement.UserProfile;
import localhost.langrid_1_2.services.UserManagement.UserManagementServiceLocator;

import org.apache.axis.client.Stub;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 370 $
 */
public class UserManagementClientImpl
extends ServiceClientImpl implements UserManagementClient{
	/**
	 * 
	 * 
	 */
	public UserManagementClientImpl(URL serviceUrl){
		super(serviceUrl);
	}

	@Override
	protected Stub createStub(URL url) throws ServiceException {
		UserManagementServiceLocator locator = new UserManagementServiceLocator();
		setUpService(locator);
		return (Stub)locator.getUserManagement(url);
	}

	public void initialize()
		throws LangridException
	{
		invoke();
	}

	public UserEntrySearchResult searchUsers(int startIndex, int maxCount
			, MatchingCondition[] conditions, Order[] orders)
		throws LangridException
	{
		return (UserEntrySearchResult)invoke(
				startIndex, maxCount, conditions, orders);
	}

	public UserEntrySearchResult searchUsersShouldChangePassword(
			int startIndex, int maxCount, int days, Order[] orders)
	throws LangridException
	{
		return (UserEntrySearchResult)invoke(
				startIndex, maxCount, days, orders);
	}

	public void addUser(
			String userId, String password
			, boolean canCallServices
			, UserProfile profile, Attribute[] attributes
			)
		throws LangridException
	{
		invoke(userId, password, canCallServices, profile, attributes);
	}

	public void deleteUser(String userId)
		throws LangridException
	{
		invoke(userId);
	}

	public UserProfile getUserProfile(String userId)
		throws LangridException
	{
		return (UserProfile)invoke(userId);
	}

	public void setUserProfile(String userId, UserProfile profile)
		throws LangridException
	{
		invoke(userId, profile);
	}

	public Attribute[] getUserAttributes(String userId, String[] attributeNames)
	throws LangridException {
		return (Attribute[])invoke(userId, attributeNames);
	}

	public void setUserAttributes(String userId, Attribute[] attributes)
	throws LangridException {
		invoke(userId, attributes);
	}

	public void setUserPassword(String userId, String password)
	throws LangridException{
		invoke(userId, password);
	}

	public Calendar getPasswordChangedDate(String userId)
	throws LangridException {
		return (Calendar)invoke(userId);
	}

	public String getUserDigestedPassword(String userId)
	throws LangridException{
		return (String)invoke(userId);
	}

	public String getUserPassword(String userId)
	throws LangridException{
		return (String)invoke(userId);
	}

	public boolean getUserCanCallServices(String userId)
	throws LangridException {
		return (Boolean)invoke(userId);
	}

	public void setUserCanCallServices(String userId, boolean canCallServices)
	throws LangridException {
		invoke(userId, canCallServices);
	}

	private static final long serialVersionUID = -7807722619271249512L;
}
