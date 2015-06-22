/*
 * $Id: ConnectionParameters.java 240 2010-10-03 01:39:10Z t-nakaguchi $
 *
 * This is a program to wrap language resources as Web services.
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
package jp.go.nict.langrid.serviceexecutor.db;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 240 $
 */
public class ConnectionParameters {
	public String getJndiDataSourceName() {
		return jndiDataSourceName;
	}

	public void setJndiDataSourceName(String jndiDataSourceName) {
		this.jndiDataSourceName = jndiDataSourceName;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public int getMaxWait() {
		return maxWait;
	}

	public void setMaxWait(int maxWait) {
		this.maxWait = maxWait;
	}

	public int getMaxPSActive() {
		return maxPSActive;
	}

	public void setMaxPSActive(int maxPSActive) {
		this.maxPSActive = maxPSActive;
	}

	public String getDbDictionary() {
		return dbDictionary;
	}

	public void setDbDictionary(String dbDictionary) {
		this.dbDictionary = dbDictionary;
	}

	/**
	 * 
	 * 
	 */
	private String jndiDataSourceName;

	/**
	 * 
	 * 
	 */
	private String driverName;

	/**
	 * 
	 * 
	 */
	private String url;

	/**
	 * 
	 * 
	 */
	private String username;

	/**
	 * 
	 * 
	 */
	private String password;

	/**
	 * 
	 * 
	 */
	private int maxActive = 5;

	/**
	 * 
	 * 
	 */
	private int maxIdle = 3;

	/**
	 * 
	 * 
	 */
	private int maxWait = 5000;

	/**
	 * 
	 * 
	 */
	private int maxPSActive = -1;

	/**
	 * 
	 * 
	 */
	private String dbDictionary;
}
