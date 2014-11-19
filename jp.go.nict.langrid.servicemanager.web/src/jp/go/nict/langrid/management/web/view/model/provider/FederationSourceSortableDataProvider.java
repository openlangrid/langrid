/*
 * $Id: FederationSourceSortableDataProvider.java 406 2011-08-25 02:12:29Z t-nakaguchi $
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

import jp.go.nict.langrid.management.web.model.FederationModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.DataService;
import jp.go.nict.langrid.management.web.model.service.FederationService;
import jp.go.nict.langrid.management.web.model.service.LangridList;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class FederationSourceSortableDataProvider
extends LangridSortableDataProvider<FederationModel> {
	/**
	 * 
	 * 
	 */
	public FederationSourceSortableDataProvider(String gridId, String userId)
	throws ServiceManagerException {
		this.gridId = gridId;
		this.userId = userId;
	}

	@Override
	protected LangridList<FederationModel> getList(int first, int count)
	throws ServiceManagerException {
		return ((FederationService)getService()).getAllRelatedSourceGridList(gridId, first,
			count);
	}

	@Override
	protected int getTotalCount() throws ServiceManagerException {
//		return ((FederationService)getService()).getAllConnectedSourceGridIdList(gridId).size();
		return ((FederationService)getService()).getAllRelatedSourceGridListTotalCount(gridId);
	}

	@Override
	protected DataService<FederationModel> getService() throws ServiceManagerException {
		return ServiceFactory.getInstance().getFederationService(gridId, gridId, userId);
	}

	private String gridId;
	private String userId;
}
