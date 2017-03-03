package jp.go.nict.langrid.servicecontainer.service;

import java.util.HashMap;
import java.util.Map;

import jp.go.nict.langrid.servicecontainer.service.ComponentServiceFactory;

public class ComponentServiceFactoryImpl implements ComponentServiceFactory{
	public <T> void add(String invocationName, Class<T> interfaceClass, T service){
		services.put(invocationName, service);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getService(String invocationName, Class<T> interfaceClass) {
		return (T)services.get(invocationName);
	}

	private Map<String, Object> services = new HashMap<>();
}
