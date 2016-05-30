package jp.go.nict.langrid.service_1_2.foundation.resourcemanagement;

import java.io.Serializable;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 225 $
 */
public class ResourceProfile implements Serializable{
	/**
	 * 
	 * 
	 */
	public ResourceProfile() {
	}
	/**
	 * 
	 * 
	 */
	public ResourceProfile(String resourceName, String resourceDescription
			, String copyrightInfo, String licenseInfo, String cpuInfo
			, String memoryInfo, String specialNoteInfo, String howToGetMembershipInfo)
	{
		this.resourceName = resourceName;
		this.resourceDescription = resourceDescription;
		this.copyrightInfo = copyrightInfo;
		this.licenseInfo = licenseInfo;
		this.cpuInfo = cpuInfo;
		this.memoryInfo = memoryInfo;
		this.specialNoteInfo = specialNoteInfo;
		this.howToGetMembershipInfo = howToGetMembershipInfo;
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
	public String getCpuInfo() {
		return cpuInfo;
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
	public String getLicenseInfo() {
		return licenseInfo;
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
	public String getResourceDescription() {
		return resourceDescription;
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
	public String getSpecialNoteInfo() {
		return specialNoteInfo;
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
	public void setCopyrightInfo(String copyrightInfo) {
		this.copyrightInfo = copyrightInfo;
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
	public void setHowToGetMembershipInfo(String howToGetMembershipInfo) {
		this.howToGetMembershipInfo = howToGetMembershipInfo;
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
	public void setMemoryInfo(String memoryInfo) {
		this.memoryInfo = memoryInfo;
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
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	
	/**
	 * 
	 * 
	 */
	public void setSpecialNoteInfo(String specialNoteInfo) {
		this.specialNoteInfo = specialNoteInfo;
	}

	private String copyrightInfo;

	private String cpuInfo;
	private String howToGetMembershipInfo;
	private String licenseInfo;
	private boolean membersOnly;
	private String memoryInfo;
	private String resourceDescription;
	private String resourceName;
	private String specialNoteInfo;
	private static final long serialVersionUID = 8495727311708154418L;
}
