/*
 * $Id: NodeListPage.java 497 2012-05-24 04:13:03Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.node;

import java.util.ArrayList;
import java.util.List;

import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.OrderDirection;
import jp.go.nict.langrid.management.web.model.NodeModel;
import jp.go.nict.langrid.management.web.model.enumeration.GridRelation;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.FederationService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.node.component.list.NodeListPanel;
import jp.go.nict.langrid.management.web.view.page.node.component.list.TabbedNodeListPanel;

import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
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
public class NodeListPage extends ServiceManagerPage {
	/**
	 * 
	 * 
	 */
	public NodeListPage() {
		String gridId = getSelfGridId();
		List<ITab> tabList = new ArrayList<ITab>();
		try {
			setTabPanel(gridId, tabList, GridRelation.SELF);
			FederationService fs = ServiceFactory.getInstance().getFederationService(gridId);
			for(String targetGridId : fs.getConnectedTargetGridIdList(gridId, new Order("tagetGridName", OrderDirection.ASCENDANT))) {
				setTabPanel(targetGridId, tabList, GridRelation.SOURCE);
			}
			add(new AjaxTabbedPanel("nodeListPanel", tabList));
		} catch(ServiceManagerException e) {
			doErrorProcess(e);
		}
	}

	protected NodeListPanel getListPanel(
			String panelId, SortableDataProvider<NodeModel> provider) {
		return new NodeListPanel(getSelfGridId(), panelId, provider);
	}

	protected TabbedNodeListPanel getTabPanel(String panelId, String gridId, GridRelation relation)
	throws ServiceManagerException
	{
		return new TabbedNodeListPanel(panelId, gridId, getSessionUserId(), false, relation);
	}

	private void setTabPanel(final String gridId, List<ITab> tabList, final GridRelation relation)
	throws ServiceManagerException
	{
		String gridName = ServiceFactory.getInstance().getGridService().get(gridId).getGridName();
		tabList.add(new AbstractTab(new Model<String>(gridName)) {
			@Override
			public Panel getPanel(String panelId) {
				try {
					TabbedNodeListPanel panel = getTabPanel(panelId, gridId, relation);
					return panel;
				} catch(ServiceManagerException e) {
					doErrorProcess(e);
				}
				return new Panel(panelId);
			}
		});
	}
}
