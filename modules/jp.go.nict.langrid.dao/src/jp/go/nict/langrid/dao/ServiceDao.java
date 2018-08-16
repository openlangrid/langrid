/*
 * $Id: ServiceDao.java 388 2011-08-23 10:24:50Z t-nakaguchi $
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

import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

import jp.go.nict.langrid.dao.entity.BPELService;
import jp.go.nict.langrid.dao.entity.Service;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 388 $
 */
public interface ServiceDao {
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
	void clearDetachedInvocations()
	throws DaoException;

	/**
	 * 
	 * 
	 */
	public List<Service> listAllServices(String serviceGridId)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	public List<Service> listServicesOfUser(String userGridId, String userId)
	throws DaoException;
	
	/**
	 * 
	 * 
	 */
	public List<BPELService> listParentServicesOf(String gridId, String serviceId)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	ServiceSearchResult searchServices(
			int startIndex, int maxCount
			, String serviceGridId, boolean acrossGrids
			, MatchingCondition[] conditions, Order[] orders)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	ServiceStatRankingSearchResult searchServiceStatRanking(
			int startIndex, int maxCount
			, String serviceAndNodeGridId, String nodeId, boolean acrossGrids
			, MatchingCondition[] conditions, int sinceDays, Order[] orders)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	void addService(Service service)
	throws DaoException, ServiceAlreadyExistsException;

	/**
	 * 
	 * 
	 */
	void deleteService(String serviceGridId, String serviceId)
	throws ServiceNotFoundException, DaoException;

	/**
	 * 
	 * 
	 */
	void deleteServicesOfGrid(String gridId)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	void deleteServicesOfUser(String userGridId, String userId)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	Service getService(String serviceGridId, String serviceId)
	throws ServiceNotFoundException, DaoException;

	/**
	 * 
	 * 
	 */
	boolean isServiceExist(String serviceGridId, String serviceId)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	public InputStream getServiceInstance(String serviceGridId, String serviceId)
	throws DaoException, ServiceNotFoundException;

	/**
	 * 
	 * 
	 */
	public InputStream getServiceWsdl(String serviceGridId, String serviceId)
	throws DaoException, ServiceNotFoundException;
}
