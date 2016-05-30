package jp.go.nict.langrid.management.web.view.page.node.component.list;

import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.OrderDirection;
import jp.go.nict.langrid.management.logic.Scope;
import jp.go.nict.langrid.management.web.model.NodeModel;
import jp.go.nict.langrid.management.web.model.enumeration.GridRelation;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.view.model.provider.NodeSortableDataProvider;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;

public class TabbedNodeListPanel extends Panel {
	public TabbedNodeListPanel(
			String panelId, String gridId, String userId, boolean isAccessRightLimit, GridRelation relation)
	throws ServiceManagerException
	{
		super(panelId);
		rewritableContainer = new WebMarkupContainer("rewritableContainer");
		rewritableContainer.setOutputMarkupId(true);
		makeListPanel(gridId, userId, isAccessRightLimit ? Scope.ACCESSIBLE : Scope.ALL);
		add(rewritableContainer);
	}
	
	public void makeListPanel(String gridId, String userId, Scope scope)
	throws ServiceManagerException 
	{
		NodeSortableDataProvider provider = new NodeSortableDataProvider(gridId, userId);
		provider.setConditions(new MatchingCondition[] {
//   		   new MatchingCondition("approved", "true", MatchingMethod.COMPLETE)
   		}
		   , new Order[]{
		      new Order("nodeName", OrderDirection.ASCENDANT)
		   }
		   , Scope.ALL);
		rewritableContainer.addOrReplace(getListPanel(gridId, "nodeList", provider));
	}
	
	public Component getRewritableComponent() {
		return rewritableContainer;
	}
	
	protected NodeListPanel getListPanel(
			String gridId, String panelId, SortableDataProvider<NodeModel> provider)
	{
		return new NodeListPanel(gridId, panelId, provider);
	}

	private WebMarkupContainer rewritableContainer;
}
