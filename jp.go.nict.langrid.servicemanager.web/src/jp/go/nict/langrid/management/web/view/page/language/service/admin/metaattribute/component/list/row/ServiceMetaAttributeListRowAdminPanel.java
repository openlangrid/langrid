package jp.go.nict.langrid.management.web.view.page.language.service.admin.metaattribute.component.list.row;

import jp.go.nict.langrid.management.web.model.ServiceMetaAttributeModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.component.link.ConfirmLink;
import jp.go.nict.langrid.management.web.view.page.language.service.admin.metaattribute.EditOfServiceMetaAttributePage;

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
public class ServiceMetaAttributeListRowAdminPanel extends Panel{
	/**
	 * 
	 * pe
	 * @throws ServiceManagerException
	 */
	public ServiceMetaAttributeListRowAdminPanel(
			String gridId, String componentId, final ServiceMetaAttributeModel model, String uniqueId)
			throws ServiceManagerException
	{
		super(componentId);
		String id = model.getAttributeId();
		add(new Label("id", id));
		add(new Label("name", model.getAttributeName()));
		add(new Label("description", model.getDescription()));
		add(new Link<String>("edit", new Model<String>(id)) {
			@Override
			public void onClick() {
				setResponsePage(new EditOfServiceMetaAttributePage(model.getDomainId(), getModelObject()));
			}
		});
		add(new ConfirmLink<String>("unregister", new Model<String>(id)
		, MessageManager.getMessage("LanguageGridOperator.protocol.message.unregister.Confirm"))
		{
			@Override
			public void onClick() {
				doDeleteProcess(model.getDomainId(), model.getAttributeId());
			}
		});
	}

	protected void doDeleteProcess(String domainId, String id) {
		// noop
	}

	private String gridId;

	private static final long serialVersionUID = 1L;
}
