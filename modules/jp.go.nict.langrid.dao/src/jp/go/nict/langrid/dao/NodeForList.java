/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2011 NICT Language Grid Project.
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

package jp.go.nict.langrid.dao;

import java.io.Serializable;
import java.util.Calendar;

public class NodeForList implements Serializable {
	public NodeForList() {}
	
	public NodeForList( Calendar createdDateTime, Calendar updatedDateTime,
						String gridId, String nodeId, String nodeName, String url,
						String ownerUserId, String organizationName, boolean active ) {
		this.createdDateTime = createdDateTime;
		this.updatedDateTime = updatedDateTime;
		this.gridId   = gridId;
		this.nodeId   = nodeId;
		this.nodeName = nodeName;
		this.url      = url;
		this.ownerUserId = ownerUserId;
		this.organizationName = organizationName;
		this.active = active;
	}
	
	public Calendar getCreatedDateTime() {
		return createdDateTime;
	}

	public Calendar getUpdatedDateTime() {
		return updatedDateTime;
	}

	public String getGridId() {
		return gridId;
	}

	public String getNodeId() {
		return nodeId;
	}
	
	public String getNodeName() {
		return nodeName;
	}
	
	public String getUrl() {
		return url;
	}
	
	public String getOwnerUserId() {
		return ownerUserId;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public boolean isActive() {
		return active;
	}

	private Calendar createdDateTime;
	private Calendar updatedDateTime;
	private String gridId;
	private String nodeId;
	private String nodeName;
	private String url;
	private String ownerUserId;
	private String organizationName;
	private boolean active;
}
