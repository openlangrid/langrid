/*
 * $Id:HibernateDomainDao.java 4384 2007-04-03 08:56:48Z nakaguchi $
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
import jp.go.nict.langrid.dao.DomainAlreadyExistsException;
import jp.go.nict.langrid.dao.DomainDao;
import jp.go.nict.langrid.dao.DomainNotFoundException;
import jp.go.nict.langrid.dao.entity.Domain;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 485 $
 */
public class HibernateDomainDao
extends HibernateCRUDDao<Domain>
implements DomainDao{
	/**
	 * 
	 * 
	 */
	public HibernateDomainDao(HibernateDaoContext context){
		super(context, Domain.class);
	}

	@Override
	public void clear() throws DaoException {
		transact(new DaoBlock(){
			@Override
			public void execute(Session session) throws DaoException {
				for(Object g : session.createCriteria(Domain.class).list()){
					session.delete(g);
				}
			}
		});
	}

	public void addDomain(Domain domain)
	throws DomainAlreadyExistsException, DaoException{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			String gid = domain.getDomainId();
			Domain current = (Domain)session.get(Domain.class, gid);
			if(current != null){
				getContext().commitTransaction();
				throw new DomainAlreadyExistsException(gid);
			} else{
				session.save(domain);
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

	public void deleteDomain(String domainId)
	throws DomainNotFoundException, DaoException{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			Domain current = (Domain)session.get(Domain.class, domainId);
			if(current == null){
				getContext().commitTransaction();
				throw new DomainNotFoundException(domainId);
			}
			session.delete(current);
			getContext().commitTransaction();
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public Domain getDomain(String domainId)
	throws DomainNotFoundException, DaoException {
		Session session = getSession();
		getContext().beginTransaction();
		try{
			Domain current = (Domain)session.get(Domain.class, domainId);
			getContext().commitTransaction();
			if(current == null){
				throw new DomainNotFoundException(domainId);
			}
			return current;
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	@Override
	public boolean isDomainExist(String domainId) throws DaoException {
		return exists(domainId);
	}

	@Override
	public List<Domain> listAllDomains() throws DaoException {
		return list();
	}
	
	@Override
	public List<Domain> listAllDomains(final String gridId) throws DaoException {
		return transact(new DaoBlockR<List<Domain>>(){
			@Override
			@SuppressWarnings("unchecked")
			public List<Domain> execute(Session session)
			throws DaoException {
				return (List<Domain>)session.createCriteria(Domain.class)
				.add(Property.forName("ownerUserGridId").eq(gridId))
				.addOrder(Order.asc("domainName"))
				.list();
			}
		});
	}
}
