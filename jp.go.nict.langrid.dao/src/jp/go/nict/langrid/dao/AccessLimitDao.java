/*
 * $Id:AccessRightDao.java 4384 2007-04-03 08:56:48Z nakaguchi $
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
package jp.go.nict.langrid.dao;

import java.util.List;

import jp.go.nict.langrid.dao.entity.AccessLimit;
import jp.go.nict.langrid.dao.entity.LimitType;
import jp.go.nict.langrid.dao.entity.Period;

/**
 * 
 * 
 * @author $Author:nakaguchi $
 * @version $Revision:4384 $
 */
public interface AccessLimitDao{
	/**
	 * 
	 * 
	 */
	void clear() throws DaoException;

	/**
	 * 
	 * 
	 */
	void clearExceptDefaults() throws DaoException;

	/**
	 * 
	 * 
	 */
	public List<AccessLimit> listAccessLimits(String serviceGridId)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	AccessLimitSearchResult searchAccessLimits(
			int startIndex, int maxCount
			, String userGridId, String userId
			, String serviceGridId, String[] serviceIds
			, Order[] orders
			)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	AccessLimit getAccessLimit(
			String userGridId, String userId
			, String serviceGridId, String serviceId, Period period
			, LimitType limitType)
	throws AccessLimitNotFoundException, DaoException;

	/**
	 * 
	 * 
	 */
	public List<AccessLimit> getAccessLimits(
			String userGridId, String userId, String serviceGridId, String serviceId)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	AccessLimit setAccessLimit(
			String userGridId, String userId, String serviceGridId, String serviceId
			, Period period, LimitType limitType, int limitCount
			)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	public AccessLimit getServiceDefaultAccessLimit(
			String userGridId, String serviceGridId, String serviceId
			, Period period, LimitType limitType)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	public List<AccessLimit> getServiceDefaultAccessLimits(
			String userGridId, String serviceGridId, String serviceId)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	void setServiceDefaultAccessLimit(
			String userGridId, String serviceGridId, String serviceId, Period period
			, LimitType limitType, int limit
			)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	void deleteAccessLimit(String userGridId, String userId
			, String serviceGridId, String serviceId, Period period
			, LimitType limitType)
	throws AccessLimitNotFoundException, DaoException;

	/**
	 * 
	 * 
	 */
	void deleteAccessLimits(String userGridId, String userId
			, String serviceGridId, String serviceId)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	void deleteAccessLimitsOfGrid(String gridId)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	void deleteAccessLimitsOfService(String serviceGridId, String serviceId)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	void deleteAccessLimitsOfUser(String userGridId, String userId)
	throws DaoException;
}
