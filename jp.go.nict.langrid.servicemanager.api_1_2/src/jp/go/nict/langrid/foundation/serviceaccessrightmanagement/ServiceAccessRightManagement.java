/*
 * $Id:ServiceAccessManagement.java 5350 2007-09-21 10:09:04Z nakaguchi $
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
package jp.go.nict.langrid.foundation.serviceaccessrightmanagement;

import static jp.go.nict.langrid.foundation.util.validation.AccessRightValidationPolicy.ADMINONLY;
import static jp.go.nict.langrid.foundation.util.validation.AccessRightValidationPolicy.SERVICEOWNER_OR_ADMIN;
import jp.go.nict.langrid.commons.validator.annotation.EachElement;
import jp.go.nict.langrid.commons.validator.annotation.IntInRange;
import jp.go.nict.langrid.commons.validator.annotation.IntNotNegative;
import jp.go.nict.langrid.commons.validator.annotation.NotEmpty;
import jp.go.nict.langrid.commons.validator.annotation.NotNull;
import jp.go.nict.langrid.commons.validator.annotation.ValidatedMethod;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.ServiceNotFoundException;
import jp.go.nict.langrid.dao.entity.Service;
import jp.go.nict.langrid.foundation.AbstractLangridService;
import jp.go.nict.langrid.foundation.annotation.AccessRightValidatedMethod;
import jp.go.nict.langrid.foundation.annotation.Log;
import jp.go.nict.langrid.foundation.annotation.TransactionMethod;
import jp.go.nict.langrid.foundation.util.validation.annotation.ValidOrder;
import jp.go.nict.langrid.foundation.util.validation.annotation.ValidServiceId;
import jp.go.nict.langrid.foundation.util.validation.annotation.ValidUserId;
import jp.go.nict.langrid.management.logic.AccessRightLogic;
import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.ServiceConfigurationException;
import jp.go.nict.langrid.service_1_2.UnknownException;
import jp.go.nict.langrid.service_1_2.foundation.Order;
import jp.go.nict.langrid.service_1_2.foundation.serviceaccessrightmanagement.AccessRight;
import jp.go.nict.langrid.service_1_2.foundation.serviceaccessrightmanagement.AccessRightNotFoundException;
import jp.go.nict.langrid.service_1_2.foundation.serviceaccessrightmanagement.AccessRightSearchResult;
import jp.go.nict.langrid.service_1_2.foundation.serviceaccessrightmanagement.ServiceAccessRightManagementService;
import jp.go.nict.langrid.service_1_2.util.ExceptionConverter;

/**
 * 
 * Access privilege administration service.
 * 
 * @author Takao Nakaguchi
 * @author $Author:nakaguchi $
 * @version $Revision:5350 $
 */
