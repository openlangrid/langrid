package jp.go.nict.langrid.management.web.view.page.user.component.list.row;

import jp.go.nict.langrid.management.web.model.TemporaryUserModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.utility.DateUtil;
import jp.go.nict.langrid.management.web.view.page.user.EditTemporaryUserPage;
import jp.go.nict.langrid.management.web.view.page.user.component.list.TemporaryUserListPanel;
import jp.go.nict.langrid.service_1_2.foundation.usermanagement.TemporaryUserEntry;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class TemporaryUserListRowPanel extends Panel{
	/**
	 * 
	 * 
	 */
	public TemporaryUserListRowPanel(
			String componentId, TemporaryUserModel entry, String uniqueId)
	throws ServiceManagerException
	{
		super(componentId);
		userEntry = entry;
		add(new Check<TemporaryUserModel>("check", new Model<TemporaryUserModel>(entry)));
		add(new Label("userId", entry.getUserId()));
		add(new Label("availableStartDatetime"
				, DateUtil.formatYMDHMWithSlash(entry.getBeginAvailableDateTime().getTime())));
		add(new Label("availableEndDatetime"
				, DateUtil.formatYMDHMWithSlash(entry.getEndAvailableDateTime().getTime())));
		add(new Link("edit"){
			@Override
			public void onClick(){
				setResponsePage(new EditTemporaryUserPage(userEntry));
			}

			private static final long serialVersionUID = 1L;
		});
	}

	private TemporaryUserModel userEntry;
}
