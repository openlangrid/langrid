package jp.go.nict.langrid.servicecontainer.handler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.servicecontainer.decorator.DecoratorChain;
import jp.go.nict.langrid.servicecontainer.decorator.Request;

public class ServiceInvocationHandler implements InvocationHandler{
	public ServiceInvocationHandler(
			ServiceContext context, String serviceId
			, DecoratorChain decoratorChain, Object service){
		this.context = context;
		this.serviceId = serviceId;
		this.decoratorChain = decoratorChain;
		this.service = service;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
	throws Throwable {
		Request r = new Request(context, context.getAuthUser(), serviceId
				, service, service.getClass(), method, args
				);
		ServiceContextUtil.setCurrentServiceContextForAbstractService(context);
		try{
			return decoratorChain.next(r);
		} finally{
			ServiceContextUtil.setCurrentServiceContextForAbstractService(null);
		}
	}

	private ServiceContext context;
	private String serviceId;
	private DecoratorChain decoratorChain;
	private Object service;
}

