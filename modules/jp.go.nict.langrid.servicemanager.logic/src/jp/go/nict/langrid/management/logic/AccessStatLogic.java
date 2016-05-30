/*
 * $Id: AccessStatLogic.java 405 2011-08-25 01:43:27Z t-nakaguchi $
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

import jp.go.nict.langrid.dao.AccessRankingEntrySearchResult;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.entity.Period;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author:nakaguchi $
 * @version $Revision:4384 $
 */
public class AccessStatLogic
extends AbstractLogic{
	/**
	 * 
	 * 
	 */
	public AccessStatLogic() throws DaoException{
	}

	@DaoTransaction
	public void clear() throws DaoException{
		getAccessStateDao().clear();
	}

	@DaoTransaction
	public void increment(String userGridId, String userId
			, String serviceAndNodeGridId, String serviceId, String nodeId
			, int requestBytes, int responseBytes, int responseMillis)
	throws DaoException{
		getAccessStateDao().increment(userGridId, userId
				, serviceAndNodeGridId, serviceId, nodeId
				, requestBytes, responseBytes, responseMillis);
	}

	@DaoTransaction
	public AccessRankingEntrySearchResult sumUpUserAccess(
	   int startIndex, int maxCount
	   , String gridId, String serviceId, String userGridId
	   , Calendar startDateTime, Calendar endDateTime
	   , Period period, Order[] orders
	)
	throws DaoException{
	   return getAccessStateDao().searchUserAccessRanking(
	      startIndex, maxCount, gridId, serviceId, userGridId
	      , startDateTime, endDateTime
	      , period, orders
	   );
	}

	@DaoTransaction
	public AccessRankingEntrySearchResult sumUpServiceAccess(
			int startIndex, int maxCount
			, String gridId, String userId
			, Calendar startDateTime, Calendar endDateTime
			, Period period, Order[] orders
			)
	throws DaoException{
		return getAccessStateDao().searchServiceAccessRanking(
				startIndex, maxCount, gridId, userId
				, startDateTime, endDateTime
				, period, orders
				);
	}
}
