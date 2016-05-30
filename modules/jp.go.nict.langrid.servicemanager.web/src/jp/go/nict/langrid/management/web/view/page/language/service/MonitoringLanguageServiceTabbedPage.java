/*
 * $Id: MonitoringLanguageServiceTabbedPage.java 497 2012-05-24 04:13:03Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.OrderDirection;
import jp.go.nict.langrid.management.web.model.ServiceModel;
import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.model.enumeration.GridRelation;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.FederationService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.view.model.LangridSearchCondition;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.language.service.component.list.ServiceListTabPanel;
import jp.go.nict.langrid.management.web.view.page.language.service.component.list.row.MonitoringLanguageServicesListRowPanel;

import org.apache.wicket.Page;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
public class MonitoringLanguageServiceTabbedPage extends ServiceManagerPage {
	/**
	 * 
	 * 
	 */
	public MonitoringLanguageServiceTabbedPage() {
		try {
			String gridId = getSelfGridId();
			List<ITab> tabList = new ArrayList<ITab>();
			setTabPanel(gridId, tabList, GridRelation.SELF);
			FederationService fs = ServiceFactory.getInstance().getFederationService(
				gridId);
			for(String targetId : fs.getConnectedTargetGridIdList(gridId, new Order("tagetGridName", OrderDirection.ASCENDANT))) {
				setTabPanel(targetId, tabList, GridRelation.SOURCE);
			}
			add(new AjaxTabbedPanel("serviceListPanel", tabList));
		} catch(ServiceManagerException e) {
			doErrorProcess(e);
		}
	}

	protected ServiceListTabPanel getTabPanel(
		String panelId, String gridId, GridRelation relation,
		LangridSearchCondition condition)
	throws ServiceManagerException {
		condition.putOrReplaceCondition("approved", true);
		return new ServiceListTabPanel(panelId, gridId, gridId, "", condition, relation) {
			@Override
			protected <T extends ServiceModel>Panel getListRowPanel(
				String nowGridId, Item<T> item, String uniqueId)
			throws ServiceManagerException {
				return new MonitoringLanguageServicesListRowPanel("row",
					item.getModelObject(), uniqueId) {
					@Override
					protected String getOrganizationName(ServiceModel entry)
					throws ServiceManagerException {
						UserModel ue = ServiceFactory.getInstance()
							.getUserService(entry.getGridId())
							.get(entry.getOwnerUserId());
						return ue == null ? "" : ue.getOrganization();
					}

					@Override
					protected Page getResponsePage(String serviceGridId,
						String serviceId, String serviceName, Calendar start, Calendar end) {
						return getMonitarPage(serviceGridId, serviceId, serviceName,
							start, end);
					}

					private static final long serialVersionUID = 1L;
				};
			}
		};
	}

	private void setTabPanel(final String gridId, List<ITab> tabList, final GridRelation relation)
	throws ServiceManagerException
	{
		String gridName = ServiceFactory.getInstance().getGridService().get(gridId).getGridName();
		tabList.add(new AbstractTab(new Model<String>(gridName)) {
			@Override
			public Panel getPanel(String panelId) {
				try {
					LangridSearchCondition condition = new LangridSearchCondition();
					ServiceListTabPanel panel = getTabPanel(panelId, gridId, relation, condition);
					return panel;
				} catch(ServiceManagerException e) {
					doErrorProcess(e);
				}
				return new Panel(panelId);
			}
		});
	}

	protected Page getMonitarPage(String serviceGridId, String serviceId,
		String serviceName, Calendar start, Calendar end) {
		return new MonitoringLanguageServiceStatisticPage(serviceGridId, serviceId,
			serviceName, start, end);
	}
}
