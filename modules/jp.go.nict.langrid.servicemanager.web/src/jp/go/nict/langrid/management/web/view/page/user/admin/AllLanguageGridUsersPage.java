package jp.go.nict.langrid.management.web.view.page.user.admin;

import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.OrderDirection;
import jp.go.nict.langrid.management.logic.Scope;
import jp.go.nict.langrid.management.web.log.LogWriter;
import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.view.model.provider.UserSortableDataProvider;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.user.admin.component.list.row.AllLanguageGridUsersListRowPanel;
import jp.go.nict.langrid.management.web.view.page.user.component.list.LanguageUserListPanel;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;


public class AllLanguageGridUsersPage extends ServiceManagerPage{
	public AllLanguageGridUsersPage(){
		UserSortableDataProvider provider;
		try {
			provider = new UserSortableDataProvider(getSelfGridId(), getSessionUserId());
			provider.setConditions(
					new MatchingCondition[]{}, new Order[]{
							new Order("organization", OrderDirection.ASCENDANT)}
					, Scope.ALL);
			LanguageUserListPanel list = new LanguageUserListPanel(
					getSelfGridId(), "userListPanel", provider)
			{
				@Override
				protected Panel getRowPanel(String gridId, Item<UserModel> item, String uniqueId)
				throws ServiceManagerException
				{
					return new AllLanguageGridUsersListRowPanel("row", item.getModelObject(), uniqueId){
						@Override
						protected void doUnregistrationProcess(UserModel model) {
							try{
								ServiceFactory.getInstance().getUserService(model.getGridId()).delete(model);
								LogWriter.writeInfo(getSessionUserId(), "\"" + model.getUserId()
										+ "\" of langrid user has been deleted.", getClass());	
							}catch(ServiceManagerException e){
								doErrorProcess(e);
							}
						}
					};
				}
	
			};
			add(list);
			add(new Link("register"){
				@Override
				public void onClick(){
					setResponsePage(new RegistrationOfLanguageGridUserPage());
				}
			});
		} catch(ServiceManagerException e) {
			doErrorProcess(e);
		}
	}
}
