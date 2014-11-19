/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2011 NICT Language Grid Project.
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

import java.util.Calendar;
import java.util.List;

import jp.go.nict.langrid.dao.entity.AccessLog;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author Masato Mori
 * @author Takao Nakaguchi
 */
public interface AccessLogDao {
	/**
	 * 
	 * 
	 */
	void clear() throws DaoException;

	/**
	 * 
	 * 
	 */
	List<AccessLog> listAccessLogsNewerThanOrEqualsTo(
			String serviceAndNodeGridId, Calendar dateTime)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	public AccessLogSearchResult searchLimitOverAccessLog(
		int startIndex, int maxCount
		, String userGridId, String userId
		, String serviceGridId, String[] serviceIds
		, Calendar startDateTime, Calendar endDateTime
		, MatchingCondition[] conditions, Order[] orders, int limitCount)
	throws DaoException;
	
	/**
	 * 
	 * 
	 */
	AccessLogSearchResult searchAccessLog(
			int startIndex, int maxCount
			, String userGridId, String userId
			, String serviceGridId, String[] serviceIds
			, Calendar startDateTime, Calendar endDateTime
			, MatchingCondition[] conditions
			, Order[] orders)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	boolean isLogExist(int logId) throws DaoException;

	/**
	 * 
	 * 
	 */
	void addAccessLog(AccessLog log)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	void deleteAccessLogBefore(Calendar dateTime)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	void deleteAccessLogsOfGrid(String gridId)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	void deleteAccessLogsOfService(String serviceGridId, String serviceId)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	void deleteAccessLogOfNode(String nodeGridId, String nodeId)
		throws DaoException;

	/**
	 * 
	 * 
	 */
	void deleteAccessLogOfUser(String userGridId, String userId)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	boolean isLogExistByNodeIds(String gridId, String nodeId, int nodeLocalId) throws DaoException;

	/**
	 * 
	 * 
	 */
	void updateAccessLogByNodeIds(AccessLog log)
	throws DaoException;
}