public class ServiceAccessRightManagement
extends AbstractLangridService
implements ServiceAccessRightManagementService
{
	/**
	 * 
	 * Constructor.
	 * 
	 */
	public ServiceAccessRightManagement(){
		try{
			logic = new AccessRightLogic();
		} catch(DaoException e){
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * Constructor.
	 * @param serviceContext Service context
	 * 
	 */
	public ServiceAccessRightManagement(ServiceContext serviceContext){
		super(serviceContext);
		try{
			logic = new AccessRightLogic();
		} catch(DaoException e){
			throw new RuntimeException(e);
		}
	}

	@AccessRightValidatedMethod(policy=ADMINONLY)
	@TransactionMethod
	@Log
	public void clear()
		throws AccessLimitExceededException
		, NoAccessPermissionException, ServiceConfigurationException
		, UnknownException
	{
		try{
			logic.clear();
		} catch(DaoException e){
			throw ExceptionConverter.convertToServiceConfigurationException(e);
		} catch(Throwable t){
			throw ExceptionConverter.convertException(t);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>When userId and serviceId are omitted, specifies an empty string.
	 * When userId is omitted, search data of all users.
	 * When serviceId is omitted, search data of all services registered by the user calling this method.
	 * </p>
	 * 
	 */
	@ValidatedMethod
	@AccessRightValidatedMethod(policy=SERVICEOWNER_OR_ADMIN, argNames="serviceId")
	@TransactionMethod
	public AccessRightSearchResult searchAccessRights(
			@IntNotNegative int startIndex
			, @IntInRange(minimum=0, maximum=100) int maxCount
			, @NotNull @ValidUserId(allowWildcard=true, allowEmpty=true) String userId
			, @NotNull @ValidServiceId(allowWildcard=true, allowEmpty=true) String serviceId
			, @NotNull @EachElement @ValidOrder Order[] orders
			)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, UnknownException
	{
		try{
			if(userId.length() == 0 || userId.equals("*")){
				return convert(logic.searchAccessRights(
						getUserChecker().getUserId(), startIndex, maxCount
						, getGridId(), serviceId, getGridId()
						, convert(orders, jp.go.nict.langrid.dao.Order[].class)
						)
						, AccessRightSearchResult.class);
			} else{
				return new AccessRightSearchResult(new AccessRight[]{
						convert(logic.getAccessRight(getGridId(), serviceId, getGridId(), userId), AccessRight.class)
				}, 1, true);
			}
		} catch(jp.go.nict.langrid.dao.UserNotFoundException e){
			throw new InvalidParameterException("userId", "user not found");
		} catch(DaoException e){
			throw ExceptionConverter.convertToServiceConfigurationException(e);
		} catch(Throwable t){
			throw ExceptionConverter.convertException(t);
		}
	}

	@AccessRightValidatedMethod()
	@TransactionMethod
	@ValidatedMethod
	public AccessRight getMyAccessRight(
			@NotEmpty @ValidServiceId String serviceId
			)
	throws AccessLimitExceededException, InvalidParameterException
	, NoAccessPermissionException, ServiceConfigurationException
	, UnknownException
	{
		try{
			String serviceGridId = getGridId();
			String[] ids = serviceId.split(":");
			if(ids.length > 1){
				serviceGridId = ids[0];
				serviceId = ids[1];
			}
			String userId = getUserChecker().getUser().getUserId();
			jp.go.nict.langrid.dao.entity.AccessRight r = logic.getAccessRight(
					serviceGridId, serviceId, getGridId(), userId);
			if(r == null){
				throw new ServiceConfigurationException(
						"no default access right found for service \""
						+ serviceId + "\".");
			}
			return convert(r, AccessRight.class);
		} catch(ServiceNotFoundException e){
			throw new InvalidParameterException(
					"serviceId", "service not found.");
		} catch(DaoException e){
			throw ExceptionConverter.convertToServiceConfigurationException(e);
		} catch(Throwable t){
			throw ExceptionConverter.convertException(t);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>By setting userID to "*", you can set the service's defined value of the service (the value applied to newly added services).</p>
	 * 
	 */
	@AccessRightValidatedMethod(
			policy=SERVICEOWNER_OR_ADMIN, argNames="serviceId"
			)
	@TransactionMethod
	@ValidatedMethod
	@Log
	public void setAccessRight(
			@NotEmpty @ValidUserId(allowWildcard=true) String userId
			, @NotEmpty @ValidServiceId String serviceId
			, boolean permitted
	)
	throws AccessLimitExceededException, InvalidParameterException,
	NoAccessPermissionException, ServiceConfigurationException,
	UnknownException
	{
		try{
			if(!userId.equals("*") && !getUserDao().isUserExist(getGridId(), userId)){
				throw new InvalidParameterException(
						"userId"
						, "user:\"" + userId + "\" is not exist."
						);
			}
			if(!getServiceDao().isServiceExist(getGridId(), serviceId)){
				throw new InvalidParameterException(
						"serviceId"
						, "service:\"" + serviceId + "\" is not exist."
						);
			}

			Service s = getServiceDao().getService(getGridId(), serviceId);
			logic.setAccessRight(
					s.getOwnerUserId(), getGridId(), serviceId, getGridId(), userId, permitted);
		} catch(InvalidParameterException e){
			throw e;
		} catch(DaoException e){
			throw ExceptionConverter.convertToServiceConfigurationException(e);
		} catch(Throwable t){
			throw ExceptionConverter.convertException(t);
		}
	}

	@ValidatedMethod
	@AccessRightValidatedMethod(policy=SERVICEOWNER_OR_ADMIN, argNames="serviceId")
	@TransactionMethod
	@Log
	public void deleteAccessRight(
			@NotEmpty @ValidUserId String userId
			, @NotEmpty @ValidServiceId String serviceId
			)
		throws AccessLimitExceededException, AccessRightNotFoundException
		, InvalidParameterException, NoAccessPermissionException
		, ServiceConfigurationException, UnknownException
	{
		try{
			logic.deleteAccessRight(getGridId(), serviceId, getGridId(), userId);
		} catch(jp.go.nict.langrid.dao.AccessRightNotFoundException e){
			throw new AccessRightNotFoundException(e.getUserId(), e.getServiceId());
		} catch(DaoException e){
			throw ExceptionConverter.convertToServiceConfigurationException(e);
		} catch(Throwable t){
			throw ExceptionConverter.convertException(t);
		}
	}

	private AccessRightLogic logic;
}
