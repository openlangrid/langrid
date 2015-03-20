/*
 * $Id:ServiceDao.java 4384 2007-04-03 08:56:48Z nakaguchi $
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

/**
 * 
 * 
 * @author $Author:nakaguchi $
 * @version $Revision:4384 $
 */
@Entity
@IdClass(ResourceAttributePK.class)
public class ResourceAttribute
extends Attribute
{
	/**
	 * 
	 * 
	 */
	public ResourceAttribute(){
	}

	/**
	 * 
	 * 
	 */
	public ResourceAttribute(String gridId, String resourceId
			, String name, String value){
		super(name, value);
		this.gridId = gridId;
		this.resourceId = resourceId;
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

	@Id
	public String getName() {
		return super.getName();
	}

	@Id
	private String gridId;
	@Id
	private String resourceId;
}
