package jp.go.nict.langrid.management.logic;

public class ResourceLogicException extends Exception{
	public ResourceLogicException(String serviceGridId, String serviceId, String message){
		super(message);
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
}
