/*
 * $Id:UserDao.java 4384 2007-04-03 08:56:48Z nakaguchi $
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

import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.MatchingMethod;
import jp.go.nict.langrid.dao.Order;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 216 $
 */
public class CriteriaUtil {
	/**
	 * 
	 * 
	 */
	public static int getCount(Criteria criteria)
	throws HibernateException
	{
		criteria.setProjection(Projections.rowCount());
		return (Integer)criteria.uniqueResult();
	}

	/**
	 * 
	 * 
	 */
	public static List<?> getList(
			Criteria criteria
			, int startIndex, int maxCount
			, Order[] orders
			)
	throws HibernateException
	{
		addOrders(criteria, orders);
		criteria.setFirstResult(startIndex);
		criteria.setMaxResults(maxCount);
		return (List<?>)criteria.list();
	}

	/**
	 * 
	 * 
	 */
	public static void setFirstAndMax(
			Criteria criteria, int firstResult, int maxResults)
	{
		criteria.setFirstResult(firstResult);
		criteria.setMaxResults(maxResults);
	}

	/**
	 * 
	 * 
	 */
	public static void addOrders(Criteria criteria, Order[] orders){
		for(Order o : orders){
			switch(o.getDirection()){
				case ASCENDANT:
					criteria.addOrder(org.hibernate.criterion.Order.asc(
							o.getFieldName()).ignoreCase());
					break;
				case DESCENDANT:
					criteria.addOrder(org.hibernate.criterion.Order.desc(
							o.getFieldName()).ignoreCase());
					break;
			}
		}
	}

	public static void addMatchingConditions(
			Criteria criteria, MatchingCondition... conds){
		for(MatchingCondition c : conds){
			if(c.getMatchingValue() instanceof String){
				String value = c.getMatchingValue().toString();
				MatchMode mode = getMatchMode(c.getMatchingMethod());
				if(mode != null){
					criteria.add(
							Property.forName(c.getFieldName())
							.like(value, mode)
							);
				} else{
					criteria.add(
							Property.forName(c.getFieldName()).eq(value)
							);
				}
			} else{
				criteria.add(
						Property.forName(c.getFieldName())
						.eq(c.getMatchingValue())
						);
			}
		}
	}

	private static MatchMode getMatchMode(MatchingMethod method){
		switch(method){
			case COMPLETE:
				return MatchMode.EXACT;
			case LANGUAGEPATH:
			default:
				return null;
			case PARTIAL:
				return MatchMode.ANYWHERE;
			case PREFIX:
				return MatchMode.START;
			case SUFFIX:
				return MatchMode.END;
		}
	}
}
