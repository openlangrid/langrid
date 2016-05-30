package jp.go.nict.langrid.servicecontainer.service.composite;

public class ServiceLoadingFailedException extends Exception{
	public ServiceLoadingFailedException(String serviceId, String msg) {
		super(msg);
		this.serviceId = serviceId;
	}

	public ServiceLoadingFailedException(Throwable t) {
		super(t);
	}

	public ServiceLoadingFailedException(String msg, Throwable t) {
		super(msg, t);
	}

	public String getServiceId() {
		return serviceId;
	}

	private String serviceId;
	private static final long serialVersionUID = -557559177367257652L;
}
