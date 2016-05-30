/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2013 Language Grid Project.
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
package jp.go.nict.langrid.dao.jsonic;

import java.util.List;

import jp.go.nict.langrid.commons.util.ListUtil;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.ServiceActionScheduleDao;
import jp.go.nict.langrid.dao.ServiceActionScheduleSearchResult;
import jp.go.nict.langrid.dao.entity.ServiceActionSchedule;

public class JsonicServiceActionScheduleDao implements ServiceActionScheduleDao {
	public JsonicServiceActionScheduleDao(JsonicDaoContext context) {
		this.context = context;
	}

	@Override
	public void clear() throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addServiceActionSchedule(ServiceActionSchedule actionSchedule)
			throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<ServiceActionSchedule> listServiceActionSchedule(String gridId)
			throws DaoException {
		return ListUtil.emptyList();
	}

	@Override
	public ServiceActionScheduleSearchResult searchServiceActionSchedule(
			int startIndex, int maxCount, MatchingCondition[] conditions,
			Order[] orders) throws DaoException {
		return new ServiceActionScheduleSearchResult(
				new ServiceActionSchedule[]{}, 0, true);
	}

	@Override
	public void deleteServiceActionSchedule(String gridId, int scheduleId)
			throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteAllServiceActionSchedule(String gridId)
			throws DaoException {
		throw new UnsupportedOperationException();
	}

	@SuppressWarnings("unused")
	private JsonicDaoContext context;
}
