package jp.go.nict.langrid.management.web.view.page.user.component.list.row;

import jp.go.nict.langrid.management.web.log.LogWriter;
import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.model.service.UserService;
import jp.go.nict.langrid.management.web.utility.StringUtil;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.component.link.ConfirmLink;
import jp.go.nict.langrid.management.web.view.page.BasePage;
import jp.go.nict.langrid.management.web.view.page.user.admin.AccessControlOfLanguageGridUsersPage;
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
public class ControlOfUserAccessRightListAdminRowPanel extends Panel{
	/**
	 * 
	 * 
	 */
	public ControlOfUserAccessRightListAdminRowPanel(
			String gridId, String componentId, final UserModel entry, String uniqueId)
	throws ServiceManagerException
	{
		super(componentId);
		this.gridId = gridId;
		UserService service = ServiceFactory.getInstance().getUserService(gridId);
		user = service.get(entry.getUserId());
		isPermitted = service.isCanCallServices(entry.getUserId());
		UserProfileLink userLink = new UserProfileLink("provider", gridId, entry.getUserId(),
				uniqueId);
		userLink.add(new Label("labelProvider", user.getOrganization()));
		add(userLink);
		add(new Label("homePage", StringUtil.shortenString(user.getHomepageUrl() == null ? "" : user.getHomepageUrl() == null ? "" : user.getHomepageUrl().getValue().toString(), 24)));
		add(new Label("accessRight", isPermitted ? new Model<String>(
				"permitted") : new Model<String>("prohibit")));
		Link<UserModel> permitLink = new ConfirmLink<UserModel>("permit", new Model<UserModel>(entry)
				, MessageManager.getMessage("LanguageGridOperator.message.permit.alert.Confirm", getLocale()))
		{
			@Override
			public void onClick(){
				changeCanCallService(getModelObject());
			}

			private static final long serialVersionUID = 1L;
		};
		
		Link<UserModel> prohibitLink = new ConfirmLink<UserModel>("prohibit", new Model<UserModel>(entry)
				, MessageManager.getMessage("LanguageGridOperator.message.prohibit.alert.Confirm", getLocale()))
		{
			@Override
			public void onClick(){
				changeCanCallService(getModelObject());
			}

			private static final long serialVersionUID = 1L;
		};
		add(permitLink.setVisible(!isPermitted));
		add(prohibitLink.setVisible(isPermitted));
	}

	private void changeCanCallService(UserModel entry) {
		try{
			ServiceFactory.getInstance().getUserService(gridId).setCanCallServices(entry.getUserId(), !isPermitted);
			LogWriter.writeInfo("", "Access control for \"" + entry.getUserId() + "\" has been changed to \""
					+ !isPermitted + "\"", getClass());
			setResponsePage(new AccessControlOfLanguageGridUsersPage());
		}catch(ServiceManagerException e){
			((BasePage)getPage()).doErrorProcess(e);
		}
	}
	
	private boolean isPermitted;
	private UserModel user;
	private String gridId;
	
}
