/*
 * $Id: CompositeServiceSortableDataProvider.java 406 2011-08-25 02:12:29Z t-nakaguchi $
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

import java.util.ArrayList;
import java.util.List;

import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.entity.ServiceContainerType;
import jp.go.nict.langrid.management.web.model.CompositeServiceModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.DataService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class CompositeServiceSortableDataProvider
extends LangridSortableDataProvider<CompositeServiceModel> {
	/**
	 * 
	 * 
	 */
	public CompositeServiceSortableDataProvider(String serviceGridId, String userGridId, String userId)
	throws ServiceManagerException {
		this.serviceGridId = serviceGridId;
		this.userGridId = userGridId;
		this.userId = userId;
	}

	@Override
	protected MatchingCondition[] getConditions() {
		List<MatchingCondition> list = new ArrayList<MatchingCondition>();
		for(MatchingCondition mc : super.getConditions()) {
			list.add(mc);
		}
		list.add(new MatchingCondition("containerType", ServiceContainerType.COMPOSITE));
		return list.toArray(new MatchingCondition[]{});
	}

	@Override
	protected DataService<CompositeServiceModel> getService()
		throws ServiceManagerException {
		return ServiceFactory.getInstance().getCompositeServiceService(serviceGridId,
			userGridId, userId);
	}

	private String userGridId;
	private String serviceGridId;
	private String userId;
}
