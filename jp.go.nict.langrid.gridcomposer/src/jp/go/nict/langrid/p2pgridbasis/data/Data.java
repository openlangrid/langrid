/*
 * $Id: Data.java 328 2010-12-08 05:43:18Z t-nakaguchi $
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
package jp.go.nict.langrid.p2pgridbasis.data;

import java.util.Calendar;

import jp.go.nict.langrid.commons.util.CalendarUtil;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 328 $
 */
public abstract class Data {
	private Calendar lastUpdate;
	private DataID id;
	private String gid;
	private DataAttributes attributes;
	public static final Calendar DEFAULT_DATE = CalendarUtil.create(2007, 1, 1);

	/**
	 * The constructor.
	 * @param gid Grid ID(set null to Domain/Grid/Federation)
	 * @param id Data ID
	 * @param lastUpdate Last updated datetime
	 * @param attributes Attributes
	 */
	public Data(String gid, DataID id, Calendar lastUpdate, DataAttributes attributes) {
		this.gid = gid;
		this.id = id;
		this.lastUpdate = lastUpdate;
		this.attributes = attributes;
	}

	/**
	 * 
	 * 
	 */
	public DataAttributes getAttributes() {
		return attributes;
	}

	/**
	 * 
	 * 
	 */
	public void setAttributes(DataAttributes attributes) {
		this.attributes = attributes;
	}

	/**
	 * 
	 * 
	 */
	public String getGridId() {
		return this.gid;
	}

	/**
	 * 
	 * 
	 */
	public void setGridId(String gid) {
		this.gid = gid;
	}

	/**
	 * 
	 * 
	 */
	public DataID getId() {
		return this.id;
	}

	/**
	 * 
	 * 
	 */
	public void setId(DataID id) {
		this.id = id;
	}

	/**
	 * 
	 * 
	 */
	public Calendar getLastUpdate() {
		return lastUpdate;
	}

	/**
	 * 
	 * 
	 */
	public void setLastUpdate(Calendar lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	/**
	 * 
	 * 
	 */
	abstract public String getType();
}
