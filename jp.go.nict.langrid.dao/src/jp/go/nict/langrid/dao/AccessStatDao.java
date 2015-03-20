/* $Id:AccessLogDao.java 4384 2007-04-03 08:56:48Z nakaguchi $
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

import java.util.Calendar;
import java.util.List;

import jp.go.nict.langrid.dao.entity.AccessStat;
import jp.go.nict.langrid.dao.entity.Period;

/**
 * 
 * 
 * @author $Author:nakaguchi $
 * @version $Revision:4384 $
 */
public interface AccessStatDao {
	/**
	 * 
	 * 
	 */
	void clear() throws DaoException;

	/**
	 * 
	 * 
	 */
	void increment(String userGridId, String userId
			, String serviceAndNodeGridId, String serviceId, String nodeId
			, int requestBytes, int responseBytes, int responseMillis)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	void increment(String userGridId, String userId
			, String serviceAndNodeGridId, String serviceId, String nodeId
			, int requestBytes, int responseBytes, int responseMillis
			, Calendar accessDateTime)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	List<AccessStat> listAccessStats(String serviceGridId) throws DaoException;

	/**
	 * 
	 * 
	 */
	List<AccessStat> listAccessStatsNewerThanOrEqualsTo(
			String serviceGridId, Calendar dateTime)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	AccessStat getAccessStat(
			String userGridId, String userId
			, String serviceAndNodeGridId, String serviceId, String nodeId
			, Calendar baseDateTime, Period period)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	List<AccessStat> getAccessStats(
			String userGridId, String userId
			, String serviceGridId, String serviceId
			, Calendar baseDateTime)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	public AccessRankingEntrySearchResult searchUserAccessRanking(int startIndex,
			int maxCount, String serviceGridId, String serviceId, String userGridId
			, Calendar startDateTime, Calendar endDateTime,
			Period period, Order[] orders) throws DaoException;

	/**
	 * 
	 * 
	 */
	public AccessRankingEntrySearchResult searchServiceAccessRanking(
			int startIndex, int maxCount
			, String serviceGridId, String userId
			, Calendar startDateTime, Calendar endDateTime,
			Period period, Order[] orders) throws DaoException;
	
	/**
	 * 
	 * 
	 */
	void deleteAccessStat(String userGridId, String userId
			, String serviceGridId, String serviceId,
			Calendar baseDateTime, Period period)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	void deleteAccessStatOfGrid(String gridId)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	void deleteAccessStatOfService(
			String serviceGridId, String serviceId)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	void deleteAccessStatOfUser(String userGridId, String userId)
	throws DaoException;
}
