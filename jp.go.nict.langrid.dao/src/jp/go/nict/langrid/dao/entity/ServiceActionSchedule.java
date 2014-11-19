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
import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 207 $
 */
@Entity
public class ServiceActionSchedule
extends UpdateManagedEntity
implements Serializable
{
   /**
	 * 
	 * 
	 */
	public ServiceActionSchedule(){
	}

	/**
	 * 
	 * 
	 */
	public ServiceActionSchedule(String gridId, Calendar bookingDateTime
			, String targetId, ScheduleTargetType targetType, ScheduleActionType operationType, String nodeId
			)
	{
		this.gridId = gridId;
		this.bookingDateTime = bookingDateTime;
		this.targetId = targetId;
		this.targetType = targetType;
		this.actionType = operationType;
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
	public Calendar getBookingDateTime() {
		return bookingDateTime;
	}

	/**
	 * 
	 * 
	 */
	public void setBookingDateTime(Calendar bookingDateTime) {
		this.bookingDateTime = bookingDateTime;
	}

	/**
	 * 
	 * 
	 */
	public String getTargetId() {
		return targetId;
	}

	/**
	 * 
	 * 
	 */
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	/**
	 * 
	 * 
	 */
	public ScheduleTargetType getTargetType() {
		return targetType;
	}

	/**
	 * 
	 * 
	 */
	public void setTargetType(ScheduleTargetType targetType) {
		this.targetType = targetType;
	}

	/**
	 * 
	 * 
	 */
	public ScheduleActionType getOperationType() {
		return actionType;
	}

	/**
	 * 
	 * 
	 */
	public void setOperationType(ScheduleActionType operationType) {
		this.actionType = operationType;
	}

	/**
	 * 
	 * 
	 */
	public void setRelated(boolean isRelated) {
		this.isRelated = isRelated;
	}

	/**
	 * 
	 * 
	 */
	public boolean isRelated() {
		return isRelated;
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
	private Calendar bookingDateTime;
	private String targetId;
	private boolean isRelated;
	private ScheduleTargetType targetType;
	private ScheduleActionType actionType;
	private String nodeId;
	private int nodeLocalId;

   private static final long serialVersionUID = 8874865228518776163L;
}
