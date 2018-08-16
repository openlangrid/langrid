/*
 * $Id:ServiceManagement.java 5259 2007-09-06 10:10:27Z nakaguchi $
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

import static jp.go.nict.langrid.foundation.util.validation.AccessRightValidationPolicy.ADMINONLY;
import static jp.go.nict.langrid.foundation.util.validation.AccessRightValidationPolicy.SERVICEOWNER_OR_ADMIN;
import static jp.go.nict.langrid.service_1_2.typed.MatchingMethod.COMPLETE;
import static jp.go.nict.langrid.service_1_2.typed.MatchingMethod.EQ;
import static jp.go.nict.langrid.service_1_2.typed.MatchingMethod.GE;
import static jp.go.nict.langrid.service_1_2.typed.MatchingMethod.GT;
import static jp.go.nict.langrid.service_1_2.typed.MatchingMethod.IN;
import static jp.go.nict.langrid.service_1_2.typed.MatchingMethod.LANGUAGEPATH;
import static jp.go.nict.langrid.service_1_2.typed.MatchingMethod.LE;
import static jp.go.nict.langrid.service_1_2.typed.MatchingMethod.LT;
import static jp.go.nict.langrid.service_1_2.typed.MatchingMethod.PARTIAL;
import static jp.go.nict.langrid.service_1_2.typed.MatchingMethod.PREFIX;
import static jp.go.nict.langrid.service_1_2.typed.MatchingMethod.SUFFIX;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.axis.MessageContext;
import org.apache.axis.transport.http.HTTPConstants;
import org.xml.sax.SAXException;

import jp.go.nict.langrid.commons.beanutils.ConversionException;
import jp.go.nict.langrid.commons.io.StreamUtil;
import jp.go.nict.langrid.commons.jxpath.BPELUtil;
import jp.go.nict.langrid.commons.jxpath.WSDLUtil;
import jp.go.nict.langrid.commons.lang.ExceptionUtil;
import jp.go.nict.langrid.commons.net.URLUtil;
import jp.go.nict.langrid.commons.parameter.ParameterContext;
import jp.go.nict.langrid.commons.util.ArrayUtil;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.commons.validator.annotation.EachElement;
import jp.go.nict.langrid.commons.validator.annotation.IntInRange;
import jp.go.nict.langrid.commons.validator.annotation.IntNotNegative;
import jp.go.nict.langrid.commons.validator.annotation.NotEmpty;
import jp.go.nict.langrid.commons.validator.annotation.NotNull;
import jp.go.nict.langrid.commons.validator.annotation.ValidatedMethod;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.commons.ws.param.ServiceContextParameterContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.MatchingMethod;
import jp.go.nict.langrid.dao.NodeDao;
import jp.go.nict.langrid.dao.ServiceDao;
import jp.go.nict.langrid.dao.ServiceSearchResult;
import jp.go.nict.langrid.dao.ServiceUtil;
import jp.go.nict.langrid.dao.entity.BPELService;
import jp.go.nict.langrid.dao.entity.Service;
import jp.go.nict.langrid.dao.entity.ServiceContainerType;
import jp.go.nict.langrid.dao.util.BPELServiceInstanceReader;
import jp.go.nict.langrid.dao.util.BPRBPELServiceInstanceReader;
import jp.go.nict.langrid.dao.util.ExternalServiceInstanceReader;
import jp.go.nict.langrid.foundation.AbstractLangridService;
import jp.go.nict.langrid.foundation.AttributedElementUpdater;
import jp.go.nict.langrid.foundation.annotation.AccessRightValidatedMethod;
import jp.go.nict.langrid.foundation.annotation.Log;
import jp.go.nict.langrid.foundation.annotation.TransactionMethod;
import jp.go.nict.langrid.foundation.util.validation.annotation.ValidAttribute;
import jp.go.nict.langrid.foundation.util.validation.annotation.ValidAttributeName;
import jp.go.nict.langrid.foundation.util.validation.annotation.ValidEnum;
import jp.go.nict.langrid.foundation.util.validation.annotation.ValidMatchingCondition;
import jp.go.nict.langrid.foundation.util.validation.annotation.ValidOrder;
import jp.go.nict.langrid.foundation.util.validation.annotation.ValidServiceId;
import jp.go.nict.langrid.foundation.util.validation.annotation.ValidUserId;
import jp.go.nict.langrid.language.InvalidLanguagePathException;
import jp.go.nict.langrid.language.InvalidLanguageTagException;
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.language.util.LanguagePathUtil;
import jp.go.nict.langrid.management.logic.QoSResult;
import jp.go.nict.langrid.management.logic.ServiceNotActivatableException;
import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.LanguagePath;
import jp.go.nict.langrid.service_1_2.LanguagePathWithType;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.ServiceConfigurationException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.service_1_2.UnknownException;
import jp.go.nict.langrid.service_1_2.UnsupportedMatchingMethodException;
import jp.go.nict.langrid.service_1_2.foundation.Attribute;
import jp.go.nict.langrid.service_1_2.foundation.MatchingCondition;
import jp.go.nict.langrid.service_1_2.foundation.Order;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.AttributeName;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.CompositeServiceEntry;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.CompositeServiceEntrySearchResult;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.Invocation;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.QoS;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.ServiceAlreadyExistsException;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.ServiceEntry;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.ServiceEntrySearchResult;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.ServiceEntryWithCompactLanguageExpression;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.ServiceEntryWithCompactLanguageExpressionSearchResult;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.ServiceInstance;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.ServiceManagementService;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.ServiceNotInactiveException;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.ServiceProfile;
import jp.go.nict.langrid.service_1_2.foundation.typed.Scope;
import jp.go.nict.langrid.service_1_2.foundation.util.ProfileKeyUtil;
import jp.go.nict.langrid.service_1_2.typed.InstanceType;
import jp.go.nict.langrid.service_1_2.util.ExceptionConverter;
import jp.go.nict.langrid.service_1_2.util.ParameterValidator;

/**
 * 
 * Service administration capabilities.
 * 
 * @author Takao Nakaguchi
 * @author $Author:nakaguchi $
 * @version $Revision:5259 $
 */
