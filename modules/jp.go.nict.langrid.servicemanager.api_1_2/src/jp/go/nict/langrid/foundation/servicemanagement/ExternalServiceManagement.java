/*
 * $Id:ExternalServiceManagement.java 5259 2007-09-06 10:10:27Z nakaguchi $
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
package jp.go.nict.langrid.foundation.servicemanagement;

import static jp.go.nict.langrid.foundation.util.validation.AccessRightValidationPolicy.SERVICEOWNER_OR_ADMIN;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import jp.go.nict.langrid.commons.validator.annotation.FieldNotNull;
import jp.go.nict.langrid.commons.validator.annotation.NotEmpty;
import jp.go.nict.langrid.commons.validator.annotation.NotNull;
import jp.go.nict.langrid.commons.validator.annotation.ValidatedMethod;
import jp.go.nict.langrid.commons.ws.Protocols;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.entity.ExternalService;
import jp.go.nict.langrid.dao.entity.Service;
import jp.go.nict.langrid.dao.entity.ServiceEndpoint;
import jp.go.nict.langrid.foundation.AbstractLangridService;
import jp.go.nict.langrid.foundation.annotation.AccessRightValidatedMethod;
import jp.go.nict.langrid.foundation.annotation.Log;
import jp.go.nict.langrid.foundation.annotation.TransactionMethod;
import jp.go.nict.langrid.foundation.util.validation.annotation.ValidServiceId;
import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.ServiceConfigurationException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.service_1_2.UnknownException;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.Endpoint;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.EndpointNotFoundException;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.ExternalServiceManagementService;
import jp.go.nict.langrid.service_1_2.util.ExceptionConverter;

/**
 * 
 * Edits the call endpoint of external services.
 * 
 * @author Takao Nakaguchi
 * @author $Author:nakaguchi $
 * @version $Revision:5259 $
 */
