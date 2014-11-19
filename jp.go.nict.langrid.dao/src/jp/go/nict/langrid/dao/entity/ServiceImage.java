/*
 * $Id: ServiceImage.java 207 2010-10-02 14:10:53Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2009 NICT Language Grid Project.
 *
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by 
 * the Free Software Foundation, either version 2.1 of the License, or (at 
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser 
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License 
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package jp.go.nict.langrid.dao.entity;

import java.util.List;

import javax.persistence.Entity;

import org.hibernate.annotations.CollectionOfElements;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 207 $
 */
@Entity
public class ServiceImage
extends UpdateManagedEntity{
	/**
	 * 
	 * 
	 */
	public ServiceImage(String serviceImageId, String serviceId,
			int activatableCount, List<String> activeHosts,
			List<String> inactiveHosts) {
		super();
		this.serviceImageId = serviceImageId;
		this.serviceId = serviceId;
		this.activatableCount = activatableCount;
		this.activeHosts = activeHosts;
		this.inactiveHosts = inactiveHosts;
	}

	public String getServiceImageId() {
		return serviceImageId;
	}

	public void setServiceImageId(String serviceImageId) {
		this.serviceImageId = serviceImageId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public int getActivatableCount() {
		return activatableCount;
	}

	public void setActivatableCount(int activatableCount) {
		this.activatableCount = activatableCount;
	}

	public List<String> getActiveHosts() {
		return activeHosts;
	}

	public void setActiveHosts(List<String> activeHosts) {
		this.activeHosts = activeHosts;
	}

	public List<String> getInactiveHosts() {
		return inactiveHosts;
	}

	public void setInactiveHosts(List<String> inactiveHosts) {
		this.inactiveHosts = inactiveHosts;
	}

	private String serviceImageId;
	private String serviceId;
	private int activatableCount;
	@CollectionOfElements
	private List<String> activeHosts;
	@CollectionOfElements
	private List<String> inactiveHosts;
}
