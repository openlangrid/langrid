/*
 * $Id: EntityAlreadyExistsException.java 399 2011-08-24 04:02:04Z t-nakaguchi $
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
package jp.go.nict.langrid.servicecontainer.executor.umbrella.dao;

import java.io.Serializable;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 399 $
 */
public class EntityAlreadyExistsException extends DaoException {
	/**
	 * 
	 * 
	 */
	public EntityAlreadyExistsException(Class<?> entityClass, Serializable id) {
		super(toMessage(entityClass, id));
		this.entityClass = entityClass;
		this.id = id;
	}

	/**
	 * 
	 * 
	 */
	public EntityAlreadyExistsException(Class<?> entityClass, Serializable id, Throwable cause){
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
		return entityClass.getSimpleName() + "(id:" + id + ") is already exists.";
	}
	private static final long serialVersionUID = 843536883751837216L;
}
