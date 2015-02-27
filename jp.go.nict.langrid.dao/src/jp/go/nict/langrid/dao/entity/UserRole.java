/*
 * $Id:UserRole.java 4384 2007-04-03 08:56:48Z nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2008 NICT Language Grid Project.
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

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * 
 * @author $Author:nakaguchi $
 * @version $Revision:4384 $
 */
@Entity
@IdClass(UserRolePK.class)
@Table(name="userRoles")
public class UserRole {
	/**
	 * 
	 * 
	 */
	public static final String USER_ROLE = "langriduser";

	/**
	 * 
	 * 
	 */
	public static final String ADMIN_ROLE = "langridadmin";
	
	/**
	 * 
	 * added by Trang
	 */
	public static final String LANGRID_SERVICE_USER_ROLE = "langridserviceuser";

	/**
	 * 
	 * 
	 */
	public UserRole(){
	}

	/**
	 * 
	 * 
	 */
	public UserRole(String gridId, String userId, String roleName){
		this.gridId = gridId;
		this.userId = userId;
		this.roleName = roleName;
	}

	@Override
	public boolean equals(Object value){
		return EqualsBuilder.reflectionEquals(this, value);
	}

	@Override
	public int hashCode(){
		return HashCodeBuilder.reflectionHashCode(this);
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
	}

	/**
	 * 
	 * 
	 */
	public String getUserId(){
		return userId;
	}

	/**
	 * 
	 * 
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * 
	 * 
	 */
	public String getRoleName(){
		return roleName;
	}

	/**
	 * 
	 * 
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Id
	private String gridId;
	@Id
	private String userId;
	@Id
	private String roleName;
}
