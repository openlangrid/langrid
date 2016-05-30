/*
 * $Id: YourComputationResourcesPage.java 8823 2009-01-21 05:25:09Z Masaaki Kamiya $
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

import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.OrderDirection;
import jp.go.nict.langrid.management.logic.Scope;
import jp.go.nict.langrid.management.web.model.NodeModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.model.provider.NodeSortableDataProvider;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.node.component.list.NodeListPanel;
import jp.go.nict.langrid.management.web.view.page.node.component.list.row.YourNodeListRowPanel;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;

/**
 * 計算資源リスト用のページ
 * @author $Author: Masaaki Kamiya $
 * @version $Revision: 8823 $
 */
public class YourNodePage extends ServiceManagerPage {
	public YourNodePage() {
		try {
			Form form = new Form("form");
			form.add(getListPanel("nodeListPanel", getProvider()));
			add(form);
			add(new Label("headline", MessageManager.getMessage(
				"ProvidingServices.language.resource.headline.YourLanguageResources"
				, getLocale())).setRenderBodyOnly(true));
		} catch(ServiceManagerException e) {
			doErrorProcess(e);
		}
	}

	protected Panel getListPanel(String listId, SortableDataProvider<NodeModel> dp) {
		return new NodeListPanel(getSelfGridId(), listId, dp) {
			private static final long serialVersionUID = 1L;

			@Override
			protected Panel getRowPanel(String gridId, Item<NodeModel> item,
				String uniqueId)
			throws ServiceManagerException {
				return new YourNodeListRowPanel(
					gridId, "row", item.getModelObject(), uniqueId);
			}
		};
	}

	protected SortableDataProvider<NodeModel> getProvider()
	throws ServiceManagerException
	{
		NodeSortableDataProvider provider = new NodeSortableDataProvider(
			getSelfGridId(), getSessionUserId());
		provider.setConditions(new MatchingCondition[]{}
			, new Order[]{
			new Order("nodeName", OrderDirection.ASCENDANT)
			}, Scope.ALL);
		return provider;
	}
}
