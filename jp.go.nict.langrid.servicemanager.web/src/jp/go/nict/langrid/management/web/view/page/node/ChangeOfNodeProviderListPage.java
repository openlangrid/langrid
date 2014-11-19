package jp.go.nict.langrid.management.web.view.page.node;

import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.OrderDirection;
import jp.go.nict.langrid.management.logic.Scope;
import jp.go.nict.langrid.management.web.model.NodeModel;
import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.view.model.provider.NodeSortableDataProvider;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.node.component.list.NodeListPanel;
import jp.go.nict.langrid.management.web.view.page.node.component.list.row.YourNodeListRowPanel;

import org.apache.wicket.Page;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author$
 * @version $Revision$
 */
public class ChangeOfNodeProviderListPage extends ServiceManagerPage {
	/**
	 * 
	 * 
	 */
	public ChangeOfNodeProviderListPage(UserModel uModel) {
		this.uModel = uModel;
		try {
			Form form = new Form("form");
			form.add(getListPanel("nodeListPanel", getProvider()));
			add(form);
		} catch(ServiceManagerException e) {
			doErrorProcess(e);
		}
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
	
	protected Panel getListPanel(String listId, SortableDataProvider<NodeModel> dp) {
		return new NodeListPanel(getSelfGridId(), listId, dp) {
			@Override
			protected Panel getRowPanel(String gridId, Item<NodeModel> item, String uniqueId)
			throws ServiceManagerException 
			{
				return new YourNodeListRowPanel(
					gridId, "row", item.getModelObject(), uniqueId)
				{
					@Override
					protected Page getDoEditPage(NodeModel node) {
						return new ChangeOfNodeProviderPage(uModel, node);
					}
				};
			}

			private static final long serialVersionUID = 1L;
		};
	}

	private UserModel uModel;
}