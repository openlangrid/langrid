/*
 * $Id: ControlOfLanguageServicesPage.java 497 2012-05-24 04:13:03Z t-nakaguchi $
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
import java.util.List;

import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.OrderDirection;
import jp.go.nict.langrid.management.web.model.ServiceModel;
import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.FederationService;
import jp.go.nict.langrid.management.web.model.service.LangridServiceService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.user.component.list.TabbedUserAccessRightListPanel;

import org.apache.wicket.Page;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
public class ControlOfLanguageServicesPage extends ServiceManagerPage {
	/**
	 * 
	 * 
	 */
	public ControlOfLanguageServicesPage(final String serviceId) {
		try {
			LangridServiceService<ServiceModel> service = ServiceFactory.getInstance()
				.getLangridServiceService(
					getSelfGridId());
			ServiceModel model = service.get(serviceId);
			selectedServiceGridId = model.getGridId();
			selectedUserGridId = model.getGridId();
			List<ITab> tabList = new ArrayList<ITab>();
			setTabPanel(selectedServiceGridId, model.getOwnerUserId(), serviceId,
				selectedServiceGridId, tabList);
			FederationService fs = ServiceFactory.getInstance().getFederationService(
				selectedServiceGridId);
			if(model.isFederatedUseAllowed()) {
				for(String sourceGridId : fs.getConnectedSourceGridIdList(selectedServiceGridId, new Order("sourceGridName", OrderDirection.ASCENDANT))) {
					setTabPanel(selectedServiceGridId, model.getOwnerUserId(), serviceId, sourceGridId, tabList);
				}
			}
			add(new AjaxTabbedPanel("tabbedUserListPanel", tabList));
			add(new Label("name", model.getServiceName()));
			add(new Link("editInitialSetting") {
				@Override
				public void onClick() {
					setResponsePage(getDoInitialSettingPage(selectedUserGridId,
						selectedServiceGridId, serviceId));
				}
			});
			add(new Link("back") {
				@Override
				public void onClick() {
					setResponsePage(getBackPage());
				}
			});
		} catch(ServiceManagerException e) {
			doErrorProcess(e);
		}
	}

	protected TabbedUserAccessRightListPanel getTabPanel(
		String panelId, String serviceGridId, String serviceOwnerUserId,
		String serviceId, String targetUserGridId)
	throws ServiceManagerException {
		return new TabbedUserAccessRightListPanel(
			panelId, getSessionUserId(), serviceGridId, serviceOwnerUserId, serviceId,
			targetUserGridId) {
			@Override
			protected Page getChangeLimitPage(ServiceModel entry, boolean permit,
				String userGridId, String userId, UserModel user) {
				return getDoChangeLimitPage(entry, permit, userGridId, userId, user);
			}

			@Override
			protected Page getChangeRightConfirmPage(ServiceModel entry, boolean permit,
				UserModel user) {
				return getDoChangeRightConfirmPage(entry, permit, user);
			}
		};
	}

	private void setTabPanel(
		final String serviceGridId, final String serviceOwnerUserId,
		final String serviceId, final String targetUserGridId, List<ITab> tabList)
	throws ServiceManagerException 
	{
		String gridName = ServiceFactory.getInstance().getGridService().get(targetUserGridId).getGridName();
		tabList.add(new AbstractTab(new Model<String>(gridName)) {
			@Override
			public Panel getPanel(String panelId) {
				try {
					selectedUserGridId = targetUserGridId;
					return getTabPanel(panelId, serviceGridId, serviceOwnerUserId,
						serviceId, targetUserGridId);
				} catch(ServiceManagerException e) {
					doErrorProcess(e);
				}
				return new Panel(panelId);
			}
		});
	}

	protected Page getBackPage() {
		return new ControlOfLanguageServicesListPage();
	}

	protected Page getDoChangeLimitPage(
			ServiceModel entry, boolean permit, String userGridId, String userId,
		UserModel user) {
		return new ControlOfLanguageServicesChangeLimitPage(entry, permit, userGridId,
			userId, user);
	}

	protected Page getDoChangeRightConfirmPage(
			ServiceModel entry, boolean permit, UserModel user) {
		return new ControlOfLanguageServicesChangeRightConfirmPage(
				entry.getServiceId(), permit, user);
	}

	protected Page getDoInitialSettingPage(String userGridId, String serviceGridId,
		String serviceId) {
		return new ControlOfLanguageServicesInitialSettingsPage(userGridId,
			serviceGridId, serviceId);
	}

	private String selectedServiceGridId;
	private String selectedUserGridId;
}
