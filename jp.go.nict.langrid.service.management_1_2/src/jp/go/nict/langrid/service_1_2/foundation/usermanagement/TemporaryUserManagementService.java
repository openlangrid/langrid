/*
 * $Id: TemporaryUserManagementService.java 225 2010-10-03 00:23:14Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.foundation.usermanagement;

import java.util.Calendar;

import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.ServiceConfigurationException;
import jp.go.nict.langrid.service_1_2.UnknownException;
import jp.go.nict.langrid.service_1_2.foundation.MatchingCondition;
import jp.go.nict.langrid.service_1_2.foundation.Order;

/**
 * 
 * The temporary user administration service's interface.
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 225 $
 */
public interface TemporaryUserManagementService {
	/**
	 * 
	 * Clears all temporary user data.
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException The service setup was not done appropriately
	 * @throws UnknownException An unknwon exception occurred
	 * 
	 */
	void clear()
	throws AccessLimitExceededException, NoAccessPermissionException
	, ServiceConfigurationException, UnknownException
	;

	/**
	 * 
	 * Completely clears data of temporary user exceeding validity period.
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException The service setup was not done appropriately
	 * @throws UnknownException An unknwon exception occurred
	 * 
	 */
	void clearExpiredUsers()
	throws AccessLimitExceededException, NoAccessPermissionException
	, ServiceConfigurationException, UnknownException
	;

	/**
	 * 
	 * Searches temporary users.
	 * @param startIndex Start index
	 * @param maxCount Number of search results
	 * @param conditions Search conditions
	 * @param orders Sort order
	 * @return User search results
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException The service setup was not done appropriately
	 * @throws UnknownException An unexpected exception occurred
	 * 
	 */
	TemporaryUserEntrySearchResult searchTemporaryUsers(
			int startIndex, int maxCount, MatchingCondition[] conditions
			, Order[] orders
			)
			throws AccessLimitExceededException, InvalidParameterException
			, NoAccessPermissionException, ServiceConfigurationException
			, UnknownException;

	/**
	 * 
	 * Adds temporary user.
	 * @param userId Temporary user ID
	 * @param password Password
	 * @param beginAvailableDateTime Starting date and time of validation
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws InvalidUserIdException An invalid user ID was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException The service setup was not done appropriately
	 * @throws UnknownException An unexpected exception occurred
	 * @throws UserAlreadyExistsException The specified user already exists
	 * 
	 */
	void addTemporaryUser(
			String userId, String password
			, Calendar beginAvailableDateTime)
			throws AccessLimitExceededException
			, InvalidParameterException, InvalidUserIdException
			, NoAccessPermissionException, ServiceConfigurationException
			, UnknownException, UserAlreadyExistsException;

	/**
	 * 
	 * Deletes temporary user.
	 * @param userId User ID of the user to be deleted
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException The service setup was not done appropriately
	 * @throws UnknownException An unexpected exception occurred
	 * @throws UserNotFoundException The specified user does not exist
	 * 
	 */
	void deleteTemporaryUser(String userId)
			throws AccessLimitExceededException, InvalidParameterException
			, NoAccessPermissionException, ServiceConfigurationException
			, UnknownException, UserNotFoundException;

	/**
	 * 
	 * Gets initial usage date and time of temporary user.
	 * @param userId Temporary Username
	 * @return Start date and time of usage
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException The service setup was not done appropriately
	 * @throws UnknownException An unexpected exception occurred
	 * @throws UserNotFoundException The specified user does not exist
	 * 
	 */
	Calendar getBeginAvailableDateTime(String userId)
			throws AccessLimitExceededException, InvalidParameterException
			, NoAccessPermissionException, ServiceConfigurationException
			, UnknownException, UserNotFoundException;

	/**
	 * 
	 * Gets initial usage date and time of temporary user.
	 * @param userId Temporary Username
	 * @return End date and time of usage
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException The service setup was not done appropriately
	 * @throws UnknownException An unexpected exception occurred
	 * @throws UserNotFoundException The specified user does not exist
	 * 
	 */
	Calendar getEndAvailableDateTime(String userId)
			throws AccessLimitExceededException, InvalidParameterException
			, NoAccessPermissionException, ServiceConfigurationException
			, UnknownException, UserNotFoundException;

	/**
	 * 
	 * Sets initial usage date and time of temporary user.
	 * @param userId User ID
	 * @param beginAvailableDateTime Starting date and time of usage
	 * @param endAvailableDateTime Ending date and time of usage
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException The service setup was not done appropriately
	 * @throws UnknownException An unexpected exception occurred
	 * @throws UserNotFoundException The specified user was not found
	 * 
	 */
	void setAvailableDateTimes(String userId
			, Calendar beginAvailableDateTime
			, Calendar endAvailableDateTime)
			throws AccessLimitExceededException, InvalidParameterException
			, NoAccessPermissionException, ServiceConfigurationException
			, UnknownException, UserNotFoundException;
}
