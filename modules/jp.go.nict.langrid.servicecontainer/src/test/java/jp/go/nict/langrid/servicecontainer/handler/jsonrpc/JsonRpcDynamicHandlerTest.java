package jp.go.nict.langrid.servicecontainer.handler.jsonrpc;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.junit.Assert;
import org.junit.Test;

import jp.go.nict.langrid.commons.beanutils.Converter;

public class JsonRpcDynamicHandlerTest {
	public interface If{
		void m(Object o, String s, int i);
	}
	@Test
	public void test() throws Throwable{
		Object[] actual = {null, null, null};
		If instance = (If)Proxy.newProxyInstance(
				Thread.currentThread().getContextClassLoader(),
				new Class<?>[]{If.class},
				(Object proxy, Method method, Object[] args) ->{
					for(int i = 0; i < args.length; i++){
						actual[i] = args[i];
					}
					return null;
				});
		JsonRpcDynamicHandler.invokeMethod(
				instance,
				If.class.getMethod("m", Object.class, String.class, int.class),
				new Object[]{"", "", ""},
				new Converter());
		Assert.assertNull(actual[0]);
		Assert.assertEquals("", actual[1]);
		Assert.assertEquals(0, actual[2]);
	}
}
