/*
 * $Id:HibernateFederationDao.java 4384 2007-04-03 08:56:48Z nakaguchi $
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

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;

import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.EntityNotFoundException;
import jp.go.nict.langrid.dao.FederationAlreadyExistsException;
import jp.go.nict.langrid.dao.FederationDao;
import jp.go.nict.langrid.dao.FederationNotFoundException;
import jp.go.nict.langrid.dao.entity.Federation;
import jp.go.nict.langrid.dao.entity.FederationPK;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 389 $
 */
public class HibernateFederationDao
extends HibernateCRUDDao<Federation>
implements FederationDao{
	/**
	 * 
	 * 
	 */
	public HibernateFederationDao(HibernateDaoContext context){
		super(context, Federation.class);
	}

	@Override
	public void clear() throws DaoException {
		super.clear();
	}

	@Override
	public List<Federation> list() throws DaoException {
		return super.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Federation> listFromOldest() throws DaoException{
		return transact(session -> {
				return (List<Federation>)session.createCriteria(Federation.class)
						.addOrder(Order.asc("createdDateTime"))
						.list();
		});

	}

	@Override
	public void deleteFederationsOf(final String gridId) throws DaoException {
		transact(new DaoBlock(){
			@Override
			public void execute(Session session) throws DaoException {
				session.createQuery("delete from Federation where sourceGridId=:gridId")
						.setString("gridId", gridId)
						.executeUpdate();
				session.createQuery("delete from Federation where targetGridId=:gridId")
						.setString("gridId", gridId)
						.executeUpdate();
			}
		});
	}

	public List<String> listSourceGridIds(final String targetGridId) throws DaoException {
		return transact(new DaoBlockR<List<String>>(){
			@SuppressWarnings("unchecked")
			public List<String> execute(Session session) throws DaoException {
				return createCriteria(session)
						.add(Property.forName("targetGridId").eq(targetGridId))
						.setProjection( Projections.property("sourceGridId"))
						.list();
			}
		});
	}

	@Override
	public List<String> listTargetGridIds(final String sourceGridId) throws DaoException {
		return transact(new DaoBlockR<List<String>>(){
			@SuppressWarnings("unchecked")
			public List<String> execute(Session session) throws DaoException {
				return createCriteria(session)
						.add(Property.forName("sourceGridId").eq(sourceGridId))
						.setProjection( Projections.property("targetGridId"))
						.list();
			}
		});
	}

	@Override
	public List<Federation> listFederationsFrom(final String sourceGridId)
	throws DaoException {
		return transact(new DaoBlockR<List<Federation>>(){
			@SuppressWarnings("unchecked")
			public List<Federation> execute(Session session) throws DaoException {
				return createCriteria(session)
						.add(Property.forName("sourceGridId").eq(sourceGridId))
						.list();
			}
		});
	}

	@Override
	public List<Federation> listFederationsToward(final String targetGridId)
	throws DaoException {
		return transact(new DaoBlockR<List<Federation>>(){
			@SuppressWarnings("unchecked")
			public List<Federation> execute(Session session) throws DaoException {
				return createCriteria(session)
						.add(Property.forName("targetGridId").eq(targetGridId))
						.list();
			}
		});
	}

	@Override
	public boolean isFederationExist(String sourceGridId, String targetGridId) throws DaoException {
		return exists(new FederationPK(sourceGridId, targetGridId));
	}

	@Override
	public Federation getFederation(String sourceGridId, String targetGridId)
	throws FederationNotFoundException, DaoException {
		try{
			return super.get(new FederationPK(sourceGridId, targetGridId));
		} catch(EntityNotFoundException e){
			throw new FederationNotFoundException(sourceGridId, targetGridId);
		}
	}

	public void addFederation(String sourceGridId, String targetGridId)
	throws FederationAlreadyExistsException, DaoException {
		addFederation(new Federation(sourceGridId, targetGridId));
	}
	
	@Override
	public void addFederation(Federation federation)
	throws FederationAlreadyExistsException, DaoException{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			if(isFederationExist(federation.getSourceGridId(), federation.getTargetGridId())){
				getContext().commitTransaction();
				throw new FederationAlreadyExistsException(
						federation.getSourceGridId(), federation.getTargetGridId());
			} else{
				session.save(federation);
				getContext().commitTransaction();
			}
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		} catch(RuntimeException e){
			getContext().rollbackTransaction();
			throw new DaoException(e);
		} catch(Error e){
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public void deleteFederation(String sourceGridId, String targetGridId)
	throws FederationNotFoundException, DaoException {
		Session session = getSession();
		getContext().beginTransaction();
		try{
			Federation current = (Federation)session.get(Federation.class, new FederationPK(sourceGridId, targetGridId));
			if(current == null){
				getContext().commitTransaction();
				throw new FederationNotFoundException(sourceGridId, targetGridId);
			}
			session.delete(current);
			getContext().commitTransaction();
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}
	
	@Override
	public void setRequesting(String sourceGridId, String targetGridId, boolean isRequesting)
	throws DaoException {
	   Session session = getSession();
	   getContext().beginTransaction();
	   try{
	      Federation current = (Federation)session.get(Federation.class, new FederationPK(sourceGridId, targetGridId));
	      if(current == null){
	         getContext().commitTransaction();
	         throw new FederationNotFoundException(sourceGridId, targetGridId);
	      }
	      current.setRequesting(isRequesting);
	      getContext().commitTransaction();
	   } catch(HibernateException e){
	      logAdditionalInfo(e);
	      getContext().rollbackTransaction();
	      throw new DaoException(e);
	   }
	}

	@Override
	public void setConnected(String sourceGridId, String targetGridId, boolean isConnected)
	throws DaoException 
	{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			Federation current = (Federation)session.get(Federation.class, new FederationPK(sourceGridId, targetGridId));
			if(current == null){
				getContext().commitTransaction();
				throw new FederationNotFoundException(sourceGridId, targetGridId);
			}
			current.setConnected(isConnected);
			getContext().commitTransaction();
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
		
	}
}
