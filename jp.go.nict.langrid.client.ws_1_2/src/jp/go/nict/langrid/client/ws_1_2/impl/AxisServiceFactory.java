package jp.go.nict.langrid.client.ws_1_2.impl;

import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.Map;

import javax.xml.namespace.QName;

import jp.go.nict.langrid.commons.beanutils.DynamicInvocationHandler;

import org.apache.axis.client.Stub;

public class AxisServiceFactory {
	public static <T> T create(Class<T> interfaceClass
			, URL url, String basicAuthUserName, String basicAuthPassword
			, Iterable<Map.Entry<String, Object>> mimeHeaders
			, Iterable<Map.Entry<QName, Object>> messageHeaders
			){
		Stub stub = AxisStubUtil.createStub(interfaceClass);

		AxisStubUtil.setUrl(stub,  url);
		AxisStubUtil.setUserName(stub, basicAuthUserName);
		AxisStubUtil.setPassword(stub, basicAuthPassword);
		AxisStubUtil.setMimeHeaders(stub, mimeHeaders);
		AxisStubUtil.setSoapHeaders(stub, messageHeaders);

		return interfaceClass.cast(Proxy.newProxyInstance(
				Thread.currentThread().getContextClassLoader()
				, new Class<?>[]{interfaceClass}
				, new DynamicInvocationHandler<Stub>(stub, AxisStubUtil.getConverter())
				));
	}
}
