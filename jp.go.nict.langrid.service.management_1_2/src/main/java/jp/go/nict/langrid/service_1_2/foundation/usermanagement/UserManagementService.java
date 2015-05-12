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
package jp.go.nict.langrid.service_1_2.foundation.usermanagement;

import java.util.Calendar;

import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.ServiceConfigurationException;
import jp.go.nict.langrid.service_1_2.UnknownException;
import jp.go.nict.langrid.service_1_2.foundation.Attribute;
import jp.go.nict.langrid.service_1_2.foundation.MatchingCondition;
import jp.go.nict.langrid.service_1_2.foundation.Order;

/**
 * 
 * The interface of the user administration service.
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 225 $
 */
public interface UserManagementService {
	/**
	 * 
	 * Clears all user data.
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
	 * Initializes user data.
	 * Call the UserDBInit.init method.
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException The service setup was not done appropriately
	 * @throws UnknownException An unknwon exception occurred
	 * 
	 */
	void initialize()
	throws AccessLimitExceededException, NoAccessPermissionException
	, ServiceConfigurationException, UnknownException;

	/**
	 * 
	 * Searches users over the specified conditions, sorts and returns.
	 * Matching and sort conditions can be set on user profile data, instance data, and attribute data.
	 * @param startIndex Starting position of acquisition
	 * @param maxCount Maximum number of acquired results
	 * @param conditions Matching conditions.  AND search
	 * @param orders Sort condition
	 * @return From among the search results, the data in the range specified by startIndex and maxCount
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException The service setup was not done appropriately
	 * @throws UnknownException An unexpected exception occurred
	 * 
	 */
	UserEntrySearchResult searchUsers(
			int startIndex, int maxCount, MatchingCondition[] conditions
			, Order[] orders
			)
			throws AccessLimitExceededException, InvalidParameterException
			, NoAccessPermissionException, ServiceConfigurationException
			, UnknownException;

	/**
	 * 
	 * Adds a user.
	 * @param userId Username
	 * @param password Password
	 * @param canCallServices Whether or not services can be called
	 * @param profile Profile
	 * @param attributes Attribute
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws InvalidUserIdException An invalid user ID was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException The service setup was not done appropriately
	 * @throws UnknownException An unexpected exception occurred
	 * @throws UserAlreadyExistsException The specified user already exists
	 * 
	 */
	void addUser(
			String userId, String password, boolean canCallServices
			, UserProfile profile, Attribute[] attributes)
			throws AccessLimitExceededException
			, InvalidParameterException, InvalidUserIdException
			, NoAccessPermissionException, ServiceConfigurationException
			, UnknownException, UserAlreadyExistsException;

	/**
	 * 
	 * Deletes a user.
	 * @param userId Username of the user to be deleted
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException The service setup was not done appropriately
	 * @throws UnknownException An unexpected exception occurred
	 * @throws UserNotFoundException The specified user does not exist
	 * 
	 */
	void deleteUser(String userId)
			throws AccessLimitExceededException, InvalidParameterException
			, NoAccessPermissionException, ServiceConfigurationException
			, UnknownException, UserNotFoundException;

	/**
	 * 
	 * Gets user's profile.
	 * The administrator and the user himself can execute this method.
	 * @param userId Username
	 * @return Profile
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException The service setup was not done appropriately
	 * @throws UnknownException An unexpected exception occurred
	 * @throws UserNotFoundException The specified user does not exist
	 * 
	 */
	UserProfile getUserProfile(String userId)
			throws AccessLimitExceededException, InvalidParameterException
			, NoAccessPermissionException, ServiceConfigurationException
			, UnknownException, UserNotFoundException;

	/**
	 * 
	 * Sets user profile data.
	 * The administrator and the user himself can execute this method.
	 * @param userId User ID
	 * @param profile User profile data
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException The service setup was not done appropriately
	 * @throws UnknownException An unexpected exception occurred
	 * @throws UserNotFoundException The specified user was not found
	 * 
	 */
	void setUserProfile(String userId, UserProfile profile)
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
	Attribute[] getUserAttributes(String userId, String[] attributeNames)
	throws AccessLimitExceededException, InvalidParameterException
	, NoAccessPermissionException, ServiceConfigurationException
	, UnknownException, UserNotFoundException;

