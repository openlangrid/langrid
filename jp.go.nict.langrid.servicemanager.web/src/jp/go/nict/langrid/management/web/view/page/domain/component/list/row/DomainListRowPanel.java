package jp.go.nict.langrid.management.web.view.page.domain.component.list.row;

import jp.go.nict.langrid.management.web.model.DomainModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.component.link.ConfirmLink;
import jp.go.nict.langrid.management.web.view.page.domain.EditPage;

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
public class DomainListRowPanel extends Panel{
	/**
	 * 
	 * 
	 */
	public DomainListRowPanel(
			String gridId, String componentId, DomainModel entry, String uniqueId)
	throws ServiceManagerException
	{
		super(componentId);
		domainEntry = entry;
		add(new Label("Id", entry.getDomainId()));
		add(new Label("Name", entry.getDomainName()));
		add(new Label("Description", entry.getDescription()));
		add(new Link("edit"){
			@Override
			public void onClick(){
				setResponsePage(new EditPage(domainEntry));
			}

		});
		add(new ConfirmLink<String>("unregister", new Model<String>(entry.getDomainId())
				, MessageManager.getMessage("LanguageGridOperator.domainSettings.message.unregister.Confirm"))
		{
			@Override
			public void onClick() {
				doDeleteProcess(getModelObject());
			}
		});
	}

	private DomainModel domainEntry;

	protected void doDeleteProcess(String id) {
	}
	private static final long serialVersionUID = 1L;
}
