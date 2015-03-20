package jp.go.nict.langrid.management.web.view.page.user.component.list.row;

import java.net.URL;

import jp.go.nict.langrid.dao.entity.EmbeddableStringValueClass;
import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.view.page.user.component.link.ExternalHomePageLink;
import jp.go.nict.langrid.management.web.view.page.user.component.link.UserProfileLink;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class LanguageUsersListRowPanel extends Panel{
	/**
	 * 
	 * 
	 */
	public LanguageUsersListRowPanel(
			String gridId, String componentId, UserModel model, String uniqueId)
	throws ServiceManagerException
	{
		super(componentId);
		EmbeddableStringValueClass<URL> url = model.getHomepageUrl();
		
		add(new ExternalHomePageLink("homePage"
				, url != null ? url.getValue().toString() : ""
				, uniqueId, 24));
		UserProfileLink link = new UserProfileLink("organization"
				, gridId, model.getUserId(), uniqueId);
		link.add(new Label("organizationLabel"
				, model.getOrganization() != null ? model.getOrganization() : ""));
		add(link);
	}
}
