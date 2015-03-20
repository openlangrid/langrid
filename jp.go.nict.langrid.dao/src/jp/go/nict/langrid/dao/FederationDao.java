/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2010 NICT Language Grid Project.
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

import jp.go.nict.langrid.dao.entity.Federation;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public interface FederationDao{
	/**
	 * 
	 * 
	 */
	void clear() throws DaoException;

	/**
	 * 
	 * 
	 */
	public List<Federation> list() throws DaoException;

	/**
	 * 
	 * 
	 */
	public List<String> listTargetGridIds(String sourceGridId)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	List<String> listSourceGridIds(String targetGridId)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	List<Federation> listFederationsFrom(String sourceGridId)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	List<Federation> listFederationsToward(String targetGridId)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	boolean isFederationExist(String sourceGridId, String targetGridId)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	Federation getFederation(String sourceGridId, String targetGridId)
	throws FederationNotFoundException, DaoException;

	/**
	 * 
	 * 
	 */
	void addFederation(String sourceGridId, String targetGridId)
	throws FederationAlreadyExistsException, DaoException;

	/**
	 * 
	 * 
	 */
	void addFederation(Federation federation)
	throws FederationAlreadyExistsException, DaoException;

	/**
	 * 
	 * 
	 */
	void deleteFederation(String sourceGridId, String targetGridId)
	throws FederationNotFoundException, DaoException;

	/**
	 * 
	 * 
	 */
	void deleteFederationsOf(String gridId)
	throws DaoException;
	
	/**
	 * 
	 * 
	 */
	void setRequesting(String sourceGridId, String targetGridId, boolean isRequesting)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	void setConnected(String sourceGridId, String targetGridId, boolean isRequesting)
	throws DaoException;
}
