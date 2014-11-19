package jp.go.nict.langrid.management.web.view.page.language.service.admin.protocol.component.list.row;

import jp.go.nict.langrid.management.web.model.ProtocolModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class ProtocolListRowPanel extends Panel{
	/**
	 * 
	 * pe
	 * @throws ServiceManagerException
	 */
	public ProtocolListRowPanel(
		String gridId, String componentId, ProtocolModel model, String uniqueId)
	throws ServiceManagerException
	{
		super(componentId);
		add(new Label("id", model.getProtocolId()));
		add(new Label("name", model.getProtocolName()));
		add(new Label("description", model.getDescription()));
	}

	private static final long serialVersionUID = 1L;
}
