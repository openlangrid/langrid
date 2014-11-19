package jp.go.nict.langrid.servicecontainer.handler.annotation;

import jp.go.nict.langrid.servicecontainer.handler.loader.DefaultServiceFactoryLoader;
import jp.go.nict.langrid.servicecontainer.handler.loader.ServiceFactoryLoader;

public class ServicesUtil {
	public static ServiceFactoryLoader[] getServiceFactoryLoaders(Class<?> clazz) {
		DefaultServiceFactoryLoader l = new DefaultServiceFactoryLoader();
		boolean added = false;
		Services ss = clazz.getAnnotation(Services.class);
		if(ss != null){
			for(Service s : ss.value()){
				l.put(s.name(), s.impl(), s.intf());
				added = true;
			}
		}
		Service s = clazz.getAnnotation(Service.class);
		if(s != null){
			l.put(s.name(), s.impl(), s.intf());
			added = true;
		}
		if(added){
			return new ServiceFactoryLoader[]{l};
		} else{
			return new ServiceFactoryLoader[]{};
		}
	}
}
