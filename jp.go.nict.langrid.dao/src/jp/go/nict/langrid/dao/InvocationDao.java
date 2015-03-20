/*
 * $Id: InvocationDao.java 388 2011-08-23 10:24:50Z t-nakaguchi $
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
package jp.go.nict.langrid.dao;

import java.util.List;

import jp.go.nict.langrid.dao.entity.Invocation;
import jp.go.nict.langrid.dao.entity.OperationRequest;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 388 $
 */
public interface InvocationDao {
	/**
	 * 
	 * 
	 */
	void clear()
	throws DaoException;
	
	/**
	 * 
	 * 
	 */
	void addInvocation(Invocation invocation)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	List<Invocation> listInvocation(String serviceGridId, String serviceId) throws DaoException;
	
	/**
	 * 
	 * 
	 */
	void deleteInvocation(String serviceGridId, String serviceId, String invocationName)
	throws DaoException;
}
