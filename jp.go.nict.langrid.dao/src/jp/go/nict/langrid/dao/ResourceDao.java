/*
 * $Id:ServiceDao.java 4384 2007-04-03 08:56:48Z nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2009 NICT Language Grid Project.
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

import jp.go.nict.langrid.dao.entity.Resource;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 214 $
 */
public interface ResourceDao {
	/**
	 * 
	 * 
	 */
	void clear()
	throws DaoException;

	/**
	 * 
	 * 
	 */
	public List<Resource> listAllResources(String resourceGridId)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	public List<Resource> listResourcesOfUser(String userGridId, String userId)
	throws DaoException;
	
	/**
	 * 
	 * 
	 */
	ResourceSearchResult searchResources(
			int startIndex, int maxCount
			, String resourceGridId, MatchingCondition[] conditions, Order[] orders)
		throws DaoException;

	/**
	 * 
	 * 
	 */
	void addResource(Resource resource)
	throws DaoException, ResourceAlreadyExistsException;

	/**
	 * 
	 * 
	 */
	void deleteResource(String resourceGridId, String resourceId)
	throws ResourceNotFoundException, DaoException;

	/**
	 * 
	 * 
	 */
	void deleteResourcesOfGrid(String gridId)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	void deleteResourcesOfUser(String userGridId, String userId)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	Resource getResource(String resourceGridId, String resourceId)
	throws ResourceNotFoundException, DaoException;

	/**
	 * 
	 * 
	 */
	boolean isResourceExist(String resourceGridId, String resourceId)
	throws DaoException;
}
