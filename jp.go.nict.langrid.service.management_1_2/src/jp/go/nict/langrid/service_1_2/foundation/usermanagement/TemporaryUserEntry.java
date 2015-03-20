/*
 * $Id: TemporaryUserEntry.java 225 2010-10-03 00:23:14Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.foundation.usermanagement;

import java.io.Serializable;
import java.util.Calendar;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * Stores a temporary user's entry data.
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 225 $
 */
public class TemporaryUserEntry
implements Serializable
{
	/**
	 * 
	 * Constructor.
	 * 
	 */
	public TemporaryUserEntry(){
	}

	/**
	 * 
	 * Constructor.
	 * @param userId Temporary user ID
	 * @param beginAvailableDateTime Starting date and time of usage
	 * @param endAvailableDateTime Ending date and time of usage
	 * 
	 */
	public TemporaryUserEntry(String userId
			, Calendar beginAvailableDateTime
			, Calendar endAvailableDateTime) {
		this.userId = userId;
		this.beginAvailableDateTime = beginAvailableDateTime;
		this.endAvailableDateTime = endAvailableDateTime;
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
	 * Gets temporary user ID.
	 * @return Temporary user ID
	 * 
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * 
	 * Sets temporary user ID.
	 * @param userId Temporary user ID
	 * 
	 */
	public void setUserId(String userId){
		this.userId = userId;
	}

	/**
	 * 
	 * Returns the usage start date and time.
	 * @return Start date and time of usage
	 * 
	 */
	public Calendar getBeginAvailableDateTime() {
		return beginAvailableDateTime;
	}

	/**
	 * 
	 * Sets the usage start date and time.
	 * @param beginAvailableDateTime Starting date and time of usage
	 * 
	 */
	public void setBeginAvailableDateTime(Calendar beginAvailableDateTime) {
		this.beginAvailableDateTime = beginAvailableDateTime;
	}

	/**
	 * 
	 * Returns the usage end date and time.
	 * @return End date and time of usage
	 * 
	 */
	public Calendar getEndAvailableDateTime() {
		return endAvailableDateTime;
	}

	/**
	 * 
	 * Sets the usage end date and time.
	 * @param endAvailableDateTime Ending date and time of usage
	 * 
	 */
	public void setEndAvailableDateTime(Calendar endAvailableDateTime) {
		this.endAvailableDateTime = endAvailableDateTime;
	}

	private String userId;
	private Calendar beginAvailableDateTime;
	private Calendar endAvailableDateTime;

	private static final long serialVersionUID = 6639017583733527280L;
}
