package jp.go.nict.langrid.management.web.view.page.user.component.list;

import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.OrderDirection;
import jp.go.nict.langrid.management.logic.Scope;
import jp.go.nict.langrid.management.web.model.enumeration.GridRelation;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.view.model.provider.UserSortableDataProvider;

import org.apache.wicket.markup.html.panel.Panel;

public class TabbedUserListPanel extends Panel {
	public TabbedUserListPanel(String panelId, String gridId, String userId, GridRelation relation)
	throws ServiceManagerException
	{
		super(panelId);
		UserSortableDataProvider provider = new UserSortableDataProvider(gridId, userId);
		provider.setConditions(
			new MatchingCondition[]{}
			, new Order[]{new Order("organization", OrderDirection.ASCENDANT)}
			, Scope.ALL);
		add(new LanguageUserListPanel(gridId, "userList", provider));
	}
}
