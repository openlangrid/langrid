/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2010 NICT Language Grid Project.
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Type;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
@Entity
public class Grid
extends UpdateManagedEntity
implements AttributedElement<GridAttribute>, Serializable{
	/**
	 * 
	 * 
	 */
	public Grid(){
	}

	/**
	 * 
	 * 
	 */
	public Grid(
			String gridId, String operatorUserId
			) {
		this.gridId = gridId;
		this.operatorUserId = operatorUserId;
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
	public String getGridName() {
		return gridName;
	}
	
	/**
	 * 
	 * 
	 */
	public void setGridName(String gridName) {
		this.gridName = gridName;
	}

	/**
	 * 
	 * 
	 */
	public String getOperatorUserId() {
		return operatorUserId;
	}

	/**
	 * 
	 * 
	 */
	public void setOperatorUserId(String opratorUserId) {
		this.operatorUserId = opratorUserId;
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
	public boolean isCommercialUseAllowed(){
		return commercialUseAllowed;
	}

	/**
	 * 
	 * 
	 */
	public void setCommercialUseAllowed(boolean commercialUseAllowed){
		this.commercialUseAllowed = commercialUseAllowed;
	}

	/**
	 * 
	 * 
	 */
	public boolean isHosted(){
		return hosted;
	}

	/**
	 * 
	 * 
	 */
	public void setHosted(boolean hosted){
		this.hosted = hosted;
	}

	/**
	 * 
	 * 
	 */
	public Collection<GridAttribute> getAttributes(){
		return attributes.values();
	}

	public GridAttribute getAttribute(String name){
		return attributes.get(name);
	}

	public void setAttribute(GridAttribute attribute){
		attributes.put(attribute.getName(), attribute);
	}

	public String getAttributeValue(String attributeName){
		Attribute a = attributes.get(attributeName);
		if(a == null) return null;
		return a.getValue();
	}

	public void setAttributes(Collection<GridAttribute> attributes) {
		this.attributes.clear();
		for(GridAttribute a : attributes){
			this.attributes.put(a.getName(), a);
		}
	}

	public void setAttributeValue(String attributeName, String attributeValue){
		GridAttribute a = getAttribute(attributeName);
		if(a == null){
			attributes.put(attributeName
					, new GridAttribute(gridId, attributeName, attributeValue));
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

	public List<Domain> getSupportedDomains(){
		return supportedDomains;
	}

	public void setSupportedDomains(List<Domain> domains){
		supportedDomains = domains;
	}

	public boolean isAutoApproveEnabled() {
		return autoApproveEnabled;
	}

	public void setAutoApproveEnabled(boolean autoApproveEnabled) {
		this.autoApproveEnabled = autoApproveEnabled;
	}

	public boolean isSymmetricRelationEnabled() {
		return symmetricRelationEnabled != null ? symmetricRelationEnabled : false;
	}

	public void setSymmetricRelationEnabled(boolean symmetricRelationEnabled) {
		this.symmetricRelationEnabled = symmetricRelationEnabled;
	}

	public boolean isTranstiveRelationEnabled() {
		return transtiveRelationEnabled != null ? transtiveRelationEnabled : false;
	}

	public void setTranstiveRelationEnabled(boolean transtiveRelationEnabled) {
		this.transtiveRelationEnabled = transtiveRelationEnabled;
	}

	protected EqualsBuilder appendSpecialEquals(
			EqualsBuilder builder, Object value
			, Collection<String> appendedFields, boolean ignoreDates){
		Grid s = (Grid)value;
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
					, GridAttribute.class, "equalsIgnoreDates"
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
	private String gridName;
	private String operatorUserId;
	@Type(type="text")
	private String url;
	private boolean hosted;
	private boolean commercialUseAllowed;
	private boolean autoApproveEnabled = false;
	private Boolean symmetricRelationEnabled = false;
	private Boolean transtiveRelationEnabled = false;

	@OneToMany(cascade=CascadeType.ALL, mappedBy="gridId")
	@MapKey(name="name")
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private Map<String, GridAttribute> attributes = new HashMap<String, GridAttribute>();

	@ManyToMany
	private List<Domain> supportedDomains = new ArrayList<Domain>();

	private static final long serialVersionUID = -549487723699790719L;
}
