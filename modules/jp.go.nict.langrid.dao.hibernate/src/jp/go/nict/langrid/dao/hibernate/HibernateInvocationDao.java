/*
 * $Id:HibernateUserDao.java 4384 2007-04-03 08:56:48Z nakaguchi $
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
import jp.go.nict.langrid.dao.InvocationDao;
import jp.go.nict.langrid.dao.InvocationNotFoundException;
import jp.go.nict.langrid.dao.entity.Invocation;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Property;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 389 $
 */
public class HibernateInvocationDao
extends HibernateDao
implements InvocationDao
{
	
	@Override
	public void addInvocation(Invocation invocation) throws DaoException {
		Session session = getSession();
		getContext().beginTransaction();
		try {
			session.save(invocation);
			getContext().commitTransaction();
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	@Override
	public void deleteInvocation(String serviceGridId, String serviceId, String invocationName)
	throws DaoException {
		Session session = getSession();
		getContext().beginTransaction();
		try{
			for(Invocation i : listInvocation(serviceGridId, serviceId)) {
				if(i.getInvocationName().equals(invocationName)){
					session.delete(i);
					getContext().commitTransaction();
					return;
				}
			}
			throw new InvocationNotFoundException(serviceGridId, serviceId, invocationName);
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}
	@Override
	@SuppressWarnings("unchecked")
	public List<Invocation> listInvocation(String serviceGridId, String serviceId)
	throws DaoException {
		Session session = getSession();
		getContext().beginTransaction();
		try{
			List<Invocation> list = (List<Invocation>)
			session.createCriteria(Invocation.class)
				.add(Property.forName("ownerServiceGridId").eq(serviceGridId))
				.add(Property.forName("ownerServiceId").eq(serviceId))
				.list();
			getContext().commitTransaction();
			return list;
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}
	
	/**
	 * 
	 * 
	 */
	public HibernateInvocationDao(HibernateDaoContext context){
		super(context);
	}

	public void clear() throws DaoException{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			session.createQuery("delete from OperationRequest").executeUpdate();
			getContext().commitTransaction();
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}
}
