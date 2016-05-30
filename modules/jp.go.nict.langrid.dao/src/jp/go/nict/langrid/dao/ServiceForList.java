package jp.go.nict.langrid.dao;

import java.io.Serializable;

import jp.go.nict.langrid.dao.entity.Service;
import jp.go.nict.langrid.dao.entity.ServiceActionSchedule;
import jp.go.nict.langrid.dao.entity.ServiceType;

public class ServiceForList implements Serializable {
	public ServiceForList(Service service, ServiceType serviceType, ServiceActionSchedule serviceActionSchedule,
							String organizationName) {
		this.service = service;
		this.serviceType = serviceType;
		this.serviceActionSchedule = serviceActionSchedule;
		this.organizationName = organizationName;
	}

	/**
	 * @return the service
	 */
	public Service getService() {
		return service;
	}
	/**
	 * @return the serviceType
	 */
	public ServiceType getServiceType() {
		return serviceType;
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

	private Service service;
	private ServiceType serviceType;
	private ServiceActionSchedule serviceActionSchedule;
	private String	organizationName;
}
