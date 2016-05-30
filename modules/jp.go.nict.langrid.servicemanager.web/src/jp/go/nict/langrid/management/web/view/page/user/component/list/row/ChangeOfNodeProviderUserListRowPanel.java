package jp.go.nict.langrid.management.web.view.page.user.component.list.row;

import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: Masaaki Kamiya $
 * @version $Revision: 10124 $
 */
public class ChangeOfNodeProviderUserListRowPanel
	extends LanguageUsersListRowPanel
{
	/**
	 * 
	 * 
	 */
	public ChangeOfNodeProviderUserListRowPanel(
		String gridId, String componentId, UserModel entry, String uniqueId)
	throws ServiceManagerException
	{
		super(gridId, componentId, entry, uniqueId);
		add(new Link<UserModel>("change", new Model<UserModel>(entry)) {
			@Override
			public void onClick() {
				doOnClick(getModelObject());
			}

			private static final long serialVersionUID = 1L;
		});
	}
	
	public void doOnClick(UserModel user) {}
}