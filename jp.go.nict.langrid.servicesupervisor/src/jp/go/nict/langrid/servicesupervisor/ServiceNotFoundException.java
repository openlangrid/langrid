package jp.go.nict.langrid.servicesupervisor;

public class ServiceNotFoundException extends Exception{
	public ServiceNotFoundException(String serviceId){
		super("service \"" + serviceId + "\" is not found.");
		this.serviceId = serviceId;
	}

	public String getServiceId(){
		return serviceId;
	}

	private String serviceId;

	private static final long serialVersionUID = 5275145779028447516L;
}
