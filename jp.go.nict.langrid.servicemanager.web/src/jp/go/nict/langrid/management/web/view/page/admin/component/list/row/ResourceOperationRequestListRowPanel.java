package jp.go.nict.langrid.management.web.view.page.admin.component.list.row;

import jp.go.nict.langrid.management.web.model.OperationRequestModel;
import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.DateUtil;
import jp.go.nict.langrid.management.web.utility.StringUtil;
import jp.go.nict.langrid.management.web.view.page.language.resource.admin.OperationOfAuthorizeRequestedResourcePage;
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
 * @version $Revision: 303 $
 */
public class ResourceOperationRequestListRowPanel extends Panel{
	/**
	 * 
	 * 
	 * @throws ServiceManagerException
	 */
	public ResourceOperationRequestListRowPanel(
		String gridId, String componentId, OperationRequestModel operation, String uniqueId)
	throws ServiceManagerException
	{
		super(componentId);
		add(new Label("date", DateUtil.formatYMDWithSlash(
				operation.getCreatedDateTime().getTime())));
		Label label = new Label("operation", operation.getContents());
		label.setEscapeModelStrings(false);
		add(label);
		UserProfileLink link = new UserProfileLink(
				"organization", operation.getGridId(), operation.getRequestedUserId(), uniqueId);
		UserModel user = ServiceFactory.getInstance().getUserService(gridId).get(operation.getRequestedUserId());
		link.add(new Label("organizationLabel", StringUtil.shortenString(user.getOrganization(), 24)));
		add(link);
		
		add(new Link<OperationRequestModel>("operate", new Model<OperationRequestModel>(operation)){
			@Override
			public void onClick() {
				setResponsePage(new OperationOfAuthorizeRequestedResourcePage(getModelObject().getTargetId()));
			}
		});
	}

	private static final long serialVersionUID = 1L;
}
