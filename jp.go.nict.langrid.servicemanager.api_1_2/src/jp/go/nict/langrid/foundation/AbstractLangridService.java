/*
 * $Id: AbstractLangridService.java 1161 2014-03-19 15:19:49Z t-nakaguchi $
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
package jp.go.nict.langrid.foundation;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.go.nict.langrid.commons.beanutils.BeanToBeanTransformer;
import jp.go.nict.langrid.commons.beanutils.ConversionException;
import jp.go.nict.langrid.commons.beanutils.Converter;
import jp.go.nict.langrid.commons.io.StreamUtil;
import jp.go.nict.langrid.commons.lang.ExceptionUtil;
import jp.go.nict.langrid.commons.parameter.ParameterContext;
import jp.go.nict.langrid.commons.transformer.ByteArrayToBlobTransformer;
import jp.go.nict.langrid.commons.transformer.ByteArrayToInputStreamTransformer;
import jp.go.nict.langrid.commons.transformer.InputStreamToByteArrayTransformer;
import jp.go.nict.langrid.commons.transformer.TransformationException;
import jp.go.nict.langrid.commons.transformer.Transformer;
import jp.go.nict.langrid.commons.util.CollectionUtil;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.commons.ws.axis.WebserviceServiceContext;
import jp.go.nict.langrid.commons.ws.param.ServiceContextParameterContext;
import jp.go.nict.langrid.dao.AccessLimitDao;
import jp.go.nict.langrid.dao.AccessLogDao;
import jp.go.nict.langrid.dao.AccessRightDao;
import jp.go.nict.langrid.dao.AccessStatDao;
import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.DaoFactory;
import jp.go.nict.langrid.dao.FederationDao;
import jp.go.nict.langrid.dao.GridDao;
import jp.go.nict.langrid.dao.NodeDao;
import jp.go.nict.langrid.dao.OverUseLimitDao;
import jp.go.nict.langrid.dao.OverUseStateDao;
import jp.go.nict.langrid.dao.ResourceDao;
import jp.go.nict.langrid.dao.ServiceDao;
import jp.go.nict.langrid.dao.ServiceDeploymentDao;
import jp.go.nict.langrid.dao.SystemPropertyDao;
import jp.go.nict.langrid.dao.TemporaryUserDao;
import jp.go.nict.langrid.dao.UserDao;
import jp.go.nict.langrid.dao.entity.AttributedElement;
import jp.go.nict.langrid.dao.entity.Invocation;
import jp.go.nict.langrid.dao.entity.Node;
import jp.go.nict.langrid.dao.entity.Resource;
import jp.go.nict.langrid.dao.entity.Service;
import jp.go.nict.langrid.dao.entity.User;
import jp.go.nict.langrid.dao.transformer.ServiceToServiceIdTransformer;
import jp.go.nict.langrid.language.CountryName;
import jp.go.nict.langrid.language.UserDefinedCountryName;
import jp.go.nict.langrid.management.logic.ServiceLogic;
import jp.go.nict.langrid.management.logic.UserLogic;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.ServiceConfigurationException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.service_1_2.UnknownException;
import jp.go.nict.langrid.service_1_2.foundation.Attribute;
import jp.go.nict.langrid.service_1_2.foundation.MatchingCondition;
import jp.go.nict.langrid.service_1_2.foundation.NodeNotFoundException;
import jp.go.nict.langrid.service_1_2.foundation.Order;
import jp.go.nict.langrid.service_1_2.foundation.nodemanagement.NodeEntry;
import jp.go.nict.langrid.service_1_2.foundation.resourcemanagement.ResourceEntry;
import jp.go.nict.langrid.service_1_2.foundation.resourcemanagement.ResourceNotFoundException;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.AttributeName;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.ServiceAlreadyExistsException;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.ServiceEntry;
import jp.go.nict.langrid.service_1_2.foundation.usermanagement.UserAlreadyExistsException;
import jp.go.nict.langrid.service_1_2.foundation.usermanagement.UserEntry;
import jp.go.nict.langrid.service_1_2.foundation.usermanagement.UserNotFoundException;
import jp.go.nict.langrid.service_1_2.transformer.LanguagePath_LanguageToWITransformer;
import jp.go.nict.langrid.service_1_2.transformer.LanguagePath_WIToLanguageTransformer;

import org.xml.sax.SAXException;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1161 $
 */
@SuppressWarnings("unchecked")
public class AbstractLangridService {
	/**
	 * 
	 * 
	 */
	public AbstractLangridService(){
		this.serviceContext = new WebserviceServiceContext();
		init();
	}

