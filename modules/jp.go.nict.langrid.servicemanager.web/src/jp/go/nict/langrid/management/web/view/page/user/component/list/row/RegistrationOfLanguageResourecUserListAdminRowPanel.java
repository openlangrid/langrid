package jp.go.nict.langrid.management.web.view.page.user.component.list.row;

import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.view.page.language.resource.admin.RegistrationOfLanguageResourceAdminPage;

import org.apache.wicket.markup.html.link.Link;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class RegistrationOfLanguageResourecUserListAdminRowPanel 
extends LanguageUsersListRowPanel
{
	/**
	 * 
	 * 
	 */
	public RegistrationOfLanguageResourecUserListAdminRowPanel(
			String gridId, String componentId, UserModel entry, String uniqueId)
	throws ServiceManagerException
	{
		super(gridId, componentId, entry, uniqueId);
		userEntry = entry;
		add(new Link("register"){
			@Override
			public void onClick(){
				setResponsePage(new RegistrationOfLanguageResourceAdminPage(userEntry));
			}

			private static final long serialVersionUID = 1L;
		});
	}

	private UserModel userEntry;
}
