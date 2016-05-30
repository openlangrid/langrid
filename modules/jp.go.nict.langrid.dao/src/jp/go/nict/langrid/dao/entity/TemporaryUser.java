/*
 * $Id:User.java 4384 2007-04-03 08:56:48Z nakaguchi $
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
import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 388 $
 */
@Entity
@IdClass(TemporaryUserPK.class)
@NamedQueries({
	@NamedQuery(
			name="TemporaryUser.isParent"
			, query="select count(*) from TemporaryUser" +
			" where gridId=:gridId and parentUserId=:parentUserId and userId=:userId"
	)
})
public class TemporaryUser
extends UpdateManagedEntity
implements Serializable
{
	/**
	 * 
	 * 
	 */
	public TemporaryUser() {
	}

	/**
	 * 
	 * 
	 */
	public TemporaryUser(String gridId, String userId, String password, String parentUserId){
		this.gridId = gridId;
		this.userId = userId;
		this.password = password;
		this.parentUserId = parentUserId;
	}

	/**
	 * 
	 * 
	 */
	public TemporaryUser(String gridId, String userId, String password, String parentUserId,
			Calendar beginAvailableDateTime, Calendar endAvailableDateTime
			) {
		this.gridId = gridId;
		this.userId = userId;
		this.password = password;
		this.parentUserId = parentUserId;
		this.beginAvailableDateTime = beginAvailableDateTime;
		this.endAvailableDateTime = endAvailableDateTime;
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
	public String getUserId() {
		return userId;
	}

	/**
	 * 
	 * 
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * 
	 * 
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 
	 * 
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 
	 * 
	 */
	public String getParentUserId() {
		return parentUserId;
	}

	/**
	 * 
	 * 
	 */
	public void setParentUserId(String parentUserId) {
		this.parentUserId = parentUserId;
	}

	/**
	 * 
	 * 
	 */
	public Calendar getBeginAvailableDateTime() {
		return beginAvailableDateTime;
	}

	/**
	 * 
	 * 
	 */
	public void setBeginAvailableDateTime(Calendar beginAvailableDateTime) {
		this.beginAvailableDateTime = beginAvailableDateTime;
	}

	/**
	 * 
	 * 
	 */
	public Calendar getEndAvailableDateTime() {
		return endAvailableDateTime;
	}

	/**
	 * 
	 * 
	 */
	public void setEndAvailableDateTime(Calendar endAvailableDateTime) {
		this.endAvailableDateTime = endAvailableDateTime;
	}

	@Id
	private String gridId;
	@Id
	private String userId;
	private String password;
	private String parentUserId;
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar beginAvailableDateTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar endAvailableDateTime;

	private static final long serialVersionUID = 217691167444074054L;
}