	/**
	 * 
	 * 
	 */
	public AbstractLangridService(ServiceContext serviceContext){
		this.serviceContext = serviceContext;
		init();
	}

	protected String getGridId(){
		String gid = getDefaultGridId();
		if(gid != null) return gid;
		String url = getServiceContext().getRequestUrl().toString();
		int prev = -1;
		int last = -1;
		int n = url.length();
		for(int i = 0; i < n; i++){
			if(url.charAt(i) == '/'){
				prev = last;
				last = i;
			}
		}
		gid = "";
		do{
			if(last == -1) break;
			if(last == (n - 1)){
				if(prev != -1){
					gid = url.substring(prev + 1, last).trim();
				} else{
					break;
				}
			} else {
				gid = url.substring(last + 1);
			}
		} while(false);
		gid = gid.trim();
		gid = gid.split("\\?")[0].trim();
		if(gid.length() == 0) return getDefaultGridId();
		if(managementServices.contains(gid)) return getDefaultGridId();
		return gid;
	}

	protected String getDefaultGridId(){
		return serviceContext.getSelfGridId();
	}

	/**
	 * 
	 * 
	 */
	protected DaoContext getDaoContext() throws DaoException{
		return getDaoFactory().getDaoContext();
	}

	/**
	 * 
	 * 
	 */
	protected ServiceContext getServiceContext(){
		return serviceContext;
	}

	/**
	 * 
	 * 
	 */
	protected SystemPropertyDao getSystemPropertyDao() throws DaoException{
		if(systemPropertyDao.get() == null){
			systemPropertyDao.set(getDaoFactory().createSystemPropertyDao());
		}
		return systemPropertyDao.get();
	}

	/**
	 * 
	 * 
	 */
	protected GridDao getGridDao() throws DaoException{
		if(gridDao.get() == null){
			gridDao.set(getDaoFactory().createGridDao());
		}
		return gridDao.get();
	}

	/**
	 * 
	 * 
	 */
	protected FederationDao getFederationDao() throws DaoException{
		if(federationDao.get() == null){
			federationDao.set(getDaoFactory().createFederationDao());
		}
		return federationDao.get();
	}

	/**
	 * 
	 * 
	 */
	protected UserDao getUserDao() throws DaoException{
		if(userDao.get() == null){
			userDao.set(getDaoFactory().createUserDao());
		}
		return userDao.get();
	}

	/**
	 * 
	 * 
	 */
	protected TemporaryUserDao getTemporaryUserDao() throws DaoException{
		if(temporaryUserDao.get() == null){
			temporaryUserDao.set(getDaoFactory().createTemporaryUserDao());
		}
		return temporaryUserDao.get();
	}

	/**
	 * 
	 * 
	 */
	protected ServiceDao getServiceDao() throws DaoException{
		if(serviceDao.get() == null){
			serviceDao.set(getDaoFactory().createServiceDao());
		}
		return serviceDao.get();
	}

	/**
	 * 
	 * 
	 */
	protected ServiceDeploymentDao getServiceDeploymentDao() throws DaoException{
		if(serviceDeploymentDao.get() == null){
			serviceDeploymentDao.set(getDaoFactory().createServiceDeploymentDao());
		}
		return serviceDeploymentDao.get();
	}

	/**
	 * 
	 * 
	 */
	protected NodeDao getNodeDao() throws DaoException{
		if(nodeDao.get() == null){
			nodeDao.set(getDaoFactory().createNodeDao());
		}
		return nodeDao.get();
	}

	/**
	 * 
	 * 
	 */
	protected AccessLogDao getAccessLogDao() throws DaoException{
		if(accessLogDao.get() == null){
			accessLogDao.set(getDaoFactory().createAccessLogDao());
		}
		return accessLogDao.get();
	}

	/**
	 * 
	 * 
	 */
	protected AccessRightDao getAccessRightDao() throws DaoException{
		if(accessRightDao.get() == null){
			accessRightDao.set(getDaoFactory().createAccessRightDao());
		}
		return accessRightDao.get();
	}

	/**
	 * 
	 * 
	 */
	protected AccessLimitDao getAccessLimitDao() throws DaoException{
		if(accessLimitDao.get() == null){
			accessLimitDao.set(getDaoFactory().createAccessLimitDao());
		}
		return accessLimitDao.get();
	}

	/**
	 * 
	 * 
	 */
	protected AccessStatDao getAccessStateDao() throws DaoException{
		if(accessStateDao.get() == null){
			accessStateDao.set(getDaoFactory().createAccessStateDao());
		}
		return accessStateDao.get();
	}

