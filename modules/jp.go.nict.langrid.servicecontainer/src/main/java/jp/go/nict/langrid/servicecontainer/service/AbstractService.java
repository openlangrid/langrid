package jp.go.nict.langrid.servicecontainer.service;

import java.io.File;

import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.servicecontainer.handler.RIProcessor;

public class AbstractService {
	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * 
	 */
	public ComponentServiceFactory getComponentServiceFactory(){
		return componentServiceFactory ;
	}

	/**
	 * 
	 */
	public void setComponentServiceFactory(ComponentServiceFactory value){
		this.componentServiceFactory = value ;
	}

	protected ServiceContext getServiceContext(){
		return RIProcessor.getCurrentServiceContext();
	}

	/**
	 * Get file at path. Path is assumed to relative path to service definition file.
	 * @param path path
	 * @return file
	 */
	protected File getFile(String path){
		ServiceContext sc = RIProcessor.getCurrentServiceContext();
		String base = sc.getInitParameter("servicesPath");
		if(base == null) base = "WEB-INF/services";
		return new File(sc.getRealPath(base + "/" + path));
	}

	private String serviceName;
	private ComponentServiceFactory componentServiceFactory
			= new NullComponentServiceFactory();
}
