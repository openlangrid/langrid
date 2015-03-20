package jp.go.nict.langrid.webapps.mock;

import java.net.MalformedURLException;
import java.net.URL;

import jp.go.nict.langrid.client.ClientFactory;
import jp.go.nict.langrid.client.ws_1_2.impl.axis.LangridAxisClientFactory;
import jp.go.nict.langrid.commons.lang.reflect.GenericsUtil;

public abstract class AbstractClientTest<T> {
	public static ClientFactory clientFactory(){
		return LangridAxisClientFactory.getInstance();
	}
	public static URL url(String serviceName) throws MalformedURLException{
		return new URL(
				"http://localhost:8080/jp.go.nict.langrid.webapps.mock/services/" +
				serviceName);
	}

	public T client(String serviceName) throws MalformedURLException{
		Class<?>[] types = GenericsUtil.getTypeArgumentClasses(
				this.getClass(), AbstractClientTest.class);
		return (T)clientFactory().create(types[0], url(serviceName));
	}
}
