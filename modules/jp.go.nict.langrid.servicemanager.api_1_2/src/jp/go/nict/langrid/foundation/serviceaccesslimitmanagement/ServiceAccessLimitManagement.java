/*
 * $Id: ServiceAccessLimitManagement.java 404 2011-08-25 01:40:39Z t-nakaguchi $
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
package jp.go.nict.langrid.foundation.serviceaccesslimitmanagement;

import static jp.go.nict.langrid.foundation.util.validation.AccessRightValidationPolicy.ADMINONLY;
import static jp.go.nict.langrid.foundation.util.validation.AccessRightValidationPolicy.SERVICEOWNER_OR_ADMIN;
import jp.go.nict.langrid.commons.validator.annotation.EachElement;
import jp.go.nict.langrid.commons.validator.annotation.IntInRange;
import jp.go.nict.langrid.commons.validator.annotation.IntNotNegative;
import jp.go.nict.langrid.commons.validator.annotation.NotEmpty;
import jp.go.nict.langrid.commons.validator.annotation.NotNull;
import jp.go.nict.langrid.commons.validator.annotation.ValidatedMethod;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.dao.AccessLimitDao;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.entity.LimitType;
import jp.go.nict.langrid.dao.entity.Period;
import jp.go.nict.langrid.foundation.AbstractLangridService;
import jp.go.nict.langrid.foundation.annotation.AccessRightValidatedMethod;
import jp.go.nict.langrid.foundation.annotation.Log;
import jp.go.nict.langrid.foundation.annotation.TransactionMethod;
import jp.go.nict.langrid.foundation.util.validation.annotation.ValidEnum;
import jp.go.nict.langrid.foundation.util.validation.annotation.ValidOrder;
import jp.go.nict.langrid.foundation.util.validation.annotation.ValidServiceId;
import jp.go.nict.langrid.foundation.util.validation.annotation.ValidUserId;
import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.ServiceConfigurationException;
import jp.go.nict.langrid.service_1_2.UnknownException;
import jp.go.nict.langrid.service_1_2.foundation.Order;
import jp.go.nict.langrid.service_1_2.foundation.serviceaccesslimitmanagement.AccessLimit;
import jp.go.nict.langrid.service_1_2.foundation.serviceaccesslimitmanagement.AccessLimitNotFoundException;
import jp.go.nict.langrid.service_1_2.foundation.serviceaccesslimitmanagement.AccessLimitSearchResult;
import jp.go.nict.langrid.service_1_2.foundation.serviceaccesslimitmanagement.ServiceAccessLimitManagementService;
import jp.go.nict.langrid.service_1_2.util.ExceptionConverter;

/**
 * 
 * Access restriction administration service.
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 404 $
 */
public class ServiceAccessLimitManagement
extends AbstractLangridService
implements ServiceAccessLimitManagementService
{
	/**
	 * 
	 * Constructor.
	 * 
	 */
	public ServiceAccessLimitManagement(){}

	/**
	 * 
	 * Constructor.
	 * @param serviceContext Service context
	 * 
	 */
	public ServiceAccessLimitManagement(ServiceContext serviceContext){
		super(serviceContext);
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
			getAccessLimitDao().clear();
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
	public AccessLimitSearchResult searchAccessLimits(
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
			String[] services = getTargetServiceIds(serviceId);
			if(services != null){
				return convert(getAccessLimitDao().searchAccessLimits(
						startIndex, maxCount, getGridId(), userId, getGridId(), services
						, convert(orders, jp.go.nict.langrid.dao.Order[].class)
						), AccessLimitSearchResult.class);
			} else{
				return new AccessLimitSearchResult(
						new AccessLimit[]{}, 0, true
						);
			}
		} catch(DaoException e){
			throw ExceptionConverter.convertToServiceConfigurationException(e);
		} catch(Throwable t){
			throw ExceptionConverter.convertException(t);
		}
	}

	@AccessRightValidatedMethod
	@TransactionMethod
	@ValidatedMethod
	public AccessLimit[] getMyAccessLimits(
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
			AccessLimitDao dao = getAccessLimitDao();
			jp.go.nict.langrid.dao.AccessLimitSearchResult r
				= dao.searchAccessLimits(
						0, Period.values().length * LimitType.values().length
						, getGridId(), getUserChecker().getUser().getUserId()
						, serviceGridId, new String[]{serviceId}
						, new jp.go.nict.langrid.dao.Order[]{}
						);
			if(r.getElements().length > 0){
				return convert(r.getElements(), AccessLimit[].class);
			} else{
				return new AccessLimit[]{};
			}
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
	@AccessRightValidatedMethod(policy=SERVICEOWNER_OR_ADMIN, argNames="serviceId")
	@TransactionMethod
	@ValidatedMethod
	@Log
	public void setAccessLimit(
			@NotEmpty @ValidUserId(allowWildcard=true) String userId
			, @NotEmpty @ValidServiceId String serviceId
			, @NotEmpty @ValidEnum(jp.go.nict.langrid.service_1_2.foundation.typed.Period.class) String period
			, @NotEmpty @ValidEnum(jp.go.nict.langrid.service_1_2.foundation.serviceaccesslimitmanagement.typed.LimitType.class) String limitType
			, @IntNotNegative int limitCount
			)
	throws AccessLimitExceededException,
			InvalidParameterException, NoAccessPermissionException,
			ServiceConfigurationException, UnknownException
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
			
			getAccessLimitDao().setAccessLimit(
					getGridId(), userId, getGridId(), serviceId
					, convert(period, Period.class)
					, convert(limitType, LimitType.class), limitCount
					);
		} catch(DaoException e){
			throw ExceptionConverter.convertToServiceConfigurationException(e);
		} catch(Throwable t){
			throw ExceptionConverter.convertException(t);
		}
	}

	@ValidatedMethod
	@TransactionMethod
	@AccessRightValidatedMethod(policy=SERVICEOWNER_OR_ADMIN, argNames="serviceId")
	@Log
	public void deleteAccessLimit(
			@NotEmpty @ValidUserId(allowWildcard=true) String userId
			, @NotEmpty @ValidServiceId String serviceId
			, @NotEmpty @ValidEnum(jp.go.nict.langrid.service_1_2.foundation.typed.Period.class) String period
			, @NotEmpty @ValidEnum(jp.go.nict.langrid.service_1_2.foundation.serviceaccesslimitmanagement.typed.LimitType.class) String limitType
			)
		throws AccessLimitNotFoundException, AccessLimitExceededException
		, InvalidParameterException, NoAccessPermissionException
		, ServiceConfigurationException, UnknownException
	{
		try{
			Period p = convert(period, Period.class);
			LimitType lt = convert(limitType, LimitType.class);
			getAccessLimitDao().deleteAccessLimit(
					getGridId(), userId, getGridId(), serviceId, p, lt);
		} catch(jp.go.nict.langrid.dao.AccessLimitNotFoundException e){
			throw new AccessLimitNotFoundException(userId, serviceId
					, period, limitType);
		} catch(DaoException e){
			throw ExceptionConverter.convertToServiceConfigurationException(e);
		} catch(Throwable t){
			throw ExceptionConverter.convertException(t);
		}
	}
}
