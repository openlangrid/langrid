/*
 * $Id:AccessLogDao.java 4384 2007-04-03 08:56:48Z nakaguchi $
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

import java.util.Calendar;

import jp.go.nict.langrid.dao.entity.Period;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author:nakaguchi $
 * @version $Revision:4384 $
 */
public interface OverUseStateDao {
	/**
	 * 
	 * 
	 */
	OverUseStateSearchResult searchOverUse(
			int startIndex, int maxCount
			, String gridId, Calendar startDateTime, Calendar endDateTime
			, Order[] orders)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	OverUseStateSearchResult searchOverUseWithPeriod(int startIndex, int maxCount,
		String gridId, Calendar startDateTime, Calendar endDateTime, Order[] orders,
		Period period) throws DaoException;
}
