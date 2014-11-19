package jp.go.nict.langrid.servicecontainer.handler.loader;

import java.util.Map;
import java.util.TreeMap;

import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.servicecontainer.handler.ServiceFactory;
import jp.go.nict.langrid.servicecontainer.handler.TargetServiceFactory;

public class DefaultServiceFactoryLoader implements ServiceFactoryLoader{
	public static class DefaultServiceFactory extends TargetServiceFactory{
		public DefaultServiceFactory(Class<?> clazz, Class<?>... interfaceClasses) {
			this.clazz = clazz;
			setInterfaceClasses(interfaceClasses);
		}
		@Override
		public <T> T createService(ClassLoader classLoader,
				ServiceContext context, Class<T> interfaceClass) {
			return newInstance();
		}
		@Override
		public Object getService() {
			Object s = super.getService();
			if(s == null){
				setService(newInstance());
			}
			return super.getService();
		}
		@SuppressWarnings("unchecked")
		private <T> T newInstance(){
			try {
				return (T)clazz.newInstance();
			} catch(InstantiationException e){
				throw new RuntimeException(e);
			} catch(IllegalAccessException e){
				throw new RuntimeException(e);
			}
		}
		private Class<?> clazz;
	}

	@Override
	public String[] listServiceNames() {
		return factories.keySet().toArray(new String[]{});
	}

	@Override
	public boolean hasServiceFactoryFor(String serviceName) {
		return factories.containsKey(serviceName);
	}

	@Override
	public ServiceFactory getServiceFactory(ClassLoader classLoader,
			ServiceContext serviceContext, String serviceName) {
		return factories.get(serviceName);
	}

	public void put(String serviceName, Class<?> serviceClass, Class<?>[] interfaces){
		factories.put(serviceName, new DefaultServiceFactory(serviceClass, interfaces));
	}

	private Map<String, TargetServiceFactory> factories = new TreeMap<String, TargetServiceFactory>();
}