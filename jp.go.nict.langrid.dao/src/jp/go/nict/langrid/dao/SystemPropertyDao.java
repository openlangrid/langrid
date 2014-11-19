/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2011 NICT Language Grid Project.
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
package jp.go.nict.langrid.dao;

import java.util.List;

import jp.go.nict.langrid.dao.entity.SystemProperty;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public interface SystemPropertyDao {
	public static final String PASSWORD_EXPIRATION_DAYS = "passwordExpirationDays";

	/**
	 * 
	 * 
	 */
	void clear() throws DaoException;

	/**
	 * 
	 * 
	 */
	List<SystemProperty> listAllProperties(String gridId) throws DaoException;

	/**
	 * 
	 * 
	 */
	String getProperty(String gridId, String name)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	void setProperty(String gridId, String name, String value)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	void deletePropertiesOfGrid(String gridId)
	throws DaoException;
}
