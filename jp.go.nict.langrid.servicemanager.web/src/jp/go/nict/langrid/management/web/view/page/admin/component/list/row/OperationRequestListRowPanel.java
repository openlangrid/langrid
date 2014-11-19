package jp.go.nict.langrid.management.web.view.page.admin.component.list.row;

import jp.go.nict.langrid.management.web.model.OperationRequestModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.utility.DateUtil;
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
public class OperationRequestListRowPanel extends Panel{
	/**
	 * 
	 * 
	 * @throws ServiceManagerException
	 */
	public OperationRequestListRowPanel(
		String gridId, String componentId, OperationRequestModel model, String uniqueId)
	throws ServiceManagerException
	{
		super(componentId);
		add(new Label("date", DateUtil.formatYMDWithSlash(
				model.getCreatedDateTime().getTime())));
		Label label = new Label("operation", model.getContents());
		label.setEscapeModelStrings(false);
		add(label);
		UserProfileLink link = new UserProfileLink(
				"organization", model.getGridId(), model.getRequestedUserId(), uniqueId);
		link.setEnabled(model.getRequestedUserId() != null);
		link.add(new Label("organizationLabel", model.getRequestedUserId() == null ? "-" : model.getRequestedUserId()));
		add(link);
	}

	private static final long serialVersionUID = 1L;
}
