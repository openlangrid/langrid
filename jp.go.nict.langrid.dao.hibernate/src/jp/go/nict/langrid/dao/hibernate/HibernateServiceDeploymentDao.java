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

import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.ServiceDeploymentDao;
import jp.go.nict.langrid.dao.ServiceDeploymentNotFoundException;
import jp.go.nict.langrid.dao.entity.ServiceDeployment;
import jp.go.nict.langrid.dao.entity.ServiceDeploymentPK;

import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 216 $
 */
public class HibernateServiceDeploymentDao
extends HibernateDao
implements ServiceDeploymentDao
{
	/**
	 * 
	 * 
	 */
	public HibernateServiceDeploymentDao(HibernateDaoContext context){
		super(context);
	}

	public void clear() throws DaoException{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			session.createQuery("delete from ServiceDeployment").executeUpdate();
			getContext().commitTransaction();
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public ServiceDeployment getServiceDeployment(
			String serviceAndNodeGridId, String serviceId, String nodeId)
	throws DaoException, ServiceDeploymentNotFoundException{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			ServiceDeployment current = (ServiceDeployment)session.get(
					ServiceDeployment.class
					, new ServiceDeploymentPK(serviceAndNodeGridId, serviceId, nodeId));
			getContext().commitTransaction();
			if(current == null){
				throw new ServiceDeploymentNotFoundException(
						serviceAndNodeGridId, serviceId, nodeId);
			}
			return current;
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}
}
