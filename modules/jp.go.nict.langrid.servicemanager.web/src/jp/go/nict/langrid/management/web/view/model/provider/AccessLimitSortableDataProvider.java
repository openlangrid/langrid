/*
 * $Id: AccessLimitSortableDataProvider.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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

import jp.go.nict.langrid.management.web.model.AccessLimitControlModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.AccessLimitControlService;
import jp.go.nict.langrid.management.web.model.service.LangridList;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class AccessLimitSortableDataProvider
extends AbstractLangridSortableDataProvider<AccessLimitControlModel>
{
	/**
	 * 
	 * 
	 */
	public AccessLimitSortableDataProvider(String gridId, String serviceId, String userId)
	throws ServiceManagerException
	{
		service = ServiceFactory.getInstance().getAccessLimitControlService(gridId, gridId, userId);
		this.serviceId = serviceId;
		this.userId = userId;
	}

	@Override
	protected LangridList<AccessLimitControlModel> getList(int first, int count)
	throws ServiceManagerException 
	{
		return service.getList(first, count, userId, serviceId, getOrders());
	}

	@Override
	protected int getTotalCount() throws ServiceManagerException {
		return service.getTotalCount(serviceId, gridId);
	}
	
	private String serviceId;
	private String userId;
	private String gridId;
	private AccessLimitControlService service;
}
