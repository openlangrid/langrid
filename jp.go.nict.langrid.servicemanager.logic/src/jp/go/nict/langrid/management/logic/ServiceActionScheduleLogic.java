/*
 * $Id: ServiceActionScheduleLogic.java 302 2010-12-01 02:49:42Z t-nakaguchi $
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

import java.util.List;

import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.ServiceActionScheduleSearchResult;
import jp.go.nict.langrid.dao.entity.ServiceActionSchedule;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author:nakaguchi $
 * @version $Revision:4384 $
 */
public class ServiceActionScheduleLogic
extends AbstractLogic{
	/**
	 * 
	 * 
	 */
	public ServiceActionScheduleLogic() throws DaoException{
	}
	
	@DaoTransaction
	public void addSchedule(ServiceActionSchedule schedule) throws DaoException {
	   getScheduleDao().addServiceActionSchedule(schedule);
	}

	@DaoTransaction
	public ServiceActionScheduleSearchResult searchSchedules(
			int startIndex, int maxCount, MatchingCondition[] conditions, Order[] orders
	) throws DaoException{
	   return getScheduleDao().searchServiceActionSchedule(
	      startIndex, maxCount, conditions, orders);
	}
	
	@DaoTransaction
	public void deleteSchedule(String gridId, int scheduleId) throws DaoException {
	   getScheduleDao().deleteServiceActionSchedule(gridId, scheduleId);
	}
	
	@DaoTransaction
	public List<ServiceActionSchedule> listAll(String grid) throws DaoException{
	   return getScheduleDao().listServiceActionSchedule(grid);
	}
}
