/*
 * $Id: HibernateCRUDDao.java 485 2012-05-24 02:38:25Z t-nakaguchi $
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
package jp.go.nict.langrid.dao.hibernate;

import java.io.Serializable;
import java.util.List;

import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.EntityAlreadyExistsException;
import jp.go.nict.langrid.dao.EntityNotFoundException;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 485 $
 */
public class HibernateCRUDDao<T> extends HibernateDao{
	/**
	 * 
	 * 
	 */
	public HibernateCRUDDao(HibernateDaoContext context, Class<T> entityClass){
		super(context);
		this.entityClass = entityClass;
	}

	public void clear() throws DaoException{
		transact(new DaoBlock(){
			public void execute(Session session) throws DaoException {
				createDeleteQuery(session, "").executeUpdate();	
			}
		});
	}

	public List<T> list()
	throws DaoException{
		return transact(new DaoBlockR<List<T>>(){
			@SuppressWarnings("unchecked")
			public List<T> execute(Session session) throws DaoException {
				return session.createCriteria(entityClass).list();
			}
		});
	}

	public boolean exists(final Serializable id)
	throws DaoException{
		return transact(new DaoBlockR<Boolean>(){
			public Boolean execute(Session session) throws DaoException {
				return session.get(entityClass, id) != null;
			}
		});
	}

	public void add(final T entity)
	throws DaoException{
		transact(new DaoBlock() {
			public void execute(Session session) throws DaoException {
				session.save(entity);
			}
		});
	}

	public T get(final Serializable id)
	throws EntityNotFoundException, DaoException{
		return transact(new DaoBlockR<T>(){
			@SuppressWarnings("unchecked")
			public T execute(Session session) throws DaoException {
				T entity = (T)session.get(entityClass, id);
				if(entity == null){
					throw new EntityNotFoundException(entityClass, id);
				}
				return entity;
			}
		});
	}

	public T getOrNull(final Serializable id)
	throws EntityNotFoundException, DaoException{
		return transact(new DaoBlockR<T>(){
			@SuppressWarnings("unchecked")
			public T execute(Session session) throws DaoException {
				return (T)session.get(entityClass, id);
			}
		});
	}

	public void delete(final T entity)
	throws DaoException{
		transact(new DaoBlock() {
			public void execute(Session session) throws DaoException {
				session.delete(entity);
			}
		});
	}

	public void delete(final Serializable id)
	throws EntityNotFoundException, DaoException{
		transact(new DaoBlock() {
			public void execute(Session session) throws DaoException {
				Object entity = session.get(entityClass, id);
				if(entity == null){
					throw new EntityNotFoundException(entityClass, id);
				}
				session.delete(entity);
			}
		});
	}

	protected void clearEach() throws DaoException{
		transact(new DaoBlock(){
			public void execute(Session session) throws DaoException {
				for(Object o : session.createCriteria(entityClass).list()){
					session.delete(o);	
				}
			}
		});
	}

	protected void add(final Serializable id, final T entity)
	throws EntityAlreadyExistsException, DaoException{
		transact(new DaoBlock() {
			public void execute(Session session) throws DaoException {
				if(session.get(entityClass, id) != null){
					throw new EntityAlreadyExistsException(entityClass, id);
				}
				session.save(entity);
			}
		});
	}

	protected void update(final T detachedEntity)
	throws DaoException{
		transact(new DaoBlock() {
			public void execute(Session session) throws DaoException {
				session.update(detachedEntity);
			}
		});
	}

	protected Criteria createCriteria(Session session) throws HibernateException{
		return session.createCriteria(entityClass);
	}

	protected Query createDeleteQuery(Session session, String additional) throws HibernateException{
		return session.createQuery("delete from " + entityClass.getName() + " " + additional);
	}

	private Class<T> entityClass;
}
