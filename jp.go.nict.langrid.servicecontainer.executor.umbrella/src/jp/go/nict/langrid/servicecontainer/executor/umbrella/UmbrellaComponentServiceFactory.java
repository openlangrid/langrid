/*
 * This is a program for Language Grid Core Node. This combines multiple
 * language resources and provides composite language services.
 * Copyright (C) 2005-2012 NICT Language Grid Project.
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
package jp.go.nict.langrid.servicecontainer.executor.umbrella;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.soap.MimeHeaders;

import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.commons.ws.LangridConstants;
import jp.go.nict.langrid.commons.ws.Protocols;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.cosee.AppAuthEndpointRewriter;
import jp.go.nict.langrid.cosee.DynamicBindingRewriter;
import jp.go.nict.langrid.cosee.Endpoint;
import jp.go.nict.langrid.cosee.EndpointRewriter;
import jp.go.nict.langrid.cosee.UserAuthEndpointRewriter;
import jp.go.nict.langrid.servicecontainer.executor.umbrella.dao.DaoException;
import jp.go.nict.langrid.servicecontainer.executor.umbrella.dao.DaoFactory;
import jp.go.nict.langrid.servicecontainer.executor.umbrella.dao.EndpointAddressProtocolDao;
import jp.go.nict.langrid.servicecontainer.executor.umbrella.dao.ServiceProtocolDao;
import jp.go.nict.langrid.servicecontainer.handler.RIProcessor;
import jp.go.nict.langrid.servicecontainer.service.ComponentServiceFactory;
import jp.go.nict.langrid.servicecontainer.service.component.AbstractComponentServiceFactory;

/**
 * 
 * 
 * @author Shingo Furukido
 * @author Takao Nakaguchi
 */
