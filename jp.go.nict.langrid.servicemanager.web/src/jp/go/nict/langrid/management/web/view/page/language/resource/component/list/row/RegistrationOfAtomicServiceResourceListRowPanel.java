package jp.go.nict.langrid.management.web.view.page.language.resource.component.list.row;

import jp.go.nict.langrid.management.web.model.ResourceModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.page.language.service.atomic.admin.RegistrationOfAtomicServiceAdminPage;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class RegistrationOfAtomicServiceResourceListRowPanel 
extends LanguageResourcesListRowPanel
{
	/**
	 * 
	 * 
	 */
	public RegistrationOfAtomicServiceResourceListRowPanel(
			String gridId, String componentId, ResourceModel resource, String uniqueId, final String ownerId)
	throws ServiceManagerException
	{
		super(gridId, componentId, resource, uniqueId);
		languageResource = resource;
		add(new Link<ResourceModel>("register"
				, new Model<ResourceModel>(languageResource))
		{
			@Override
			public void onClick(){
				setResponsePage(
						new RegistrationOfAtomicServiceAdminPage(
						getModelObject().getResourceId(), ownerId));
			}

			private static final long serialVersionUID = 1L;
		}.add(new AttributeAppender("value", new Model<String>(MessageManager.getMessage(
				"Common.label.Register", getLocale())), " ")));
	}

	private ResourceModel languageResource;
	private static final long serialVersionUID = 1L;
}
