package jp.go.nict.langrid.management.web.view.page.user.component.list.row;

import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.view.page.language.service.composite.admin.RegistrationOfCompositeServiceProfileAdminPage;

import org.apache.wicket.markup.html.link.Link;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class RegistrationOfCompositeServiceUserListAdminRowPanel 
extends LanguageUsersListRowPanel
{
	/**
	 * 
	 * 
	 */
	public RegistrationOfCompositeServiceUserListAdminRowPanel(
			String gridId, String componentId, UserModel entry, String uniqueId)
	throws ServiceManagerException
	{
		super(gridId, componentId, entry, uniqueId);
		userEntry = entry;
		add(new Link("register"){
			@Override
			public void onClick(){
				setResponsePage(new RegistrationOfCompositeServiceProfileAdminPage(userEntry.getUserId()));
			}

			private static final long serialVersionUID = 1L;
		});
	}

	private UserModel userEntry;
}