	/*
	 * 
	 * Gets the attribute data of multiple users.
	 * @param userIds Array of user IDs
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
/*	IdAndAttributes[] getUsersAttributes(String[] userIds, String[] attributeNames)
	throws AccessLimitExceededException, InvalidParameterException
	, NoAccessPermissionException, ServiceConfigurationException
	, UnknownException, UserNotFoundException;
*/
	/**
	 * 
	 * Sets user's attribute data.
	 * @param userId User ID
	 * @param attributes Array of attribute data.
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException The service setup was not done appropriately
	 * @throws UnknownException An unexpected exception occurred
	 * @throws UserNotFoundException The specified user was not found
	 * 
	 */
	void setUserAttributes(String userId, Attribute[] attributes)
	throws AccessLimitExceededException, InvalidParameterException
	, NoAccessPermissionException, ServiceConfigurationException
	, UnknownException, UserNotFoundException;

	/**
	 * 
	 * Changes user's password.
	 * The administrator and the user himself can execute this method.
	 * @param userId Username
	 * @param password Password
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException The service setup was not done appropriately
	 * @throws UnknownException An unexpected exception occurred
	 * @throws UserNotFoundException The specified user does not exist
	 * 
	 */
	void setUserPassword(String userId, String password)
			throws AccessLimitExceededException, InvalidParameterException
			, NoAccessPermissionException, ServiceConfigurationException
			, UnknownException, UserNotFoundException;

	/**
	 * 
	 * Gets date of user password change.
	 * @param userId Username
	 * @return Date of password change
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException The service setup was not done appropriately
	 * @throws UnknownException An unexpected exception occurred
	 * @throws UserNotFoundException The specified user does not exist
	 * 
	 */
	Calendar getPasswordChangedDate(String userId)
			throws AccessLimitExceededException, InvalidParameterException
			, NoAccessPermissionException, ServiceConfigurationException
			, UnknownException, UserNotFoundException;

	/**
	 * 
	 * Gets user's password.
	 * Only the administrator can execute this method.
	 * Future deletion.
	 * @param userId Username
	 * @return Password
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException The service setup was not done appropriately
	 * @throws UnknownException An unexpected exception occurred
	 * @throws UserNotFoundException The specified user does not exist
	 * 
	 */
	String getUserDigestedPassword(String userId)
			throws AccessLimitExceededException, InvalidParameterException
			, NoAccessPermissionException, ServiceConfigurationException
			, UnknownException, UserNotFoundException;

	/**
	 * 
	 * Gets whether or not user can call simple, composite services.
	 * @param userId Username
	 * @return Whether or not the user can call simple and composite services
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException The service setup was not done appropriately
	 * @throws UnknownException An unexpected exception occurred
	 * @throws UserNotFoundException The specified user does not exist
	 * 
	 */
	boolean getUserCanCallServices(String userId)
	throws AccessLimitExceededException, InvalidParameterException
	, NoAccessPermissionException, ServiceConfigurationException
	, UnknownException, UserNotFoundException;

	/**
	 * 
	 * Sets whether or not user can call simple, composite services.
	 * @param userId Username
	 * @param canCallServices Whether or not the user can call simple and composite services
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException The service setup was not done appropriately
	 * @throws UnknownException An unexpected exception occurred
	 * @throws UserNotFoundException The specified user does not exist
	 * 
	 */
	void setUserCanCallServices(String userId, boolean canCallServices)
	throws AccessLimitExceededException, InvalidParameterException
	, NoAccessPermissionException, ServiceConfigurationException
	, UnknownException, UserNotFoundException;

	/**
	 * 
	 * Returns users whose password must be changed.
	 * @param startIndex Start index
	 * @param maxCount Number of search results
	 * @param days Days elapsed since password change
	 * @param orders Sort order
	 * @return Users whose passwords must be changed
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException The service setup was not done appropriately
	 * @throws UnknownException An unexpected exception occurred
	 * 
	 */
	public UserEntrySearchResult searchUsersShouldChangePassword(
			int startIndex, int maxCount, int days
			, Order[] orders)
	throws AccessLimitExceededException, InvalidParameterException
	, NoAccessPermissionException, ServiceConfigurationException
	, UnknownException;
}
