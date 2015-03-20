/*
 * $Id: EntityNotFoundException.java 214 2010-10-02 14:32:38Z t-nakaguchi $
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

import java.io.Serializable;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 214 $
 */
public class EntityNotFoundException extends DaoException {
	/**
	 * 
	 * 
	 */
	public EntityNotFoundException(Class<?> entityClass, Serializable id) {
		super(toMessage(entityClass, id));
		this.id = id;
	}

	/**
	 * 
	 * 
	 */
	public EntityNotFoundException(Class<?> entityClass, Serializable id, Throwable cause){
		super(toMessage(entityClass, id), cause);
		this.id = id;
	}

	/**
	 * 
	 * 
	 */
	public Class<?> getEntityClass(){
		return entityClass;
	}

	/**
	 * 
	 * 
	 */
	public Serializable getGridId(){
		return id;
	}

	private Class<?> entityClass;
	private Serializable id;

	private static String toMessage(Class<?> entityClass, Serializable id){
		return entityClass.getSimpleName() + "(id:" + id + ") is not found.";
	}
	private static final long serialVersionUID = -7381944618530470258L;
}
