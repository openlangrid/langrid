package jp.go.nict.langrid.management.web.model;

import jp.go.nict.langrid.management.web.model.languagepath.LanguagePathModel;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class ResourceModel extends ServiceGridModel {
	/**
	 * 
	 * 
	 */
	public String getResourceId() {
		return resourceId;
	}

	/**
	 * 
	 * 
	 */
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	/**
	 * 
	 * 
	 */
	public String getResourceName() {
		return resourceName;
	}

	/**
	 * 
	 * 
	 */
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	/**
	 * 
	 * 
	 */
	public String getResourceDescription() {
		return resourceDescription;
	}

	/**
	 * 
	 * 
	 */
	public void setResourceDescription(String resourceDescription) {
		this.resourceDescription = resourceDescription;
	}

	/**
	 * 
	 * 
	 */
	public String getSpecialNoteInfo() {
		return specialNoteInfo;
	}

	/**
	 * 
	 * 
	 */
	public void setSpecialNoteInfo(String specialNoteInfo) {
		this.specialNoteInfo = specialNoteInfo;
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
	public void setCopyrightInfo(String copyrightInfo) {
		this.copyrightInfo = copyrightInfo;
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
	public void setLicenseInfo(String licenseInfo) {
		this.licenseInfo = licenseInfo;
	}

	/**
	 * 
	 * 
	 */
	public String getCpuInfo() {
		return cpuInfo;
	}

	/**
	 * 
	 * 
	 */
	public void setCpuInfo(String cpuInfo) {
		this.cpuInfo = cpuInfo;
	}

	/**
	 * 
	 * 
	 */
	public String getMemoryInfo() {
		return memoryInfo;
	}

	/**
	 * 
	 * 
	 */
	public void setMemoryInfo(String memoryInfo) {
		this.memoryInfo = memoryInfo;
	}

	/**
	 * 
	 * 
	 */
	public ResourceTypeModel getResourceType() {
		return resourceType;
	}

	/**
	 * 
	 * 
	 */
	public void setResourceType(ResourceTypeModel resourceType) {
		this.resourceType = resourceType;
	}

	/**
	 * 
	 * 
	 */
	public String getOsInfo() {
		return osInfo;
	}

	/**
	 * 
	 * 
	 */
	public void setOsInfo(String osInfo) {
		this.osInfo = osInfo;
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
	public void setOwnerUserId(String ownerUserId) {
		this.ownerUserId = ownerUserId;
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
	public boolean isActive() {
		return active;
	}

	/**
	 * 
	 * 
	 */
	public void setApproved(boolean authorized) {
		this.approved = authorized;
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
	public LanguagePathModel getSupportedLanguagePathModel() {
		return supportedLanguagePathModel;
	}

	/**
	 * 
	 * 
	 */
	public void setSupportedLanguagePathModel(
		LanguagePathModel supportedLanguagePathModel) {
		this.supportedLanguagePathModel = supportedLanguagePathModel;
	}

	private String resourceId;
	private String resourceName;

	private ResourceTypeModel resourceType;
	private LanguagePathModel supportedLanguagePathModel;
	
	private String resourceDescription;
	private String specialNoteInfo;
	private String copyrightInfo;
	private String licenseInfo;
	private String cpuInfo;
	private String memoryInfo;
	private String osInfo;
	private String ownerUserId;
	
	private boolean active;
	private boolean approved;

	private static final long serialVersionUID = -6497545514942787975L;
}
