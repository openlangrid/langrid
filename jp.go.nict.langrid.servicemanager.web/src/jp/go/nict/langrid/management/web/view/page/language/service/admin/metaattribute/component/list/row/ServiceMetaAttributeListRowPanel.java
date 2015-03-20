package jp.go.nict.langrid.management.web.view.page.language.service.admin.metaattribute.component.list.row;

import jp.go.nict.langrid.management.web.model.ServiceMetaAttributeModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * 
 * 
 * @author Masato Mori
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class ServiceMetaAttributeListRowPanel extends Panel{
	/**
	 * 
	 * pe
	 * @throws ServiceManagerException
	 */
	public ServiceMetaAttributeListRowPanel(
		String gridId, String componentId, ServiceMetaAttributeModel model, String uniqueId)
	throws ServiceManagerException
	{
		super(componentId);
		add(new Label("id", model.getAttributeId()));
		add(new Label("name", model.getAttributeName()));
		add(new Label("description", model.getDescription()));
	}

	private static final long serialVersionUID = 1L;
}