public class ExternalServiceManagement
extends AbstractLangridService
implements ExternalServiceManagementService
{
	/**
	 * 
	 * Constructor.
	 * 
	 */
	public ExternalServiceManagement(){}

	/**
	 * 
	 * Constructor.
	 * @param serviceContext Service context
	 * 
	 */
	public ExternalServiceManagement(ServiceContext serviceContext){
		super(serviceContext);
	}

	@ValidatedMethod
	@AccessRightValidatedMethod(policy=SERVICEOWNER_OR_ADMIN, argNames="serviceId")
	@TransactionMethod
	public Endpoint[] listActualEndpoints(
			@NotEmpty @ValidServiceId String serviceId
			)
	throws AccessLimitExceededException, InvalidParameterException
	, NoAccessPermissionException, ServiceConfigurationException
	, ServiceNotFoundException, UnknownException
	{
		ExternalService service = getService(serviceId);
		try{
			List<ServiceEndpoint> endpoints =
				service.getServiceEndpoints();
			Endpoint[] result = new Endpoint[endpoints.size()];
			for(int i = 0; i < result.length; i++){
				ServiceEndpoint ep = endpoints.get(i);
				result[i] = new Endpoint(
						ep.getUrl().toString()
						, ep.getAuthUserName()
						, ep.getAuthPassword()
						, ep.isEnabled()
						);
			}
			return result;
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	@ValidatedMethod
	@AccessRightValidatedMethod(policy=SERVICEOWNER_OR_ADMIN, argNames="serviceId")
	@TransactionMethod
	@Log
	public void addActualEndpoint(
			@NotEmpty @ValidServiceId String serviceId
			, @NotNull @FieldNotNull(name="userName") Endpoint endpoint
			)
	throws AccessLimitExceededException, InvalidParameterException
	, NoAccessPermissionException, ServiceConfigurationException
	, ServiceNotFoundException, UnknownException
	{
		ExternalService service = getService(serviceId);
		try{
			for(ServiceEndpoint ep : service.getServiceEndpoints()){
				if(!ep.getUrl().toString().equals(endpoint.getUrl())) continue;
				if(!ep.getAuthUserName().equals(endpoint.getUserName())) continue;
				throw new InvalidParameterException("endpoint", "already exists.");
			}
			service.getServiceEndpoints().add(
					new ServiceEndpoint(
							getGridId(), serviceId
							, Protocols.DEFAULT
							, new URL(endpoint.getUrl())
							, endpoint.getUserName()
							, endpoint.getPassword()
							, endpoint.isEnabled()
					));
		} catch(MalformedURLException e){
			throw new InvalidParameterException("endpoint", "Malformed url found.");
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	@ValidatedMethod
	@AccessRightValidatedMethod(policy=SERVICEOWNER_OR_ADMIN, argNames="serviceId")
	@TransactionMethod
	@Log
	public void deleteActualEndpoint(
			@NotEmpty @ValidServiceId String serviceId
			, @NotEmpty String url
			, @NotNull String userName
			)
	throws AccessLimitExceededException, EndpointNotFoundException
	, InvalidParameterException, NoAccessPermissionException
	, ServiceConfigurationException, ServiceNotFoundException
	, UnknownException
	{
		ExternalService service = getService(serviceId);
		try{
			Iterator<ServiceEndpoint> it =
				service.getServiceEndpoints().iterator();
			while(it.hasNext()){
				ServiceEndpoint ep = it.next();
				if(!ep.getUrl().toString().equals(url)) continue;
				it.remove();
				return;
			}
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
		throw new EndpointNotFoundException(url, userName);
	}

	@ValidatedMethod
	@AccessRightValidatedMethod(policy=SERVICEOWNER_OR_ADMIN, argNames="serviceId")
	@TransactionMethod
	@Log
	public void enableActualEndpoint(
			@NotEmpty @ValidServiceId String serviceId
			, @NotEmpty String url
			, @NotNull String userName
			)
	throws AccessLimitExceededException, EndpointNotFoundException
	, InvalidParameterException
	, NoAccessPermissionException, ServiceConfigurationException
	, ServiceNotFoundException, UnknownException
	{
		ExternalService service = getService(serviceId);
		try{
			for(ServiceEndpoint ep : service.getServiceEndpoints()){
				if(!ep.getUrl().toString().equals(url)) continue;
				if(!ep.getAuthUserName().equals(userName)) continue;
				ep.setEnabled(true);
				ep.setDisabledByErrorDate(null);
				ep.touchUpdatedDateTime();
				return;
			}
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
		throw new EndpointNotFoundException(url, userName);
	}

	@ValidatedMethod
	@AccessRightValidatedMethod(policy=SERVICEOWNER_OR_ADMIN, argNames="serviceId")
	@TransactionMethod
	@Log
	public void disableActualEndpoint(
			@NotEmpty @ValidServiceId String serviceId
			, @NotEmpty String url
			, @NotNull String userName
			)
	throws AccessLimitExceededException, EndpointNotFoundException
	, InvalidParameterException, NoAccessPermissionException
	, ServiceConfigurationException, ServiceNotFoundException
	, UnknownException
	{
		ExternalService service = getService(serviceId);
		try{
			for(ServiceEndpoint ep : service.getServiceEndpoints()){
				if(!ep.getUrl().toString().equals(url)) continue;
				if(!ep.getAuthUserName().equals(userName)) continue;
				ep.setEnabled(false);
				// 
				// Clear it so it doesn't revive on its own
				// 
				ep.setDisabledByErrorDate(null);
				ep.touchUpdatedDateTime();
				return;
			}
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
		throw new EndpointNotFoundException(url, userName);
	}

	private ExternalService getService(String serviceId)
	throws ServiceConfigurationException, ServiceNotFoundException
	{
		try{
			Service service = getServiceDao().getService(getGridId(), serviceId);
			if(!(service instanceof ExternalService)){
				throw new ServiceNotFoundException(serviceId, "no External Service found");
			}
			return (ExternalService)service;
		} catch(DaoException e){
			throw convertException(e);
		}
	}
}
