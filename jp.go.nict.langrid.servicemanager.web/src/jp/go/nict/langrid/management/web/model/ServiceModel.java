package jp.go.nict.langrid.management.web.model;

import java.util.LinkedHashSet;
import java.util.Set;

import jp.go.nict.langrid.dao.entity.InstanceType;
import jp.go.nict.langrid.management.web.model.languagepath.LanguagePathModel;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
public class ServiceModel extends ServiceGridModel {
	/**
	 * 
	 * 
	 */
	public Set<String> getAllowedAppProvision() {
		return this.allowedAppProvision;
	}

	/**	
	 * 
	 * 
	 */
	public Set<String> getAllowedUsage() {
		return this.allowedUsage;
	}

	/**
	 * 
	 * 
	 */
	public String getContainerType() {
		return containerType;
	}

	/**
	 * 
	 * 
	 */
	public String getCopyrightInfo() {
		return copyrightInfo;
	}

	/**
	 * 
	 * 
	 */
	public String getHowToGetMembershipInfo() {
		return howToGetMembershipInfo;
	}

	/**
	 * 
	 * 
	 */
	public byte[] getInstance() {
		return instance;
	}

	/**
	 * 
	 * 
	 */
	public int getInstanceSize() {
		return instanceSize;
	}

	/**
	 * 
	 * 
	 */
	public InstanceType getInstanceType() {
		return instanceType;
	}

	/**
	 * 
	 * 
	 */
	public String getLicenseInfo() {
		return licenseInfo;
	}

	/**
	 * 
	 * 
	 */
	public String getOwnerUserId() {
		return ownerUserId;
	}

	/**
	 * 
	 * 
	 */
	public String getServiceDescription() {
		return serviceDescription;
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
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * 
	 * 
	 */
	public ServiceTypeModel getServiceType() {
		return serviceType;
	}

	/**
	 * 
	 * 
	 */
	public LanguagePathModel getSupportedLanguagePathModel() {
		return supportedLanguagePathModel;
	}

	/**
	 * 
	 * 
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * 
	 * 
	 */
	public boolean isApproved() {
		return approved;
	}

	/**
	 * 
	 * 
	 */
	public boolean isFederatedUseAllowed() {
		return federatedUseAllowed;
	}

	/**
	 * 
	 * 
	 */
	public boolean isMembersOnly() {
		return membersOnly;
	}

	/**
	 * 
	 * 
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * 
	 * 
	 */
	public void setAllowedAppProvision(Set<String> allowedAppProvision) {
		Set<String> copied = new LinkedHashSet<String>();
		for(String s : allowedAppProvision) {
			copied.add(s);
		}
		this.allowedAppProvision = copied;
	}

	/**
	 * 
	 * 
	 */
	public void setAllowedUsage(Set<String> allowedUsage) {
		Set<String> copied = new LinkedHashSet<String>();
		for(String s : allowedUsage) {
			copied.add(s);
		}
		this.allowedUsage = copied;
	}

	/**
	 * 
	 * 
	 */
	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	/**
	 * 
	 * 
	 */
	public void setContainerType(String containerType) {
		this.containerType = containerType;
	}

	/**
	 * 
	 * 
	 */
	public void setCopyrightInfo(String copyrightInfo) {
		this.copyrightInfo = copyrightInfo;
	}

	/**
	 * 
	 * 
	 */
	public void setFederatedUseAllowed(boolean federatedUseAllowed) {
		this.federatedUseAllowed = federatedUseAllowed;
	}

	/**
	 * 
	 * 
	 */
	public void setHowToGetMembershipInfo(String howToGetMembershipInfo) {
		this.howToGetMembershipInfo = howToGetMembershipInfo;
	}

	/**
	 * 
	 * 
	 */
	public void setInstance(byte[] instance) {
		this.instance = instance;
	}

	/**
	 * 
	 * 
	 */
	public void setInstanceSize(int instanceSize) {
		this.instanceSize = instanceSize;
	}

	/**
	 * 
	 * 
	 */
	public void setInstanceType(InstanceType instanceType) {
		this.instanceType = instanceType;
	}

	/**
	 * 
	 * 
	 */
	public void setLicenseInfo(String licenseInfo) {
		this.licenseInfo = licenseInfo;
	}

	/**
	 * 
	 * 
	 */
	public void setMembersOnly(boolean membersOnly) {
		this.membersOnly = membersOnly;
	}

	/**
	 * 
	 * 
	 */
	public void setOwnerUserId(String ownerUserId) {
		this.ownerUserId = ownerUserId;
	}

	/**
	 * 
	 * 
	 */
	public void setServiceDescription(String serviceDescription) {
		this.serviceDescription = serviceDescription;
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
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * 
	 * 
	 */
	public void setServiceType(ServiceTypeModel serviceType) {
		this.serviceType = serviceType;
	}

	/**
	 * 
	 * 
	 */
	public void setSupportedLanguagePathModel(LanguagePathModel supportedLanguagePathModel) {
		this.supportedLanguagePathModel = supportedLanguagePathModel;
	}

	private String serviceId;
	private String serviceName;
	private ServiceTypeModel serviceType;
	private String serviceDescription;
	private LanguagePathModel supportedLanguagePathModel;
	private String licenseInfo;
	private String copyrightInfo;
	private String ownerUserId;
	private byte[] instance;
	private int instanceSize;
	private InstanceType instanceType;
	private boolean membersOnly;
	private String howToGetMembershipInfo;
	private Set<String> allowedAppProvision;
	private Set<String> allowedUsage;
	private boolean federatedUseAllowed;
	private boolean approved;
	private boolean active;
	private String containerType;

}
