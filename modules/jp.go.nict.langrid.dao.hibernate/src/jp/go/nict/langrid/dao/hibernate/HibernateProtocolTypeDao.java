/*
 * $Id:HibernateProtocolTypeDao.java 4384 2007-04-03 08:56:48Z nakaguchi $
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

import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.ProtocolAlreadyExistsException;
import jp.go.nict.langrid.dao.ProtocolDao;
import jp.go.nict.langrid.dao.ProtocolNotFoundException;
import jp.go.nict.langrid.dao.entity.Protocol;
import jp.go.nict.langrid.dao.entity.ServiceType;
import jp.go.nict.langrid.dao.hibernate.HibernateDao.DaoBlockR;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 389 $
 */
public class HibernateProtocolTypeDao
extends HibernateCRUDDao<Protocol>
implements ProtocolDao{
	/**
	 * 
	 * 
	 */
	public HibernateProtocolTypeDao(HibernateDaoContext context){
		super(context, Protocol.class);
	}

	@Override
	public void clear() throws DaoException {
		super.clear();
	}

	@Override
	public List<Protocol> listAllProtocols() throws DaoException {
		return list();
	}

	public void addProtocol(Protocol protocolType)
	throws ProtocolAlreadyExistsException, DaoException{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			String gid = protocolType.getProtocolId();
			Protocol current = (Protocol)session.get(Protocol.class, gid);
			if(current != null){
				getContext().commitTransaction();
				throw new ProtocolAlreadyExistsException(gid);
			} else{
				session.save(protocolType);
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

	public void deleteProtocol(String protocolTypeId)
	throws ProtocolNotFoundException, DaoException{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			Protocol current = (Protocol)session.get(Protocol.class, protocolTypeId);
			if(current == null){
				getContext().commitTransaction();
				throw new ProtocolNotFoundException(protocolTypeId);
			}
			session.delete(current);
			getContext().commitTransaction();
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public Protocol getProtocol(String protocolTypeId)
	throws ProtocolNotFoundException, DaoException {
		Session session = getSession();
		getContext().beginTransaction();
		try{
			Protocol current = (Protocol)session.get(Protocol.class, protocolTypeId);
			getContext().commitTransaction();
			if(current == null){
				throw new ProtocolNotFoundException(protocolTypeId);
			}
			return current;
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	@Override
	public boolean isProtocolExist(String protocolTypeId) throws DaoException {
		return exists(protocolTypeId);
	}
	
	@Override
	public List<Protocol> listAllProtocols(final String gridId) throws DaoException {
		return transact(new DaoBlockR<List<Protocol>>(){
			@Override
			@SuppressWarnings("unchecked")
			public List<Protocol> execute(Session session)
			throws DaoException {
				return (List<Protocol>)session.createCriteria(Protocol.class)
				.add(Property.forName("ownerUserGridId").eq(gridId))
				.addOrder(Order.asc("protocolName"))
				.list();
			}
		});
	}
}