public class UmbrellaComponentServiceFactory
extends AbstractComponentServiceFactory
implements ComponentServiceFactory{
	@Override
	public void setCacheEnabled(boolean cacheEnabled) {
		this.cacheEnabled = cacheEnabled;
		for(AbstractComponentServiceFactory f : factories.values()){
			f.setCacheEnabled(cacheEnabled);
		}
		if(defaultServiceFactory != null) defaultServiceFactory.setCacheEnabled(cacheEnabled);
	}

	@Override
	public void setCacheCapacity(int cacheCapacity) {
		this.cacheCapacity = cacheCapacity;
		for(AbstractComponentServiceFactory f : factories.values()){
			f.setCacheCapacity(cacheCapacity);
		}
		if(defaultServiceFactory != null) defaultServiceFactory.setCacheCapacity(cacheCapacity);
	}

	@Override
	public void setCacheTtlSec(int cacheTtlSec) {
		this.cacheTtlSec = cacheTtlSec;
		for(AbstractComponentServiceFactory f : factories.values()){
			f.setCacheTtlSec(cacheTtlSec);
		}
		if(defaultServiceFactory != null) defaultServiceFactory.setCacheTtlSec(cacheTtlSec);
	}

	@Override
	public <T> T getService(final String invocationName, final Class<T> interfaceClass) {
		return interfaceClass.cast(Proxy.newProxyInstance(
				Thread.currentThread().getContextClassLoader(),
				new Class<?>[]{interfaceClass},
				new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						Pair<Long, Endpoint> value = getInvocationIdAndEndpoint(
								invocationName, method, args);
						if("AbstractService".equals(value.getSecond().getServiceId())){
							throw new RuntimeException(invocationName + " must be binded.");
						}
						return method.invoke(
								getFactory(value.getSecond().getProtocol()).getService(
										invocationName, value.getFirst(), value.getSecond(), interfaceClass)
								, args);
					}
				}));
	}

	@Override
	public <T> T getService(final String invocationName, long invocationId, final Endpoint endpoint
			, final Class<T> interfaceClass) {
		return interfaceClass.cast(Proxy.newProxyInstance(
				Thread.currentThread().getContextClassLoader(),
				new Class<?>[]{interfaceClass},
				new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						Pair<Long, Endpoint> value = getInvocationIdAndEndpoint(
								invocationName, endpoint, method, args);
						if("AbstractService".equals(value.getSecond().getServiceId())){
							throw new RuntimeException(invocationName + " must be binded.");
						}
						return method.invoke(getFactory(value.getSecond().getProtocol()).getService(
								invocationName, value.getFirst(), value.getSecond(), interfaceClass),
								args);
					}
				}));
	}

	public Map<String, AbstractComponentServiceFactory> getFactories() {
		return factories;
	}

	public void setFactories(Map<String, AbstractComponentServiceFactory> factories) {
		this.factories = factories;
		for(AbstractComponentServiceFactory f : factories.values()){
			f.setCacheCapacity(cacheCapacity);
			f.setCacheTtlSec(cacheTtlSec);
			f.setCacheEnabled(cacheEnabled);
		}
	}

	AbstractComponentServiceFactory getFactory(String protocol){
		if(defaultServiceFactory == null){
			initDefaultFactories();
		}
		AbstractComponentServiceFactory f = factories.get(protocol);
		if(f == null){
			ServiceContext sc = RIProcessor.getCurrentServiceContext();
			if(sc != null){
				MimeHeaders mimeHeaders = sc.getRequestMimeHeaders();
				if(mimeHeaders != null){
					String[] p = mimeHeaders.getHeader(LangridConstants.HTTPHEADER_PROTOCOL);
					if(p != null && p.length > 0){
						f = factories.get(p[0]);
					}
				}
			}
		}
		if(f == null){
			f = defaultServiceFactory;
		}
		return f;
	}

	Pair<Long, Endpoint> getInvocationIdAndEndpoint(String invocationName, Method method, Object[] args)
	throws DaoException{
		long iid = RIProcessor.newInvocationId();
		Endpoint endpoint = RIProcessor.rewriteEndpoint(iid, invocationName
				, getEndpointRewriters(RIProcessor.getCurrentServiceContext()),
				method, args);
		return Pair.create(iid, endpoint);
	}

	Pair<Long, Endpoint> getInvocationIdAndEndpoint(String invocationName, Endpoint original,
			Method method, Object[] args)
	throws DaoException{
		long iid = RIProcessor.newInvocationId();
		Endpoint endpoint = RIProcessor.rewriteEndpoint(iid, invocationName
				, getEndpointRewriters(RIProcessor.getCurrentServiceContext()), original,
				method, args);
		return Pair.create(iid, endpoint);
	}

	private static DaoFactory daoFactory;
	private static ServiceProtocolDao spDao;
	private static EndpointAddressProtocolDao inpDao;

	private static synchronized DaoFactory getDaoFactory(ServiceContext context)
	throws DaoException{
		if(daoFactory == null){
			String path = context.getRealPath("WEB-INF/db");
			daoFactory = DaoFactory.createInstance(path);
		}
		return daoFactory;
	}

	private static synchronized ServiceProtocolDao getServiceProtocolDao(ServiceContext context)
	throws DaoException{
		if(spDao == null){
			spDao = getDaoFactory(context).createServiceProtocolDao();
		}
		return spDao ;
	}

	private static synchronized EndpointAddressProtocolDao getEndpointAddressProtocolDao(ServiceContext context)
	throws DaoException{
		if(inpDao == null){
			inpDao = getDaoFactory(context).createEndpointAddressProtocolDao();
		}
		return inpDao;
	}

	private synchronized EndpointRewriter[] getEndpointRewriters(ServiceContext context)
	throws DaoException{
		if(rewriters == null){
			rewriters = new EndpointRewriter[]{
					new DynamicBindingRewriter()
					, new ProtocolResolveEndpointRewriter(
							getServiceProtocolDao(context), getEndpointAddressProtocolDao(context))
					, new AppAuthEndpointRewriter()
					, new UserAuthEndpointRewriter()
			};
		}
		RIProcessor.initEndpointRewriters(rewriters);
		return rewriters;
	}

	private synchronized void initDefaultFactories(){
		putFactoryIfAvailable(
				Protocols.SOAP_RPCENCODED,
				"jp.go.nict.langrid.servicecontainer.executor.axis.AxisComponentServiceFactory");
		putFactoryIfAvailable(
				Protocols.PROTOBUF_RPC,
				"jp.go.nict.langrid.servicecontainer.executor.protobufrpc.PbComponentServiceFactory");
		putFactoryIfAvailable(
				Protocols.JSON_RPC,
				"jp.go.nict.langrid.servicecontainer.executor.jsonrpc.JsonRpcComponentServiceFactory");
		putFactoryIfAvailable(
				"JAVA_WITH_FE_CALL",
				"jp.go.nict.langrid.servicecontainer.executor.java.JavaComponentServiceFactory");
		for(AbstractComponentServiceFactory f : factories.values()){
			f.setCacheCapacity(cacheCapacity);
			f.setCacheTtlSec(cacheTtlSec);
			f.setCacheEnabled(cacheEnabled);
		}
		defaultServiceFactory = factories.get(Protocols.SOAP_RPCENCODED);
		defaultServiceFactory.setCacheCapacity(cacheCapacity);
		defaultServiceFactory.setCacheTtlSec(cacheTtlSec);
		defaultServiceFactory.setCacheEnabled(cacheEnabled);
	}

	private void putFactoryIfAvailable(String protocol, String className){
		try{
			factories.put(
					protocol,
					(AbstractComponentServiceFactory)Class.forName(className).newInstance());
		} catch(ClassNotFoundException e){
			logger.warning("failed to find factory class: " + className);
		} catch(IllegalAccessException e){
			logger.log(Level.WARNING, "failed to create factory: " + className, e);
		} catch(InstantiationException e){
			logger.log(Level.WARNING, "failed to create factory: " + className, e);
		} catch(Throwable t){
			logger.log(Level.WARNING, "failed to create factory: " + className, t);
		}
	}

	private AbstractComponentServiceFactory defaultServiceFactory;
	private EndpointRewriter[] rewriters;
	private Map<String, AbstractComponentServiceFactory> factories
			= new HashMap<String, AbstractComponentServiceFactory>();
	private boolean cacheEnabled;
	private int cacheTtlSec;
	private int cacheCapacity;

	private static Logger logger = Logger.getLogger(UmbrellaComponentServiceFactory.class.getName());
}
