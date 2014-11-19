package jp.go.nict.langrid.management.logic;

public class ServiceLogicException extends Exception{
	public ServiceLogicException(String serviceGridId, String serviceId, String message){
		super(message);
		this.serviceGridId = serviceGridId;
		this.serviceId = serviceId;
	}

	public ServiceLogicException(String serviceGridId, String serviceId, Throwable throwable){
		super(throwable);
		this.serviceGridId = serviceGridId;
		this.serviceId = serviceId;
	}

	public String getServiceGridId() {
		return serviceGridId;
	}
	public void setServiceGridId(String serviceGridId) {
		this.serviceGridId = serviceGridId;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	private String serviceGridId;
	private String serviceId;

	private static final long serialVersionUID = -749757498202218873L;
}
