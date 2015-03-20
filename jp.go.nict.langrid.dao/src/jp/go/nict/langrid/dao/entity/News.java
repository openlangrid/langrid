/*
 * $Id:Endpoint.java 4384 2007-04-03 08:56:48Z nakaguchi $
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

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Type;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 388 $
 */
@Entity
public class News
extends UpdateManagedEntity
implements Serializable
{
	/**
	 * 
	 * 
	 */
	public News(){
	}

	/**
	 * 
	 * 
	 */
	public News(String gridId, String contents, String nodeId) {
		this.gridId = gridId;
		this.contents = contents;
		this.nodeId = nodeId;
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
	public String getContents() {
		return contents;
	}

	/**
	 * 
	 * 
	 */
	public void setContents(String contents) {
		this.contents = contents;
	}

	/**
	 * 
	 * 
	 */
	public int getId() {
		return id;
	}

	/**
	 * 
	 * 
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 
	 * 
	 */
	public String getNodeId() {
		return nodeId;
	}

	/**
	 * 
	 * 
	 */
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	/**
	 * 
	 * 
	 */
	public int getNodeLocalId() {
		return nodeLocalId;
	}

	/**
	 * 
	 * 
	 */
	public void setNodeLocalId(int nodeLocalId) {
		this.nodeLocalId = nodeLocalId;
	}

	@Id
	@GeneratedValue
	private int id;

	private String gridId;

	@Type(type="text")
	private String contents;
	private String nodeId;
	private int nodeLocalId;

	private static final long serialVersionUID = -7803103783421294083L;
}
