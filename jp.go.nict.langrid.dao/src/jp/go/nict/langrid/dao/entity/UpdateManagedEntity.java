/*
 * $Id: UpdateManagedEntity.java 207 2010-10-02 14:10:53Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2009 NICT Language Grid Project.
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import jp.go.nict.langrid.commons.util.CalendarUtil;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 207 $
 */
@MappedSuperclass
public abstract class UpdateManagedEntity {
	/**
	 * 
	 * 
	 */
	public UpdateManagedEntity(){}

	/**
	 * 
	 * 
	 */
	public UpdateManagedEntity(
			Calendar createdDateTime, Calendar updatedDateTime){
		this.createdDateTime = createdDateTime;
		this.updatedDateTime = updatedDateTime;
	}

	@Override
	public boolean equals(Object value){
		if(value == null) return false;
		if(!(getClass().equals(value.getClass()))) return false;
		List<String> appendedFields = new ArrayList<String>();
		EqualsBuilder b = new EqualsBuilder();
		appendSpecialEquals(b, value, appendedFields, false);
		return EqualsBuilder.reflectionEquals(this, value, appendedFields)
			&& b.isEquals();
	}

	/**
	 * 
	 * 
	 */
	public boolean equalsIgnoreDates(Object value){
		if(value == null) return false;
		if(!(getClass().equals(value.getClass()))) return false;
		List<String> appendedFields = new ArrayList<String>();
		appendedFields.add("createdDateTime");
		appendedFields.add("updatedDateTime");
		EqualsBuilder b = new EqualsBuilder();
		appendSpecialEquals(b, value, appendedFields, true);
		return EqualsBuilder.reflectionEquals(this, value, appendedFields)
			&& b.isEquals();
	}

	@Override
	public int hashCode(){
		List<String> appendedFields = new ArrayList<String>();
		HashCodeBuilder b = new HashCodeBuilder();
		appendSpecialHashCode(b, appendedFields);
		return new HashCodeBuilder()
			.appendSuper(HashCodeBuilder.reflectionHashCode(
					this, appendedFields))
			.appendSuper(b.toHashCode())
			.toHashCode();
	}

	@Override
	public String toString(){
		ReflectionToStringBuilder b = new ReflectionToStringBuilder(this);
		b.setExcludeFieldNames(new String[]{"createdDateTime", "updatedDateTime"});
		b.append("createdDateTime", createdDateTime != null ?
				CalendarUtil.formatToDefault(createdDateTime) :
				null);
		b.append("updatedDateTime", updatedDateTime != null ?
				CalendarUtil.formatToDefault(updatedDateTime) :
				null);
		return b.toString();
	}

	/**
	 * 
	 * 
	 */
	public Calendar getCreatedDateTime() {
		return createdDateTime;
	}

	/**
	 * 
	 * 
	 */
	public void setCreatedDateTime(Calendar createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	/**
	 * 
	 * 
	 */
	public Calendar getUpdatedDateTime() {
		return updatedDateTime;
	}

	/**
	 * 
	 * 
	 */
	public void setUpdatedDateTime(Calendar updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
	}

	/**
	 * 
	 * 
	 */
	public void touchUpdatedDateTime(){
		setUpdatedDateTime(Calendar.getInstance());
	}

	/**
	 * 
	 * 
	 */
	protected EqualsBuilder appendSpecialEquals(
			EqualsBuilder builder, Object value
			, Collection<String> appendedFields
			, boolean ignoreDates){
		return builder;
	}

	/**
	 * 
	 * 
	 */
	protected HashCodeBuilder appendSpecialHashCode(
			HashCodeBuilder builder
			, Collection<String> appendedFields){
		return builder;
	}

	@Temporal(TemporalType.TIMESTAMP)
	private Calendar createdDateTime;

	@Temporal(TemporalType.TIMESTAMP)
	private Calendar updatedDateTime;

	{
		Calendar now = Calendar.getInstance();
		createdDateTime = now;
		updatedDateTime = now;
	}
}
