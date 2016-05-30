/*
 * $Id: UserManagementService.java 225 2010-10-03 00:23:14Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_3.foundation.usermanagement;

import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.ServiceConfigurationException;
import jp.go.nict.langrid.service_1_2.UnknownException;
import jp.go.nict.langrid.service_1_2.foundation.Attribute;
import jp.go.nict.langrid.service_1_2.foundation.MatchingCondition;
import jp.go.nict.langrid.service_1_2.foundation.Order;
import jp.go.nict.langrid.service_1_2.foundation.usermanagement.UserEntrySearchResult;
import jp.go.nict.langrid.service_1_2.foundation.usermanagement.UserNotFoundException;
import jp.go.nict.langrid.service_1_2.foundation.usermanagement.UserProfile;

/**
 * 
 * The interface of the user administration service.
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 225 $
 */
public interface UserManagementService
extends jp.go.nict.langrid.service_1_2.foundation.usermanagement.UserManagementService{
	/**
	 * 
	 * 
	 */
	UserEntrySearchResult searchAssociatedUsers(
			int startIndex, int maxCount, String gridId
			, MatchingCondition[] conditions, Order[] orders
			)
			throws AccessLimitExceededException, InvalidParameterException
			, NoAccessPermissionException, ServiceConfigurationException
			, UnknownException;

	/**
	 * 
	 * 
	 */
	UserProfile getAssociatedUserProfile(String gridId, String userId)
	throws AccessLimitExceededException, InvalidParameterException
	, NoAccessPermissionException, ServiceConfigurationException
	, UnknownException, UserNotFoundException;

	/**
	 * 
	 * Gets user's attribute data.
	 * @param userId User ID
	 * @param attributeNames Array of names of acquired attributes
	 * @return User attribute data
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException The service setup was not done appropriately
	 * @throws UnknownException An unexpected exception occurred
	 * @throws UserNotFoundException The specified user was not found
	 * 
	 */
	Attribute[] getAssociatedUserAttributes(String gridId, String userId, String[] attributeNames)
	throws AccessLimitExceededException, InvalidParameterException
	, NoAccessPermissionException, ServiceConfigurationException
	, UnknownException, UserNotFoundException;

	/**
	 * 
	 * 
	 */
	UserProfile getOperatorProfile()
	throws ServiceConfigurationException, UnknownException;
}
