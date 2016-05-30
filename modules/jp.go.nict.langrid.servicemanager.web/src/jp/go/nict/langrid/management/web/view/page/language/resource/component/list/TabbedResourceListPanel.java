package jp.go.nict.langrid.management.web.view.page.language.resource.component.list;

import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.MatchingMethod;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.OrderDirection;
import jp.go.nict.langrid.management.logic.Scope;
import jp.go.nict.langrid.management.web.model.ResourceModel;
import jp.go.nict.langrid.management.web.model.enumeration.GridRelation;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.view.model.provider.ResourceSortableDataProvider;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;

public class TabbedResourceListPanel extends Panel {
	public TabbedResourceListPanel(
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
		ResourceSortableDataProvider provider = new ResourceSortableDataProvider(gridId, userId);
		provider.setConditions(new MatchingCondition[] {
   		   new MatchingCondition("approved", "true", MatchingMethod.COMPLETE)
   		}
		   , new Order[]{
		      new Order("resourceName", OrderDirection.ASCENDANT)
		   }
		   , scope);
		rewritableContainer.addOrReplace(getListPanel(gridId, "resourceList", provider));
	}
	
	public Component getRewritableComponent() {
		return rewritableContainer;
	}
	
	protected ResourceListPanel getListPanel(
			String gridId, String panelId, SortableDataProvider<ResourceModel> provider)
	{
		return new ResourceListPanel(gridId, panelId, provider);
	}

	private WebMarkupContainer rewritableContainer;
}
