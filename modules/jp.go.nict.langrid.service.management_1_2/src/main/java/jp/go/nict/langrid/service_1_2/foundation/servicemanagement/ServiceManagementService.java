/*
 * $Id: ServiceManagementService.java 458 2011-12-21 10:27:33Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.foundation.servicemanagement;

import java.util.Calendar;

import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.ServiceConfigurationException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.service_1_2.UnknownException;
import jp.go.nict.langrid.service_1_2.UnsupportedMatchingMethodException;
import jp.go.nict.langrid.service_1_2.foundation.Attribute;
import jp.go.nict.langrid.service_1_2.foundation.MatchingCondition;
import jp.go.nict.langrid.service_1_2.foundation.Order;

/**
 * 
 * The interface of the service administration service.
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 458 $
 */
public interface ServiceManagementService {
	/**
	 * 
	 * Erase all services.
	 * Related access logs, access privileges (access denial, access restrictions) will also be deleted.
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException The service is not set up appropriately
	 * @throws UnknownException Process failed
	 * 
	 */
	void clear()
		throws AccessLimitExceededException, NoAccessPermissionException
		, ServiceConfigurationException
		, UnknownException;

	/**
	 * 
	 * Searches services over the specified conditions, sorts and returns.
	 * Searches both simple and compound services.
	 * Matching and sort conditions can be set on service profile data, instance data, and attribute data.
	 * @param startIndex Starting position of result acquisition
	 * @param maxCount Maximum number of acquired results
	 * @param conditions Matching conditions.  AND search
	 * @param orders Sort condition
	 * @param scope Object range (scope)("ALL", "MINE", "ACCESSIBLE")
	 * @return From among the search results, the data in the range specified by startIndex and maxCount
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException The service is not set up appropriately
	 * @throws UnknownException Process failed for an unknown reason
	 * @throws UnsupportedMatchingMethodException An unsupported matching method was specified
	 * 
	 */
	ServiceEntrySearchResult searchServices(
			int startIndex, int maxCount, MatchingCondition[] conditions
			, Order[] orders, String scope)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, UnknownException, UnsupportedMatchingMethodException;

	ServiceEntrySearchResult searchServicesWithQos(
			int startIndex, int maxCount, MatchingCondition[] conditions
			, Order[] orders, String scope,
			String[] qosTypes, Calendar qosBeginTime, Calendar qosEndTime)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, UnknownException, UnsupportedMatchingMethodException;

	/**
	 * 
	 * Searches services over the specified conditions, sorts and returns.
	 * Searches both simple and compound services.
	 * Matching and sort conditions can be set on service profile data, instance data, and attribute data.
	 * @param startIndex Starting position of result acquisition
	 * @param maxCount Maximum number of acquired results
	 * @param conditions Matching conditions.  AND search
	 * @param orders Sort condition
	 * @param scope Object range (scope)("ALL", "MINE", "ACCESSIBLE")
	 * @return From among the search results, the data in the range specified by startIndex and maxCount
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException The service is not set up appropriately
	 * @throws UnknownException Process failed for an unknown reason
	 * @throws UnsupportedMatchingMethodException An unsupported matching method was specified
	 * 
	 */
	ServiceEntryWithCompactLanguageExpressionSearchResult searchServicesWithCompactLanguageExpression(
			int startIndex, int maxCount, MatchingCondition[] conditions
			, Order[] orders, String scope)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, UnknownException, UnsupportedMatchingMethodException;

	/**
	 * 
	 * Adds service.
	 * @param serviceId Service ID
	 * @param profile Service data
	 * @param instance Service instance (Contents differ according to the type of service instance)
	 * @param attributes Service's extended attribute data
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceAlreadyExistsException The specified service already exists
	 * @throws ServiceConfigurationException Process failed because service setup is invalid
	 * @throws UnknownException Process failed
	 * 
	 */
	void addService(String serviceId, ServiceProfile profile
			, ServiceInstance instance, Attribute[] attributes)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceAlreadyExistsException
		, ServiceConfigurationException, UnknownException
		;

	/**
	 * 
	 * Sets the user with ownership and adds the service.
	 * @param ownerUserId User who has ownership
	 * @param serviceId Service ID
	 * @param profile Service data
	 * @param instance Service instance (Contents differ according to the type of service instance)
	 * @param attributes Service's extended attribute data
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceAlreadyExistsException The specified service already exists
	 * @throws ServiceConfigurationException Process failed because service setup is invalid
	 * @throws UnknownException Process failed
	 * 
	 */
	void addServiceAs(String ownerUserId, String serviceId
			, ServiceProfile profile
			, ServiceInstance instance, Attribute[] attributes)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceAlreadyExistsException
		, ServiceConfigurationException, UnknownException
		;

	/**
	 * 
	 * Deletes a registered service.
	 * @param serviceId ID of the deleted service
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException Process failed because service setup is invalid
	 * @throws ServiceNotFoundException The specified service was not found
	 * @throws ServiceNotInactiveException The specified service is not inactive
	 * @throws UnknownException Process failed due to an unknown cause
	 * 
	 */
	void deleteService(String serviceId)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, ServiceNotFoundException, ServiceNotInactiveException
		, UnknownException
		;

	/**
	 * 
	 * Gets the service's profile data.
	 * @param serviceId Service ID
	 * @return Profile data
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException Process failed because service setup is invalid
	 * @throws ServiceNotFoundException The specified service was not found
	 * @throws UnknownException Process failed due to an unknown cause
	 * 
	 */
	ServiceProfile getServiceProfile(String serviceId)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, ServiceNotFoundException, UnknownException
		;

