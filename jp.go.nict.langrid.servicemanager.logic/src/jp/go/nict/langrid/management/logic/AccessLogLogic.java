/*
 * $Id: AccessLogLogic.java 405 2011-08-25 01:43:27Z t-nakaguchi $
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
package jp.go.nict.langrid.management.logic;

import java.util.Calendar;

import jp.go.nict.langrid.dao.AccessLogSearchResult;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.entity.AccessLog;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author:nakaguchi $
 * @version $Revision:4384 $
 */
public class AccessLogLogic
extends AbstractLogic{
	/**
	 * 
	 * 
	 */
	public AccessLogLogic() throws DaoException{
	}

	@DaoTransaction
	public void clear() throws DaoException{
		getAccessLogDao().clear();
	}

	@DaoTransaction
	public void addAccessLog(AccessLog log) throws DaoException{
		getAccessLogDao().addAccessLog(log);
	}
	
	@DaoTransaction
	public AccessLogSearchResult searchLimitOverAccessLogs(
		int startIndex, int maxCount
		, String userGridId, String userId
		, String serviceGridId, String[] serviceIds
		, Calendar startDateTime, Calendar endDateTime
		, MatchingCondition[] conditions
		, Order[] orders, int limitCount
	)
	throws DaoException
	{
		return getAccessLogDao().searchLimitOverAccessLog(
			startIndex, maxCount
			, userGridId, userId
			, serviceGridId, serviceIds
			, startDateTime, endDateTime
			, conditions
			, orders, limitCount
		);
	}

	@DaoTransaction
	public AccessLogSearchResult searchAccessLogs(
      int startIndex, int maxCount
      , String userGridId, String userId
      , String serviceGridId, String[] serviceIds
      , Calendar startDateTime, Calendar endDateTime
      , MatchingCondition[] conditions
      , Order[] orders
      )
   throws DaoException
   {
	   return getAccessLogDao().searchAccessLog(
         startIndex, maxCount
         , userGridId, userId
         , serviceGridId, serviceIds
         , startDateTime, endDateTime
         , conditions
         , orders
         );
   }

	@DaoTransaction
	public AccessLogSearchResult searchAccessLogsOfService(
			int startIndex, int maxCount
			, String serviceGridId, String serviceId
			, Calendar startDateTime, Calendar endDateTime
			, Order[] orders
			)
		throws DaoException
	{
		return getAccessLogDao().searchAccessLog(
				startIndex, maxCount, "", ""
				, serviceGridId, new String[]{serviceId}
				, startDateTime, endDateTime
				, new MatchingCondition[]{}
				, orders
				);
	}

	@DaoTransaction
	public AccessLogSearchResult searchAccessLogsOfUser(
			int startIndex, int maxCount
			, String userGridId, String userId
			, Calendar startDateTime, Calendar endDateTime
			, Order[] orders
			)
		throws DaoException
	{
		return getAccessLogDao().searchAccessLog(
				startIndex, maxCount
				, userGridId, userId
				, "", new String[]{}
				, startDateTime, endDateTime
				, new MatchingCondition[]{}
				, orders
				);
	}
}
