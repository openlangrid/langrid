package jp.go.nict.langrid.servicecontainer.handler.loader;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.servicecontainer.handler.ServiceFactory;
import jp.go.nict.langrid.servicecontainer.handler.TargetServiceFactory;

public class DefaultServiceFactoryLoader implements ServiceFactoryLoader{
	public static class DefaultServiceFactory<T> extends TargetServiceFactory{
		public DefaultServiceFactory(Class<T> clazz, Class<?>[] interfaceClasses, Consumer<? super T> initializer) {
			this.clazz = clazz;
			this.initializer = initializer;
			setInterfaceClasses(interfaceClasses);
		}
		@Override
		public <U> U createService(ClassLoader classLoader,
				ServiceContext context, Class<U> interfaceClass) {
			return newInstance();
		}
		@Override
		public synchronized Object getService() {
			Object s = super.getService();
			if(s == null){
				setService(newInstance());
			}
			return super.getService();
		}
		@SuppressWarnings("unchecked")
		private <U> U newInstance(){
			try {
				Object instance = clazz.newInstance();
				initializer.accept((T)instance);
				return (U)instance;
			} catch(InstantiationException e){
				throw new RuntimeException(e);
			} catch(IllegalAccessException e){
				throw new RuntimeException(e);
			}
		}
		private Class<?> clazz;
		private Consumer<? super T> initializer;
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

	public <T> void put(String serviceName, Class<T> serviceClass, Class<?>[] interfaces){
		factories.put(serviceName, new DefaultServiceFactory<T>(serviceClass, interfaces, DefaultServiceFactoryLoader.<T>empty()));
	}

	public <T> void put(String serviceName, Class<T> serviceClass, Class<?>[] interfaces, Consumer<? super T> initializer){
		factories.put(serviceName, new DefaultServiceFactory<T>(serviceClass, interfaces, initializer));
	}

	private Map<String, TargetServiceFactory> factories = new TreeMap<String, TargetServiceFactory>();
	@SuppressWarnings("rawtypes")
	private static final Consumer empty = new Consumer(){
		public void accept(Object value) {
	}};
	@SuppressWarnings("unchecked")
	private static final <U> Consumer<U> empty(){
		return empty;
	}
}