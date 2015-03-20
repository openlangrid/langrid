/*
 * $Id: BPEL.java 184 2010-10-02 10:49:08Z t-nakaguchi $
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

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * 
 * @author  $Author: t-nakaguchi $
 * @version  $Revision: 184 $
 */
public class BPEL {
	/**
	 * 
	 * 
	 */
	public BPELVersion getBpelVersion() {
		return bpelVersion;
	}

	/**
	 * 
	 * 
	 */
	public void setBpelVersion(BPELVersion bpelVersion) {
		this.bpelVersion = bpelVersion;
	}

	/**
	 * 
	 * 
	 */
	public URI getTargetNamespace() {
		return targetNamespace;
	}

	/**
	 * 
	 * 
	 */
	public void setTargetNamespace(URI targetNamespace) {
		this.targetNamespace = targetNamespace;
	}

	/**
	 * 
	 * 
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * 
	 * 
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * 
	 * 
	 */
	public Iterable<PartnerLink> getPartnerLinks() {
		return partnerLinks;
	}

	/**
	 * 
	 * 
	 */
	public void setPartnerLinks(Iterable<PartnerLink> partnerLinks) {
		this.partnerLinks = partnerLinks;
	}

	/**
	 * 
	 * 
	 */
	public String getProcessName() {
		return processName;
	}

	/**
	 * 
	 * 
	 */
	public void setProcessName(String processName) {
		this.processName = processName;
	}

	/**
	 * 
	 * 
	 */
	public byte[] getBody() {
		return body;
	}

	/**
	 * 
	 * 
	 */
	public void setBody(byte[] body) {
		this.body = body;
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	private BPELVersion bpelVersion;
	private URI targetNamespace;
	private String filename;
	private Iterable<PartnerLink> partnerLinks;
	private String processName;
	private byte[] body;
}
