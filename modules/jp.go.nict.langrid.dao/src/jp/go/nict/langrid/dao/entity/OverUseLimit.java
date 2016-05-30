/*
 * $Id:AccessConstraintSearchResult.java 4090 2007-02-02 05:55:39Z nakaguchi $
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
package jp.go.nict.langrid.dao.entity;

import java.io.Serializable;
import java.util.Arrays;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 207 $
 */
@Entity
@IdClass(OverUseLimitPK.class)
public class OverUseLimit
extends UpdateManagedEntity
implements Serializable
{
	/**
	 * 
	 * 
	 */
	public OverUseLimit(){
	}

	/**
	 * 
	 * 
	 */
	public OverUseLimit(
			String gridId
			, Period period, LimitType limitType, int limitCount
			) {
		this.gridId = gridId;
		this.period = period;
		this.limitType = limitType;
		this.limitCount = limitCount;
	}

	@Override
	public boolean equals(Object value){
		return EqualsBuilder.reflectionEquals(
				this, value, new String[]{"updatedDate"}
				);
	}

	/**
	 * 
	 * 
	 */
	public boolean equalsIgnoreDates(OverUseLimit value){
		EqualsBuilder b = new EqualsBuilder();

		b.appendSuper(EqualsBuilder.reflectionEquals(
				this, value, Arrays.asList(
						"createdDateTime", "updatedDateTime"
						)
				));

		return b.isEquals();
	}

	@Override
	public int hashCode(){
		return HashCodeBuilder.reflectionHashCode(
				this, new String[]{"createdDateTime", "updatedDateTime"}
				);
	}

	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
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
	public Period getPeriod() {
		return period;
	}

	/**
	 * 
	 * 
	 */
	public void setPeriod(Period period) {
		this.period = period;
	}

	/**
	 * 
	 * 
	 */
	public LimitType getLimitType() {
		return limitType;
	}

	/**
	 * 
	 * 
	 */
	public void setLimitType(LimitType limitType) {
		this.limitType = limitType;
	}

	/**
	 * 
	 * 
	 */
	public int getLimitCount() {
		return limitCount;
	}

	/**
	 * 
	 * 
	 */
	public void setLimitCount(int limitCount) {
		this.limitCount = limitCount;
	}

	@Id
	private String gridId;
	@Id
	private Period period;
	@Id
	private LimitType limitType;
	private int limitCount;

	private static final long serialVersionUID = 6995749291905772250L;
}
