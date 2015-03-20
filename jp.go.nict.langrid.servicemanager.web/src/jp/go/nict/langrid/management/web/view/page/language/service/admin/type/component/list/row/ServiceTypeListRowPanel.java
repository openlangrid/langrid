package jp.go.nict.langrid.management.web.view.page.language.service.admin.type.component.list.row;

import jp.go.nict.langrid.management.web.model.ServiceTypeModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.utility.resource.MessageUtil;
import jp.go.nict.langrid.management.web.view.component.label.HyphenedUrlExtractionLabel;
import jp.go.nict.langrid.management.web.view.page.language.service.admin.type.component.link.ServiceTypeProfileLink;
import jp.go.nict.langrid.management.web.view.page.user.component.link.ExternalHomePageLink;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class ServiceTypeListRowPanel extends Panel{
	/**
	 * 
	 * pe
	 * @throws ServiceManagerException
	 */
	public ServiceTypeListRowPanel(
		String gridId, String componentId, ServiceTypeModel model, String uniqueId)
	throws ServiceManagerException
	{
		super(componentId);
		String id = model.getTypeId();
		ServiceTypeProfileLink link;
		add(link = new ServiceTypeProfileLink("profileLink", model.getDomainId(), id, uniqueId));
		link.add(new Label("profileLinkLabel", model.getTypeName()));
		add(new HyphenedUrlExtractionLabel("description", model.getDescription()));
		String url = MessageUtil.getCoreNodeUrl();
		if( ! url.endsWith("/")) {
			url += "/";
		}
		url += "wsdl/st/" + model.getDomainId() + ":" + model.getTypeId();
		ExternalHomePageLink wlink = new ExternalHomePageLink("wsdlLink", url, model.getTypeId()); 
		add(wlink);
	}
	
	private static final long serialVersionUID = 1L;
}
