/*
 * $Id:DaoFactory.java 4384 2007-04-03 08:56:48Z nakaguchi $
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

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.commons.util.function.Functions.RunnableWithException;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 214 $
 */
public interface DaoContext {
	/**
	 * 
	 * 
	 */
	<T> void addEntityListener(Class<T> clazz, EntityListener<T> listener);

	/**
	 * 
	 * 
	 */
	<T> void removeEntityListener(Class<T> clazz, EntityListener<T> listener);

	/**
	 * 
	 * 
	 */
	void addTransactionListener(TransactionListener listener);

	/**
	 * 
	 * 
	 */
	void removeTransactionListener(TransactionListener listener);

	/**
	 * 
	 * 
	 */
	void beginTransaction() throws ConnectException, DaoException;

	/**
	 * 
	 * 
	 */
	void commitTransaction() throws DaoException;

	/**
	 * 
	 * 
	 */
	void rollbackTransaction() throws DaoException;

	/**
	 * 
	 * 
	 */
	int getTransactionNestCount();

	/**
	 * List key and updated datetime of elements that meets conditions and are ordered by date.
	 * @param entityClass
	 * @param conditions
	 * @return
	 * @throws DaoException
	 */
	List<Pair<Object, Calendar>> listAllIdAndUpdates(
			Class<?> entityClass,
			@SuppressWarnings("unchecked") Pair<String, String>... conditions)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	<T> T loadEntity(Class<T> clazz, Serializable id) throws DaoException;

	<T> boolean removeEntity(Class<T> clazz, Serializable id) throws DaoException;

	/**
	 * 
	 * 
	 */
	void mergeEntity(Object entity) throws DaoException;

	/**
	 * 
	 * 
	 */
	void updateEntity(Object entity) throws DaoException;

	/**
	 * 
	 * 
	 */
	void refreshEntity(Object entity) throws DaoException;

	/**
	 * 
	 * 
	 */
//	Blob createBlob(InputStream stream) throws IOException;

	/**
	 * 
	 * 
	 */
//	Clob createClob(String text) throws IOException;

	void transact(RunnableWithException<DaoException> c)
	throws DaoException;
}
