/*
 * $Id: AccessStatisticsSortableDataProvider.java 406 2011-08-25 02:12:29Z t-nakaguchi $
 * 
 * This is a program for Language Grid Core Node. This combines multiple language resources and
 * provides composite language services. Copyright (C) 2005-2008 NICT Language Grid Project.
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation, either version
 * 2.1 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package jp.go.nict.langrid.management.web.view.model.provider;

import java.util.Calendar;

import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.OrderDirection;
import jp.go.nict.langrid.dao.entity.Period;
import jp.go.nict.langrid.management.web.model.ExecutionInformationStatisticsModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.LangridList;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.model.service.ServiceInformationService;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class AccessStatisticsSortableDataProvider
extends AbstractLangridSortableDataProvider<ExecutionInformationStatisticsModel>
{
   /**
    * 
    * 
    */
	public AccessStatisticsSortableDataProvider(String serviceGridId, String serviceId, String userGridId
			, String userId , Calendar start, Calendar end)
	throws ServiceManagerException
	{
		service = ServiceFactory.getInstance().getServiceInformationService(serviceGridId, userGridId, userId);
		this.serviceId = serviceId;
		this.start = start;
		this.end = end;
	}

	/**
	 * 
	 * 
	 */
	public void setEnd(Calendar end) {
		this.end = end;
	}
	
	/**
	 * 
	 * 
	 */
	public void setStart(Calendar start) {
		this.start = start;
	}
	
	@Override
	protected LangridList<ExecutionInformationStatisticsModel> getList(int first, int count)
	throws ServiceManagerException 
	{
		return service.getStatisticsList(first, count, serviceId, start, end, Period.DAY
				, new Order[]{
					new Order("accessCount", OrderDirection.DESCENDANT)
					, new Order("responseBytes", OrderDirection.DESCENDANT)});
	}
	
	@Override
	protected int getTotalCount() throws ServiceManagerException {
		return service.getStatisticsTotalCount(serviceId, start, end, Period.DAY);
	}

	private final ServiceInformationService service;
	private final String serviceId;
	private Calendar start;
	private Calendar end;
}
