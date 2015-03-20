package jp.go.nict.langrid.management.web.view.page.language.service.admin.type;

import jp.go.nict.langrid.management.web.model.ServiceTypeModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.resource.MessageUtil;
import jp.go.nict.langrid.management.web.view.component.label.HyphenedLabel;
import jp.go.nict.langrid.management.web.view.component.label.HyphenedUrlExtractionLabel;
import jp.go.nict.langrid.management.web.view.page.PopupPage;
import jp.go.nict.langrid.management.web.view.page.user.component.link.ExternalHomePageLink;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;

public class ServiceTypeProfilePage extends PopupPage {
	public ServiceTypeProfilePage(PageParameters parametars) {
		String typeId = parametars.getString("id");
		String domainId = parametars.getString("domainId");
		if(domainId == null || typeId == null) {
			redirectTop();
		}
		try {
			String gridId = getSelfGridId();
			ServiceFactory factory = ServiceFactory.getInstance();
			ServiceTypeModel model = factory.getServiceTypeService(gridId).get(domainId,
				typeId);
			add(new Label("id", model.getTypeId()));
			add(new HyphenedLabel("name", model.getTypeName()));
			add(new HyphenedUrlExtractionLabel("description", model.getDescription()));
			String url = MessageUtil.getCoreNodeUrl();
			if(!url.endsWith("/")) {
				url += "/";
			}
			url += "wsdl/st/" + model.getDomainId() + ":" + model.getTypeId();
			ExternalHomePageLink wlink = new ExternalHomePageLink("wsdlLink", url,
				model.getTypeId());
			add(wlink);
		} catch(ServiceManagerException e) {
			doErrorProcessForPopup(e);
		}
	}
}
