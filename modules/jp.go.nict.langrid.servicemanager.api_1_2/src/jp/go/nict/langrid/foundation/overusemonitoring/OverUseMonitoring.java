/*
 * $Id:ServiceMonitor.java 5259 2007-09-06 10:10:27Z nakaguchi $
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
package jp.go.nict.langrid.foundation.overusemonitoring;

import static jp.go.nict.langrid.foundation.util.validation.AccessRightValidationPolicy.ADMINONLY;

import java.util.Calendar;

import jp.go.nict.langrid.commons.validator.annotation.EachElement;
import jp.go.nict.langrid.commons.validator.annotation.IntInRange;
import jp.go.nict.langrid.commons.validator.annotation.IntNotNegative;
import jp.go.nict.langrid.commons.validator.annotation.NotEmpty;
import jp.go.nict.langrid.commons.validator.annotation.NotNull;
import jp.go.nict.langrid.commons.validator.annotation.ValidatedMethod;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.entity.LimitType;
import jp.go.nict.langrid.dao.entity.Period;
import jp.go.nict.langrid.foundation.AbstractLangridService;
import jp.go.nict.langrid.foundation.annotation.AccessRightValidatedMethod;
import jp.go.nict.langrid.foundation.annotation.Log;
import jp.go.nict.langrid.foundation.annotation.TransactionMethod;
import jp.go.nict.langrid.foundation.util.validation.annotation.ValidEnum;
import jp.go.nict.langrid.foundation.util.validation.annotation.ValidOrder;
import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.ServiceConfigurationException;
import jp.go.nict.langrid.service_1_2.UnknownException;
import jp.go.nict.langrid.service_1_2.foundation.Order;
import jp.go.nict.langrid.service_1_2.foundation.overusemonitoring.OverUseLimit;
import jp.go.nict.langrid.service_1_2.foundation.overusemonitoring.OverUseMonitorService;
import jp.go.nict.langrid.service_1_2.foundation.overusemonitoring.OverUseStateSearchResult;
import jp.go.nict.langrid.service_1_2.util.ExceptionConverter;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 302 $
 */
public class OverUseMonitoring
extends AbstractLangridService
implements OverUseMonitorService
{
	/**
	 * 
	 * 
	 */
	public OverUseMonitoring(){}

	/**
	 * 
	 * 
	 */
	public OverUseMonitoring(ServiceContext serviceContext){
		super(serviceContext);
	}

	@AccessRightValidatedMethod(policy=ADMINONLY)
	@TransactionMethod
	@Log
	public void clearOverUseLimits()
		throws AccessLimitExceededException
		, NoAccessPermissionException, ServiceConfigurationException
		, UnknownException
	{
		try{
			getOverUseLimitDao().clear();
		} catch(DaoException e){
			throw ExceptionConverter.convertToServiceConfigurationException(e);
		} catch(Throwable t){
			throw ExceptionConverter.convertException(t);
		}
	}

	@AccessRightValidatedMethod(policy=ADMINONLY)
	@TransactionMethod
	@ValidatedMethod
	public OverUseLimit[] listOverUseLimits(
			@NotNull @EachElement @ValidOrder Order[] orders
			)
	throws AccessLimitExceededException, InvalidParameterException
	, NoAccessPermissionException,
	ServiceConfigurationException, UnknownException {
		try{
			return convert(getOverUseLimitDao().listOverUseLimits(getGridId(), 
					convert(orders, jp.go.nict.langrid.dao.Order[].class)
					).toArray(new jp.go.nict.langrid.dao.entity.OverUseLimit[]{})
					, OverUseLimit[].class);
		} catch(DaoException e){
			throw ExceptionConverter.convertToServiceConfigurationException(e);
		} catch(Throwable t){
			throw ExceptionConverter.convertException(t);
		}
	}

	@AccessRightValidatedMethod(policy=ADMINONLY)
	@TransactionMethod
	@ValidatedMethod
	@Log
	public void setOverUseLimit(
			@NotEmpty @ValidEnum(jp.go.nict.langrid.service_1_2.foundation.typed.Period.class)
			String period
			,
			@NotEmpty @ValidEnum(jp.go.nict.langrid.service_1_2.foundation.serviceaccesslimitmanagement.typed.LimitType.class)
			String limitType
			,
			@IntNotNegative
			int limitValue
			)
	throws AccessLimitExceededException, InvalidParameterException
	, NoAccessPermissionException, ServiceConfigurationException
	, UnknownException
	{
		try{
			getOverUseLimitDao().setOverUseLimit(getGridId(), 
					convert(period, Period.class)
					, convert(limitType, LimitType.class)
					, limitValue);
		} catch(DaoException e){
			throw ExceptionConverter.convertToServiceConfigurationException(e);
		} catch(Throwable t){
			throw ExceptionConverter.convertException(t);
		}
	}

	@AccessRightValidatedMethod(policy=ADMINONLY)
	@TransactionMethod
	@ValidatedMethod
	@Log
	public void deleteOverUseUseLimit(
			@NotEmpty @ValidEnum(jp.go.nict.langrid.service_1_2.foundation.typed.Period.class)
			String period
			,
			@NotEmpty @ValidEnum(jp.go.nict.langrid.service_1_2.foundation.serviceaccesslimitmanagement.typed.LimitType.class)
			String limitType
			) throws AccessLimitExceededException,
			NoAccessPermissionException, ServiceConfigurationException,
			UnknownException
	{
		try{
			getOverUseLimitDao().deleteOverUseLimit(getGridId(), 
					convert(period, Period.class)
					, convert(limitType, LimitType.class)
					);
		} catch(DaoException e){
			throw ExceptionConverter.convertToServiceConfigurationException(e);
		} catch(Throwable t){
			throw ExceptionConverter.convertException(t);
		}
	}

	@AccessRightValidatedMethod(policy=ADMINONLY)
	@TransactionMethod
	@ValidatedMethod
	public OverUseStateSearchResult searchOverUseState(
			@IntNotNegative int startIndex
			, @IntInRange(minimum=0, maximum=100) int maxCount
			, @NotNull Calendar startDateTime
			, @NotNull Calendar endDateTime
			, @NotNull @EachElement @ValidOrder Order[] orders
			)
	throws AccessLimitExceededException, InvalidParameterException
	, NoAccessPermissionException,
	ServiceConfigurationException, UnknownException
	{
		if(startDateTime.after(endDateTime)){
			throw new InvalidParameterException(
					"endDateTime"
					, "endDateTime must be after of startDateTime"
					);
		}
		try{
			return convert(getOverUseStateDao().searchOverUse(
					startIndex, maxCount
					, getGridId(), startDateTime, endDateTime
					, convert(orders, jp.go.nict.langrid.dao.Order[].class)
					)
					, OverUseStateSearchResult.class);
		} catch(DaoException e){
			throw ExceptionConverter.convertToServiceConfigurationException(e);
		} catch(Throwable t){
			throw ExceptionConverter.convertException(t);
		}
	}
}
