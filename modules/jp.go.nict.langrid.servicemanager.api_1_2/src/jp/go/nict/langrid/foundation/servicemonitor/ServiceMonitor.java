/*
 * $Id: ServiceMonitor.java 404 2011-08-25 01:40:39Z t-nakaguchi $
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
package jp.go.nict.langrid.foundation.servicemonitor;

import static jp.go.nict.langrid.foundation.util.validation.AccessRightValidationPolicy.ADMINONLY;
import static jp.go.nict.langrid.foundation.util.validation.AccessRightValidationPolicy.SELF_OR_ADMIN;
import static jp.go.nict.langrid.foundation.util.validation.AccessRightValidationPolicy.SERVICEOWNER_OR_ADMIN;

import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.go.nict.langrid.commons.validator.annotation.EachElement;
import jp.go.nict.langrid.commons.validator.annotation.IntInRange;
import jp.go.nict.langrid.commons.validator.annotation.IntNotNegative;
import jp.go.nict.langrid.commons.validator.annotation.NotEmpty;
import jp.go.nict.langrid.commons.validator.annotation.NotNull;
import jp.go.nict.langrid.commons.validator.annotation.ValidatedMethod;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.dao.AccessRankingEntrySearchResult;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.MatchingCondition;
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
import jp.go.nict.langrid.service_1_2.foundation.servicemonitor.AccessLog;
import jp.go.nict.langrid.service_1_2.foundation.servicemonitor.AccessLogSearchResult;
import jp.go.nict.langrid.service_1_2.foundation.servicemonitor.ServiceCallLog;
import jp.go.nict.langrid.service_1_2.foundation.servicemonitor.ServiceCallLogSearchResult;
import jp.go.nict.langrid.service_1_2.foundation.servicemonitor.ServiceMonitorService;
import jp.go.nict.langrid.service_1_2.foundation.servicemonitor.UserAccessEntry;
import jp.go.nict.langrid.service_1_2.foundation.servicemonitor.UserAccessEntrySearchResult;
import jp.go.nict.langrid.service_1_2.util.ExceptionConverter;

/**
 * 
 * Service monitor.
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 404 $
 */
