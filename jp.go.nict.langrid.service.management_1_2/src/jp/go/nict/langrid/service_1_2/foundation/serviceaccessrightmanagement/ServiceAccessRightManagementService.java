/*
 * $Id: ServiceAccessRightManagementService.java 225 2010-10-03 00:23:14Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.foundation.serviceaccessrightmanagement;

import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.ServiceConfigurationException;
import jp.go.nict.langrid.service_1_2.UnknownException;
import jp.go.nict.langrid.service_1_2.foundation.Order;

/**
 * 
 * Service to carry out access privilege administration.
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 225 $
 */
public interface ServiceAccessRightManagementService {
	/**
	 * 
	 * Deletes access privilege data.
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException The service setup was not done appropriately
	 * @throws UnknownException An unexpected exception occurred
	 * 
	 */
	public void clear()
	throws AccessLimitExceededException
	, NoAccessPermissionException, ServiceConfigurationException
	, UnknownException;

	/**
	 * 
	 * Searches access privilege data over the specified conditions, sorts and returns.
	 * @param startIndex Starting position of result acquisition
	 * @param maxCount Maximum number of acquired results
	 * @param userId Target user ID for searching access privilege
	 * @param serviceId Target service IDs of searched access privileges
	 * @param orders Sort condition
	 * @return From among the search results, the data in the range specified by startIndex and maxCount
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException The service setup was not done appropriately
	 * @throws UnknownException An unexpected exception occurred
	 * 
	 */
	AccessRightSearchResult searchAccessRights(
			int startIndex, int maxCount, String userId, String serviceId
			, Order[] orders
			)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException, UnknownException;

	/**
	 * 
	 * Searches access privilege data of the called user.
	 * @param serviceId Service ID
	 * @return Access permission
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException The service setup was not done appropriately
	 * @throws UnknownException An unexpected exception occurred
	 * 
	 */
	AccessRight getMyAccessRight(String serviceId)
	throws AccessLimitExceededException, InvalidParameterException
	, NoAccessPermissionException, ServiceConfigurationException
	, UnknownException;

	/**
	 * 
	 * Sets access privilege.
	 * Overwrites when the access privilege against the same user or service is already set.
	 * @param userId User ID
	 * @param serviceId Service ID
	 * @param permitted Whether permitted or not
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException The service setup was not done appropriately
	 * @throws UnknownException An unexpected exception occurred
	 * 
	 */
	void setAccessRight(String userId, String serviceId, boolean permitted)
	throws AccessLimitExceededException, InvalidParameterException
	, NoAccessPermissionException, ServiceConfigurationException, UnknownException;

	/**
	 * 
	 * Deletes access privilege.
	 * @param userId Target user ID of the access privilege to be deleted. To specify all, use "*"
	 * @param serviceId Target service IDs of access privileges to be deleted. To specify all, use "*"
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws AccessRightNotFoundException The access privilege does not exist
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException The service setup was not done appropriately
	 * @throws UnknownException An unexpected exception occurred
	 * 
	 */
	void deleteAccessRight(String userId, String serviceId)
		throws AccessLimitExceededException, AccessRightNotFoundException
		, InvalidParameterException, NoAccessPermissionException
		, ServiceConfigurationException, UnknownException;
}
