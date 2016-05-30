/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2010 NICT Language Grid Project.
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
package jp.go.nict.langrid.management.web.servlet;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author Takao Nakaguchi
 */
public class FederationResponse {
	public String getGridId() {
		return gridId;
	}

	public void setGridId(String gridId) {
		this.gridId = gridId;
	}

	public String getOperatorOrganization() {
		return operatorOrganization;
	}

	public void setOperatorOrganization(String organization) {
		this.operatorOrganization = organization;
	}

	public String getOperatorHomepage() {
		return operatorHomepage;
	}

	public void setOperatorHomepage(String homepage) {
		this.operatorHomepage = homepage;
	}

	/**
	 * 
	 * 
	 */
	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}
	
	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getGridUrl() {
		return gridUrl;
	}

	public void setGridUrl(String gridUrl) {
		this.gridUrl = gridUrl;
	}

	public boolean isHosted() {
		return hosted;
	}

	public void setHosted(boolean hosted) {
		this.hosted = hosted;
	}

	public boolean isCommercialUseAllowed() {
		return commercialUseAllowed;
	}

	public void setCommercialUseAllowed(boolean commercialUseAllowed) {
		this.commercialUseAllowed = commercialUseAllowed;
	}

	public boolean isAutoApproveEnabled() {
		return autoApproveEnabled;
	}

	public void setAutoApproveEnabled(boolean autoApproveEnabled) {
		this.autoApproveEnabled = autoApproveEnabled;
	}

	public String getNewsMessage() {
		return newsMessage;
	}

	public void setNewsMessage(String newsMessage) {
		this.newsMessage = newsMessage;
	}
	
	public String getGridName() {
		return gridName;
	}
	
	public void setGridName(String gridName) {
		this.gridName = gridName;
	}

	private boolean approved;
	
	private String operatorOrganization;
	private String operatorHomepage;
	private String newsMessage;
	
	// Grid daata from grid table;
	private String gridId;
	private String gridName;
	private String operatorId;
	private String gridUrl;
	private boolean hosted;
	private boolean commercialUseAllowed;
	private boolean autoApproveEnabled;
}
