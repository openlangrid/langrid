package jp.go.nict.langrid.management.web.view.page.language.service.admin.type.component.list.row;

import jp.go.nict.langrid.management.web.model.ServiceTypeModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.utility.resource.MessageUtil;
import jp.go.nict.langrid.management.web.view.component.label.HyphenedUrlExtractionLabel;
import jp.go.nict.langrid.management.web.view.component.link.ConfirmLink;
import jp.go.nict.langrid.management.web.view.page.language.service.admin.type.EditOfServiceTypePage;
import jp.go.nict.langrid.management.web.view.page.language.service.admin.type.component.link.ServiceTypeProfileLink;

import org.apache.wicket.extensions.markup.html.basic.SmartLinkLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class ServiceTypeListRowAdminPanel extends Panel {
	/**
	 * 
	 * pe
	 * @throws ServiceManagerException
	 */
	public ServiceTypeListRowAdminPanel(
		String gridId, String componentId, final ServiceTypeModel model, String uniqueId)
	throws ServiceManagerException {
		super(componentId);
		String id = model.getTypeId();
		ServiceTypeProfileLink link;
		add(link = new ServiceTypeProfileLink("profileLink", model.getDomainId(), id,
			uniqueId));
		link.add(new Label("profileLinkLabel", model.getTypeName()));
		add(new HyphenedUrlExtractionLabel("description", model.getDescription()));
		String url = MessageUtil.getCoreNodeUrl();
		if( ! url.endsWith("/")) {
			url += "/";
		}
		url += "wsdl/st/" + model.getDomainId() + ":" + model.getTypeId();
		add(new SmartLinkLabel("wsdlLink", new Model<String>(url)));
		add(new Link<String>("edit", new Model<String>(id)) {
			@Override
			public void onClick() {
				setResponsePage(new EditOfServiceTypePage(model.getDomainId(), getModelObject()));
			}
		});
		add(new ConfirmLink<String>("unregister", new Model<String>(id)
			, MessageManager.getMessage(
				"LanguageGridOperator.protocol.message.unregister.Confirm")) {
			@Override
			public void onClick() {
				doDeleteProcess(model.getDomainId(), model.getTypeId());
			}
		});
	}

	protected void doDeleteProcess(String domainId, String id) {
		// noop
	}

	private String gridId;
	private static final long serialVersionUID = 1L;
}