	/**
	 * 
	 * 
	 */
	protected OverUseLimitDao getOverUseLimitDao() throws DaoException{
		if(overUseLimitDao.get() == null){
			overUseLimitDao.set(getDaoFactory().createOverUseLimitDao());
		}
		return overUseLimitDao.get();
	}

	/**
	 * 
	 * 
	 */
	protected OverUseStateDao getOverUseStateDao() throws DaoException{
		if(overUseStateDao.get() == null){
			overUseStateDao.set(getDaoFactory().createOverUseStateDao());
		}
		return overUseStateDao.get();
	}

	/**
	 * 
	 * 
	 */
	protected ResourceDao getResourceDao() throws DaoException {
		if(resourceDao.get() == null){
			resourceDao.set(getDaoFactory().createResourceDao());
		}
		return resourceDao.get();
	}

	/**
	 * 
	 * 
	 */
	protected UserChecker getUserChecker() throws DaoException{
		if(userChecker.get() == null){
			String gid = serviceContext.getSelfGridId();
			if(gid == null) gid = getGridId();
			userChecker.set(new UserChecker(
					serviceContext, getDaoContext()
					, gid, getUserDao(), getServiceDao()
					));
		}
		return userChecker.get();
	}

	/**
	 * 
	 * 
	 */
	protected DaoFactory getDaoFactory() throws DaoException{
		if(factory.get() == null){
			factory.set(DaoFactory.createInstance());
		}
		return factory.get();
	}

	/**
	 * 
	 * 
	 */
	protected synchronized static void unsetFactory() {
	}

	/**
	 * 
	 * 
	 */
	protected String[] getTargetServiceIds(String serviceId)
		throws DaoException, ServiceConfigurationException
		, UserNotFoundException
	{
		if(serviceId.length() == 0){
			if(getUserChecker().isAuthUserAdmin()){
				return new String[]{};
			} else{
				List<Service> services = getServiceDao().listServicesOfUser(
						getGridId(), getUserChecker().getUserId());
				if(services.size() > 0){
					return CollectionUtil.collect(
							services, new ServiceToServiceIdTransformer()
							).toArray(new String[]{});
				} else{
					return null;
				}
			}
		} else{
			return new String[]{serviceId};
		}
	}

	/**
	 * 
	 * 
	 */
	protected static ServiceConfigurationException convertException(
		DaoException e)
	{
		logger.log(Level.SEVERE, "", e);
		return new ServiceConfigurationException(
				ExceptionUtil.getMessageWithStackTrace(e)
				);
	}

	/**
	 * 
	 * 
	 */
	protected static ServiceAlreadyExistsException convertException(
		jp.go.nict.langrid.dao.ServiceAlreadyExistsException e)
	{
		return new ServiceAlreadyExistsException(
			e.getServiceId()
			, ExceptionUtil.getMessageWithStackTrace(e)
			);
	}

	/**
	 * 
	 * 
	 */
	protected static ServiceNotFoundException convertException(
		jp.go.nict.langrid.dao.ServiceNotFoundException e)
	{
		return new ServiceNotFoundException(e.getServiceId());
	}

	/**
	 * 
	 * 
	 */
	protected static NodeNotFoundException convertException(
		jp.go.nict.langrid.dao.NodeNotFoundException e)
	{
		return new NodeNotFoundException(e.getNodeId());
	}

	/**
	 * 
	 * 
	 */
	protected static UserAlreadyExistsException convertException(
		jp.go.nict.langrid.dao.UserAlreadyExistsException e)
	{
		return new UserAlreadyExistsException(
			e.getUserId()
			);
	}

	/**
	 * 
	 * 
	 */
	protected static UserNotFoundException convertException(
		jp.go.nict.langrid.dao.UserNotFoundException e)
	{
		return new UserNotFoundException(
			e.getUserId()
			);
	}

	/**
	 * 
	 * 
	 */
	protected static ResourceNotFoundException convertException(
			jp.go.nict.langrid.dao.ResourceNotFoundException e)
	{
		return new ResourceNotFoundException(
				e.getResourceId()
		);
	}

	/**
	 * 
	 * 
	 */
	protected static UnknownException convertException(
		IOException e)
	{
		logger.log(Level.SEVERE, "", e);
		return new UnknownException(
				ExceptionUtil.getMessageWithStackTrace(e)
				);
	}

	/**
	 * 
	 * 
	 */
	protected static UnknownException convertException(
		SAXException e)
	{
		logger.log(Level.SEVERE, "", e);
		return new UnknownException(
				ExceptionUtil.getMessageWithStackTrace(e)
				);
	}

	protected Converter getConverter(){
		return converter;
	}

