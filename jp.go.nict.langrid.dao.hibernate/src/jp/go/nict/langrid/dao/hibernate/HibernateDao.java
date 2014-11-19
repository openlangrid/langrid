/*
 * $Id:HibernateDao.java 4384 2007-04-03 08:56:48Z nakaguchi $
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
package jp.go.nict.langrid.dao.hibernate;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.go.nict.langrid.dao.DaoException;

import org.hibernate.HibernateException;
import org.hibernate.JDBCException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * 
 * 
 * @author $Author:nakaguchi $
 * @version $Revision:4384 $
 */
public class HibernateDao{
	/**
	 * 
	 * 
	 */
	public HibernateDao(HibernateDaoContext context){
		this.context = context;
	}

	/**
	 * 
	 * 
	 */
	public HibernateDaoContext getContext(){
		return context;
	}

	protected Session getSession() throws HibernateException{
		return context.getSession();
	}

	protected void deleteEntities(final Class<?> entityClass) throws DaoException{
		transact(new DaoBlock() {
			@Override
			public void execute(Session session) throws DaoException {
				for(Object e : session.createCriteria(entityClass).list()){
					session.delete(e);
				}
			}
		});
	}

	protected interface DaoBlock{
		void execute(Session session) throws DaoException;
	}

	protected interface DaoBlockR<R>{
		R execute(Session session) throws DaoException;
	}

	protected void transact(DaoBlock block) throws DaoException{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			block.execute(session);
			getContext().commitTransaction();
		} catch(DaoException e){
			rollbackAtDaoException(e);
			throw e;
		} catch(RuntimeException e){
			rollbackAtRuntimeException(e);
			throw e;
		} catch(Error e){
			rollbackAtError(e);
			throw e;
		}
	}

	protected <T> T transact(DaoBlockR<T> block) throws DaoException{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			T ret = block.execute(session);
			getContext().commitTransaction();
			return ret;
		} catch(DaoException e){
			rollbackAtDaoException(e);
			throw e;
		} catch(RuntimeException e){
			rollbackAtRuntimeException(e);
			throw new DaoException(e);
		} catch(Error e){
			rollbackAtError(e);
			throw e;
		}
	}

	protected Query createDeleteQuery(Class<?> clazz)
	throws HibernateException{
		return createDeleteQuery(clazz, "");
	}

	protected Query createDeleteQuery(Class<?> clazz, String whereClause)
	throws HibernateException{
		return getSession().createQuery("delete from " + clazz.getName()
				+ " " + whereClause);
	}

	protected static void logAdditionalInfo(RuntimeException exception){
		if(exception instanceof JDBCException){
			SQLException e = ((JDBCException)exception).getSQLException();
			while(e != null){
				logger.log(Level.WARNING, "Next SQLException", e);
				e = e.getNextException();
			}
		}
	}

	protected static void logWhenRollbacking(Exception e){
		logger.log(Level.WARNING, "Exception when rollbacking transaction", e);
	}

	private void rollbackAtDaoException(DaoException e)
	throws DaoException{
		Throwable cause = e.getCause();
		if(cause instanceof RuntimeException){
			logAdditionalInfo((RuntimeException)cause);
		}
		try{
			getContext().rollbackTransaction();
		} catch(DaoException de){
			logWhenRollbacking(de);
		}
	}

	private void rollbackAtRuntimeException(RuntimeException e)
	throws DaoException{
		logAdditionalInfo(e);
		try{
			getContext().rollbackTransaction();
		} catch(DaoException de){
			logWhenRollbacking(de);
		}
	}

	private void rollbackAtError(Error e)
	throws DaoException{
		try{
			getContext().rollbackTransaction();
		} catch(Throwable th){
			// 
			// 
			th.printStackTrace();
		}
	}

	private HibernateDaoContext context;
	private static Logger logger = Logger.getLogger(
			HibernateDao.class.getName());
}
