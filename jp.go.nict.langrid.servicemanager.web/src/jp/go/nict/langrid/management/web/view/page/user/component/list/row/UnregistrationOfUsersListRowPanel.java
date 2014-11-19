package jp.go.nict.langrid.management.web.view.page.user.component.list.row;

import jp.go.nict.langrid.service_1_2.foundation.usermanagement.UserEntry;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class UnregistrationOfUsersListRowPanel extends Panel{
	/**
	 * 
	 * 
	 */
	public UnregistrationOfUsersListRowPanel(
			String componentId, UserEntry entry, String uniqueId)
	{
		super(componentId);
		add(new Label("userId", entry.getUserId()));
		add(new Check<UserEntry>("check"
				, new Model<UserEntry>(entry)));
		add(new Label("organization", entry.getOrganization()));
	}

}
