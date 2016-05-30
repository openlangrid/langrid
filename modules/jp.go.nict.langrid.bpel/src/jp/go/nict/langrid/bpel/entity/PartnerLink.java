/*
 * $Id: PartnerLink.java 184 2010-10-02 10:49:08Z t-nakaguchi $
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

import java.util.logging.Logger;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 184 $
 */
public class PartnerLink {
	/**
	 * 
	 * 
	 */
	public PartnerLink(){
	}

	/**
	 * 
	 * 
	 */
	public PartnerLink(NamespaceContext context, Node partnerLinkNode){
		NamedNodeMap attrs = partnerLinkNode.getAttributes();
		setName(getAttr(attrs, "name"));
		setMyRole(getAttr(attrs, "myRole"));
		setPartnerRole(getAttr(attrs, "partnerRole"));
		String[] plts = getAttr(attrs, "partnerLinkType").split(":", 2);
		if(plts.length == 2){
			String uri = context.getNamespaceURI(plts[0]);
			if(uri == null){
				uri = plts[0];
				logger.warning("failed to resolve namespace prefix \""
						+ plts[0] + "\" for element \"" + plts[1]
						+ "\"");
			}
			setPartnerLinkType(new QName(uri, plts[1]));
		} else{
			setPartnerLinkType(new QName(plts[0]));			
		}
	}

	private static String getAttr(NamedNodeMap attrs, String name){
		Node attr = attrs.getNamedItem(name);
		if(attr == null) return null;
		else return attr.getNodeValue();
	}

	/**
	 * 
	 * 
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * 
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * 
	 */
	public String getMyRole() {
		return myRole;
	}

	/**
	 * 
	 * 
	 */
	public void setMyRole(String myRole) {
		this.myRole = myRole;
	}

	/**
	 * 
	 * 
	 */
	public String getPartnerRole() {
		return partnerRole;
	}

	/**
	 * 
	 * 
	 */
	public void setPartnerRole(String partnerRole) {
		this.partnerRole = partnerRole;
	}

	/**
	 * 
	 * 
	 */
	public String getPartnerInvokeHandlerClass() {
		return partnerInvokeHandlerClass;
	}

	/**
	 * 
	 * 
	 */
	public void setPartnerInvokeHandlerClass(String value) {
		this.partnerInvokeHandlerClass = value;
	}

	/**
	 * 
	 * 
	 */
	public QName getPartnerLinkType() {
		return partnerLinkType;
	}

	/**
	 * 
	 * 
	 */
	public void setPartnerLinkType(QName partnerLinkType) {
		this.partnerLinkType = partnerLinkType;
	}

	/**
	 * 
	 * 
	 */
	public EndpointReference getEndpointReference(){
		return endpointReference;
	}

	/**
	 * 
	 * 
	 */
	public void setEndpointReference(EndpointReference endpointReference){
		this.endpointReference= endpointReference;
	}

	/**
	 * 
	 * 
	 */
	public String getService(){
		return service;
	}

	/**
	 * 
	 * 
	 */
	public void setService(String service){
		  this.service= service;
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	private String name;
	private String myRole;
	private QName partnerLinkType;
	private String partnerRole;
	private String partnerInvokeHandlerClass;
	private EndpointReference endpointReference;
	private String service;
	private static Logger logger = Logger.getLogger(
			PartnerLink.class.getName());
}
