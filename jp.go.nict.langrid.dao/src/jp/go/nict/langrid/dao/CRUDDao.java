/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2012 NICT Language Grid Project.
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
import java.util.List;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public interface CRUDDao<T>{
	void clear() throws DaoException;
	List<T> list() throws DaoException;
	boolean exists(Serializable id) throws DaoException;
	T get(Serializable id) throws EntityNotFoundException, DaoException;
	T getOrNull(Serializable id) throws DaoException;
	void add(T entity) throws EntityAlreadyExistsException, DaoException;
	void delete(T entity) throws DaoException;
	void delete(Serializable id) throws EntityNotFoundException, DaoException;
}
