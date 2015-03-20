package jp.go.nict.langrid.management.web.view.page.language.resource.admin.metaattribute.component.list.row;

import jp.go.nict.langrid.management.web.model.ResourceMetaAttributeModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.component.label.HyphenedLabel;
import jp.go.nict.langrid.management.web.view.component.link.ConfirmLink;
import jp.go.nict.langrid.management.web.view.page.language.resource.admin.metaattribute.EditPage;

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
public class ListRowPanel extends Panel{
	/**
	 * 
	 * 
	 */
	public ListRowPanel(
			String gridId, String componentId, final ResourceMetaAttributeModel model, String uniqueId)
	throws ServiceManagerException
	{
		super(componentId);
		add(new Label("Id", model.getAttributeId()));
		add(new HyphenedLabel("Name", model.getAttributeName()));
		add(new HyphenedLabel("Description", model.getDescription()));
		add(new Link("edit"){
			@Override
			public void onClick(){
				setResponsePage(new EditPage(model));
			}
		});
		add(new ConfirmLink<String>("unregister", new Model<String>()
				, MessageManager.getMessage("LanguageGridOperator.resourcemetaattribute.message.unregister.Confirm"))
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
	
	private static final long serialVersionUID = 1L;
}
