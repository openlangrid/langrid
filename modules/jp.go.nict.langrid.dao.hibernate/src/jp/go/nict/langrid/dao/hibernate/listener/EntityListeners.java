/*
 * $Id:HibernateDao.java 4384 2007-04-03 08:56:48Z nakaguchi $
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
package jp.go.nict.langrid.dao.hibernate.listener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import jp.go.nict.langrid.dao.EntityListener;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 216 $
 */
public class EntityListeners {
	/**
	 * 
	 * 
	 */
	public synchronized <T> void add(Class<T> clazz, EntityListener<T> listener){
		Collection<EntityListener<?>> collection = get(clazz);;
		collection.add(listener);
	}

	/**
	 * 
	 * 
	 */
	public synchronized <T> void remove(Class<T> clazz, EntityListener<T> listener){
		Collection<EntityListener<?>> collection = get(clazz);
		collection.remove(listener);
		if(collection.size() == 0){
			listeners.remove(clazz);
		}
	}

	/**
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void fireInsert(Serializable id, Object entity){
		Collection<EntityListener<?>> copy = getCopy(entity.getClass());
		if(copy == null) return;
		for(EntityListener<?> l : copy){
			((EntityListener<Object>)l).onInsert(id, entity);
		}
	}

	/**
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void fireUpdate(Serializable id, Object entity, String[] modifiedProperties){
		Collection<EntityListener<?>> copy = getCopy(entity.getClass());
		if(copy == null) return;
		for(EntityListener<?> l : copy){
			((EntityListener<Object>)l).onUpdate(id, entity, modifiedProperties);
		}
	}

	/**
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void fireDelete(Serializable id, Class<?> clazz){
		Collection<EntityListener<?>> copy = getCopy(clazz);
		if(copy == null) return;
		for(EntityListener<?> l : copy){
			((EntityListener<Object>)l).onDelete(id, (Class<Object>)clazz);
		}
	}

	/**
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void fireCollectionEntityRecreate(
			Serializable ownerId, Class<?> ownerClass
			, Serializable entityId, Object entity){
		Collection<EntityListener<?>> copy = getCopy(ownerClass);
		if(copy == null) return;
		for(EntityListener<?> l : copy){
			((EntityListener<Object>)l).onCollectionEntityRecreate(
					ownerId, (Class<Object>)ownerClass
					, entityId, entity);
		}
	}

	/**
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void fireCollectionEntityUpdate(Serializable ownerId, Class<?> ownerClass
			, String collectionPropertyName
			, Serializable entityId, Object entity){
		Collection<EntityListener<?>> copy = getCopy(ownerClass);
		if(copy == null) return;
		for(EntityListener<?> l : copy){
			((EntityListener<Object>)l).onCollectionEntityUpdate(
					ownerId, (Class<Object>)ownerClass
					, collectionPropertyName, entityId, entity);
		}
	}

	/**
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void fireCollectionEntityRemove(Serializable ownerId, Class<?> ownerClass
			, String collectionPropertyName
			, Serializable entityId, Class<?> entityClass){
		Collection<EntityListener<?>> copy = getCopy(ownerClass);
		if(copy == null) return;
		for(EntityListener<?> l : copy){
			((EntityListener<Object>)l).onCollectionEntityRemove(
					ownerId, (Class<Object>)ownerClass
					, collectionPropertyName, entityId, entityClass);
		}
	}

	private synchronized Collection<EntityListener<?>> get(Class<?> clazz){
		Collection<EntityListener<?>> collection = listeners.get(clazz);
		if(collection == null){
			collection = new ArrayList<EntityListener<?>>();
			listeners.put(clazz, collection);
		}
		return collection;
	}

	private synchronized Collection<EntityListener<?>> getCopy(Class<?> clazz){
		Collection<EntityListener<?>> collection = listeners.get(clazz);
		if(collection == null) return null;
		else return new ArrayList<EntityListener<?>>(collection);
	}

	private Map<Class<?>, Collection<EntityListener<?>>> listeners
			= Collections.synchronizedMap(
					new HashMap<Class<?>, Collection<EntityListener<?>>>()
					);
}
