/*
 * $Id: ProcessInfo.java 184 2010-10-02 10:49:08Z t-nakaguchi $
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
package jp.go.nict.langrid.bpel.entity;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;


import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 184 $
 */
public class ProcessInfo {
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	/**
	 * 
	 * 
	 */
	public MyRoleBinding getBinding() {
		return binding;
	}

	/**
	 * 
	 * 
	 */
	public void setBinding(MyRoleBinding binding) {
		this.binding = binding;
	}

	/**
	 * 
	 * 
	 */
	public Map<URI, WSDL> getWsdls(){
		return wsdls;
	}

	/**
	 * 
	 * 
	 */
	public void setWsdls(Map<URI, WSDL> wsdls){
		this.wsdls= wsdls;
	}

	/**
	 * 
	 * 
	 */
	public BPEL getBpel(){
		return bpel;
	}

	/**
	 * 
	 * 
	 */
	public void setBpel(BPEL bpel){
		this.bpel= bpel;
	}

	/**
	 * 
	 * 
	 */
	public Map<String, PartnerLink> getPartnerLinks(){
		return plnks;
	}

	private Map<URI, WSDL> wsdls;
	private BPEL bpel;
	private Map<String, PartnerLink> plnks = new HashMap<String, PartnerLink>();
	private MyRoleBinding binding = MyRoleBinding.RPC;
}
