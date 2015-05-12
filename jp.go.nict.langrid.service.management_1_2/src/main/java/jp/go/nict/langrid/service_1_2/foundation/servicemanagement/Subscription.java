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
package jp.go.nict.langrid.service_1_2.foundation.servicemanagement;

import java.util.Calendar;

public class Subscription {
	public Subscription() {
	}
	public Subscription(String userGridId, String userId,
			String sourceServiceGridId, String sourceServiceId,
			String targetServiceGridId, String targetServiceId,
			Calendar createdDateTime) {
		this.userGridId = userGridId;
		this.userId = userId;
		this.sourceServiceGridId = sourceServiceGridId;
		this.sourceServiceId = sourceServiceId;
		this.targetServiceGridId = targetServiceGridId;
		this.targetServiceId = targetServiceId;
		this.createdDateTime = createdDateTime;
	}

	public String getUserGridId() {
		return userGridId;
	}
	public void setUserGridId(String userGridId) {
		this.userGridId = userGridId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getSourceServiceGridId() {
		return sourceServiceGridId;
	}
	public void setSourceServiceGridId(String sourceServiceGridId) {
		this.sourceServiceGridId = sourceServiceGridId;
	}
	public String getSourceServiceId() {
		return sourceServiceId;
	}
	public void setSourceServiceId(String sourceServiceId) {
		this.sourceServiceId = sourceServiceId;
	}
	public String getTargetServiceGridId() {
		return targetServiceGridId;
	}
	public void setTargetServiceGridId(String targetServiceGridId) {
		this.targetServiceGridId = targetServiceGridId;
	}
	public String getTargetServiceId() {
		return targetServiceId;
	}
	public void setTargetServiceId(String targetServiceId) {
		this.targetServiceId = targetServiceId;
	}
	public Calendar getCreatedDateTime() {
		return createdDateTime;
	}
	public void setCreatedDateTime(Calendar createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	private String userGridId;
	private String userId;
	private String sourceServiceGridId;
	private String sourceServiceId;
	private String targetServiceGridId;
	private String targetServiceId;
	private Calendar createdDateTime;
}
