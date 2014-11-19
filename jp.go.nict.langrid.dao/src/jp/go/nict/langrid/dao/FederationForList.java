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
import java.net.URL;

import jp.go.nict.langrid.dao.entity.User;

public class FederationForList implements Serializable {
	public FederationForList(String sourceGridId, String sourceGridName, 
					String targetGridId, String targetGridName, String targetGridAccessToken, 
					URL targetGridUserHomepage, String targetGridUserId,
					String targetGridUserOrganization,
					boolean requesting,
					User user) {
		this.sourceGridId = sourceGridId;
		this.sourceGridName = sourceGridName;
		this.targetGridId = targetGridId;
		this.targetGridName = targetGridName;
		this.targetGridAccessToken = targetGridAccessToken;
		this.targetGridUserHomepage = targetGridUserHomepage;
		this.targetGridUserId = targetGridUserId;
		this.targetGridUserOrganization = targetGridUserOrganization;
		this.requesting = requesting;
		this.user = user;
	}
	
	/**
	 * @return the requesting
	 */
	public boolean isRequesting() {
		return requesting;
	}
	/**
	 * @return the sourceGridId
	 */
	public String getSourceGridId() {
		return sourceGridId;
	}
	/**
	 * @return the sourceGridName
	 */
	public String getSourceGridName() {
		return sourceGridName;
	}
	/**
	 * @return the targetGridAccessToken
	 */
	public String getTargetGridAccessToken() {
		return targetGridAccessToken;
	}
	/**
	 * @return the targetGridId
	 */
	public String getTargetGridId() {
		return targetGridId;
	}
	/**
	 * @return the targetGridName
	 */
	public String getTargetGridName() {
		return targetGridName;
	}
	/**
	 * @return the targetGridUserHomepage
	 */
	public URL getTargetGridUserHomepage() {
		return targetGridUserHomepage;
	}
	/**
	 * @return the targetGridUserId
	 */
	public String getTargetGridUserId() {
		return targetGridUserId;
	}
	/**
	 * @return the targetGridUserOrganization
	 */
	public String getTargetGridUserOrganization() {
		return targetGridUserOrganization;
	}
	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}
	private boolean requesting;
	private String	sourceGridId;
	private String	sourceGridName;
	private String	targetGridAccessToken;
	private String	targetGridId;
	private String	targetGridName;
	private URL		targetGridUserHomepage;
	private String	targetGridUserId;
	private String	targetGridUserOrganization;
	private User	user;
}
