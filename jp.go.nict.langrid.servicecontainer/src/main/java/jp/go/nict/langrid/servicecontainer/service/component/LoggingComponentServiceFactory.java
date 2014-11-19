package jp.go.nict.langrid.servicecontainer.service.component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.logging.Logger;

import jp.go.nict.langrid.commons.lang.ExceptionUtil;
import jp.go.nict.langrid.servicecontainer.service.ComponentServiceFactory;

public class LoggingComponentServiceFactory implements ComponentServiceFactory {
	public LoggingComponentServiceFactory(ComponentServiceFactory original){
		this.original = original;
	}

	@Override
	public <T> T getService(final String invocationName, final Class<T> interfaceClass) {
		final T service = original.getService(invocationName, interfaceClass);
		if(service == null) return service;
		return interfaceClass.cast(Proxy.newProxyInstance(
				Thread.currentThread().getContextClassLoader()
				, new Class<?>[]{interfaceClass}
				, new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args)
							throws Throwable {
						logger.info(String.format("service %s:%s invoked. %s.%s(%s)"
								, invocationName, interfaceClass.getName()
								, service.getClass().getName(), method.getName()
								, Arrays.toString(args)
								));
						try{
							Object ret = method.invoke(service, args);
							if(ret != null){
								logger.info(String.format("service %s:%s returned. %s.%s -> %s"
										, invocationName, interfaceClass.getName()
										, service.getClass().getName(), method.getName()
										, ret.getClass().isArray() ? (ret.getClass().getComponentType().isPrimitive() ? ret : (
												((Object[])ret).length + " elements:" + Arrays.toString((Object[])ret))
												): ret
										));
							} else{
								logger.info(String.format("service %s:%s returned. %s.%s -> null(or void)"
										, invocationName, interfaceClass.getName()
										, service.getClass().getName(), method.getName()
										));
							}
							return ret;
						} catch(InvocationTargetException e){
							logger.info(String.format("service %s:%s throwed exception. %s.%s -> %s"
									, invocationName, interfaceClass.getName()
									, service.getClass().getName(), method.getName()
									, ExceptionUtil.getMessageWithStackTrace(e.getCause())
									));
							throw e;
						} catch(Throwable t){
							logger.info(String.format("service %s:%s throwed exception. %s.%s -> %s"
									, invocationName, interfaceClass.getName()
									, service.getClass().getName(), method.getName()
									, ExceptionUtil.getMessageWithStackTrace(t)
									));
							throw t;
						}
					}
				}));
	}

	private ComponentServiceFactory original;
	private static Logger logger = Logger.getLogger(LoggingComponentServiceFactory.class.getName());
}