public class ServiceManagement
extends AbstractLangridService
implements ServiceManagementService
{
	/**
	 * 
	 * Constructor.
	 * 
	 */
	public ServiceManagement(){
		init();
	}

	/**
	 * 
	 * Constructor.
	 * @param serviceContext Service context
	 * 
	 */
	public ServiceManagement(ServiceContext serviceContext){
		super(serviceContext);
		init();
	}

	@AccessRightValidatedMethod(policy=ADMINONLY)
	@TransactionMethod
	@Log
	public void clear()
		throws AccessLimitExceededException, NoAccessPermissionException
		, ServiceConfigurationException, UnknownException
	{
		try{
			getServiceLogic().clear();
		} catch(DaoException e){
			throw convertException(e);
		} catch(Throwable t){
			t.printStackTrace();
			throw ExceptionConverter.convertException(t);
		}
	}

	@AccessRightValidatedMethod
	@ValidatedMethod
	@TransactionMethod
	public ServiceEntrySearchResult searchServices(
			@IntNotNegative int startIndex
			, @IntInRange(minimum=0, maximum=100) int maxCount
			, @NotNull @EachElement
			@ValidMatchingCondition(supportedMatchingMethods={
					COMPLETE, PARTIAL, PREFIX, SUFFIX
					, LANGUAGEPATH, IN, EQ, GT, GE, LT, LE
			})
			MatchingCondition[] conditions
			, @NotNull @EachElement @ValidOrder Order[] orders
			, @NotEmpty @ValidEnum(Scope.class) String scope
			)
			throws AccessLimitExceededException,
			InvalidParameterException, NoAccessPermissionException,
			ServiceConfigurationException, UnknownException,
			UnsupportedMatchingMethodException
	{
		adjustDateFieldName(conditions);
		jp.go.nict.langrid.dao.MatchingCondition[] conds = adjustCondsForSearch(conditions);
		adjustDateFieldName(orders);
		jp.go.nict.langrid.dao.Order[] ords = null;
		try{
			for(Order o : orders){
				if(o.getFieldName().equals("serviceType")){
					throw new InvalidParameterException(
							"orders", "ordering by serviceType is not supported.");
				}
			}
			ords = convert(orders, jp.go.nict.langrid.dao.Order[].class);
		} catch(ConversionException e){
			throw new InvalidParameterException(
					"orders", e.getMessage()
					);
		}

		try{
			ServiceSearchResult r = getServiceLogic().searchServicesOverGrids(
					startIndex, maxCount
					, getGridId(), getUserChecker().getUserId()
					, conds, ords, jp.go.nict.langrid.management.logic.Scope.valueOf(scope)
					);
			int n = r.getElements().length;
			ServiceEntry[] entries = new ServiceEntry[n];
			for(int i = 0; i < n; i++){
				Service s = r.getElements()[i];
				ServiceEntry et = convert(s, ServiceEntry.class);
				if(!disableIntegridFunctionality){
					et.setServiceId(s.getGridId() + ":" + s.getServiceId());
					et.setOwnerUserId(s.getGridId() + ":" + et.getOwnerUserId());
				}
				et.setServiceTypeDomain(
						Optional.ofNullable(s.getServiceTypeDomainId()).orElse("UNKNOWN"));
				et.setServiceType(
						Optional.ofNullable(s.getServiceTypeId()).orElse("OTHER").toUpperCase());
				et.setSupportedLanguages(generateSupportedLanguages(s));
				if(getCoreNodeUrl().length() > 0){
					et.setEndpointUrl(getCoreNodeUrl() + "invoker/" + s.getGridId() + ":" + s.getServiceId());
				}
				entries[i] = et;
			}
			return new ServiceEntrySearchResult(
					entries, r.getTotalCount(), r.isTotalCountFixed()
					);
		} catch(DaoException e){
			throw convertException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	@AccessRightValidatedMethod
	@ValidatedMethod
	@TransactionMethod
	@Override
	public ServiceEntrySearchResult searchServicesWithQos(
			@IntNotNegative int startIndex
			, @IntInRange(minimum=0, maximum=100) int maxCount
			, @NotNull @EachElement
			@ValidMatchingCondition(supportedMatchingMethods={
					COMPLETE, PARTIAL, PREFIX, SUFFIX
					, LANGUAGEPATH, IN, EQ, GT, GE, LT, LE
			})
			MatchingCondition[] conditions
			, @NotNull @EachElement @ValidOrder Order[] orders
			, @NotEmpty @ValidEnum(Scope.class) String scope,
			@NotNull String[] qosTypes,
			@NotNull Calendar qosBeginTime,
			@NotNull Calendar qosEndTime)
			throws AccessLimitExceededException, InvalidParameterException, NoAccessPermissionException,
			ServiceConfigurationException, UnknownException, UnsupportedMatchingMethodException {
		adjustDateFieldName(conditions);
		jp.go.nict.langrid.dao.MatchingCondition[] conds = adjustCondsForSearch(conditions);
		adjustDateFieldName(orders);
		jp.go.nict.langrid.dao.Order[] ords = null;
		try{
			for(Order o : orders){
				if(o.getFieldName().equals("serviceType")){
					throw new InvalidParameterException(
							"orders", "ordering by serviceType is not supported.");
				}
			}
			ords = convert(orders, jp.go.nict.langrid.dao.Order[].class);
		} catch(ConversionException e){
			throw new InvalidParameterException(
					"orders", e.getMessage()
					);
		}

		try{
			ServiceSearchResult r = getServiceLogic().searchServicesOverGrids(
					startIndex, maxCount
					, getGridId(), getUserChecker().getUserId()
					, conds, ords, jp.go.nict.langrid.management.logic.Scope.valueOf(scope)
					);
			int n = r.getElements().length;
			ServiceEntry[] entries = new ServiceEntry[n];
			QoSResult[][] qos = getServiceLogic().getQoS(qosTypes, getGridId(),
					ArrayUtil.collect(r.getElements(), String.class, s -> s.getServiceId()),
					qosBeginTime, qosEndTime);
			for(int i = 0; i < n; i++){
				Service s = r.getElements()[i];
				ServiceEntry et = convert(s, ServiceEntry.class);
				if(!disableIntegridFunctionality){
					et.setServiceId(s.getGridId() + ":" + s.getServiceId());
					et.setOwnerUserId(s.getGridId() + ":" + et.getOwnerUserId());
				}
				et.setServiceTypeDomain(
						Optional.ofNullable(s.getServiceTypeDomainId()).orElse("UNKNOWN"));
				et.setServiceType(
						Optional.ofNullable(s.getServiceTypeId()).orElse("OTHER").toUpperCase());
				et.setSupportedLanguages(generateSupportedLanguages(s));
				if(getCoreNodeUrl().length() > 0){
					et.setEndpointUrl(getCoreNodeUrl() + "invoker/" + s.getGridId() + ":" + s.getServiceId());
				}
				et.setQos(ArrayUtil.collect(qos[i], QoS.class, q -> {
					return new QoS(q.getType().name(), q.getDenominator(), q.getValue());
				}));
				entries[i] = et;
			}
			return new ServiceEntrySearchResult(
					entries, r.getTotalCount(), r.isTotalCountFixed()
					);
		} catch(DaoException e){
			throw convertException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	@AccessRightValidatedMethod
	@ValidatedMethod
	@TransactionMethod
	public ServiceEntryWithCompactLanguageExpressionSearchResult searchServicesWithCompactLanguageExpression(
			@IntNotNegative int startIndex
			, @IntInRange(minimum=0, maximum=100) int maxCount
			, @NotNull @EachElement
			@ValidMatchingCondition(supportedMatchingMethods={
					COMPLETE, PARTIAL, PREFIX, SUFFIX
					, LANGUAGEPATH, IN, EQ, GT, GE, LT, LE
			})
			MatchingCondition[] conditions
			, @NotNull @EachElement @ValidOrder Order[] orders
			, @NotEmpty @ValidEnum(Scope.class) String scope
			)
			throws AccessLimitExceededException,
			InvalidParameterException, NoAccessPermissionException,
			ServiceConfigurationException, UnknownException,
			UnsupportedMatchingMethodException
	{
		adjustDateFieldName(conditions);
		jp.go.nict.langrid.dao.MatchingCondition[] conds = adjustCondsForSearch(conditions);

		adjustDateFieldName(orders);
		jp.go.nict.langrid.dao.Order[] ords = null;
		try{
			for(Order o : orders){
				if(o.getFieldName().equals("serviceType")){
					throw new InvalidParameterException(
							"orders", "ordering by serviceType is not supported.");
				}
			}
			ords = convert(orders, jp.go.nict.langrid.dao.Order[].class);
		} catch(ConversionException e){
			throw new InvalidParameterException(
					"orders", e.getMessage()
					);
		}

		try{
			ServiceSearchResult r = getServiceLogic().searchServicesOverGrids(
					startIndex, maxCount
					, getGridId(), getUserChecker().getUserId()
					, conds, ords, jp.go.nict.langrid.management.logic.Scope.valueOf(scope)
					);
			int n = r.getElements().length;
			ServiceEntryWithCompactLanguageExpression[] entries = new ServiceEntryWithCompactLanguageExpression[n];
			for(int i = 0; i < n; i++){
				Service s = r.getElements()[i];
				ServiceEntryWithCompactLanguageExpression et = convert(s, ServiceEntryWithCompactLanguageExpression.class);
				if(!disableIntegridFunctionality){
					et.setServiceId(s.getGridId() + ":" + s.getServiceId());
					et.setOwnerUserId(s.getGridId() + ":" + et.getOwnerUserId());
				}
				et.setServiceTypeDomainId(
						Optional.ofNullable(s.getServiceTypeDomainId()).orElse("UNKNOWN"));
				et.setServiceTypeId(
						Optional.ofNullable(s.getServiceTypeId()).orElse("OTHER").toUpperCase());
				et.setSupportedLanguages(generateSupportedLanguagesInCompactExpression(s));
				if(getCoreNodeUrl().length() > 0){
					et.setEndpointUrl(getCoreNodeUrl() + "invoker/" + s.getGridId() + ":" + s.getServiceId());
				}
				entries[i] = et;
			}
			return new ServiceEntryWithCompactLanguageExpressionSearchResult(
					entries, r.getTotalCount(), r.isTotalCountFixed()
					);
		} catch(DaoException e){
			throw convertException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	/**
	 * 
	 * Searches composite services.
	 * <p>Matching conditions can be set for the service's profile, instance, and attribute data.</p>
	 * <p>In CompositeServiceEntry, from among the services the composite service refers to, the list of what is registered in the language grid is included (servicesInUse).
	 * The service's references are resolved at the time the composite service is registered.</p>
	 * @param startIndex Starting position of search results
	 * @param maxCount Maximum number of search results
	 * @param conditions Matching conditions
	 * @param orders Sort direction
	 * @param scope Object range (scope)("ALL", "MINE", "ACCESSIBLE")
	 * @return Search results
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ServiceConfigurationException The service is not set up appropriately
	 * @throws UnknownException Process failed for an unknown reason
	 * @throws UnsupportedMatchingMethodException An unsupported matching method was specified
	 * 
	 */
	@AccessRightValidatedMethod
	@ValidatedMethod
	@TransactionMethod
	public CompositeServiceEntrySearchResult searchCompositeServices(
			@IntNotNegative int startIndex
			, @IntInRange(minimum=0, maximum=100) int maxCount
			, @NotNull @EachElement @ValidMatchingCondition MatchingCondition[] conditions
			, @NotNull @EachElement @ValidOrder Order[] orders
			, @NotEmpty @ValidEnum(Scope.class) String scope
			)
			throws AccessLimitExceededException,
			InvalidParameterException, NoAccessPermissionException,
			ServiceConfigurationException, UnknownException,
			UnsupportedMatchingMethodException
	{
		StringBuilder callerInfo = new StringBuilder();
		MessageContext ctx = MessageContext.getCurrentContext();
		if(ctx != null){
			HttpServletRequest req = (HttpServletRequest)ctx.getProperty(
					HTTPConstants.MC_HTTP_SERVLETREQUEST);
			if(req != null){
				callerInfo.append("userId: ");
				callerInfo.append(getServiceContext().getAuthUser());
				callerInfo.append("  remoteAddress: ");
				callerInfo.append(req.getRemoteAddr());
				callerInfo.append("  agent: ");
				callerInfo.append(req.getHeader("User-Agent"));
				callerInfo.append("  startIndex: ");
				callerInfo.append(startIndex);
				callerInfo.append("  maxCount: ");
				callerInfo.append(maxCount);
				callerInfo.append("  conditions: ");
				callerInfo.append(Arrays.toString(conditions));
				callerInfo.append("  orders: ");
				callerInfo.append(Arrays.toString(orders));
				callerInfo.append("  scope: ");
				callerInfo.append(scope);
			}
		}
		logger.info("searchCompositeServices called.  " + callerInfo);

		jp.go.nict.langrid.dao.MatchingCondition[] conds = null;
		adjustDateFieldName(conditions);
		try{
			conds = convert(
					conditions
					, jp.go.nict.langrid.dao.MatchingCondition[].class
					);
		} catch(ConversionException e){
			throw new InvalidParameterException(
					"conditions", e.getMessage());
		}
		conds = ArrayUtil.append(
				conds
				, new jp.go.nict.langrid.dao.MatchingCondition(
						"instanceType", InstanceType.BPEL)
				);

		jp.go.nict.langrid.dao.Order[] ords = null;
		adjustDateFieldName(orders);
		try{
			ords = convert(orders, jp.go.nict.langrid.dao.Order[].class);
		} catch(ConversionException e){
			throw new InvalidParameterException(
					"orders", e.getMessage()
					);
		}
		ServiceEntrySearchResult r = null;
		try{
			r = convert(getServiceLogic().searchServices(
					startIndex, maxCount
					, getGridId(), getUserChecker().getUserId()
					, getGridId(), true
					, conds, ords, jp.go.nict.langrid.management.logic.Scope.valueOf(scope)
					), ServiceEntrySearchResult.class);
		} catch(DaoException e){
			throw convertException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
		CompositeServiceEntry[] entries = new CompositeServiceEntry[
				r.getElements().length];
		try{
			for(int i = 0; i < r.getElements().length; i++){
				ServiceEntry entry = r.getElements()[i];
				String serviceId = entry.getServiceId();
				BPELService service = (BPELService)getServiceDao().getService(
						getGridId(), serviceId);
				List<ServiceEntry> servicesInUse = new ArrayList<ServiceEntry>();
				for(jp.go.nict.langrid.dao.entity.Invocation invocation : service.getInvocations()){
					try{
						servicesInUse.add(convert(
							getServiceDao().getService(
									invocation.getServiceGridId(), invocation.getServiceId())
							, ServiceEntry.class));
					} catch(jp.go.nict.langrid.dao.ServiceNotFoundException e){
						// 
						// When not found, everything but ID is NULL
						// 
						if(invocation.getServiceGridId() != null){
							servicesInUse.add(new ServiceEntry(
									invocation.getServiceId(), null, null, null, null
									, null, null, null, null, null, null
									, false
									));
						}
					}
				}
				entries[i] = new CompositeServiceEntry(
						entry.getServiceId(), entry.getServiceName()
						, entry.getServiceDescription()
						, entry.getServiceTypeDomain()
						, entry.getServiceType()
						, entry.getInstanceType()
						, entry.getSupportedLanguages()
						, entry.getEndpointUrl()
						, entry.getOwnerUserId()
						, entry.getRegisteredDate(), entry.getUpdatedDate()
						, entry.isActive()
						, servicesInUse.toArray(new ServiceEntry[]{})
						);
			}
			return new CompositeServiceEntrySearchResult(
					entries, r.getTotalCount(), r.isTotalCountFixed()
					);
		} catch(DaoException e){
			throw convertException(e);
		} catch(Throwable t){
			t.printStackTrace();
			throw ExceptionConverter.convertException(t);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p></p>
	 * 
	 * <strong>Process done upon addition:</strong>
	 * <p>For existing users, the access privileges of the services to be added will be set.
	 * When default access privilege data is included in attributes then they are set,if none it is set to "permitted". Access restrictions are not added at all.</p>
	 * <p>When the added service is a composite service, the targetNamespace of the WSDL file it refers to
	 * Existing services are searched by this, and if found, added to the list of referred services.
	 * The return value of searchCompositeServices is included in this list.
	 * Used when the servicesInUse property of CompositeService is set.</p>
	 * <strong>Format of ServiceInstance#instance</strong>
	 * <p>In the case of external services, stores the service's WSDL compressed as a jar (filename is "serviceID.wsdl").
	 * If a composite service, the BPEL file ("serviceID.bpel")and the WSDL file group that refers to it
	 * Specifies a jar compressed ("foo.wsdl").</p>
	 * 
	 */
	@AccessRightValidatedMethod(policy=ADMINONLY)
	@ValidatedMethod
	@TransactionMethod
	@Log
	public void addService(
			@NotEmpty @ValidServiceId String serviceId
			, @NotNull ServiceProfile profile
			, @NotNull ServiceInstance instance
			, @NotNull @EachElement @ValidAttribute Attribute[] attributes
			)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceAlreadyExistsException
		, ServiceConfigurationException, UnknownException
	{
		String authUserId = getServiceContext().getAuthUser();
		doAddService(authUserId, serviceId, profile, instance, attributes);
	}

	/**
	 * {@inheritDoc}
	 * <p></p>
	 * 
	 * <strong>Process done upon addition:</strong>
	 * <p>For existing users, the access privileges of the services to be added will be set.
	 * When default access privilege data is included in attributes then they are set,if none it is set to "permitted". Access restrictions are not added at all.</p>
	 * <p>When the added service is a composite service, the targetNamespace of the WSDL file it refers to
	 * Existing services are searched by this, and if found, added to the list of referred services.
	 * The return value of searchCompositeServices is included in this list.
	 * Used when the servicesInUse property of CompositeService is set.</p>
	 * <strong>Format of ServiceInstance#instance</strong>
	 * <p>In the case of external services, stores the service's WSDL compressed as a jar (filename is "serviceID.wsdl").
	 * If a composite service, the BPEL file ("serviceID.bpel")and the WSDL file group that refers to it
	 * Specifies a jar compressed ("foo.wsdl").</p>
	 * 
	 */
	@AccessRightValidatedMethod(policy=ADMINONLY)
	@ValidatedMethod
	@TransactionMethod
	@Log
	public void addServiceAs(
			@NotEmpty @ValidUserId String ownerUserId
			, @NotEmpty @ValidServiceId String serviceId
			, @NotNull ServiceProfile profile
			, @NotNull ServiceInstance instance
			, @NotNull @EachElement @ValidAttribute Attribute[] attributes
			)
	throws AccessLimitExceededException, InvalidParameterException
	, NoAccessPermissionException, ServiceAlreadyExistsException
	, ServiceConfigurationException, UnknownException
	{
		doAddService(ownerUserId, serviceId, profile, instance, attributes);
	}

	@ValidatedMethod
	@AccessRightValidatedMethod(policy=SERVICEOWNER_OR_ADMIN, argNames="serviceId")
	@TransactionMethod
	public void deleteService(
			@NotEmpty @ValidServiceId String serviceId
			)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, ServiceNotFoundException, ServiceNotInactiveException
		, UnknownException
	{
		try{
			getServiceLogic().deleteService(getGridId(), serviceId);
		} catch(jp.go.nict.langrid.management.logic.ServiceNotInactiveException e){
			throw new ServiceNotInactiveException(
					e.getServiceId());
		} catch(jp.go.nict.langrid.dao.ServiceNotFoundException e){
			throw convertException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	@ValidatedMethod
	@AccessRightValidatedMethod
	@TransactionMethod
	public ServiceProfile getServiceProfile(
			@NotEmpty @ValidServiceId String serviceId
			)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, ServiceNotFoundException, UnknownException
	{
		try{
			String gridId;
			String[] ids = serviceId.split(":", 2);
			if(ids.length == 2){
				gridId = ids[0];
				serviceId = ids[1];
			} else{
				gridId = getGridId();
				serviceId = ids[0];
			}
			return convert(
					getServiceDao().getService(gridId, serviceId)
					, ServiceProfile.class
					);
		} catch(jp.go.nict.langrid.dao.ServiceNotFoundException e){
			throw convertException(e);
		} catch(DaoException e){
			throw convertException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	@ValidatedMethod
	@AccessRightValidatedMethod(policy=SERVICEOWNER_OR_ADMIN, argNames="serviceId")
	@TransactionMethod
	@Log
	public void setServiceProfile(
			@NotEmpty @ValidServiceId String serviceId
			, @NotNull ServiceProfile profile
			)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, ServiceNotFoundException, UnknownException
	{
		try{
			ServiceDao dao = getServiceDao();
			Service s = dao.getService(getGridId(), serviceId);
			copyProperties(s, profile);
		} catch(jp.go.nict.langrid.dao.ServiceNotFoundException e){
			throw convertException(e);
		} catch(DaoException e){
			throw convertException(e);
		} catch(ConversionException e){
			Throwable t = e;
			if(e.getCause() != null){
				t = e.getCause();
			}
			throw new InvalidParameterException(
					"profile", t.toString());
//*
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
//*/
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>When an attribute name is specified that does not exist in attributeNames, that attribute name is ignored.
	 * When an empty string is set, gets all attributes which can be acquired.</p>
	 * 
	 */
	@ValidatedMethod
	@AccessRightValidatedMethod
	@TransactionMethod
	public Attribute[] getServiceAttributes(
			@NotEmpty @ValidServiceId String serviceId
			, @NotNull @EachElement @ValidAttributeName String[] attributeNames
			)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, ServiceNotFoundException, UnknownException
	{
		String gridId;
		String[] ids = serviceId.split(":", 2);
		if(ids.length == 2){
			gridId = ids[0];
			serviceId = ids[1];
		} else{
			gridId = getGridId();
			serviceId = ids[0];
		}
		Set<String> names = new HashSet<String>();
		for(String n : attributeNames){
			names.add(n);
		}
		try{
			ServiceDao dao = getServiceDao();
			Service s = dao.getService(gridId, serviceId);
			List<Attribute> attrs = new ArrayList<Attribute>();
			if(names.size() == 0){
				for(jp.go.nict.langrid.dao.entity.Attribute a : s.getAttributes()){
					attrs.add(new Attribute(a.getName(), a.getValue()));
				}
			} else{
				for(jp.go.nict.langrid.dao.entity.Attribute a : s.getAttributes()){
					if(names.contains(a.getName())){
						attrs.add(new Attribute(a.getName(), a.getValue()));
					}
				}
			}
			return attrs.toArray(new Attribute[]{});
		} catch(jp.go.nict.langrid.dao.ServiceNotFoundException e){
			throw convertException(e);
		} catch(DaoException e){
			throw convertException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>When the specified attribute's value field is an empty string, that attribute is deleted.</p>
	 * 
	 */
	@ValidatedMethod
	@AccessRightValidatedMethod(policy=SERVICEOWNER_OR_ADMIN, argNames="serviceId")
	@TransactionMethod
	@Log
	public void setServiceAttributes(
			@NotEmpty @ValidServiceId String serviceId
			, @NotNull @EachElement @ValidAttribute Attribute[] attributes
			)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, ServiceNotFoundException, UnknownException
	{
		validateInputAttribute("attributes", attributes);
		try{
			ServiceDao dao = getServiceDao();
			Service s = dao.getService(getGridId(), serviceId);
			AttributedElementUpdater.updateAttributes(
					s, attributes
					, ServiceUtil.getProperties());
		} catch(jp.go.nict.langrid.dao.ServiceNotFoundException e){
			throw convertException(e);
//*
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
//*/
		}
	}

	@ValidatedMethod
	@AccessRightValidatedMethod(policy=SERVICEOWNER_OR_ADMIN, argNames="serviceId")
	@TransactionMethod
	public ServiceInstance getServiceInstance(
			@NotEmpty @ValidServiceId String serviceId
			)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, ServiceNotFoundException, UnknownException
	{
		try{
			return convert(
					getServiceDao().getService(getGridId(), serviceId)
					, ServiceInstance.class
					);
		} catch(jp.go.nict.langrid.dao.ServiceNotFoundException e){
			throw convertException(e);
		} catch(DaoException e){
			throw convertException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>When this API is called against an active service, throws ServiceNotInactiveException.</p>
	 * 
	 */
	@ValidatedMethod
	@AccessRightValidatedMethod(policy=SERVICEOWNER_OR_ADMIN, argNames="serviceId")
	@TransactionMethod
	@Log
	public void setServiceInstance(
			@NotEmpty @ValidServiceId String serviceId
			, @NotNull ServiceInstance instance
			)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, ServiceNotFoundException, ServiceNotInactiveException
		, UnknownException
	{
		validateInputInstanceInstance(
				"instance.instance"
				, getValidInstanceType("instance.instanceType", instance.getInstanceType())
				, instance.getInstance());
		try{
			String gridId = getGridId();
			ServiceDao dao = getServiceDao();
			Service s = dao.getService(gridId, serviceId);
			if(s.isActive()){
				throw new ServiceNotInactiveException(gridId + ":" + serviceId);
			}
			copyProperties(s, instance);
			s.touchUpdatedDateTime();
		} catch(ServiceNotInactiveException e){
			throw e;
		} catch(jp.go.nict.langrid.dao.ServiceNotFoundException e){
			throw convertException(e);
		} catch(DaoException e){
			throw convertException(e);
//*
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
//*/
		}
	}

	@ValidatedMethod
	@AccessRightValidatedMethod
	@TransactionMethod
	public byte[] getServiceWsdl(
			@NotEmpty @ValidServiceId String serviceId
			)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, ServiceNotFoundException, UnknownException
	{
		String gridId;
		String[] ids = serviceId.split(":", 2);
		if(ids.length == 2){
			gridId = ids[0];
			serviceId = ids[1];
		} else{
			gridId = getGridId();
			serviceId = ids[0];
		}
		try{
			return getServiceLogic().getServiceWsdl(getCoreNodeUrl(), gridId, serviceId);
		} catch(jp.go.nict.langrid.dao.ServiceNotFoundException e){
			throw convertException(e);
		} catch(DaoException e){
			throw convertException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>Doesn't do anything when the state is already active.</p>
	 * 
	 */
	@ValidatedMethod
	@AccessRightValidatedMethod(policy=SERVICEOWNER_OR_ADMIN, argNames="serviceId")
	@TransactionMethod
	@Log
	public void activateService(
			@NotEmpty @ValidServiceId String serviceId
			)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, jp.go.nict.langrid.service_1_2.foundation.servicemanagement.ServiceNotActivatableException
		, ServiceNotFoundException, UnknownException
	{
		try{
			getServiceLogic().activateService(getGridId(), serviceId);
		} catch(jp.go.nict.langrid.dao.ServiceNotFoundException e){
			throw convertException(e);
		} catch(DaoException e){
			throw convertException(e);
		} catch(jp.go.nict.langrid.management.logic.ServiceNotActivatableException e){
			throw new jp.go.nict.langrid.service_1_2.foundation.servicemanagement.ServiceNotActivatableException(
					serviceId, ExceptionUtil.getMessageWithStackTrace(e));
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>Does nothing if already in an inactive state.</p>
	 * 
	 */
	@ValidatedMethod
	@AccessRightValidatedMethod(policy=ADMINONLY)
	@TransactionMethod
	@Log
	public void deactivateService(
			@NotEmpty @ValidServiceId String serviceId
			)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, jp.go.nict.langrid.service_1_2.foundation.servicemanagement.ServiceNotDeactivatableException
		, ServiceNotFoundException, UnknownException
	{
		try{
			getServiceLogic().deactivateService(getGridId(), serviceId);
		} catch(jp.go.nict.langrid.dao.ServiceNotFoundException e){
			throw convertException(e);
		} catch(DaoException e){
			throw convertException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	@ValidatedMethod
	@AccessRightValidatedMethod
	@TransactionMethod
	public boolean isServiceActive(
			@NotEmpty @ValidServiceId String serviceId
			)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, ServiceNotFoundException, UnknownException
	{
		try{
			return getServiceLogic().isServiceActive(getGridId(), serviceId);
		} catch(jp.go.nict.langrid.dao.ServiceNotFoundException e){
			throw convertException(e);
		} catch(DaoException e){
			throw convertException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>When called against a simple service always returns an empty result.</p>
	 * 
	 */
	@ValidatedMethod
	@AccessRightValidatedMethod
	@TransactionMethod
	public Invocation[] getExternalInvocations(
			@NotEmpty @ValidServiceId String serviceId
			)
			throws AccessLimitExceededException, InvalidParameterException,
			NoAccessPermissionException, ServiceConfigurationException,
			ServiceNotFoundException, UnknownException
	{
		try{
			String gridId;
			String[] ids = serviceId.split(":", 2);
			if(ids.length == 2){
				gridId = ids[0];
				serviceId = ids[1];
			} else{
				gridId = getGridId();
				serviceId = ids[0];
			}
			return convert(
					getServiceLogic().getExternalInvocations(gridId, serviceId)
					, Invocation[].class
					);
		} catch(jp.go.nict.langrid.dao.ServiceNotFoundException e){
			throw convertException(e);
		} catch(DaoException e){
			throw convertException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	@ValidatedMethod
	@AccessRightValidatedMethod(policy=SERVICEOWNER_OR_ADMIN)
	@TransactionMethod
	public boolean isServiceVisible(@NotEmpty @ValidServiceId String serviceId)
	throws AccessLimitExceededException, InvalidParameterException,
			NoAccessPermissionException, ServiceConfigurationException,
			ServiceNotFoundException, UnknownException
	{
		try{
			return getServiceLogic().isServiceVisible(getGridId(), serviceId);
		} catch(jp.go.nict.langrid.dao.ServiceNotFoundException e){
			throw convertException(e);
		} catch(DaoException e){
			throw convertException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	@ValidatedMethod
	@AccessRightValidatedMethod(policy=SERVICEOWNER_OR_ADMIN)
	@TransactionMethod
	@Log
	public void setServiceVisible(
			@NotEmpty @ValidServiceId String serviceId
			, boolean visible)
			throws AccessLimitExceededException, InvalidParameterException,
			NoAccessPermissionException, ServiceConfigurationException,
			ServiceNotFoundException, UnknownException {
		try{
			ServiceDao dao = getServiceDao();
			dao.getService(getGridId(), serviceId).setVisible(visible);
		} catch(jp.go.nict.langrid.dao.ServiceNotFoundException e){
			throw convertException(e);
		} catch(DaoException e){
			throw convertException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	private jp.go.nict.langrid.dao.MatchingCondition[] adjustCondsForSearch(MatchingCondition[] conditions)
	throws InvalidParameterException{
		try{
			List<jp.go.nict.langrid.dao.MatchingCondition> condList
					= new ArrayList<jp.go.nict.langrid.dao.MatchingCondition>();
			boolean containsApproved = false;
			for(MatchingCondition c : conditions){
				String fn = c.getFieldName().toLowerCase();
				if(fn.equals("approved")){
					containsApproved = true;
				} else if(fn.equals("instancetype")){
					String mv = c.getMatchingValue().toLowerCase();
					if(mv.equals("external")){
						condList.add(new jp.go.nict.langrid.dao.MatchingCondition(
								"containerType", ServiceContainerType.ATOMIC
								));
					} else if(mv.equals("bpel")){
						condList.add(new jp.go.nict.langrid.dao.MatchingCondition(
								"containerType", ServiceContainerType.COMPOSITE
								));
					}
					continue;
				} else if(fn.equals("servicetype")){
					condList.add(new jp.go.nict.langrid.dao.MatchingCondition(
							"serviceTypeId", c.getMatchingValue()
							));
					continue;
				}
				condList.add(convert(c, jp.go.nict.langrid.dao.MatchingCondition.class));
			}
			if(!containsApproved){
				condList.add(new jp.go.nict.langrid.dao.MatchingCondition(
						"approved", "true", MatchingMethod.COMPLETE));
			}
			return condList.toArray(new jp.go.nict.langrid.dao.MatchingCondition[]{});
		} catch(ConversionException e){
			throw new InvalidParameterException(
					"conditions", e.getMessage()
					);
		}
	}

	private void doAddService(
			String ownerUserId, String serviceId
			, ServiceProfile profile, ServiceInstance instance
			, Attribute[] attributes
			)
	throws AccessLimitExceededException, InvalidParameterException
	, NoAccessPermissionException, ServiceAlreadyExistsException
	, ServiceConfigurationException, UnknownException
	{		
		InstanceType instanceType = null;
		try{
			// instance check
			instanceType = getValidInstanceType("instance.instanceType", instance.getInstanceType());
		} catch(InvalidParameterException e){
			throw e;
		} catch(Throwable t){
			t.printStackTrace();
			throw ExceptionConverter.convertException(t);
		}

		try{
			ServiceDao dao = getServiceDao();
			NodeDao ndao = getNodeDao();
			Map<String, String> attrs = new HashMap<String, String>();
			for(Attribute a : attributes){
				attrs.put(a.getName(), a.getValue());
			}
			Pair<Service, Boolean> ret = ServiceCreator.createService(
					dao, ndao, getGridId(), serviceId, instanceType, profile, instance, attrs
					, ownerUserId, getConverter(), getServiceContext());

			Service s = ret.getFirst();
			boolean defaultActive = s.isActive();
			boolean defaultPermitted = ret.getSecond();
			getServiceLogic().addService(
					ownerUserId, s
					, URLUtil.getContextUrl(getServiceContext().getRequestUrl()) + "invoker/"
					, defaultPermitted);

			if(defaultActive){
				try{
					getServiceLogic().activateService(s.getGridId(), s.getServiceId());
				} catch(ServiceNotActivatableException e){
					logger.log(Level.WARNING, "failed to activate service.", e);
				}
			}
		} catch(InvalidServiceInstanceException e){
			throw new InvalidParameterException(
					"instance.instance", "Service instance isn't valid data.["
					+ e.toString() + "]");
		} catch(jp.go.nict.langrid.dao.ServiceAlreadyExistsException e){
			throw convertException(e);
		} catch(jp.go.nict.langrid.dao.DaoException e){
			throw convertException(e);
		} catch(Throwable e){
			e.printStackTrace();
			throw ExceptionConverter.convertException(e);
		}
	}

	private LanguagePath[] generateSupportedLanguages(Service service){
		List<LanguagePath> paths = new ArrayList<LanguagePath>();
		try{
			// supported language
			String v = service.getAttributeValue("supportedLanguages");
			if(v != null){
				jp.go.nict.langrid.language.LanguagePath[] p = LanguagePathUtil.decodeLanguagePathArray(v);
				for(Object path : p){
					paths.add(convert(path, LanguagePath.class));
				}
			}
		} catch(InvalidLanguagePathException e){
		} catch(InvalidLanguageTagException e){
		}
		try{
			// supported language pairs - any combination
			String v = service.getAttributeValue("supportedLanguagePairs_AnyCombination");
			if(v != null){
				Set<Language> langs = new HashSet<Language>();
				jp.go.nict.langrid.language.LanguagePath[] p = LanguagePathUtil.decodeLanguagePathArray(v);
				for(jp.go.nict.langrid.language.LanguagePath path : p){
					for(Language l : path.getPath()){
						langs.add(l);
					}
				}
				Language[] ls = langs.toArray(new Language[]{});
				int n = ls.length;
				for(int i = 0; i < (n - 1); i++){
					for(int j = i + 1; j < n; j++){
						paths.add(new LanguagePath(new String[]{ls[i].getCode(), ls[j].getCode()}));
						paths.add(new LanguagePath(new String[]{ls[j].getCode(), ls[i].getCode()}));
					}
				}
			}
		} catch(InvalidLanguagePathException e){
		} catch(InvalidLanguageTagException e){
		}
		try{
			// supported language pairs - pair list
			String v = service.getAttributeValue("supportedLanguagePairs_PairList");
			if(v != null){
				jp.go.nict.langrid.language.LanguagePath[] p = LanguagePathUtil.decodeLanguagePathArray(v);
				for(jp.go.nict.langrid.language.LanguagePath path : p){
					paths.add(convert(path, LanguagePath.class));
				}
			}
		} catch(InvalidLanguagePathException e){
		} catch(InvalidLanguageTagException e){
		}
		try{
			// supported language pairs - any combination
			String v = service.getAttributeValue("supportedLanguagePaths_AnyCombination");
			if(v != null){
				Set<Language> langs = new HashSet<Language>();
				jp.go.nict.langrid.language.LanguagePath[] p = LanguagePathUtil.decodeLanguagePathArray(v);
				for(jp.go.nict.langrid.language.LanguagePath path : p){
					for(Language l : path.getPath()){
						langs.add(l);
					}
				}
				Language[] ls = langs.toArray(new Language[]{});
				int n = ls.length;
				for(int i = 0; i < (n - 1); i++){
					for(int j = i + 1; j < n; j++){
						paths.add(new LanguagePath(new String[]{ls[i].getCode(), ls[j].getCode()}));
					}
				}
			}
		} catch(InvalidLanguagePathException e){
		} catch(InvalidLanguageTagException e){
		}
		try{
			// supported language pairs - pair list
			String v = service.getAttributeValue("supportedLanguagePaths_PathList");
			if(v != null){
				jp.go.nict.langrid.language.LanguagePath[] p = LanguagePathUtil.decodeLanguagePathArray(v);
				for(jp.go.nict.langrid.language.LanguagePath path : p){
					paths.add(convert(path, LanguagePath.class));
				}
			}
		} catch(InvalidLanguagePathException e){
		} catch(InvalidLanguageTagException e){
		}
		return paths.toArray(new LanguagePath[]{});
	}

	private LanguagePathWithType[] generateSupportedLanguagesInCompactExpression(Service service){
		List<LanguagePathWithType> paths = new ArrayList<LanguagePathWithType>();
		
		try{
			// supported language
			String v = service.getAttributeValue("supportedLanguages");
			if(v != null){
				jp.go.nict.langrid.language.LanguagePath[] p = LanguagePathUtil.decodeLanguagePathArray(v);
				Set<String> list = new LinkedHashSet<String>();
				for(jp.go.nict.langrid.language.LanguagePath path : p){
					for(String l : convert(path, LanguagePath.class).getLanguages()) list.add(l);
				}
				paths.add(new LanguagePathWithType(
						list.toArray(new String[]{}), "LANG_LIST"
						));
			}
		} catch(InvalidLanguagePathException e){
		} catch(InvalidLanguageTagException e){
		}
		try{
			// supported language pairs - any combination
			String v = service.getAttributeValue("supportedLanguagePairs_AnyCombination");
			if(v != null){
				jp.go.nict.langrid.language.LanguagePath[] p = LanguagePathUtil.decodeLanguagePathArray(v);
				for(jp.go.nict.langrid.language.LanguagePath path : p){
					paths.add(new LanguagePathWithType(
							convert(path, LanguagePath.class).getLanguages(), "PAIR_COMBINATION"
							));
				}
			}
		} catch(InvalidLanguagePathException e){
		} catch(InvalidLanguageTagException e){
		}
		try{
			// supported language pairs - pair list
			String v = service.getAttributeValue("supportedLanguagePairs_PairList");
			if(v != null){
				jp.go.nict.langrid.language.LanguagePath[] p = LanguagePathUtil.decodeLanguagePathArray(v);
				Set<LanguagePath> hist = new HashSet<LanguagePath>();
				for(jp.go.nict.langrid.language.LanguagePath path : p){
					LanguagePath pair = convert(path, LanguagePath.class);
					if(hist.contains(pair)) continue;
					hist.add(pair);
					String[] langs = pair.getLanguages();
					if(langs.length < 2) continue;
					paths.add(new LanguagePathWithType(
							new String[]{langs[0], langs[langs.length - 1]}, "PAIR"
							));
				}
			}
		} catch(InvalidLanguagePathException e){
		} catch(InvalidLanguageTagException e){
		}
		try{
			// supported language pairs - any combination
			String v = service.getAttributeValue("supportedLanguagePaths_AnyCombination");
			if(v != null){
				jp.go.nict.langrid.language.LanguagePath[] p = LanguagePathUtil.decodeLanguagePathArray(v);
				for(jp.go.nict.langrid.language.LanguagePath path : p){
					paths.add(new LanguagePathWithType(
							convert(path, LanguagePath.class).getLanguages(), "PATH_COMBINATION"
							));
				}
			}
		} catch(InvalidLanguagePathException e){
		} catch(InvalidLanguageTagException e){
		}
		try{
			// supported language pairs - pair list
			String v = service.getAttributeValue("supportedLanguagePaths_PathList");
			if(v != null){
				jp.go.nict.langrid.language.LanguagePath[] p = LanguagePathUtil.decodeLanguagePathArray(v);
				Set<LanguagePath> hist = new HashSet<LanguagePath>();
				for(jp.go.nict.langrid.language.LanguagePath path : p){
					LanguagePath pair = convert(path, LanguagePath.class);
					if(hist.contains(pair)) continue;
					hist.add(pair);
					paths.add(new LanguagePathWithType(
							convert(path, LanguagePath.class).getLanguages(), "PATH"
							));
				}
			}
		} catch(InvalidLanguagePathException e){
		} catch(InvalidLanguageTagException e){
		}
		return paths.toArray(new LanguagePathWithType[]{});
	}
	

	private static InstanceType getValidInstanceType(String parameterName, String value)
		throws InvalidParameterException
	{
		if(value == null){
			throw new InvalidParameterException(parameterName, "is null");
		}
		try{
			return InstanceType.valueOf(value);
		} catch(IllegalArgumentException e){
			throw new InvalidParameterException(parameterName, "is not a valid instance type");
		}
	}

	private static void validateInputInstanceInstance(
			String parameterName
			, InstanceType instanceType, byte[] instance)
		throws InvalidParameterException
	{
		if(instance == null) throw new InvalidParameterException(
				parameterName, " is null."
				);
		if(instance.length == 0) return;
		try{
			ByteArrayInputStream is = new ByteArrayInputStream(instance);
			switch(instanceType){
				case EXTERNAL:{
					ExternalServiceInstanceReader r = new ExternalServiceInstanceReader(is);
					if(WSDLUtil.getTargetNamespace(r.getWsdl()) == null){
						throw new InvalidParameterException(
								parameterName + ".WSDL"
								, "can't find targetNamespace of WSDL");
					}
					break;
				}
				case BPEL:
					BPELServiceInstanceReader r = new BPRBPELServiceInstanceReader(is);
					byte[] b = null;
					InputStream bis = r.getBpel();
					try{
						b = StreamUtil.readAsBytes(bis);
					} finally{
						bis.close();
					}
					if(BPELUtil.getTargetNamespace(new ByteArrayInputStream(b)) == null
							&& BPELUtil.getWSBPEL_2_0_TargetNamespace(new ByteArrayInputStream(b)) == null)
					{
						throw new InvalidParameterException(
								parameterName + ".BPEL"
								, "can't find targetNamespace of BPEL");
					}
					for(int i = 0; i < r.getWsdlCount(); i++){
						if(WSDLUtil.getTargetNamespace(r.getWsdl(i)) == null){
							throw new InvalidParameterException(
									parameterName + "WSDL[" + i + "]"
									, "can't find targetNamespace of WSDL[" + i + "]");
						}
					}
				case LAR:
				case SCRIPT:
				default:
			}
		} catch(IOException e){
			throw new InvalidParameterException(parameterName, ExceptionUtil.getMessageWithStackTrace(e));
		} catch(SAXException e){
			throw new InvalidParameterException(parameterName, ExceptionUtil.getMessageWithStackTrace(e));
		} catch(URISyntaxException e){
			throw new InvalidParameterException(parameterName, ExceptionUtil.getMessageWithStackTrace(e));
		}
	}

	private static void validateInputAttribute(
			String parameterName, Attribute[] attributes)
		throws InvalidParameterException
	{
		ParameterValidator.objectNotNull(parameterName, attributes);

		Map<String, String> attrs = new HashMap<String, String>();
		for(Attribute e : attributes){
			if(e.getName() == null){
				throw new InvalidParameterException(
					parameterName, "has null key");
			}
			if(e.getName().length() == 0){
				throw new InvalidParameterException(
					parameterName, "has 0 length key");
			}
			attrs.put(e.getName(), e.getValue());
		}

		for(AttributeName key : ProfileKeyUtil.readonlyKeys){
			assertUnreservedKey(parameterName, attrs, key);
		}
	}

	/**
	 * 
	 * Tests whether the reservation key is included.
	 * When not included, throws an invalid parameter exception.
	 * @param parameterName Parameter name
	 * @param profile Profile
	 * @param key Reserved key
	 * @throws InvalidParameterException Invalid parameter exception
	 * 
	 */
	static private void assertUnreservedKey(
		String parameterName, Map<String, String> attributes, AttributeName key
		)
		throws InvalidParameterException
	{
		if(attributes.get(key.getAttributeName()) != null){
			throwReservedKeyException(parameterName, key);
		}
	}

	/**
	 * 
	 * Throws an invalid parameter exception.
	 * @param parameterName Parameter name
	 * @param key Key name
	 * @throws InvalidParameterException Invalid parameter exception
	 * 
	 */
	static private void throwReservedKeyException(
		String parameterName, AttributeName key)
		throws InvalidParameterException
	{
		throw new InvalidParameterException(
			String.format("%s.key[\"%s\"]", parameterName, key.getAttributeName())
			, "is reserved"
			);
	}

	private void init(){
		ParameterContext p = new ServiceContextParameterContext(getServiceContext());
		disableIntegridFunctionality = p.getBoolean(
				"langrid.disableIntergridFunctionality", false);
	}

	private boolean disableIntegridFunctionality;

	private static Logger logger = Logger.getLogger(
			ServiceManagement.class.getName());
}
