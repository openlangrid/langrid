package jp.go.nict.langrid.management.web.view.page.node;

import java.util.HashSet;
import java.util.Set;

import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.OrderDirection;
import jp.go.nict.langrid.management.logic.Scope;
import jp.go.nict.langrid.management.web.model.NodeModel;
import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.LangridList;
import jp.go.nict.langrid.management.web.model.service.UserService;
import jp.go.nict.langrid.management.web.view.model.provider.UserSortableDataProvider;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.user.component.list.LanguageUserListPanel;
import jp.go.nict.langrid.management.web.view.page.user.component.list.row.ChangeOfNodeProviderUserListRowPanel;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author$
 * @version $Revision$
 */
public class ChangeOfNodeProviderUserListPage extends ServiceManagerPage {
	/**
	 * 
	 * 
	 */
	public ChangeOfNodeProviderUserListPage(final NodeModel node) {
		try {
			UserSortableDataProvider dp = new UserSortableDataProvider(getSelfGridId(),
				getSessionUserId())
			{
				@Override
				protected LangridList<UserModel> getList(int first, int count)
				throws ServiceManagerException
				{
					UserService service = (UserService)getService();
					Set<String> excludes = new HashSet<String>();
					excludes.add(node.getOwnerUserId());
					return service.getListExcludeIds(first, count
						, getConditions(), getOrders(), getScope()
						, excludes);
				}
			};
			dp.setConditions(new MatchingCondition[]{}, new Order[]{
				new Order("organization", OrderDirection.ASCENDANT)}, Scope.ALL);
			add(new LanguageUserListPanel(getSelfGridId(), "userListPanel", dp) {
				@Override
				protected Panel getRowPanel(String gridId, Item<UserModel> item, String uniqueId)
				throws ServiceManagerException 
				{
					return new ChangeOfNodeProviderUserListRowPanel(
						gridId, "row", item.getModelObject(), uniqueId) {
						@Override
						public void doOnClick(UserModel user) {
							setResponsePage(new ChangeOfNodeProviderPage(user, node));
						}
					};
				}

				private static final long serialVersionUID = 1L;
			});
		} catch(ServiceManagerException e) {
			doErrorProcess(e);
		}
	}
}
