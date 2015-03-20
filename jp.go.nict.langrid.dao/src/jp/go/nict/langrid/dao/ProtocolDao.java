/*
 * $Id: ProtocolDao.java 388 2011-08-23 10:24:50Z t-nakaguchi $
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
package jp.go.nict.langrid.dao;

import java.util.List;

import jp.go.nict.langrid.dao.entity.Protocol;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author:nakaguchi $
 * @version $Revision: 388 $
 */
public interface ProtocolDao{
	/**
	 * 
	 * 
	 */
	void clear() throws DaoException;

	/**
	 * 
	 * 
	 */
	public List<Protocol> listAllProtocols()
	throws DaoException;

	/**
	 * 
	 * 
	 */
	void addProtocol(Protocol protcol)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	void deleteProtocol(String protcolId)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	Protocol getProtocol(String protcolId)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	boolean isProtocolExist(String protcolId)
	throws DaoException;
	
	/**
	 * 
	 * 
	 */
	public List<Protocol> listAllProtocols(String gridId)
	throws DaoException;
}
