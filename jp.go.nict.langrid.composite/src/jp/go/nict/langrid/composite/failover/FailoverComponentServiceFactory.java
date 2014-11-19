/*
 * This is a program for Language Grid Core Node. This combines multiple
 * language resources and provides composite language services.
 * Copyright (C) 2011 NICT Language Grid Project.
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
package jp.go.nict.langrid.composite.failover;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import jp.go.nict.langrid.servicecontainer.service.ComponentServiceFactory;

public class FailoverComponentServiceFactory implements ComponentServiceFactory{
	class FailoverInvocationHandler<T> implements InvocationHandler{
		public FailoverInvocationHandler(
				String invocationName, Class<T> interfaceClass){
			this.invocationName = invocationName;
			this.interfaceClass = interfaceClass;
			findFirstService();
		}

		private T getCurrentService(){
			return currentService;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
		throws Throwable {
			Throwable firstException = null;
			int startIndex = currentIndex;
			while(true){
				try{
					return method.invoke(currentService, args);
				} catch(InvocationTargetException e){
					if(firstException == null){
						firstException = e.getCause();
					}
				}
				boolean found = findNextService(startIndex);
				if(!found) break;
			}
			if(firstException != null){
				throw firstException;
			} else{
				throw new RuntimeException();
			}
		}

		private void findFirstService(){
			currentService = orig.getService(invocationName, interfaceClass);
		}

		private boolean findNextService(int until){
			while(true){
				currentIndex = (currentIndex + 1) % 10;
				if(currentIndex == until) break;
				T s = orig.getService(
						invocationName + ((currentIndex != 0) ? "Alt" + currentIndex : "")
						, interfaceClass);
				if(s != null){
					currentService = s;
					return true;
				}
			}
			return false;
		}

		private String invocationName;
		private Class<T> interfaceClass;
		private int currentIndex = 0;
		private T currentService;
	}

	public FailoverComponentServiceFactory(){
	}

	public FailoverComponentServiceFactory(ComponentServiceFactory orig){
		setOriginalFactory(orig);
	}

	public void setOriginalFactory(ComponentServiceFactory orig){
		this.orig = orig;
	}

	@Override
	public <T> T getService(String invocationName, Class<T> interfaceClass) {
		FailoverInvocationHandler<T> handler = new FailoverInvocationHandler<T>(
				invocationName, interfaceClass);
		if(handler.getCurrentService() == null) return null;
		return interfaceClass.cast(Proxy.newProxyInstance(
				Thread.currentThread().getContextClassLoader()
				, new Class<?>[]{interfaceClass}
				, handler));
	}

	private ComponentServiceFactory orig;
}
