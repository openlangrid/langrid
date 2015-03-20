/*
 * $Id: UserManagementClient.java 370 2011-08-19 10:10:18Z t-nakaguchi $
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

import java.util.Calendar;

import jp.go.nict.langrid.client.ws_1_2.ServiceClient;
import jp.go.nict.langrid.client.ws_1_2.error.LangridException;
import jp.go.nict.langrid.service_1_2.foundation.Attribute;
import jp.go.nict.langrid.service_1_2.foundation.typed.MatchingCondition;
import jp.go.nict.langrid.service_1_2.foundation.typed.Order;
import jp.go.nict.langrid.service_1_2.foundation.usermanagement.UserEntrySearchResult;
import jp.go.nict.langrid.service_1_2.foundation.usermanagement.UserProfile;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 370 $
 */
public interface UserManagementClient extends ServiceClient{
	/**
	 * 
	 * 
	 */
	void initialize()
		throws LangridException;

	/**
	 * 
	 * 
	 */
	UserEntrySearchResult searchUsers(
			int startIndex, int maxCount
			, MatchingCondition[] conditions, Order[] orders)
			throws LangridException;

	/**
	 * 
	 * 
	 */
	public UserEntrySearchResult searchUsersShouldChangePassword(
			int startIndex, int maxCount, int days
			, Order[] orders)
	throws LangridException;

	/**
	 * 
	 * 
	 */
	void addUser(String userId, String password
			, boolean canCallServices
			, UserProfile profile, Attribute[] attributes)
	throws LangridException;

	/**
	 * 
	 * 
	 */
	void deleteUser(String userId)
			throws LangridException;

	/**
	 * 
	 * 
	 */
	UserProfile getUserProfile(String userId)
			throws LangridException;

	/**
	 * 
	 * 
	 */
	void setUserProfile(String userId, UserProfile profile)
			throws LangridException;

	/**
	 * 
	 * 
	 */
	Attribute[] getUserAttributes(String userId, String[] attributeNames)
	throws LangridException;

	/**
	 * 
	 * 
	 */
	void setUserAttributes(String userId, Attribute[] attributes)
	throws LangridException;

	/**
	 * 
	 * 
	 */
	void setUserPassword(String userId, String password)
	throws LangridException;

	/**
	 * 
	 * 
	 */
	Calendar getPasswordChangedDate(String userId)
	throws LangridException;

	/**
	 * 
	 * 
	 */
	String getUserDigestedPassword(String userId)
	throws LangridException;

	/**
	 * 
	 * 
	 */
	String getUserPassword(String userId)
	throws LangridException;

	/**
	 * 
	 * 
	 */
	boolean getUserCanCallServices(String userId)
	throws LangridException;

	/**
	 * 
	 * 
	 */
	void setUserCanCallServices(String userId, boolean canCallServices)
	throws LangridException;
}
