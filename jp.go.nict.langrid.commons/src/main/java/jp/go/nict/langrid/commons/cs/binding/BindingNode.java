/*
 * $Id: BindingNode.java 1498 2015-02-13 03:50:47Z t-nakaguchi $
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

import jp.go.nict.langrid.commons.rpc.RpcHeader;
import jp.go.nict.langrid.commons.rpc.TransportHeader;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1498 $
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

	public BindingNode(String invocationName, Condition[] conditions, String serviceId){
		this.invocationName = invocationName;
		this.conditions = conditions;
		this.serviceId = serviceId;
	}

	public BindingNode(String invocationName, String paramName, String op, String value, String serviceId){
		this.invocationName = invocationName;
		this.conditions = new Condition[]{
				new Condition(paramName, op, value)
		};
		this.serviceId = serviceId;
	}

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

	public Condition[] getConditions() {
		return conditions;
	}
	public void setConditions(Condition[] conditions) {
		this.conditions = conditions;
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

	public List<TransportHeader> getTransportHeaders() {
		return transportHeaders;
	}

	public void setTransportHeaders(List<TransportHeader> transportHeaders) {
		this.transportHeaders = transportHeaders;
	}

	public List<RpcHeader> getRpcHeaders() {
		return rpcHeaders;
	}

	public void setRpcHeaders(List<RpcHeader> rpcHeaders) {
		this.rpcHeaders = rpcHeaders;
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
	private Condition[] conditions;
	private String gridId;
	private String serviceId;
	private List<TransportHeader> transportHeaders = new ArrayList<TransportHeader>();
	private List<RpcHeader> rpcHeaders = new ArrayList<RpcHeader>();
	private List<BindingNode> children = new ArrayList<BindingNode>();

	private static final long serialVersionUID = -5225173090656327140L;
}
