package jp.go.nict.langrid.management.web.model;

import java.util.List;

import jp.go.nict.langrid.dao.entity.ServiceActionSchedule;
import jp.go.nict.langrid.management.web.model.languagepath.LanguagePathModel;

public class ResourceModelForList extends ServiceGridModel {
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

	/**
	 * 
	 * 
	 */
	public ServiceActionSchedule getServiceActionSchedule() {
		return serviceActionSchedule;
	}

	/**
	 * 
	 * 
	 */
	public void setServiceActionSchedule(ServiceActionSchedule serviceActionSchedule) {
		this.serviceActionSchedule = serviceActionSchedule;
	}

	/**
	 * 
	 * 
	 */
	public String getOrganizationName() {
		return organizationName;
	}

	/**
	 * 
	 * 
	 */
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	
	/**
	 * 
	 * 
	 */
	public List<String> getServiceIdList() {
		return usedResourceIdList;
	}

	/**
	 * 
	 * 
	 */
	public void setUsedResourceIdList(List<String> serviceIdList) {
		this.usedResourceIdList = serviceIdList;
	}

	public boolean hasUsedResourceId() {
		if ( usedResourceIdList == null ) {
			return false;
		}
		return usedResourceIdList.contains(resourceId);
	}
		
	private String resourceId;
	private String resourceName;
	private String ownerUserId;
	private ResourceTypeModel resourceType;
	private LanguagePathModel supportedLanguagePathModel;

	private boolean active;
	private boolean approved;

	private ServiceActionSchedule serviceActionSchedule;
	private String	organizationName;
	
	private List<String> usedResourceIdList;
}
