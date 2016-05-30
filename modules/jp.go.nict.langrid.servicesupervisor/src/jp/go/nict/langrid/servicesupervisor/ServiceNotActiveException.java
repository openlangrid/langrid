package jp.go.nict.langrid.servicesupervisor;

public class ServiceNotActiveException extends Exception{
	public ServiceNotActiveException(String serviceId){
		super("service \"" + serviceId + "\" is not active.");
		this.serviceId = serviceId;
	}

	public String getServiceId(){
		return serviceId;
	}

	private String serviceId;

	private static final long serialVersionUID = 8869469577982160389L;
}
