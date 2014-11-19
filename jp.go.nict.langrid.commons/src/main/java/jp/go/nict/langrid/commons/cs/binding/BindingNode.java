/*
 * $Id: BindingNode.java 190 2010-10-02 11:28:33Z t-nakaguchi $
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
package jp.go.nict.langrid.commons.cs.binding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 190 $
 */
public class BindingNode
implements Serializable
{
	/**
	 * 
	 * 
	 */
	public BindingNode(){
	}

	/**
	 * 
	 * 
	 */
	public BindingNode(String invocationName, String gridId, String serviceId){
		this.invocationName = invocationName;
		this.gridId = gridId;
		this.serviceId = serviceId;
	}

	/**
	 * 
	 * 
	 */
	public BindingNode(String invocationName, String serviceId){
		this.invocationName = invocationName;
		this.serviceId = serviceId;
	}

	/**
	 * 
	 * 
	 */
	public String getInvocationName() {
		return invocationName;
	}

	/**
	 * 
	 * 
	 */
	public void setInvocationName(String invocationName) {
		this.invocationName = invocationName;
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
	public String getServiceId() {
		return serviceId;
	}

	/**
	 * 
	 * 
	 */
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	/**
	 * 
	 * 
	 */
	public List<BindingNode> getChildren() {
		return children;
	}

	/**
	 * 
	 * 
	 */
	public void setChildren(List<BindingNode> children) {
		this.children = children;
	}

	/**
	 * 
	 * 
	 */
	public BindingNode addChild(BindingNode child){
		children.add(child);
		return this;
	}

	private String invocationName;
	private String gridId;
	private String serviceId;
	private List<BindingNode> children = new ArrayList<BindingNode>();

	private static final long serialVersionUID = -5225173090656327140L;
}
