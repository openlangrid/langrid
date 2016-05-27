/*
 * This is a program for Language Grid Core Node. This combines multiple
 * language resources and provides composite language services.
 * Copyright (C) NICT Language Grid Project.
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
package jp.go.nict.langrid.servicecontainer.handler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import jp.go.nict.langrid.commons.ws.LangridConstants;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.servicecontainer.decorator.DecoratorChain;
import jp.go.nict.langrid.servicecontainer.decorator.Request;

public class ServiceInvocationHandler implements InvocationHandler{
	public ServiceInvocationHandler(
			ServiceContext context, String serviceId
			, DecoratorChain decoratorChain, Object service,
			StringBuilder invocationLog){
		this.context = context;
		this.serviceId = serviceId;
		this.decoratorChain = decoratorChain;
		this.service = service;
		this.invocationLog = invocationLog;
	}

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
		} catch(InvocationTargetException e){
			throw e.getTargetException();
		} finally{
			if(invocationLog != null){
				RIProcessor.getCurrentProcessorContext().addResponseHeader(
						LangridConstants.ACTOR_SERVICE_INVOCATIONLOG,
						"log",
						invocationLog.toString());
			}
			ServiceContextUtil.setCurrentServiceContextForAbstractService(null);
		}
	}

	private ServiceContext context;
	private String serviceId;
	private DecoratorChain decoratorChain;
	private Object service;
	private StringBuilder invocationLog;
}

