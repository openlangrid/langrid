package jp.go.nict.langrid.service_1_2.foundation.resourcemanagement;

import java.io.Serializable;
import java.util.Calendar;

import jp.go.nict.langrid.service_1_2.LanguagePath;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 225 $
 */
public class ResourceEntry implements Serializable{
	/**
	 * 
	 * 
	 */
	public ResourceEntry() {
	}

	/**
	 * 
	 * 
	 */
	public ResourceEntry(String resourceId, String resourceName, String resourceType
			, String ownerUserId, String ownerUserOrganization, LanguagePath[] supportedLanguages
			, boolean authorized, boolean active, Calendar registeredDate, Calendar updatedDate)
	{
		this.resourceId = resourceId;
		this.resourceName = resourceName;
		this.resourceType = resourceType;
		this.ownerUserId = ownerUserId;
		this.ownerUserOrganization = ownerUserOrganization;
		this.supportedLanguages = supportedLanguages;
		this.authorized = authorized;
		this.active = active;
		this.registeredDate = registeredDate;
		this.updatedDate = updatedDate;
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
	public String getOwnerUserOrganization() {
		return ownerUserOrganization;
	}
	
	/**
	 * 
	 * 
	 */
	public Calendar getRegisteredDate() {
		return registeredDate;
	}
	
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
	public String getResourceName() {
		return resourceName;
	}
	
	/**
	 * 
	 * 
	 */
	public String getResourceType() {
		return resourceType;
	}
	
	/**
	 * 
	 * 
	 */
	public LanguagePath[] getSupportedLanguages() {
		return supportedLanguages;
	}
	
	/**
	 * 
	 * 
	 */
	public Calendar getUpdatedDate() {
		return updatedDate;
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
	public boolean isAuthorized() {
		return authorized;
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
	public void setAuthorized(boolean authorized) {
		this.authorized = authorized;
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
	public void setOwnerUserOrganization(String ownerUserOrganization) {
		this.ownerUserOrganization = ownerUserOrganization;
	}
	
	/**
	 * 
	 * 
	 */
	public void setRegisteredDate(Calendar registeredDate) {
		this.registeredDate = registeredDate;
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
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	
	/**
	 * 
	 * 
	 */
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	
	/**
	 * 
	 * 
	 */
	public void setSupportedLanguages(LanguagePath[] supportedLanguages) {
		this.supportedLanguages = supportedLanguages;
	}
	
	/**
	 * 
	 * 
	 */
	public void setUpdatedDate(Calendar updatedDate) {
		this.updatedDate = updatedDate;
	}

	private String ownerUserId;
	private String ownerUserOrganization;
	private String resourceId;
	private String resourceName;
	private String resourceType;
	private LanguagePath[] supportedLanguages;
	private boolean active;
	private boolean authorized;
	private Calendar registeredDate;
	private Calendar updatedDate;
	
	private static final long serialVersionUID = 7296236157295001379L;
}
