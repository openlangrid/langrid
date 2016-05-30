/*
 * $Id: NodeProfile.java 225 2010-10-03 00:23:14Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.foundation.nodemanagement;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 225 $
 */
public class NodeProfile
implements Serializable
{
	/**
	 * 
	 * 
	 */
	public NodeProfile(){
	}

	/**
	 * 
	 * 
	 */
	public NodeProfile(String nodeName
			, String nodeType, String url, String os, String cpu
			, String memory
			, String specialNotes) {
		this.nodeName = nodeName;
		this.nodeType = nodeType;
		this.url = url;
		this.os = os;
		this.cpu = cpu;
		this.memory = memory;
		this.specialNotes = specialNotes;
	}

	@Override
	public boolean equals(Object value){
		return EqualsBuilder.reflectionEquals(this, value);
	}

	@Override
	public int hashCode(){
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	/**
	 * 
	 * 
	 */
	public String getNodeName() {
		return nodeName;
	}

	/**
	 * 
	 * 
	 */
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	/**
	 * 
	 * 
	 */
	public String getNodeType() {
		return nodeType;
	}

	/**
	 * 
	 * 
	 */
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	/**
	 * 
	 * 
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 
	 * 
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 
	 * 
	 */
	public String getOs() {
		return os;
	}

	/**
	 * 
	 * 
	 */
	public void setOs(String os) {
		this.os = os;
	}

	/**
	 * 
	 * 
	 */
	public String getCpu() {
		return cpu;
	}

	/**
	 * 
	 * 
	 */
	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	/**
	 * 
	 * 
	 */
	public String getMemory() {
		return memory;
	}

	/**
	 * 
	 * 
	 */
	public void setMemory(String memory) {
		this.memory = memory;
	}

	/**
	 * 
	 * 
	 */
	public String getSpecialNotes() {
		return specialNotes;
	}

	/**
	 * 
	 * 
	 */
	public void setSpecialNotes(String specialNotes) {
		this.specialNotes = specialNotes;
	}

	private String nodeName;
	private String nodeType;
	private String url;
	private String os;
	private String cpu;
	private String memory;
	private String specialNotes;

	private static final long serialVersionUID = 7283998817371299031L;
}
