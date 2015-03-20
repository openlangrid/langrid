/*
 * $Id: EntityListener.java 214 2010-10-02 14:32:38Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
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
package jp.go.nict.langrid.dao;

import java.io.Serializable;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 214 $
 */
public interface EntityListener<T>{
	/**
	 * 
	 * 
	 */
	void onInsert(Serializable id, T entity);

	/**
	 * 
	 * 
	 */
	void onUpdate(Serializable id, T entity, String[] modifiedProperties);

	/**
	 * 
	 * 
	 */
	void onDelete(Serializable id, Class<T> clazz);

	/**
	 * 
	 * 
	 */
	void onCollectionEntityRecreate(
			Serializable ownerId, Class<T> ownerClass
			, Serializable entityId, Object entity
			);

	/**
	 * 
	 * 
	 */
	void onCollectionEntityUpdate(
			Serializable ownerId, Class<T> ownerClass
			, String collectionPropertyName
			, Serializable entityId, Object entity
			);

	/**
	 * 
	 * 
	 */
	void onCollectionEntityRemove(
			Serializable ownerId, Class<T> ownerClass
			, String collectionPropertyName
			, Serializable entityId, Class<?> entityClass
			);
}
