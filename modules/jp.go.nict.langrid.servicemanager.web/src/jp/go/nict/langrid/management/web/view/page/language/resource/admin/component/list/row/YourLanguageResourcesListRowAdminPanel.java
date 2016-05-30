package jp.go.nict.langrid.management.web.view.page.language.resource.admin.component.list.row;

import jp.go.nict.langrid.management.web.model.ResourceModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.view.page.language.resource.admin.OperationOfAuthorizeRequestedResourceAdminPage;
import jp.go.nict.langrid.management.web.view.page.language.resource.admin.UnregistrationOfLanguageResourcesAdminPage;
import jp.go.nict.langrid.management.web.view.page.language.resource.admin.YourLanguageResourcesEditAdminPage;
import jp.go.nict.langrid.management.web.view.page.language.resource.component.list.row.YourLanguageResourcesListRowPanel;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class YourLanguageResourcesListRowAdminPanel extends YourLanguageResourcesListRowPanel{
	/**
	 * 
	 * 
	 */
	public YourLanguageResourcesListRowAdminPanel(
			String gridId, String componentId, ResourceModel resource, String uniqueId)
	throws ServiceManagerException
	{
		super(gridId, componentId, resource, uniqueId);
		Link approve;
		add(approve = new Link<ResourceModel>(
			"approve", new Model<ResourceModel>(resource)) 
		{
			@Override
			public void onClick() {
				setResponsePage(new OperationOfAuthorizeRequestedResourceAdminPage(
				   getModelObject().getResourceId()));
			}
		});
		approve.setVisible( ! resource.isApproved());
	}
	
	@Override
	protected Page getDoEditPage(ResourceModel resource){
		return new YourLanguageResourcesEditAdminPage(resource.getResourceId());
	}
	
	@Override
	protected Page getDoUnregisterPage(ResourceModel resource){
		return new UnregistrationOfLanguageResourcesAdminPage(resource);
	}
	
	private static final long serialVersionUID = 1L;
}
