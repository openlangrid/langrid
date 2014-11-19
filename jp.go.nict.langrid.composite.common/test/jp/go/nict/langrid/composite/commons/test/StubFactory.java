/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
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
package jp.go.nict.langrid.composite.commons.test;

import java.lang.reflect.Proxy;
import java.net.MalformedURLException;
import java.net.URL;

import jp.go.nict.langrid.client.axis.ProxySelectingAxisSocketFactory;
import jp.go.nict.langrid.client.axis.ProxySelectingSecureAxisSocketFactory;
import jp.go.nict.langrid.client.ws_1_2.impl.AxisStubUtil;
import jp.go.nict.langrid.commons.beanutils.DynamicInvocationHandler;
import jp.go.nict.langrid.commons.net.proxy.pac.PacUtil;

import org.apache.axis.AxisProperties;
import org.apache.axis.client.Stub;
import org.apache.axis.components.net.SecureSocketFactory;
import org.apache.axis.components.net.SocketFactory;

public class StubFactory {
	public static <T> T create(Class<T> interfaceClass, String serviceId){
		try{
			Stub s = AxisStubUtil.createStub(interfaceClass);
			AxisStubUtil.setUrl(s, new URL(TestContext.baseUrl + serviceId));
			AxisStubUtil.setUserName(s, TestContext.userId);
			AxisStubUtil.setPassword(s, TestContext.password);
			return interfaceClass.cast(Proxy.newProxyInstance(
					Thread.currentThread().getContextClassLoader()
					, new Class<?>[]{interfaceClass}
					, new DynamicInvocationHandler<Stub>(s, AxisStubUtil.getConverter())
					));
		} catch(MalformedURLException e){
			throw new RuntimeException(e);
		}
	}

	static{
		try{
			System.setProperty("java.net.useSystemProxies", "true");
		}catch(SecurityException e){
			e.printStackTrace();
		}
		AxisProperties.setProperty(SocketFactory.class.getName(),
				ProxySelectingAxisSocketFactory.class.getName());
		AxisProperties.setProperty(SecureSocketFactory.class.getName(),
				ProxySelectingSecureAxisSocketFactory.class.getName());
		PacUtil.setupDefaultProxySelector();
	}
}
