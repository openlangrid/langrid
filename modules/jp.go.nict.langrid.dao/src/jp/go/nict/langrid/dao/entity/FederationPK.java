/*
 * $Id: FederationPK.java 205 2010-10-02 13:53:40Z t-nakaguchi $
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

import java.io.Serializable;

import javax.persistence.Embeddable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 205 $
 */
@Embeddable
public class FederationPK implements Serializable{
	/**
	 * 
	 * 
	 */
	public FederationPK(){
		recalcHashCode();
	}

	/**
	 * 
	 * 
	 */
	public FederationPK(String sourceGridId, String targetGridId){
		this.sourceGridId = sourceGridId;
		this.targetGridId = targetGridId;
		recalcHashCode();
	}

	@Override
	public boolean equals(Object value){
		return EqualsBuilder.reflectionEquals(this, value);
	}

	@Override
	public int hashCode(){
		return hashCode;
	}

	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	/**
	 * 
	 * 
	 */
	public String getSourceGridId() {
		return sourceGridId;
	}

	/**
	 * 
	 * 
	 */
	public void setSourceGridId(String sourceGridId) {
		this.sourceGridId = sourceGridId;
		recalcHashCode();
	}

	/**
	 * 
	 * 
	 */
	public String getTargetGridId() {
		return targetGridId;
	}

	/**
	 * 
	 * 
	 */
	public void setTargetGridId(String targetGridId) {
		this.targetGridId = targetGridId;
		recalcHashCode();
	}

	private void recalcHashCode(){
		hashCode = new HashCodeBuilder()
			.append(sourceGridId)
			.append(targetGridId)
			.hashCode();
	}

	private String sourceGridId;
	private String targetGridId;
	private transient int hashCode;
	private static final long serialVersionUID = 8015866063513065382L;
}
