package jp.go.nict.langrid.dao.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

/**
 * 
 * 
 * @author $Author:nakaguchi $
 * @version $Revision:4384 $
 */
@Entity
@IdClass(ServiceAttributePK.class)
public class ServiceAttribute
extends Attribute
{
	/**
	 * 
	 * 
	 */
	public ServiceAttribute(){
	}

	/**
	 * 
	 * 
	 */
	public ServiceAttribute(String gridId, String serviceId, String name, String value){
		super(name, value);
		this.gridId = gridId;
		this.serviceId = serviceId;
	}

	/**
	 * 
	 * 
	 */
	public String getGridId() {
		return gridId;
	}

	/**
	 * 
	 * 
	 */
	public void setGridId(String gridId) {
		this.gridId = gridId;
	}

	/**
	 * @return the serviceId
	 */
	public String getServiceId() {
		return serviceId;
	}
	/**
	 * @param serviceId the serviceId to set
	 */
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	@Id
	public String getName() {
		return super.getName();
	}

	@Id
	private String gridId;
	@Id
	private String serviceId;
}
