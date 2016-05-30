/*
 * $Id: GenericHandler.java 214 2010-10-02 14:32:38Z t-nakaguchi $
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import jp.go.nict.langrid.commons.util.Pair;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 214 $
 */
public abstract class GenericHandler<T> implements EntityListener<T>, TransactionListener{
	public void onInsert(Serializable id, T entity) {
		updatedAndRemovedIds.get().getFirst().add(id);
	}

	public void onUpdate(Serializable id, T entity, String[] modifiedProperties) {
		updatedAndRemovedIds.get().getFirst().add(id);
		Map<Serializable, Set<String>> id2Props = updatedProperties.get();
		Set<String> props = id2Props.get(id);
		if(props == null){
			props = new HashSet<String>();
			id2Props.put(id, props);

		}
		for(String p : modifiedProperties){
			props.add(p);
		}
	}

	public void onDelete(Serializable id, Class<T> clazz){
		updatedAndRemovedIds.get().getSecond().add(id);
	}

	public void onCollectionEntityRecreate(
			Serializable ownerId, Class<T> ownerClass
			, Serializable entityId, Object entity) {
		updatedAndRemovedIds.get().getFirst().add(ownerId);
	}

	public void onCollectionEntityUpdate(
			Serializable ownerId, Class<T> ownerClass
			, String collectionPropertyName
			, Serializable entityId, Object entity) {
		updatedAndRemovedIds.get().getFirst().add(ownerId);
		Map<Serializable, Set<String>> id2Props = updatedProperties.get();
		Set<String> props = id2Props.get(ownerId);
		if(props == null){
			props = new HashSet<String>();
			id2Props.put(ownerId, props);

		}
		props.add(collectionPropertyName);
	}

	public void onCollectionEntityRemove(
			Serializable ownerId, Class<T> ownerClass
			, String collectionPropertyName
			, Serializable entityId, Class<?> entityClass) {
		updatedAndRemovedIds.get().getFirst().add(ownerId);
	}

	public void onBeginTransaction() {
	}

	public void onCommitTransaction() {
		Pair<Set<Serializable>, Set<Serializable>> ids = updatedAndRemovedIds.get();
		Set<Serializable> updatedEntityIds = ids.getFirst();
		Set<Serializable> removedEntityIds = ids.getSecond();
		Map<Serializable, Set<String>> id2Props = updatedProperties.get();
		if(updatedEntityIds.size() == 0 && removedEntityIds.size() == 0) return;

		Set<Serializable> u = new LinkedHashSet<Serializable>(updatedEntityIds);
		Set<Serializable> r = new LinkedHashSet<Serializable>(removedEntityIds);
		Map<Serializable, Set<String>> p = new HashMap<Serializable, Set<String>>(id2Props);
		updatedEntityIds.clear();
		removedEntityIds.clear();
		id2Props.clear();

		boolean start = onNotificationStart();
		if(!start) return;
		try{
			for(Serializable id : r){
				doRemove(id);
				u.remove(id);
			}
			Set<String> empty = new HashSet<String>();
			for(Serializable id : u){
				Set<String> props = p.get(id);
				if(props == null) props = empty;
				doUpdate(id, props);
			}
		} finally{
			onNotificationEnd();
		}
	}

	public void onRollbackTransaction() {
		Pair<Set<Serializable>, Set<Serializable>> ids = updatedAndRemovedIds.get();
		ids.getFirst().clear();
		ids.getSecond().clear();
		updatedProperties.get().clear();
	}

	protected abstract boolean onNotificationStart();
	protected abstract void doUpdate(Serializable id, Set<String> updatedProperties);
	protected abstract void doRemove(Serializable id);
	protected abstract void onNotificationEnd();

	private ThreadLocal<Pair<Set<Serializable>, Set<Serializable>>> updatedAndRemovedIds
			= new ThreadLocal<Pair<Set<Serializable>, Set<Serializable>>>(){
				protected Pair<Set<Serializable>, Set<Serializable>> initialValue() {
					return new Pair<Set<Serializable>, Set<Serializable>>(
							new LinkedHashSet<Serializable>(), new LinkedHashSet<Serializable>());
				}
			};
	private ThreadLocal<Map<Serializable, Set<String>>> updatedProperties
			= new ThreadLocal<Map<Serializable, Set<String>>>(){
				protected Map<Serializable, Set<String>> initialValue(){
					return new HashMap<Serializable, Set<String>>();
				}
			};
}