public class ServiceMonitor
	extends AbstractLangridService
	implements ServiceMonitorService
{
	/**
	 * 
	 * Constructor.
	 * 
	 */
	public ServiceMonitor(){}

	/**
	 * 
	 * Constructor.
	 * @param serviceContext Service context
	 * 
	 */
	public ServiceMonitor(ServiceContext serviceContext){
		super(serviceContext);
	}

	@AccessRightValidatedMethod(policy=ADMINONLY)
	@TransactionMethod
	@Log
	public void clear()
			throws AccessLimitExceededException, NoAccessPermissionException
			, ServiceConfigurationException, UnknownException
	{
		try{
			getAccessLogDao().clear();
			getAccessStateDao().clear();
		} catch(DaoException e){
			logger.log(Level.SEVERE, "", e);
			throw ExceptionConverter.convertToServiceConfigurationException(e);
		} catch(Throwable t){
			logger.log(Level.SEVERE, "", t);
			throw ExceptionConverter.convertException(t);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>When an empty string is specified as userId, doesn't narrow down according to user.
	 * When an empty string is specified for serviceId, do a search on all services registered by the user calling this API.</p>
	 * 
	 */
	@ValidatedMethod
	@AccessRightValidatedMethod(policy=SERVICEOWNER_OR_ADMIN, argNames="serviceId")
	@TransactionMethod
	public AccessLogSearchResult searchAccessLogs(
			@IntNotNegative int startIndex
			, @IntInRange(minimum=0, maximum=100) int maxCount
			, @NotNull @ValidUserId(allowEmpty=true) String userId
			, @NotNull @ValidServiceId(allowEmpty=true) String serviceId
			, @NotNull Calendar startDateTime
			, @NotNull Calendar endDateTime
			, @NotNull @EachElement @ValidOrder Order[] orders
			)
			throws InvalidParameterException, NoAccessPermissionException
			, ServiceConfigurationException, UnknownException
	{
		if(startDateTime.after(endDateTime)){
			throw new InvalidParameterException(
					"endDateTime"
					, "endDateTime must be after of startDateTime"
					);
		}
		try{
			String[] services = getTargetServiceIds(serviceId);
			if(services != null){
				return convert(getAccessLogDao().searchAccessLog(
						startIndex, maxCount, getGridId(), userId, getGridId(), services
						, startDateTime, endDateTime
						, new MatchingCondition[]{}
						, convert(orders, jp.go.nict.langrid.dao.Order[].class)
						)
						, AccessLogSearchResult.class);
			} else{
				return new AccessLogSearchResult(new AccessLog[]{}, 0, true);
			}
		} catch(ServiceConfigurationException e){
			throw e;
		} catch(DaoException e){
			logger.log(Level.SEVERE, "", e);
			throw ExceptionConverter.convertToServiceConfigurationException(e);
		} catch(Throwable t){
			logger.log(Level.SEVERE, "", t);
			throw ExceptionConverter.convertException(t);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * 
	 */
	@ValidatedMethod
	@AccessRightValidatedMethod(policy=SELF_OR_ADMIN, argNames="userId")
	@TransactionMethod
	public ServiceCallLogSearchResult searchServiceCallLogs(
			@IntNotNegative int startIndex
			, @IntInRange(minimum=0, maximum=100) int maxCount
			, @NotNull @ValidUserId(allowEmpty=true) String userId
			, @NotNull @ValidServiceId(allowEmpty=true) String serviceId
			, @NotNull Calendar startDateTime
			, @NotNull Calendar endDateTime
			, @NotNull @EachElement @ValidOrder Order[] orders
			)
			throws InvalidParameterException, NoAccessPermissionException
			, ServiceConfigurationException, UnknownException
	{
		if(startDateTime.after(endDateTime)){
			throw new InvalidParameterException(
					"endDateTime"
					, "endDateTime must be after of startDateTime"
					);
		}
		try{
			String[] services = getTargetServiceIds(serviceId);
			if(services != null){
				return convert(getAccessLogDao().searchAccessLog(
						startIndex, maxCount, getGridId(), userId, getGridId(), services
						, startDateTime, endDateTime
						, new MatchingCondition[]{new MatchingCondition("callNest", 0)}
						, convert(orders, jp.go.nict.langrid.dao.Order[].class)
						)
						, ServiceCallLogSearchResult.class);
			} else{
				return new ServiceCallLogSearchResult(new ServiceCallLog[]{}, 0, true);
			}
		} catch(ServiceConfigurationException e){
			throw e;
		} catch(DaoException e){
			logger.log(Level.SEVERE, "", e);
			throw ExceptionConverter.convertToServiceConfigurationException(e);
		} catch(Throwable t){
			logger.log(Level.SEVERE, "", t);
			throw ExceptionConverter.convertException(t);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>When an empty string is specified as userId, doesn't narrow down according to user.
	 * When an empty string is specified for serviceId, do a search on all services registered by the user calling this API.</p>
	 * 
	 */
	@ValidatedMethod
	@AccessRightValidatedMethod(policy=SERVICEOWNER_OR_ADMIN, argNames="serviceId")
	@TransactionMethod
	public int[] getAccessCounts(
			@NotNull @ValidUserId(allowEmpty=true) String userId
			, @NotNull @ValidServiceId(allowEmpty=true) String serviceId
			, @NotNull Calendar baseDateTime
			, @NotEmpty @ValidEnum(jp.go.nict.langrid.service_1_2.foundation.typed.Period.class) String period
			)
			throws InvalidParameterException, NoAccessPermissionException
			, ServiceConfigurationException, UnknownException
	{
		try{
			if(period.equalsIgnoreCase("YEAR")){
				return new int[12];
			} else if(period.equalsIgnoreCase("MONTH")){
				return new int[31];
			} else if(period.equalsIgnoreCase("DATE")){
				return new int[24];
			} else {
				return new int[]{};
			}
		} catch(Throwable t){
			logger.log(Level.SEVERE, "", t);
			throw ExceptionConverter.convertException(t);
		}
	}

	@AccessRightValidatedMethod(policy=SERVICEOWNER_OR_ADMIN, argNames="serviceId")
	@TransactionMethod
	@ValidatedMethod
	public UserAccessEntrySearchResult sumUpUserAccess(
			@IntNotNegative int startIndex
			, @IntInRange(minimum=0, maximum=100) int maxCount
			, @NotEmpty @ValidServiceId String serviceId
			, @NotNull Calendar startDateTime
			, @NotNull Calendar endDateTime
			, @NotEmpty @ValidEnum(jp.go.nict.langrid.service_1_2.foundation.typed.Period.class) String period
			, @NotNull @EachElement @ValidOrder Order[] orders
			)
			throws AccessLimitExceededException,
			InvalidParameterException, NoAccessPermissionException,
			ServiceConfigurationException, UnknownException
	{
		try{
			String serviceGridId = getGridId();
			String[] ids = serviceId.split(":");
			if(ids.length > 1){
				serviceGridId = ids[0];
				serviceId = ids[1];
			}
			AccessRankingEntrySearchResult r = getAccessStateDao().searchUserAccessRanking(
					startIndex, maxCount, serviceGridId, serviceId, getGridId()
					, startDateTime, endDateTime
					, Period.valueOf(period)
					, convert(orders, jp.go.nict.langrid.dao.Order[].class)
					);
			return new UserAccessEntrySearchResult(
					convert(r.getElements(), UserAccessEntry[].class)
					, r.getTotalCount()
					, true
					);
		} catch(DaoException e){
			throw ExceptionConverter.convertToServiceConfigurationException(e);
		} catch(Throwable e){
			UnknownException ex = ExceptionConverter.convertException(e);
			ex.printStackTrace();
			throw ex;
		}
	}

	/**
	 * 
	 */
	@Override
	public void getServiceQoS(String serviceId, String[] qosTypes, Calendar startDateTime, Calendar endDateTime)
			throws AccessLimitExceededException, InvalidParameterException, NoAccessPermissionException,
			ServiceConfigurationException, UnknownException {
		
	}

	private static Logger logger = Logger.getLogger(
			ServiceMonitor.class.getName());
}
