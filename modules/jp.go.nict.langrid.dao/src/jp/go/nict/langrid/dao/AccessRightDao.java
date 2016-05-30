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

import jp.go.nict.langrid.dao.entity.AccessRight;
import jp.go.nict.langrid.dao.entity.ServicePK;

/**
 * 
 * 
 * @author $Author:nakaguchi $
 * @version $Revision:4384 $
 */
public interface AccessRightDao
{
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
	List<AccessRight> listAccessRights(String serviceGridId)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	Iterable<ServicePK> listAccessibleServices(String userGridId, String userid)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	AccessRightSearchResult searchAccessRights(
			int startIndex, int maxCount
			, String userGridId, String userId
			, String serviceGridId, String[] serviceIds
			, Order[] orders)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	AccessRightSearchResult searchAccessRightsAccordingToDefaultAndOwner(
			int startIndex, int maxCount
			, String userGridId, String userId
			, String serviceGridId, String[] serviceIds, String ownerUserId
			, Order[] orders)
		throws DaoException;

	/**
	 * 
	 * 
	 */
	AccessRight getAccessRight(
			String userGridId, String userId
			, String serviceGridId, String serviceId)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	AccessRight setAccessRight(
			String userGridId, String userId
			, String serviceGridId, String serviceId
			, boolean permitted)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	void deleteAccessRight(
			String userGridId, String userId
			, String serviceGridId, String serviceId)
	throws AccessRightNotFoundException, DaoException;

	/**
	 * 
	 * 
	 */
	AccessRight getGridDefaultAccessRight(
			String userGridId, String serviceGridId, String serviceId)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	AccessRight setGridDefaultAccessRight(
			String userGridId, String serviceGridId, String serviceId
			, boolean permitted)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	void deleteGridDefaultAccessRight(
			String userGridId, String serviceGridId, String serviceId)
	throws AccessRightNotFoundException, DaoException;

	/**
	 * 
	 * 
	 */
	void adjustUserRights(
			String userGridId, String serviceGridId, String serviceId, String ownerUserId
			, boolean permitted)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	AccessRight getServiceDefaultAccessRight(String serviceGridId, String serviceId)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	AccessRight setServiceDefaultAccessRight(String serviceGridId, String serviceId, boolean permitted)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	void adjustGridDefaultRights(String serviceGridId, String serviceId, boolean permitted)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	void deleteAccessRightsOfGrid(String gridId)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	void deleteAccessRightsOfService(String serviceGridId, String serviceId)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	void deleteAccessRightsOfUser(String userGridId, String userId)
	throws DaoException;
}
