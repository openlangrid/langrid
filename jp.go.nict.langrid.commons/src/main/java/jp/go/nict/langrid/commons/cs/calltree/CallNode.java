/*
 * $Id: CallNode.java 190 2010-10-02 11:28:33Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2009 NICT Language Grid Project.
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
package jp.go.nict.langrid.commons.cs.calltree;

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
public class CallNode
implements Serializable
{
	/**
	 * 
	 * 
	 */
	public CallNode() {
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
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * 
	 * 
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * 
	 * 
	 */
	public String getServiceCopyright() {
		return serviceCopyright;
	}

	/**
	 * 
	 * 
	 */
	public void setServiceCopyright(String serviceCopyright) {
		this.serviceCopyright = serviceCopyright;
	}

	/**
	 * 
	 * 
	 */
	public String getServiceLicense() {
		return serviceLicense;
	}

	/**
	 * 
	 * 
	 */
	public void setServiceLicense(String serviceLicense) {
		this.serviceLicense = serviceLicense;
	}

	/**
	 * 
	 * 
	 */
	public long getResponseTimeMillis() {
		return responseTimeMillis;
	}

	/**
	 * 
	 * 
	 */
	public void setResponseTimeMillis(long responseTimeMillis) {
		this.responseTimeMillis = responseTimeMillis;
	}

	/**
	 * 
	 * 
	 */
	public String getFaultCode() {
		return faultCode;
	}
	/**
	 * 
	 * 
	 */
	public void setFaultCode(String faultCode) {
		this.faultCode = faultCode;
	}

	/**
	 * 
	 * 
	 */
	public String getFaultString() {
		return faultString;
	}

	/**
	 * 
	 * 
	 */
	public void setFaultString(String faultString) {
		this.faultString = faultString;
	}

	/**
	 * 
	 * 
	 */
	public List<CallNode> getChildren() {
		return children;
	}

	/**
	 * 
	 * 
	 */
	public void setChildren(List<CallNode> children) {
		this.children = children;
	}

	private String invocationName;
	private String serviceId;
	private String serviceName;
	private String serviceCopyright;
	private String serviceLicense;
	private long responseTimeMillis;
	private String faultCode;
	private String faultString;
	private List<CallNode> children = new ArrayList<CallNode>();

	private static final long serialVersionUID = -4626139584999556612L;
}
