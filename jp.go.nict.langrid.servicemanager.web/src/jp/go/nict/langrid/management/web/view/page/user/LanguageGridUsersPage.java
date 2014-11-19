/*
 * $Id: LanguageGridUsersPage.java 497 2012-05-24 04:13:03Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.user;

import java.util.ArrayList;
import java.util.List;

import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.OrderDirection;
import jp.go.nict.langrid.management.web.model.enumeration.GridRelation;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.FederationService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.user.component.list.TabbedUserListPanel;

import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
public class LanguageGridUsersPage extends ServiceManagerPage {
	/**
	 * 
	 * 
	 */
	public LanguageGridUsersPage() {
		try {
			List<ITab> tabList = new ArrayList<ITab>();
			String gridId = getSelfGridId();
			setTabPanel(gridId, tabList, GridRelation.SELF);
			//   	   List<String> cache = new ArrayList<String>();
			List<String> cache = new ArrayList<String>();
			FederationService fs = ServiceFactory.getInstance().getFederationService(gridId);
			// target grid
			for(String targetGridId : fs.getConnectedTargetGridIdList(gridId, new Order("tagetGridName", OrderDirection.ASCENDANT))) {
				setTabPanel(targetGridId, tabList, GridRelation.TARGET);
				cache.add(targetGridId);
			}
			// source grid
			for(String targetGridId : fs.getConnectedSourceGridIdList(gridId, new Order("sourceGridName", OrderDirection.ASCENDANT))) {
				if(cache.contains(targetGridId)) {
					continue;
				}
				setTabPanel(targetGridId, tabList, GridRelation.SOURCE);
			}
			add(new AjaxTabbedPanel("tabbedUserListPanel", tabList));
		} catch(ServiceManagerException e) {
			doErrorProcess(e);
		}
	}

	protected TabbedUserListPanel getTabPanel(
		String panelId, String targetGridId, GridRelation relation)
	throws ServiceManagerException
	{
		return new TabbedUserListPanel(panelId, targetGridId, getSessionUserId(), relation);
	}

	private void setTabPanel(final String gridId, List<ITab> tabList, final GridRelation relation)
	throws ServiceManagerException
	{
		String gridName = ServiceFactory.getInstance().getGridService().get(gridId).getGridName();
		tabList.add(new AbstractTab(new Model<String>(gridName)) {
			@Override
			public Panel getPanel(String panelId) {
				try {
					return getTabPanel(panelId, gridId, relation);
				} catch(ServiceManagerException e) {
					doErrorProcess(e);
				}
				return new Panel(panelId);
			}
		});
	}
}
