package jp.go.nict.langrid.management.web.view.page.language.service.admin.protocol.component.list.row;

import jp.go.nict.langrid.management.web.model.ProtocolModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.component.link.ConfirmLink;
import jp.go.nict.langrid.management.web.view.page.language.service.admin.protocol.EditOfServiceProtocolPage;

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
public class ProtocolListRowAdminPanel extends Panel{
	/**
	 * 
	 * pe
	 * @throws ServiceManagerException
	 */
	public ProtocolListRowAdminPanel(
		String gridId, String componentId, ProtocolModel model, String uniqueId)
	throws ServiceManagerException
	{
		super(componentId);
		String id = model.getProtocolId();
		add(new Label("id", id));
		add(new Label("name", model.getProtocolName()));
		add(new Label("description", model.getDescription()));
		add(new Link<String>("edit", new Model<String>(id)) {
		   @Override
		   public void onClick() {
		      setResponsePage(new EditOfServiceProtocolPage(getModelObject()));
		   }
		});
		add(new ConfirmLink<String>("unregister", new Model<String>(id)
		   , MessageManager.getMessage("LanguageGridOperator.protocol.message.unregister.Confirm"))
		{
		   @Override
		   public void onClick() {
		      doDeleteProcess(getModelObject());
		   }
		});
	}
	
	protected void doDeleteProcess(String id) {
	   // noop
	}

	private String gridId;
	
	private static final long serialVersionUID = 1L;
}
