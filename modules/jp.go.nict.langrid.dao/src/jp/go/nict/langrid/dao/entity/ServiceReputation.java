package jp.go.nict.langrid.dao.entity;

import java.io.Serializable;

public class ServiceReputation
extends UpdateManagedEntity
implements Serializable{
	public ServiceReputation() {
	}
	public ServiceReputation(String raterUserGridId, String raterUserId, String serviceGridId, String serviceId,
			int reputation) {
		this.raterUserGridId = raterUserGridId;
		this.raterUserId = raterUserId;
		this.serviceGridId = serviceGridId;
		this.serviceId = serviceId;
		this.reputation = reputation;
	}

	

	public String getRaterUserGridId() {
		return raterUserGridId;
	}
	public void setRaterUserGridId(String raterUserGridId) {
		this.raterUserGridId = raterUserGridId;
	}
	public String getRaterUserId() {
		return raterUserId;
	}
	public void setRaterUserId(String raterUserId) {
		this.raterUserId = raterUserId;
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
	public int getReputation() {
		return reputation;
	}
	public void setReputation(int reputation) {
		this.reputation = reputation;
	}

	private static final long serialVersionUID = -7972732401100102285L;
	private String raterUserGridId;
	private String raterUserId;
	private String serviceGridId;
	private String serviceId;
	private int reputation;
}