	protected void copyProperties(Object target, Object source){
		converter.copyProperties(target, source);
	}

	protected <T extends jp.go.nict.langrid.dao.entity.Attribute> void copyAttributes(
			AttributedElement<T> element
			, Attribute[] attrs
			, AttributeName[]... ignoreAttrs)
	{
		Set<String> ignoreNames = new HashSet<String>();
		for(AttributeName[] names : ignoreAttrs){
			for(AttributeName e : names){
				ignoreNames.add(e.getAttributeName());
			}
		}
		for(Attribute a : attrs){
			if(ignoreNames.contains(a.getName())) continue;
			element.setAttributeValue(a.getName(), a.getValue());
		}
	}

	protected <T extends jp.go.nict.langrid.dao.entity.Attribute> void copyAttributes(
			AttributedElement<T> element
			, Attribute[] attrs
			, String... ignoreAttrs)
	{
		Set<String> ignoreNames = new HashSet<String>(Arrays.asList(ignoreAttrs));
		for(Attribute a : attrs){
			if(ignoreNames.contains(a.getName())) continue;
			element.setAttributeValue(a.getName(), a.getValue());
		}
	}

	protected <T> T convert(Object value, Class<T> target)
	throws ConversionException
	{
		return converter.convert(value, target);
	}

	protected void adjustDateFieldName(MatchingCondition[] conditions)
	throws InvalidParameterException{
		for(MatchingCondition c : conditions){
			if(c.getFieldName().equals("registeredDate")){
				c.setFieldName("createdDateTime");
			} else if(c.getFieldName().equals("updatedDate")){
				c.setFieldName("updatedDateTime");
			}
		}
	}

	protected void adjustDateFieldName(Order[] orders)
	throws InvalidParameterException{
		for(Order o : orders){
			if(o.getFieldName().equals("registeredDate")){
				o.setFieldName("createdDateTime");
			} else if(o.getFieldName().equals("updatedDate")){
				o.setFieldName("updatedDateTime");
			}
		}
	}

	protected jp.go.nict.langrid.dao.Order[] convertUserOrder(
			String parameterName, Order[] orders)
	throws InvalidParameterException{
		try{
			jp.go.nict.langrid.dao.Order[] ords = convert(orders, jp.go.nict.langrid.dao.Order[].class);
			for(jp.go.nict.langrid.dao.Order o : ords){
				if(o.getFieldName().equals("registeredDate")){
					o.setFieldName("createdDateTime");
				} else if(o.getFieldName().equals("updatedDate")){
					o.setFieldName("updatedDateTime");
				}
			}
			return ords;
		} catch(ConversionException e){
			throw new InvalidParameterException(
					parameterName, e.getMessage()
					);
		}
	}

	protected ServiceLogic getServiceLogic(){
		return serviceLogic;
	}

	protected UserLogic getUserLogic(){
		return userLogic;
	}

	protected String getCoreNodeUrl(){
		return coreNodeUrl;
	}

	private void init(){
		ParameterContext p = new ServiceContextParameterContext(getServiceContext());
		String activeBpelServicesUrl = p.getValue(
				"langrid.activeBpelServicesUrl");
		String activeBpelDeployBinding = p.getString(
				"langrid.activeBpelDeployBinding", "RPC");
		coreNodeUrl = p.getString(
				"langrid.node.url", "");
		try{
			serviceLogic = new ServiceLogic(activeBpelServicesUrl, "", "", activeBpelDeployBinding);
			userLogic = new UserLogic();
		} catch(DaoException e){
			throw new RuntimeException(e);
		}
	}

