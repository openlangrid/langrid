/*
 * $Id: AccessLimitLogic.java 302 2010-12-01 02:49:42Z t-nakaguchi $
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

import jp.go.nict.langrid.dao.AccessLimitDao;
import jp.go.nict.langrid.dao.AccessLimitNotFoundException;
import jp.go.nict.langrid.dao.AccessLimitSearchResult;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.OrderDirection;
import jp.go.nict.langrid.dao.ServiceNotFoundException;
import jp.go.nict.langrid.dao.UserNotFoundException;
import jp.go.nict.langrid.dao.entity.AccessLimit;
import jp.go.nict.langrid.dao.entity.LimitType;
import jp.go.nict.langrid.dao.entity.Period;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author:nakaguchi $
 * @version $Revision:4384 $
 */
public class AccessLimitLogic
extends AbstractLogic{
	/**
	 * 
	 * 
	 */
	public AccessLimitLogic() throws DaoException{
	}

	@DaoTransaction
	public void clear() throws DaoException{
		getAccessLimitDao().clear();
	}

	@DaoTransaction
	public AccessLimitSearchResult searchAccessLimits(
			int startIndex, int maxCount
			, String userGridId
			, String serviceGridId, String serviceId
			, Order[] orders
			)
	throws DaoException{
		return getAccessLimitDao().searchAccessLimits(
				startIndex, maxCount, userGridId, ""
				, serviceGridId, new String[]{serviceId}
				, orders
				);
	}

	@DaoTransaction
	public AccessLimit[] getMyAccessLimits(
			String userGridId, String userId
			, String serviceGridId, String serviceId
			)
	throws DaoException
	{
		AccessLimitDao dao = getAccessLimitDao();
		AccessLimitSearchResult r = dao.searchAccessLimits(
				0, Period.values().length * LimitType.values().length
				, userGridId, userId, serviceGridId, new String[]{serviceId}
				, new Order[]{
					new Order("period", OrderDirection.ASCENDANT)
					, new Order("limitType", OrderDirection.ASCENDANT)
				}
				);
		return r.getElements();
	}

	@DaoTransaction
	public void setAccessLimit(
			String userGridId, String userId
			, String serviceGridId, String serviceId
			, Period period, LimitType limitType, int limitCount
			)
	throws DaoException, UserNotFoundException, ServiceNotFoundException{
		if(!userId.equals("*") && !getUserDao().isUserExist(userGridId, userId)){
			throw new UserNotFoundException(userGridId, userId);
		}
		if(!getServiceDao().isServiceExist(serviceGridId, serviceId)){
			throw new ServiceNotFoundException(serviceGridId, serviceId);
		}
		getAccessLimitDao().setAccessLimit(
				userGridId, userId, serviceGridId, serviceId
				, period, limitType, limitCount
				);
	}

	@DaoTransaction
	public void deleteAccessLimit(
			String userGridId, String userId
			, String serviceGridId, String serviceId
			, Period period, LimitType limitType
			)
	throws DaoException, AccessLimitNotFoundException{
		getAccessLimitDao().deleteAccessLimit(
				userGridId, userId, serviceGridId, serviceId, period, limitType);
	}
}
