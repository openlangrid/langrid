package jp.go.nict.langrid.servicecontainer.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import jp.go.nict.langrid.commons.ws.ServiceContext;

public class ServiceContextUtil {
	public static void setCurrentServiceContextForAbstractService(ServiceContext context){
		if(setCurrentContextMethod == null) return;
		try {
			setCurrentContextMethod.invoke(null, context);
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		}
	}

	private static Method setCurrentContextMethod;
	private static Class<?> abstractServiceClass;
	static{
		try {
			abstractServiceClass = Class.forName("jp.go.nict.langrid.wrapper.ws_1_2.AbstractService");
			setCurrentContextMethod = abstractServiceClass.getMethod("setCurrentServiceContext", ServiceContext.class);
		} catch (ClassNotFoundException e) {
		} catch (NoSuchMethodException e) {
		} catch (SecurityException e) {
		}
	}
}
