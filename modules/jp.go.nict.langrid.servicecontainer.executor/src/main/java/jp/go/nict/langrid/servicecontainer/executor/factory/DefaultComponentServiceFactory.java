package jp.go.nict.langrid.servicecontainer.executor.factory;

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
import jp.go.nict.langrid.servicecontainer.handler.RIProcessor;
import jp.go.nict.langrid.servicecontainer.service.ComponentServiceFactory;
import jp.go.nict.langrid.servicecontainer.service.component.AbstractComponentServiceFactory;

public class DefaultComponentServiceFactory
extends AbstractComponentServiceFactory
implements ComponentServiceFactory{
	@Override
	public <T> T getService(String invocationName, Class<T> interfaceClass) {
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
	public <T> T getService(String invocationName, long invocationId, Endpoint endpoint
			, Class<T> interfaceClass) {
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
						return method.invoke(
								getFactory(value.getSecond().getProtocol()).getService(
										invocationName, value.getFirst(), value.getSecond(), interfaceClass)
								, args);
					}
				}));
	}

	public Map<String, AbstractComponentServiceFactory> getFactories() {
		return factories;
	}

	public void setFactories(Map<String, AbstractComponentServiceFactory> factories) {
		this.factories = factories;
	}

	private AbstractComponentServiceFactory getFactory(String protocol){
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

	private Pair<Long, Endpoint> getInvocationIdAndEndpoint(
			String invocationName, Method method, Object[] args){
		long iid = RIProcessor.newInvocationId();
		Endpoint endpoint = RIProcessor.rewriteEndpoint(iid, invocationName
				, getEndpointRewriters(RIProcessor.getCurrentServiceContext()),
				method, args);
		return Pair.create(iid, endpoint);
	}

	private Pair<Long, Endpoint> getInvocationIdAndEndpoint(
			String invocationName, Endpoint original
			, Method method, Object[] args){
		long iid = RIProcessor.newInvocationId();
		Endpoint endpoint = RIProcessor.rewriteEndpoint(iid, invocationName
				, getEndpointRewriters(RIProcessor.getCurrentServiceContext()), original,
				method, args);
		return Pair.create(iid, endpoint);
	}

	private synchronized EndpointRewriter[] getEndpointRewriters(ServiceContext context){
		if(rewriters == null){
			rewriters = new EndpointRewriter[]{
					new DynamicBindingRewriter()
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
				Protocols.SOAP_RPCENCODED,
				"jp.go.nict.langrid.servicecontainer.executor.soap.SoapComponentServiceFactory");
		putFactoryIfAvailable(
				Protocols.PROTOBUF_RPC,
				"jp.go.nict.langrid.servicecontainer.executor.protobufrpc.PbComponentServiceFactory");
		putFactoryIfAvailable(
				Protocols.JSON_RPC,
				"jp.go.nict.langrid.servicecontainer.executor.jsonrpc.JsonRpcComponentServiceFactory");
		putFactoryIfAvailable(
				"JAVA_WITH_FE_CALL",
				"jp.go.nict.langrid.servicecontainer.executor.java.JavaComponentServiceFactory");
		defaultServiceFactory = factories.get(Protocols.SOAP_RPCENCODED);
	}

	private void putFactoryIfAvailable(String protocol, String className){
		try{
			if(factories.containsKey(protocol)) return;
			factories.put(
					protocol,
					(AbstractComponentServiceFactory)Class.forName(className).newInstance());
		} catch(ClassNotFoundException e){
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

	private static Logger logger = Logger.getLogger(DefaultComponentServiceFactory.class.getName());
}
