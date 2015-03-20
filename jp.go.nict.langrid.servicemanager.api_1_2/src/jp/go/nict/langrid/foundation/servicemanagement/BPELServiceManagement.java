/*
 * $Id:BPELServiceManagement.java 5259 2007-09-06 10:10:27Z nakaguchi $
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

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.commons.validator.annotation.NotEmpty;
import jp.go.nict.langrid.commons.validator.annotation.ValidatedMethod;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.commons.ws.util.InvalidLangridUriException;
import jp.go.nict.langrid.commons.ws.util.LangridUriUtil;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.entity.BPELService;
import jp.go.nict.langrid.dao.entity.Service;
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
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.BPELServiceManagementService;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.ServiceEntry;
import jp.go.nict.langrid.service_1_2.util.ExceptionConverter;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author:nakaguchi $
 * @version $Revision:5259 $
 */
public class BPELServiceManagement
extends AbstractLangridService
implements BPELServiceManagementService
{
	/**
	 * 
	 * 
	 */
	public BPELServiceManagement(){}

	/**
	 * 
	 * 
	 */
	public BPELServiceManagement(ServiceContext serviceContext){
		super(serviceContext);
	}

	@ValidatedMethod
	@AccessRightValidatedMethod(policy=SERVICEOWNER_OR_ADMIN, argNames="serviceId")
	@TransactionMethod
	@Log
	public String[] listPartnerNamespaces(
			@NotEmpty @ValidServiceId String serviceId
			)
	throws AccessLimitExceededException, InvalidParameterException
	, NoAccessPermissionException, ServiceConfigurationException
	, ServiceNotFoundException, UnknownException
	{
		serviceId = serviceId.trim();
		BPELService service = getService(serviceId);
		return service.getPartnerServiceNamespaceURIs().toArray(new String[]{});
	}

	@ValidatedMethod
	@AccessRightValidatedMethod(policy=SERVICEOWNER_OR_ADMIN, argNames="serviceId")
	@TransactionMethod
	@Log
	public ServiceEntry[] listPartnerServices(
			@NotEmpty @ValidServiceId String serviceId
			)
	throws AccessLimitExceededException, InvalidParameterException
	, NoAccessPermissionException, ServiceConfigurationException
	, ServiceNotFoundException, UnknownException
	{
		serviceId = serviceId.trim();
		try{
			BPELService service = getService(serviceId);
			List<ServiceEntry> entries = new ArrayList<ServiceEntry>();
			for(String uri : service.getPartnerServiceNamespaceURIs()){
				try{
					Pair<String, String> ids = LangridUriUtil.extractServiceIds(new URI(uri));
					Service s = getServiceDao().getService(ids.getFirst(), ids.getSecond());
					entries.add(convert(s, ServiceEntry.class));
				} catch(InvalidLangridUriException e){
				} catch(jp.go.nict.langrid.dao.ServiceNotFoundException e){
				}
			}
			return entries.toArray(new ServiceEntry[]{});
		} catch(ServiceConfigurationException e){
			throw e;
		} catch(ServiceNotFoundException e){
			throw e;
		} catch(DaoException e){
			throw convertException(e);
		} catch(Throwable t){
			throw ExceptionConverter.convertException(t);
		}
	}

	private BPELService getService(String serviceId)
		throws ServiceConfigurationException, ServiceNotFoundException
	{
		try{
			Service service = getServiceDao().getService(getGridId(), serviceId);
			if(!(service instanceof BPELService)){
				throw new ServiceNotFoundException(serviceId, "no BPEL Service found");
			}
			return (BPELService)service;
		} catch(DaoException e){
			throw convertException(e);
		}
	}
}
