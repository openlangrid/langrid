package jp.go.nict.langrid.management.web.view.page.user.component.list.row;

import jp.go.nict.langrid.management.web.model.ResourceModel;
import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.StringUtil;
import jp.go.nict.langrid.management.web.view.page.user.component.link.UserProfileLink;
import jp.go.nict.langrid.service_1_2.foundation.serviceaccessrightmanagement.AccessRight;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class UserAccessRightListRowPanel extends Panel{
	/**
	 * 
	 * 
	 */
	public UserAccessRightListRowPanel(
			String gridId, String componentId, final AccessRight entry
			, String uniqueId, final ResourceModel resource)
	throws ServiceManagerException
	{
		super(componentId);
		UserModel user = ServiceFactory.getInstance().getUserService(gridId).get(entry.getUserId());
		UserProfileLink userLink = new UserProfileLink("provider", gridId, entry.getUserId(), uniqueId);
		userLink.add(new Label("labelProvider", user.getOrganization()));
		add(userLink);
		add(new Label("homePage", StringUtil.shortenString(user.getHomepageUrl() == null ? "" : user.getHomepageUrl() == null ? "" : user.getHomepageUrl().getValue().toString(), 24)));
		add(new Label("accessRight", entry.isPermitted() ? new Model<String>("permitted")
						: new Model<String>("prohibit")));
	}

}
