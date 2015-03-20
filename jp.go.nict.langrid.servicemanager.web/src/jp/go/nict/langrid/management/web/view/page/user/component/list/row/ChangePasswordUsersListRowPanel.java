package jp.go.nict.langrid.management.web.view.page.user.component.list.row;

import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.view.page.user.admin.ChangePasswordAdminPage;
import jp.go.nict.langrid.management.web.view.page.user.component.link.ExternalHomePageLink;
import jp.go.nict.langrid.management.web.view.page.user.component.link.UserProfileLink;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class ChangePasswordUsersListRowPanel extends Panel{
	/**
	 * 
	 * 
	 */
	public ChangePasswordUsersListRowPanel(
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
		add(new Link("change"){
			@Override
			public void onClick(){
				setResponsePage(new ChangePasswordAdminPage(userEntry));
			}

			private static final long serialVersionUID = 1L;
		});
	}

	private UserModel userEntry;
}
