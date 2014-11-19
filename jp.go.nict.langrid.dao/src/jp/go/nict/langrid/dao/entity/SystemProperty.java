/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2010 NICT Language Grid Project.
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

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author Masato Mori
 */
@Entity
@IdClass(SystemPropertyPK.class)
public class SystemProperty
implements Serializable{
	/**
	 * 
	 * 
	 */
	public SystemProperty(){
	}

	/**
	 * 
	 * 
	 */
	public SystemProperty(String gridId, String name, String value){
		this.gridId = gridId;
		this.name = name;
		this.value = value;
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
	public String getName() {
		return name;
	}

	/**
	 * 
	 * 
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * 
	 */
	public String getValue() {
		return value;
	}

	/**
	 * 
	 * 
	 */
	public void setValue(String value) {
		this.value = value;
	}

	@Id
	private String gridId;
	@Id
	private String name;
	private String value;
	private static final long serialVersionUID = 2830372182041247466L;
}
