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

import jp.go.nict.langrid.management.web.model.GridModel;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author Takao Nakaguchi
 */
public class FederationResponse {
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

	public String getNewsMessage() {
		return newsMessage;
	}

	public void setNewsMessage(String newsMessage) {
		this.newsMessage = newsMessage;
	}

	public GridModel getTargetGrid() {
		return targetGrid;
	}
	
	public void setTargetGrid(GridModel targetGrid) {
		this.targetGrid = targetGrid;
	}

	public String getReverseConnectionToken() {
		return reverseConnectionToken;
	}

	public void setReverseConnectionToken(String reverseConnectionToken) {
		this.reverseConnectionToken = reverseConnectionToken;
	}

	public boolean isSymmetric() {
		return symmetric;
	}
	
	public void setSymmetric(boolean symmetric) {
		this.symmetric = symmetric;
	}
	
	public boolean isTransitive() {
		return transitive;
	}

	public void setTransitive(boolean transitive) {
		this.transitive = transitive;
	}

	private boolean approved;

	private String operatorOrganization;
	private String operatorHomepage;
	private String newsMessage;
	private String reverseConnectionToken;
	boolean symmetric;
	boolean transitive;

	private GridModel targetGrid;
}
