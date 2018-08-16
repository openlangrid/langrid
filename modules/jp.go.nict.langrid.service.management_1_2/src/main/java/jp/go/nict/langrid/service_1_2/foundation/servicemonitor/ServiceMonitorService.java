/*
 * $Id: ServiceMonitorService.java 225 2010-10-03 00:23:14Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.foundation.servicemonitor;

import java.util.Calendar;

import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.ServiceConfigurationException;
import jp.go.nict.langrid.service_1_2.UnknownException;
import jp.go.nict.langrid.service_1_2.foundation.Order;

/**
 * 
 * The service monitor's interface.
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 225 $
 */
public interface ServiceMonitorService {
	/**
	 * 
	 * Erases access log.
	 * Each access total and access log will be deleted.
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
	 * Searches access log.
	 * @param startIndex Starting position of acquisition
	 * @param maxCount Maximum number of acquired results
	 * @param userId User ID
	 * @param serviceId Service ID
	 * @param startDateTime Log start date and time. Logs at this date and time are included in results
	 * @param endDateTime End date and time of the log. Logs at this date and time are not included in the results
	 * @param orders Sort direction
	 * @return Array of the searched logs
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException The service setup was not done appropriately
	 * @throws UnknownException An unknwon exception occurred
	 * 
	 */
	 AccessLogSearchResult searchAccessLogs(
			int startIndex, int maxCount
			, String userId, String serviceId
			, Calendar startDateTime, Calendar endDateTime
			, Order[] orders
			)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, UnknownException;

	/**
	 * 
	 * Returns the access count over the specified totalling period.
	 * @param userId User ID
	 * @param serviceId Service ID. When unspecified, an empty string
	 * @param baseDateTime Base date and time
	 * @param period Aggregation period ("DAY" or "MONTH" or "YEAR")
	 * @return Access totals (int[24] or int[31] or int[12])
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException The service setup was not done appropriately
	 * @throws UnknownException An unknwon exception occurred
	 * 
	 */
	int[] getAccessCounts(
			String userId, String serviceId
			, Calendar baseDateTime, String period
			)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, UnknownException;

	/**
	 * 
	 * Totals the user accesses of the specified service.
	 * For the sort, the UserAccessEntry field can be specified ("accessCount", "transferredSize").
	 * @param startIndex Starting position of acquisition
	 * @param maxCount Maximum number of acquired results
	 * @param serviceId Service ID
	 * @param startDateTime Start date and time of aggregation
	 * @param endDateTime End date and time of aggregation
	 * @param period Period to aggregate ranking ("YEAR", "MONTH", "DATE")
	 * @param orders Sort condition
	 * @return From among the total results, data from the range specified by startIndex and maxCount
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException The parameter is invalid
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException The service setup was not done appropriately
	 * @throws UnknownException An unknown error occurred
	 * 
	 */
	UserAccessEntrySearchResult sumUpUserAccess(
			int startIndex, int maxCount
			, String serviceId
			, Calendar startDateTime
			, Calendar endDateTime
			, String period
			, Order[] orders
			)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, UnknownException
		;

	/**
	 * 
	 * 
	 */
	ServiceCallLogSearchResult searchServiceCallLogs(
			int startIndex, int maxCount
			, String userId, String serviceId
			, Calendar startDateTime, Calendar endDateTime
			, Order[] orders
			)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, UnknownException;

	void getServiceQoS(String serviceId, String[] qosTypes, Calendar startDateTime, Calendar endDateTime)
			throws AccessLimitExceededException, InvalidParameterException
			, NoAccessPermissionException, ServiceConfigurationException
			, UnknownException;
	
}
