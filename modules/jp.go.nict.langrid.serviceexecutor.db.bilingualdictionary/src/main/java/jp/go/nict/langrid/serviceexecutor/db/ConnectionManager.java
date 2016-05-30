/*
 * $Id: ConnectionManager.java 240 2010-10-03 01:39:10Z t-nakaguchi $
 *
 * This is a program to wrap language resources as Web services.
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
package jp.go.nict.langrid.serviceexecutor.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 240 $
 */
public class ConnectionManager {
	public ConnectionManager(ConnectionParameters params)
	throws NamingException
	{
		if(params.getJndiDataSourceName() != null){
			initWithJNDI("java:comp/env/" + params.getJndiDataSourceName());
		} else{
			initWithBasicDataSource(
					params.getDriverName(), params.getUrl(), params.getUsername(), params.getPassword()
					, params.getMaxActive(), params.getMaxIdle(), params.getMaxWait()
					, params.getMaxPSActive()
					);
		}
	}

	/**
	 * 
	 * 
	 */
	public synchronized Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

	private void initWithJNDI(String jndiName)
	throws NamingException{
		this.dataSource = (DataSource)new InitialContext(
				System.getProperties()).lookup(jndiName);
	}

	private void initWithBasicDataSource(
			String driverClassName, String connectionUrl
			, String userName, String password
			, int maxActive, int maxIdle, int maxWait
			, int maxPSActive)
	{
		BasicDataSource bds = new BasicDataSource();
		bds.setDriverClassName(driverClassName);
		bds.setUrl(connectionUrl);
		bds.setUsername(userName);
		bds.setPassword(password);
		bds.setMaxActive(maxActive);
		bds.setMaxIdle(maxIdle);
		bds.setMaxWait(maxWait);
		if(maxPSActive != -1){
			bds.setMaxOpenPreparedStatements(maxPSActive);
		}
		this.dataSource = bds;
	}

	private DataSource dataSource;
}
