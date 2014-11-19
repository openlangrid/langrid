/*
 * $Id: ServiceAccessLimitManagementService.java 225 2010-10-03 00:23:14Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.foundation.serviceaccesslimitmanagement;

import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.ServiceConfigurationException;
import jp.go.nict.langrid.service_1_2.UnknownException;
import jp.go.nict.langrid.service_1_2.foundation.Order;

/**
 * 
 * Service to carry out access restriction administration.
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 225 $
 */
public interface ServiceAccessLimitManagementService {
	/**
	 * 
	 * Deletes access restriction data.
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
	 * Searches access restriction data over the specified conditions, sorts and returns.
	 * @param startIndex Starting position of result acquisition
	 * @param maxCount Maximum number of acquired results
	 * @param userId Target user ID for searching access restriction
	 * @param serviceId Target service IDs of searched access restrictions
	 * @param orders Sort condition
	 * @return From among the search results, the data in the range specified by startIndex and maxCount
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException The service setup was not done appropriately
	 * @throws UnknownException An unexpected exception occurred
	 * 
	 */
	AccessLimitSearchResult searchAccessLimits(
			int startIndex, int maxCount, String userId, String serviceId
			, Order[] orders)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, UnknownException;

	/**
	 * 
	 * Gets the access restrictions set for the called service.
	 * @param serviceId Service ID
	 * @return Array of access restrictions
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException The service setup was not done appropriately
	 * @throws UnknownException An unexpected exception occurred
	 * 
	 */
	public AccessLimit[] getMyAccessLimits(String serviceId)
	throws AccessLimitExceededException, InvalidParameterException
	, NoAccessPermissionException, ServiceConfigurationException
	, UnknownException;

	/**
	 * 
	 * Sets access restriction.
	 * The access restriction data's ID is not used.
	 * Overwrites when the access restriction against the same user, service, period, or restriction is already set.
	 * @param userId User ID
	 * @param serviceId Service ID
	 * @param period Units of period
	 * @param limitType Restriction type
	 * @param limitCount Restriction value
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException The service setup was not done appropriately
	 * @throws UnknownException An unexpected exception occurred
	 * 
	 */
	void setAccessLimit(String userId, String serviceId,
			String period, String limitType, int limitCount)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, UnknownException;

	/**
	 * 
	 * Deletes access restriction.
	 * @param userId User ID
	 * @param serviceId Service ID
	 * @param period Units of period
	 * @param limitType Restriction type
	 * @param limitCount Restriction value
	 * @throws AccessLimitNotFoundException The access restriction does not exist
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException The service setup was not done appropriately
	 * @throws UnknownException An unexpected exception occurred
	 * 
	 */
	public void deleteAccessLimit(String userId, String serviceId
			, String period, String limitType)
		throws AccessLimitNotFoundException, AccessLimitExceededException
		, InvalidParameterException, NoAccessPermissionException
		, ServiceConfigurationException, UnknownException;
}
