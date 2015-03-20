/*
 * $Id: ResourceTypeSortableDataProvider.java 328 2010-12-08 05:43:18Z t-nakaguchi $
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

import jp.go.nict.langrid.management.web.model.ResourceTypeModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.DataService;
import jp.go.nict.langrid.management.web.model.service.LangridList;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;

/**
 * 
 * 
 * @author Masato Mori
 * @author $Author: t-nakaguchi $
 * @version $Revision: 328 $
 */
public class ResourceTypeSortableDataProvider extends
LangridSortableDataProvider<ResourceTypeModel> {
	/**
	 * 
	 * 
	 */
	public ResourceTypeSortableDataProvider(String gridId, String domainId, String userId)
	throws ServiceManagerException {
		this.gridId = gridId;
		this.userId = userId;
		this.domainId = domainId;
	}

	@Override
	protected LangridList<ResourceTypeModel> getList(int first, int count)
	throws ServiceManagerException {
		LangridList<ResourceTypeModel> list = new LangridList<ResourceTypeModel>();
		for(ResourceTypeModel model : ServiceFactory.getInstance()
			.getResourceTypeService(
				gridId).getAllList(domainId).subList(first, first + count)) {
			list.add(model);
		}
		return list;
	}

	@Override
	protected int getTotalCount() throws ServiceManagerException {
		return ServiceFactory.getInstance().getResourceTypeService(
			gridId).getAllList(domainId).size();
	}

	@Override
	protected DataService<ResourceTypeModel> getService() throws ServiceManagerException {
		return ServiceFactory.getInstance().getResourceTypeService(gridId, gridId, userId);
	}

	private String gridId;
	private String userId;
	private String domainId;
}
