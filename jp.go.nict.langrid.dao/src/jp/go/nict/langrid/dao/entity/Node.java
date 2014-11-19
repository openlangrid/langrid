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
package jp.go.nict.langrid.dao.entity;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.Cascade;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
@Entity
@IdClass(NodePK.class)
public class Node
extends UpdateManagedEntity
implements AttributedElement<NodeAttribute>, Serializable{
	/**
	 * 
	 * 
	 */
	public Node(){
	}

	/**
	 * 
	 * 
	 */
	public Node(String gridId, String nodeId) {
		this.gridId = gridId;
		this.nodeId = nodeId;
	}

	/**
	 * 
	 * 
	 */
	public Node(
			String gridId, String nodeId, String nodeName
			, URL url, String ownerUserId, boolean active
			) {
		this.gridId = gridId;
		this.nodeId = nodeId;
		this.nodeName = nodeName;
		setUrl(url);
		this.ownerUserId = ownerUserId;
		this.active = active;
	}

	/**
	 * 
	 * 
	 */
	public Node(String gridId, String nodeId, String nodeName
			, URL url, String ownerUserId, String ownerUserOrganization, boolean active
			, String os, String cpu, String memory, String specialNotes) {
		this.gridId = gridId;
		this.nodeId = nodeId;
		this.nodeName = nodeName;
		setUrl(url);
		this.ownerUserId = ownerUserId;
		this.ownerUserOrganization = ownerUserOrganization;
		this.active = active;
		this.os = os;
		this.cpu = cpu;
		this.memory = memory;
		this.specialNotes = specialNotes;
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
	public String getNodeId() {
		return nodeId;
	}

	/**
	 * 
	 * 
	 */
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
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
	public URL getUrl() {
		if(url == null) return null;
		return url.getValue();
	}

	/**
	 * 
	 * 
	 */
	public void setUrl(URL url) {
		if(url == null){
			this.url = null;
		} else if(this.url == null){
			this.url = new EmbeddableStringValueClass<URL>(url);
		} else{
			this.url.setValue(url);
		}
	}

	/**
	 * 
	 * 
	 */
	public String getOwnerUserId() {
		return ownerUserId;
	}

	/**
	 * 
	 * 
	 */
	public void setOwnerUserId(String ownerUserId) {
		this.ownerUserId = ownerUserId;
	}
	
	/**
	 * 
	 * 
	 */
	public String getOwnerUserOrganization() {
		return ownerUserOrganization;
	}
	
	/**
	 * 
	 * 
	 */
	public void setOwnerUserOrganization(String ownerUserOrganization) {
		this.ownerUserOrganization = ownerUserOrganization;
	}

	/**
	 * 
	 * 
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * 
	 * 
	 */
	public void setActive(boolean active) {
		this.active = active;
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

	/**
	 * 
	 * 
	 */
	public boolean isMirror() {
		return mirror;
	}

	/**
	 * 
	 * 
	 */
	public void setMirror(boolean mirror) {
		this.mirror = mirror;
	}

	/**
	 * 
	 * 
	 */
	public String getAccessToken() {
		return accessToken;
	}

	/**
	 * 
	 * 
	 */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Collection<NodeAttribute> getAttributes(){
		return attributes.values();
	}

	public void setAttributes(Collection<NodeAttribute> attributes){
		this.attributes.clear();
		for(NodeAttribute a : attributes){
			this.attributes.put(a.getName(), a);
		}
	}

	public NodeAttribute getAttribute(String name){
		return attributes.get(name);
	}

	public void setAttribute(NodeAttribute attribute){
		attributes.put(attribute.getName(), attribute);
	}

	public String getAttributeValue(String attributeName){
		Attribute a = attributes.get(attributeName);
		if(a == null) return null;
		return a.getValue();
	}

	public void setAttributeValue(String attributeName, String attributeValue){
		NodeAttribute a = getAttribute(attributeName);
		if(a == null){
			attributes.put(attributeName
					, new NodeAttribute(gridId, nodeId, attributeName, attributeValue));
		} else{
			a.setValue(attributeValue);
			a.touchUpdatedDateTime();
		}
	}

	public void removeAttribute(String attributeName){
		if(attributes.containsKey(attributeName)){
			attributes.remove(attributeName);
		}
	}

	protected EqualsBuilder appendSpecialEquals(
			EqualsBuilder builder, Object value
			, Collection<String> appendedFields, boolean ignoreDates){
		Node s = (Node)value;
		EqualsBuilder b = super.appendSpecialEquals(
				builder, value, appendedFields, ignoreDates);
		appendedFields.add("attributes");
		if(!ignoreDates){
			EqualsBuilderUtil.appendAsSet(
					b, getAttributes(), s.getAttributes()
					);
			return b;
		}
		try{
			EqualsBuilderUtil.appendAsSet(
					b, getAttributes(), s.getAttributes()
					, NodeAttribute.class, "equalsIgnoreDates"
					);
			return b;
		} catch(IllegalAccessException e){
			throw new RuntimeException(e);
		} catch(InvocationTargetException e){
			throw new RuntimeException(e);
		} catch(NoSuchMethodException e){
			throw new RuntimeException(e);
		}
	}

	@Override
	protected HashCodeBuilder appendSpecialHashCode(HashCodeBuilder builder,
			Collection<String> appendedFields) {
		HashCodeBuilder b = super.appendSpecialHashCode(builder, appendedFields);
		appendedFields.add("attributes");
		return b
			.append(attributes.entrySet())
			;
	}

	@Id
	private String gridId;
	@Id
	private String nodeId;
	private String nodeName;
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name="stringValue"
				, column=@Column(name="urlStringValue")
			),
			@AttributeOverride(name="clazz"
				, column=@Column(name="urlClazz")
				)
			})
	private EmbeddableStringValueClass<URL> url;
	private String ownerUserId;
	private String ownerUserOrganization;
	private boolean active;

	private String os;
	private String cpu;
	private String memory;
	private String specialNotes;

	private boolean mirror;
	private String accessToken;

	@OneToMany(cascade=CascadeType.ALL, mappedBy="nodeId")
	@JoinColumns({
		@JoinColumn(name="gridId")
		, @JoinColumn(name="nodeId")
	})
	@MapKey(name="name")
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private Map<String, NodeAttribute> attributes = new HashMap<String, NodeAttribute>();

	private static final long serialVersionUID = 5745291835515693757L;
}
