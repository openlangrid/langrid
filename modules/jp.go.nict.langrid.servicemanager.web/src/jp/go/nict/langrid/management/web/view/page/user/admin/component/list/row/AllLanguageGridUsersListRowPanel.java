package jp.go.nict.langrid.management.web.view.page.user.admin.component.list.row;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.MatchingMethod;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.management.logic.Scope;
import jp.go.nict.langrid.management.web.log.LogWriter;
import jp.go.nict.langrid.management.web.model.ResourceModel;
import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ResourceService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.component.link.ConfirmLink;
import jp.go.nict.langrid.management.web.view.page.user.admin.AllLanguageGridUsersPage;
import jp.go.nict.langrid.management.web.view.page.user.admin.ChangePasswordAdminPage;
import jp.go.nict.langrid.management.web.view.page.user.admin.EditUserProfileAdminPage;
import jp.go.nict.langrid.management.web.view.page.user.component.link.ExternalHomePageLink;
import jp.go.nict.langrid.management.web.view.page.user.component.link.UserProfileLink;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class AllLanguageGridUsersListRowPanel extends Panel{
	/**
	 * 
	 * 
	 */
	public AllLanguageGridUsersListRowPanel(
			String componentId, UserModel entry, String uniqueId)
	throws ServiceManagerException
	{
		super(componentId);
		userEntry = entry;
		add(new ExternalHomePageLink("homePage"
				, entry.getHomepageUrl() == null ? "" : entry.getHomepageUrl() == null ? "" : entry.getHomepageUrl().getValue().toString(), uniqueId, 24));
		UserProfileLink link = new UserProfileLink("organization"
				, entry.getGridId(), entry.getUserId(), uniqueId);
		link.add(new Label("organizationLabel", entry.getOrganization()));
		add(link);
		add(new Link("edit"){
			@Override
			public void onClick(){
				setResponsePage(new EditUserProfileAdminPage(userEntry));
			}

			private static final long serialVersionUID = 1L;
		});
		add(new Link("changePassword"){
			@Override
			public void onClick(){
				setResponsePage(new ChangePasswordAdminPage(userEntry));
			}
			
			private static final long serialVersionUID = 1L;
		});
		add(new ConfirmLink<UserModel>("unregister", new Model<UserModel>(entry)
				, MessageManager.getMessage("LanguageGridOperator.message.unregister.alert.Confirm", getLocale()))
		{
			@Override
			public void onClick(){
				ResourceService service;
				try {
					service = ServiceFactory.getInstance().getResourceService(
					   getModelObject().getGridId(), getModelObject().getGridId(), getModelObject().getUserId());
					List<ResourceModel> list = service.getList(0, 1, new MatchingCondition[]{
					   new MatchingCondition("ownerUserId", getModelObject().getUserId(), MatchingMethod.COMPLETE)
					}, new Order[]{}, Scope.ALL);
					int count = list.size();
					MatchingCondition[] mc = new MatchingCondition[]{new MatchingCondition("ownerUserId", getModelObject().getUserId(), MatchingMethod.COMPLETE)};
					count += ServiceFactory.getInstance().getLangridServiceService(getModelObject().getGridId()).getTotalCount(mc, Scope.MINE);
					
					if(0 < count) {
					   Map<String, String> map = new HashMap<String, String>();
						map.put("id", getModelObject().getUserId());
					   error(MessageManager.getMessage(
										"message.error.delete.user.resources.check", map));
						return;
					}
					
					doUnregistrationProcess(getModelObject());
					setResponsePage(new AllLanguageGridUsersPage());
				} catch(ServiceManagerException e) {
					LogWriter.writeError(getModelObject().getUserId(), e, this.getClass());
				}
			}
			
			private static final long serialVersionUID = 1L;
		});
	}
	
	protected void doUnregistrationProcess(UserModel entry) {
		// noop
	}

	private UserModel userEntry;

}
