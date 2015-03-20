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
package jp.go.nict.langrid.client.ws_1_2.impl;

import java.io.Serializable;
import java.util.Collection;

import jp.go.nict.langrid.client.RequestAttributes;
import jp.go.nict.langrid.client.ResponseAttributes;
import jp.go.nict.langrid.client.ws_1_2.ServiceClient;
import jp.go.nict.langrid.commons.cs.binding.BindingNode;
import jp.go.nict.langrid.commons.cs.calltree.CallNode;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class ServiceClientReqResAttributesAdapter
implements ServiceClient, Serializable{
	/**
	 * 
	 * 
	 */
	public ServiceClientReqResAttributesAdapter(
			RequestAttributes reqAttr, ResponseAttributes resAttr){
		this.reqAttr = reqAttr;
		this.resAttr = resAttr;
	}

	public void setUserId(String userId){
		reqAttr.setUserId(userId);
	}

	public void setPassword(String password){
		reqAttr.setPassword(password);
	}

	public Collection<BindingNode> getTreeBindings(){
		return reqAttr.getTreeBindings();
	}

	@Override
	public void addRequestHeader(String name, String value) {
		reqAttr.addRequestMimeHeader(name, value);
	}

	public String getLastName(){
		return resAttr.getServiceName();
	}

	public String getLastCopyrightInfo() {
		return resAttr.getCopyright();
	}

	public String getLastLicenseInfo() {
		return resAttr.getLicenseInfo();
	}

	public Collection<CallNode> getLastCallTree(){
		return resAttr.getCallTree();
	}

	private RequestAttributes reqAttr;
	private ResponseAttributes resAttr;
	private static final long serialVersionUID = 6715452983197392318L;
}
