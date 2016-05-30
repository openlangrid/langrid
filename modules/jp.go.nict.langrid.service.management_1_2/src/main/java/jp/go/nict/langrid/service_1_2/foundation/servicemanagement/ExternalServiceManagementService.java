/*
 * $Id: ExternalServiceManagementService.java 225 2010-10-03 00:23:14Z t-nakaguchi $
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

import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.ServiceConfigurationException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.service_1_2.UnknownException;

/**
 * 
 * Provides an API to administer data unique to external services
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 225 $
 */
public interface ExternalServiceManagementService {
	/**
	 * 
	 * Gets a list of the endpoints of the specified service.
	 * @param serviceId Service ID
	 * @return List of endpoints
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException The service is not set up appropriately
	 * @throws ServiceNotFoundException The specified service was not found
	 * @throws UnknownException Process failed for an unknown reason
	 * 
	 */
	Endpoint[] listActualEndpoints(String serviceId)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, ServiceNotFoundException, UnknownException
		;

	/**
	 * 
	 * Adds an endpoint. Endpoints are differentiated by url and userName.
	 * When an endpoint having the same url and userName has already been added, an InvalidParameterException is thrown.
	 * @param serviceId Service ID
	 * @param endpoint Endpoint
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException The service is not set up appropriately
	 * @throws ServiceNotFoundException The specified service was not found
	 * @throws UnknownException Process failed for an unknown reason
	 * 
	 */
	void addActualEndpoint(String serviceId, Endpoint endpoint)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, ServiceNotFoundException, UnknownException
		;

	/**
	 * 
	 * Deletes endpoint.
	 * @param serviceId Service ID
	 * @param url The URL of the endpoint
	 * @param userName Username for BASIC authentication at the endpoint
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws EndpointNotFoundException The endpoint could not be found
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException The service is not set up appropriately
	 * @throws ServiceNotFoundException The specified service was not found
	 * @throws UnknownException Process failed for an unknown reason
	 * 
	 */
	void deleteActualEndpoint(String serviceId, String url, String userName)
		throws AccessLimitExceededException, EndpointNotFoundException
		, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, ServiceNotFoundException, UnknownException
		;

	/**
	 * 
	 * Validates an endpoint.
	 * @param serviceId Service ID
	 * @param url The URL of the endpoint
	 * @param userName Username for BASIC authentication at the endpoint
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws EndpointNotFoundException The endpoint could not be found
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException The service is not set up appropriately
	 * @throws ServiceNotFoundException The specified service was not found
	 * @throws UnknownException Process failed for an unknown reason
	 * 
	 */
	void enableActualEndpoint(
			String serviceId, String url, String userName
			)
		throws AccessLimitExceededException, EndpointNotFoundException
		, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, ServiceNotFoundException, UnknownException
		;

	/**
	 * 
	 * Invalidates an endpoint.
	 * @param serviceId Service ID
	 * @param url The URL of the endpoint
	 * @param userName Username for BASIC authentication at the endpoint
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws EndpointNotFoundException The endpoint could not be found
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException The service is not set up appropriately
	 * @throws ServiceNotFoundException The specified service was not found
	 * @throws UnknownException Process failed for an unknown reason
	 * 
	 */
	void disableActualEndpoint(
			String serviceId, String url, String userName
			)
		throws AccessLimitExceededException, EndpointNotFoundException
		, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, ServiceNotFoundException, UnknownException
		;
}
