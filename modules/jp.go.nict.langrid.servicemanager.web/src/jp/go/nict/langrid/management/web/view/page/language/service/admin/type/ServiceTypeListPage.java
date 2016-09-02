/*
 * $Id: ServiceTypeListPage.java 497 2012-05-24 04:13:03Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.service.admin.type;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.Model;

import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.OrderDirection;
import jp.go.nict.langrid.management.logic.Scope;
import jp.go.nict.langrid.management.web.model.DomainModel;
import jp.go.nict.langrid.management.web.model.ServiceTypeModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.DomainService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.view.model.provider.ServiceTypeSortableDataProvider;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.language.service.admin.type.component.list.EmptyServiceTypeListPanel;
import jp.go.nict.langrid.management.web.view.page.language.service.admin.type.component.list.ServiceTypeListPanel;
import jp.go.nict.langrid.management.web.view.page.language.service.admin.type.component.list.row.ServiceTypeListRowPanel;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
public class ServiceTypeListPage extends ServiceManagerPage {
	/**
	 * 
	 * 
	 */
	public ServiceTypeListPage() {
		try {
			String gridId = getSelfGridId();
			List<ITab> tabList = new ArrayList<ITab>();
			DomainService ds = ServiceFactory.getInstance().getDomainService(gridId);
			for(DomainModel dm : ds.getAllList()) {
				setTabPanel(gridId, dm.getDomainName(), tabList, dm.getDomainId());
			}
			for(String sourceGridId : ServiceFactory.getInstance()
				.getFederationService(gridId)
				.getReachableTargetGridIdListFrom(gridId))
			{
				for(DomainModel dm : ds.getListOnGrid(sourceGridId)) {
					setTabPanel(sourceGridId, dm.getDomainName(), tabList,
						dm.getDomainId());
				}
			}
			if(tabList.size() == 0) {
				add(new EmptyServiceTypeListPanel("serviceTypeListPanel"));
			} else {
				add(new AjaxTabbedPanel("serviceTypeListPanel", tabList));
			}
		} catch(ServiceManagerException e) {
			doErrorProcess(e);
		}
	}

	protected ServiceTypeListPanel getTabPanel(String panelId, String gridId,
		String domainId)
	throws ServiceManagerException
	{
		ServiceTypeSortableDataProvider provider = new ServiceTypeSortableDataProvider(
			gridId, domainId, getSessionUserId());
		provider.setConditions(
			new MatchingCondition[]{
				new MatchingCondition("gridId", gridId)
				, new MatchingCondition("domainId", domainId)
			}, new Order[]{new Order("serviceTypeId", OrderDirection.ASCENDANT)}
			, Scope.ALL);
		return new ServiceTypeListPanel(gridId, panelId, provider) {
			@Override
			protected Panel getRowPanel(
				String gridId, Item<ServiceTypeModel> item, String uniqueId)
			throws ServiceManagerException
			{
				return new ServiceTypeListRowPanel(gridId, "row", item.getModelObject(),
					uniqueId);
			}
		};
	}

	private void setTabPanel(
		final String gridId, String tabName, List<ITab> tabList, final String domainId)
	{
		tabList.add(new AbstractTab(new Model<String>(tabName)) {
			@Override
			public Panel getPanel(String panelId) {
				try {
					ServiceTypeListPanel panel = getTabPanel(panelId, gridId, domainId);
					return panel;
				} catch(ServiceManagerException e) {
					doErrorProcess(e);
				}
				return new Panel(panelId);
			}
		});
	}
}