	/**
	 * 
	 * Sets the service's profile data.
	 * @param serviceId Service ID
	 * @param profile Profile data 
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException Process failed because service setup is invalid
	 * @throws ServiceNotFoundException The specified service was not found
	 * @throws UnknownException Process failed due to an unknown cause
	 * 
	 */
	void setServiceProfile(String serviceId, ServiceProfile profile)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, ServiceNotFoundException, UnknownException
		;

	/**
	 * 
	 * Gets service attributes.
	 * @param serviceId Service ID
	 * @param attributeNames Array of names of acquired attributes
	 * @return Attribute array
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException Process failed because service setup is invalid
	 * @throws ServiceNotFoundException The specified service was not found
	 * @throws UnknownException Process failed due to an unknown cause
	 * 
	 */
	Attribute[] getServiceAttributes(String serviceId, String[] attributeNames)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, ServiceNotFoundException, UnknownException
		;

	/**
	 * 
	 * Sets service attributes.
	 * @param serviceId Service ID
	 * @param attributes Array of attributes to be set
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException Process failed because service setup is invalid
	 * @throws ServiceNotFoundException The specified service was not found
	 * @throws UnknownException Process failed due to an unknown cause
	 * 
	 */
	void setServiceAttributes(String serviceId, Attribute[] attributes)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, ServiceNotFoundException, UnknownException
		;

	/**
	 * 
	 * Gets data about the instance of the service.
	 * @param serviceId Service ID
	 * @return Data concerning instance of service
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException Process failed because service setup is invalid
	 * @throws ServiceNotFoundException The specified service was not found
	 * @throws UnknownException Process failed due to an unknown cause
	 * 
	 */
	ServiceInstance getServiceInstance(String serviceId)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, ServiceNotFoundException, UnknownException
		;

	/**
	 * 
	 * Sets data about the instance of the service.
	 * @param serviceId Service ID
	 * @param instance Data relating to the instance of the service
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException Process failed because service setup is invalid
	 * @throws ServiceNotFoundException The specified service was not found
	 * @throws ServiceNotInactiveException The service is not inactive
	 * @throws UnknownException Process failed due to an unknown cause
	 * 
	 */
	void setServiceInstance(String serviceId, ServiceInstance instance)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, ServiceNotFoundException, ServiceNotInactiveException
		, UnknownException
		;

	/**
	 * 
	 * Gets the service's WSDL.
	 * @param serviceId Service ID
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException Process failed because service setup is invalid
	 * @throws ServiceNotFoundException The specified service was not found
	 * @throws UnknownException Process failed due to an unknown cause
	 * 
	 */
	byte[] getServiceWsdl(String serviceId)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, ServiceNotFoundException, UnknownException
		;

	/**
	 * 
	 * Makes service active.
	 * @param serviceId Service ID
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException Process failed because service setup is invalid
	 * @throws ServiceNotActivatableException Service cannot be made active
	 * @throws ServiceNotFoundException The specified service was not found
	 * @throws UnknownException Process failed due to an unknown cause
	 * 
	 */
	void activateService(String serviceId)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, ServiceNotActivatableException, ServiceNotFoundException
		, UnknownException
		;

	/**
	 * 
	 * Makes service inactive.
	 * Does nothing if already in an inactive state.
	 * @param serviceId Service ID
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException Process failed because service setup is invalid
	 * @throws ServiceNotDeactivatableException Service cannot be made inactive
	 * @throws ServiceNotFoundException The specified service was not found
	 * @throws UnknownException Process failed due to an unknown cause
	 * 
	 */
	void deactivateService(String serviceId)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, ServiceNotDeactivatableException
		, ServiceNotFoundException, UnknownException
		;

	/**
	 * 
	 * Returns whether the service is active or not.
	 * @param serviceId Service ID
	 * @return True when active
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException Process failed because service setup is invalid
	 * @throws ServiceNotFoundException The specified service was not found
	 * @throws UnknownException Process failed due to an unknown cause
	 * 
	 */
	boolean isServiceActive(String serviceId)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, ServiceNotFoundException, UnknownException
		;

	/**
	 * 
	 * Gets external service call data.
	 * @param serviceId Service ID
	 * @return External service call data
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException Process failed because service setup is invalid
	 * @throws ServiceNotFoundException The specified service was not found
	 * @throws UnknownException Process failed due to an unknown cause
	 * 
	 */
	Invocation[] getExternalInvocations(String serviceId)
	throws AccessLimitExceededException, InvalidParameterException
	, NoAccessPermissionException, ServiceConfigurationException
	, ServiceNotFoundException, UnknownException
	;

	/**
	 * 
	 * Returns whether the service is visible or not.
	 * @param serviceId Service ID
	 * @return true when visible
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException Process failed because service setup is invalid
	 * @throws ServiceNotFoundException The specified service was not found
	 * @throws UnknownException Process failed due to an unknown cause
	 * 
	 */
	boolean isServiceVisible(String serviceId)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, ServiceNotFoundException, UnknownException
		;

	/**
	 * 
	 * Sets whether the service is visible or not.
	 * @param serviceId Service ID
	 * @param visible True when visible
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException Process failed because service setup is invalid
	 * @throws ServiceNotFoundException The specified service was not found
	 * @throws UnknownException Process failed due to an unknown cause
	 * 
	 */
	void setServiceVisible(String serviceId, boolean visible)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, ServiceNotFoundException, UnknownException
		;
}
