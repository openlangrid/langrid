package jp.go.nict.langrid.dao;

import java.io.Serializable;

import jp.go.nict.langrid.dao.entity.Resource;
import jp.go.nict.langrid.dao.entity.ResourceType;
import jp.go.nict.langrid.dao.entity.ServiceActionSchedule;

public class ResourceForList implements Serializable {
	public ResourceForList(Resource res, ResourceType rtype, ServiceActionSchedule sas, String organization) {
		this.resource = res;
		this.resourceType = rtype;
		this.serviceActionSchedule = sas;
		this.organizationName = organization;
	}

	/**
	 * @return the resource
	 */
	public Resource getResource() {
		return resource;
	}
	/**
	 * @return the resourceType
	 */
	public ResourceType getResourceType() {
		return resourceType;
	}
	/**
	 * @return the serviceActionSchedule
	 */
	public ServiceActionSchedule getServiceActionSchedule() {
		return serviceActionSchedule;
	}
	/**
	 * @return the organizationName
	 */
	public String getOrganizationName() {
		return organizationName;
	}

	private Resource 	resource;
	private ResourceType resourceType;
	private ServiceActionSchedule serviceActionSchedule;
	private String	organizationName;
}