	private ServiceContext serviceContext;
	private String coreNodeUrl;
	private ServiceLogic serviceLogic;
	private UserLogic userLogic;
	private ThreadLocal<DaoFactory> factory = new ThreadLocal<DaoFactory>();
	private ThreadLocal<SystemPropertyDao> systemPropertyDao = new ThreadLocal<SystemPropertyDao>();
	private ThreadLocal<GridDao> gridDao = new ThreadLocal<GridDao>();
	private ThreadLocal<FederationDao> federationDao = new ThreadLocal<FederationDao>();
	private ThreadLocal<UserDao> userDao = new ThreadLocal<UserDao>();
	private ThreadLocal<TemporaryUserDao> temporaryUserDao = new ThreadLocal<TemporaryUserDao>();
	private ThreadLocal<ServiceDao> serviceDao = new ThreadLocal<ServiceDao>();
	private ThreadLocal<ServiceDeploymentDao> serviceDeploymentDao = new ThreadLocal<ServiceDeploymentDao>();
	private ThreadLocal<NodeDao> nodeDao = new ThreadLocal<NodeDao>();
	private ThreadLocal<AccessLogDao> accessLogDao = new ThreadLocal<AccessLogDao>();
	private ThreadLocal<AccessRightDao> accessRightDao = new ThreadLocal<AccessRightDao>();
	private ThreadLocal<AccessLimitDao> accessLimitDao = new ThreadLocal<AccessLimitDao>();
	private ThreadLocal<AccessStatDao> accessStateDao = new ThreadLocal<AccessStatDao>();
	private ThreadLocal<OverUseLimitDao> overUseLimitDao = new ThreadLocal<OverUseLimitDao>();
	private ThreadLocal<OverUseStateDao> overUseStateDao = new ThreadLocal<OverUseStateDao>();
	private ThreadLocal<ResourceDao> resourceDao = new ThreadLocal<ResourceDao>();
	private ThreadLocal<UserChecker> userChecker = new ThreadLocal<UserChecker>();
	private static Converter converter = new Converter();
	private static Logger logger = Logger.getLogger(
			AbstractLangridService.class.getName());
	private static <T, U> void addBeanTransformerWithAlias(
			Converter converter
			, Class<T> srcClass, Class<U> destClass
			, Pair<String, String>... aliases){
		try {
			converter.addTransformerConversion(
					srcClass, destClass,
					new BeanToBeanTransformer<T, U>(
							converter, destClass, CollectionUtil.asMap(aliases)));
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		}
	}
	private static Set<String> managementServices = new HashSet<String>();
	static{
		managementServices.add("Authentication");
		managementServices.add("UserManagement");
		managementServices.add("TemporaryUserManagement");
		managementServices.add("ServiceManagement");
		managementServices.add("SubscriptionManagement");
		managementServices.add("ResourceManagement");
		managementServices.add("NodeManagement");
		managementServices.add("ServiceAccessRightManagement");
		managementServices.add("ServiceAccessLimitManagement");
		managementServices.add("ServiceMonitor");
		managementServices.add("OverUseMonitoring");
		converter.addTransformerConversion(new ByteArrayToInputStreamTransformer());
		converter.addTransformerConversion(new ByteArrayToBlobTransformer());
		converter.addTransformerConversion(new InputStreamToByteArrayTransformer());
		converter.addTransformerConversion(new LanguagePath_WIToLanguageTransformer());
		converter.addTransformerConversion(new LanguagePath_LanguageToWITransformer());
		converter.addTransformerConversion(
				Pair.class, NodeEntry.class
				, new Transformer<Pair, NodeEntry>(){
					public NodeEntry transform(Pair value)
					throws TransformationException {
						NodeEntry entry = converter.convert(value.getFirst(), NodeEntry.class);
						entry.setOwnerUserOrganization(((User)value.getSecond()).getOrganization());
						return entry;
					}}
				);
		converter.addTransformerConversion(
				Blob.class, byte[].class,
				new Transformer<Blob, byte[]>(){
					public byte[] transform(Blob value) throws TransformationException {
						try{
							InputStream is = value.getBinaryStream();
							try{
								return StreamUtil.readAsBytes(is);
							} catch (IOException e) {
								throw new TransformationException(e);
							} finally{
								try {
									is.close();
								} catch (IOException e) {
									throw new TransformationException(e);
								}
							}
						} catch (SQLException e) {
							throw new TransformationException(e);
						}
					}
				});
		converter.addConcreteClassAlias(CountryName.class, UserDefinedCountryName.class);
		addBeanTransformerWithAlias(
				converter, Service.class, ServiceEntry.class
				, Pair.create("createdDateTime", "registeredDate")
				, Pair.create("updatedDateTime", "updatedDate")
				);
		addBeanTransformerWithAlias(
				converter, User.class, UserEntry.class
				, Pair.create("createdDateTime", "registeredDate")
				, Pair.create("updatedDateTime", "updatedDate")
				);
		addBeanTransformerWithAlias(
				converter, Node.class, NodeEntry.class
				, Pair.create("createdDateTime", "registeredDate")
				, Pair.create("updatedDateTime", "updatedDate")
				);
		addBeanTransformerWithAlias(
				converter, Resource.class, ResourceEntry.class
				, Pair.create("createdDateTime", "registeredDate")
				, Pair.create("updatedDateTime", "updatedDate")
		);
		addBeanTransformerWithAlias(
				converter, Invocation.class, jp.go.nict.langrid.service_1_2.foundation.servicemanagement.Invocation.class
				, Pair.create("serviceTypeId", "serviceType"));
	}
}
