package jp.go.nict.langrid.management.web.view.page.language.service.admin.component.list.row;

import jp.go.nict.langrid.management.web.model.ServiceModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.view.page.language.service.admin.OperationOfAuthorizeRequestedServiceAdminPage;
import jp.go.nict.langrid.management.web.view.page.language.service.admin.UnregistrationOfLanguageServicesAdminPage;
import jp.go.nict.langrid.management.web.view.page.language.service.atomic.admin.AtomicServiceEditAdminPage;
import jp.go.nict.langrid.management.web.view.page.language.service.component.list.row.YourLanguageServicesListRowPanel;
import jp.go.nict.langrid.management.web.view.page.language.service.composite.admin.BpelCompositeServiceEditAdminPage;
import jp.go.nict.langrid.management.web.view.page.language.service.composite.admin.JavaCompositeServiceEditAdminPage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class YourLanguageServicesListRowAdminPanel extends YourLanguageServicesListRowPanel{
	/**
	 * 
	 * 
	 */
	public YourLanguageServicesListRowAdminPanel(
			String componentId, ServiceModel model, String uniqueId)
	throws ServiceManagerException
	{
		super(componentId, model, uniqueId);
		Link<String> authorizeLink = new Link<String>(
			"authorize", new Model<String>(model.getServiceId())) 
		{
			@Override
			public void onClick() {
				setResponsePage(new OperationOfAuthorizeRequestedServiceAdminPage(getModelObject()));
			}
		};
		authorizeLink.setVisible( ! model.isApproved());
		add(authorizeLink);
	}

	@Override
	protected Page getEditAtomicServicePage(String serviceId){
		return new AtomicServiceEditAdminPage(serviceId);
	}
	
	@Override
	protected Page getEditJavaCompositeServicePage(String serviceId) {
		return new JavaCompositeServiceEditAdminPage(serviceId);
	}
	
	@Override
	protected Page getEditCompositeServicePage(String serviceId){
		return new BpelCompositeServiceEditAdminPage(serviceId);
	}
	
	@Override
	protected Page getUnregisterServicePage(String serviceId){
		return new UnregistrationOfLanguageServicesAdminPage(serviceId);
	}
	
}
