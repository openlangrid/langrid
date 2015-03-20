/*
 * $Id: ResourceMetaAttributeSortableDataProvider.java 328 2010-12-08 05:43:18Z t-nakaguchi $
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

import jp.go.nict.langrid.management.web.model.ResourceMetaAttributeModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.DataService;
import jp.go.nict.langrid.management.web.model.service.LangridList;
import jp.go.nict.langrid.management.web.model.service.ResourceMetaAttributeService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;

/**
 * 
 * 
 * @author Masato Mori
 * @author $Author: t-nakaguchi $
 * @version $Revision: 328 $
 */
public class ResourceMetaAttributeSortableDataProvider
extends LangridSortableDataProvider<ResourceMetaAttributeModel> {
	/**
	 * 
	 * 
	 */
	public ResourceMetaAttributeSortableDataProvider(String gridId, String userId, String domainId)
	throws ServiceManagerException {
		this.domainId = domainId;
		this.gridId = gridId;
		this.userId = userId;
	}

	@Override
	protected LangridList<ResourceMetaAttributeModel> getList(int first, int count)
	throws ServiceManagerException {
		return ((ResourceMetaAttributeService)getService()).getListOnDomain(first, count, domainId);
	}

	@Override
	protected int getTotalCount() throws ServiceManagerException {
		return ((ResourceMetaAttributeService)getService()).getAllListOnDomain(domainId).size();
	}

	@Override
	protected DataService<ResourceMetaAttributeModel> getService()
		throws ServiceManagerException {
		return ServiceFactory.getInstance().getResourceMetaAttributeService(gridId, gridId, userId);
	}

	private String gridId;
	private String userId;
	private String domainId;
}
