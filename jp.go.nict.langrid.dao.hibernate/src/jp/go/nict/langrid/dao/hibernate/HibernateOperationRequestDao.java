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
import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.OperationRequestDao;
import jp.go.nict.langrid.dao.OperationRequestNotFoundException;
import jp.go.nict.langrid.dao.OperationRequestSearchResult;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.OrderDirection;
import jp.go.nict.langrid.dao.entity.OperationRequest;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Property;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 216 $
 */
public class HibernateOperationRequestDao
extends HibernateDao
implements OperationRequestDao
{
	/**
	 * 
	 * 
	 */
	public HibernateOperationRequestDao(HibernateDaoContext context){
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

	@SuppressWarnings("unchecked")
	public List<OperationRequest> listOperationRequest(String operationGridId)
	throws DaoException
	{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			List<OperationRequest> list = (List<OperationRequest>)
			session.createCriteria(OperationRequest.class)
				.add(Property.forName("gridId").eq(operationGridId))
				.list();
			getContext().commitTransaction();
			return list;
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public OperationRequestSearchResult searchOperationRequsests(
			int startIndex, int maxCount, MatchingCondition[] conditions, Order[] orders)
	throws DaoException
	{
		if(orders.length == 0){
			orders = new Order[]{new Order(
					"updatedDateTime", OrderDirection.DESCENDANT
					)};
		}

		Session session = getSession();
		getContext().beginTransaction();
		try{
			Criteria c = session.createCriteria(OperationRequest.class);
			CriteriaUtil.addMatchingConditions(c, conditions);

			List<OperationRequest> elements = (List<OperationRequest>)CriteriaUtil.getList(
					c, startIndex, maxCount, orders);
			int totalCount = 0;
			if(elements.size() < maxCount){
				totalCount = elements.size() + startIndex;
			} else{
				Criteria cr = session.createCriteria(OperationRequest.class);
				totalCount = CriteriaUtil.getCount(cr);
			}
			OperationRequestSearchResult r = new OperationRequestSearchResult(
					elements.toArray(new OperationRequest[]{}), totalCount, true
					);
			getContext().commitTransaction();
			return r;
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

	public void addOperationRequest(OperationRequest operation) throws DaoException {
		Session session = getSession();
		getContext().beginTransaction();
		try{
			session.save(operation);
			operation.setNodeLocalId(operation.getId());
			getContext().commitTransaction();
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	@Override
	public void deleteOperationRequest(String operationGridId, int operationId)
	throws DaoException
	{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			for(OperationRequest ro : listOperationRequest(operationGridId)) {
				if(ro.getId() == operationId){
					session.delete(ro);
					getContext().commitTransaction();
					return;
				}
			}
			throw new OperationRequestNotFoundException(operationGridId, operationId);
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	@Override
	public void deleteAllOperationRequest(String operationGridId) throws DaoException {
		Session session = getSession();
		getContext().beginTransaction();
		try{
			for(OperationRequest ro : listOperationRequest(operationGridId)) {
				session.delete(ro);
			}
			getContext().commitTransaction();
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}
}
