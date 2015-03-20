package jp.go.nict.langrid.management.web.view.page.language.resource.admin.type.component.list.row;

import jp.go.nict.langrid.management.web.model.ResourceTypeModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.component.link.ConfirmLink;
import jp.go.nict.langrid.management.web.view.page.language.resource.admin.type.EditPage;
import jp.go.nict.langrid.management.web.view.page.language.resource.admin.type.component.link.ResourceTypeProfileLink;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masato Mori
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class ResourceTypeListRowPanel extends Panel{
	/**
	 * 
	 * 
	 */
	public ResourceTypeListRowPanel(
			String gridId, String componentId, ResourceTypeModel entry, String uniqueId)
	throws ServiceManagerException
	{
		super(componentId);
		resourceTypeEntry = entry;
		domainId = entry.getDomainId();
		typeId = entry.getResourceTypeId();
		ResourceTypeProfileLink link;
		add(link = new ResourceTypeProfileLink("profileLink", entry.getDomainId(), entry.getResourceTypeId(), uniqueId));
		link.add(new Label("profileLinkLabel", entry.getResourceTypeId()));
		add(new Label("Description", entry.getDescription()));
		add(new Link("edit"){
			@Override
			public void onClick(){
				setResponsePage(new EditPage(resourceTypeEntry));
			}

		});
		add(new ConfirmLink<String>("unregister", new Model<String>()
				, MessageManager.getMessage("LanguageGridOperator.resourcetype.message.unregister.Confirm"))
		{
			@Override
			public void onClick() {
				doDeleteProcess(domainId, typeId);
			}
		});
	}

	private ResourceTypeModel resourceTypeEntry;

	protected void doDeleteProcess(String domainId, String typeId) {
	   // noop
	}
	
	private String domainId;
	private String typeId;
	
	private static final long serialVersionUID = 1L;
}
