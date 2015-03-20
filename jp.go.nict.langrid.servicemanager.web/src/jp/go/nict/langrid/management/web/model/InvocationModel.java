package jp.go.nict.langrid.management.web.model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class InvocationModel extends ServiceDomainModel {
	/**
	 * 
	 * 
	 */
	public String getOwnerServiceGridId() {
		return ownerServiceGridId;
	}

	/**
	 * 
	 * 
	 */
	public void setOwnerServiceGridId(String ownerServiceGridId) {
		this.ownerServiceGridId = ownerServiceGridId;
	}

	/**
	 * 
	 * 
	 */
	public String getOwnerServiceId() {
		return ownerServiceId;
	}

	/**
	 * 
	 * 
	 */
	public void setOwnerServiceId(String ownerServiceId) {
		this.ownerServiceId = ownerServiceId;
	}

	/**
	 * 
	 * 
	 */
	public String getInvocationName() {
		return invocationName;
	}

	/**
	 * 
	 * 
	 */
	public void setInvocationName(String invocationName) {
		this.invocationName = invocationName;
	}

	/**
	 * 
	 * 
	 */
	public String getServiceGridId() {
		return serviceGridId;
	}

	/**
	 * 
	 * 
	 */
	public void setServiceGridId(String serviceGridId) {
		this.serviceGridId = serviceGridId;
	}
	
	/**
	 * 
	 * 
	 */
	public String getServiceId() {
		return serviceId;
	}

	/**
	 * 
	 * 
	 */
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	/**
	 * 
	 * 
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * 
	 * 
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * 
	 * 
	 */
	public String getServiceTypeId() {
		return serviceTypeId;
	}

	/**
	 * 
	 * 
	 */
	public void setServiceTypeId(String serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}

	private String ownerServiceGridId;
	private String ownerServiceId;
	private String invocationName;
	private String serviceGridId;
	private String serviceId;
	private String serviceName;
	private String serviceTypeId;
	
	private static final long serialVersionUID = 3987170166684421271L;
}
