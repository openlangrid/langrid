/*
 * $Id: GridEntry.java 225 2010-10-03 00:23:14Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_3.foundation;

import java.io.Serializable;
import java.util.Calendar;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import jp.go.nict.langrid.service_1_2.foundation.usermanagement.UserEntry;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 225 $
 */
public class GridEntry
implements Serializable
{
	public GridEntry(){
	}

	public GridEntry(String gridId, String gridName, String url, UserEntry operator, Calendar registeredDate,
			Calendar updatedDate) {
		this.gridId = gridId;
		this.gridName = gridName;
		this.url = url;
		this.operator = operator;
		this.registeredDate = registeredDate;
		this.updatedDate = updatedDate;
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

	public String getGridId() {
		return gridId;
	}

	public void setGridId(String gridId) {
		this.gridId = gridId;
	}

	public String getGridName() {
		return gridName;
	}

	public void setGridName(String gridName) {
		this.gridName = gridName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public UserEntry getOperator() {
		return operator;
	}

	public void setOperator(UserEntry operator) {
		this.operator = operator;
	}

	public Calendar getRegisteredDate() {
		return registeredDate;
	}

	public void setRegisteredDate(Calendar registeredDate) {
		this.registeredDate = registeredDate;
	}

	public Calendar getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Calendar updatedDate) {
		this.updatedDate = updatedDate;
	}


	private String gridId;
	private String gridName;
	private String url;
	private UserEntry operator;
	private Calendar registeredDate;
	private Calendar updatedDate;

	private static final long serialVersionUID = -2279766886877060280L;
}
