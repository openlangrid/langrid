/*
 * $Id: OverUseLimitLogic.java 405 2011-08-25 01:43:27Z t-nakaguchi $
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
import java.util.List;

import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.OverUseStateSearchResult;
import jp.go.nict.langrid.dao.entity.LimitType;
import jp.go.nict.langrid.dao.entity.OverUseLimit;
import jp.go.nict.langrid.dao.entity.Period;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author:nakaguchi $
 * @version $Revision:4384 $
 */
public class OverUseLimitLogic
extends AbstractLogic{
	/**
	 * 
	 * 
	 */
	public OverUseLimitLogic() throws DaoException{
	}

	@DaoTransaction
	public void clearOverUseLimits()
	throws DaoException{
		getOverUseLimitDao().clear();
	}

	@DaoTransaction
	public List<OverUseLimit> listOverUseLimits(String gridId, Order[] orders)
	throws DaoException {
		return getOverUseLimitDao().listOverUseLimits(
				gridId, orders);
	}

	@DaoTransaction
	public void setOverUseLimit(
			String gridId, Period period, LimitType limitType, int limitValue
			)
	throws DaoException{
		getOverUseLimitDao().setOverUseLimit(
				gridId, period, limitType, limitValue
				);
	}

	@DaoTransaction
	public void deleteOverUseLimit(
			String gridId, Period period, LimitType limitType
			)
	throws DaoException{
		getOverUseLimitDao().deleteOverUseLimit(
				gridId, period, limitType
				);
	}

	@DaoTransaction
	public OverUseStateSearchResult searchOverUseState(
			int startIndex, int maxCount
			, String gridId
			, Calendar startDateTime, Calendar endDateTime
			, Order[] orders
			)
	throws DaoException{
		if(startDateTime.after(endDateTime)){
			throw new IllegalArgumentException(
					"endDateTime must be after of startDateTime"
					);
		}
		return getOverUseStateDao().searchOverUse(
				startIndex, maxCount
				, gridId, startDateTime, endDateTime
				, orders
				);
	}

	@DaoTransaction
	public OverUseStateSearchResult searchOverUseStateWithPeriod(
		int startIndex, int maxCount
		, String gridId
		, Calendar startDateTime, Calendar endDateTime
		, Order[] orders, Period period
	)
	throws DaoException{
		if(startDateTime.after(endDateTime)){
			throw new IllegalArgumentException(
				"endDateTime must be after of startDateTime"
			);
		}
		return getOverUseStateDao().searchOverUseWithPeriod(
			startIndex, maxCount
			, gridId, startDateTime, endDateTime
			, orders, period
		);
	}
}
