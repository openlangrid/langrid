/*
 * $Id:HibernateAccessRightDao.java 4384 2007-04-03 08:56:48Z nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2009 NICT Language Grid Project.
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

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 207 $
 */
public class ServicePK implements Serializable{
	/**
	 * 
	 * 
	 */
	public ServicePK(){
		recalcHashCode();
	}

	/**
	 * 
	 * 
	 */
	public ServicePK(String gridId, String serviceId){
		this.gridId = gridId;
		this.serviceId = serviceId;
		recalcHashCode();
	}

	@Override
	public boolean equals(Object value){
		return EqualsBuilder.reflectionEquals(this, value);
	}

	@Override
	public int hashCode(){
		return hashCode;
	}

	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
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
		recalcHashCode();
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
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
		recalcHashCode();
	}

	private void recalcHashCode(){
		hashCode = new HashCodeBuilder()
			.append(gridId)
			.append(serviceId)
			.toHashCode();
	}

	private String gridId;
	private String serviceId;
	private transient int hashCode;
	private static final long serialVersionUID = 3691907882623347049L;
}
