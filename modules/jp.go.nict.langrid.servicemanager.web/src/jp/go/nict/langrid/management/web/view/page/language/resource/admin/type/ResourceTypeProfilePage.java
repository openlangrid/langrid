package jp.go.nict.langrid.management.web.view.page.language.resource.admin.type;

import jp.go.nict.langrid.management.web.model.ResourceMetaAttributeModel;
import jp.go.nict.langrid.management.web.model.ResourceTypeModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.view.component.label.HyphenedLabel;
import jp.go.nict.langrid.management.web.view.component.label.HyphenedUrlExtractionLabel;
import jp.go.nict.langrid.management.web.view.page.PopupPage;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.RepeatingView;

public class ResourceTypeProfilePage extends PopupPage {
   public ResourceTypeProfilePage(PageParameters parametars) {
      if(parametars.getString("id") == null){
         redirectTop();
      }
      try {
         String typeId = parametars.getString("id");
         String domainId = parametars.getString("domainId");
         String gridId = getSelfGridId();
         ServiceFactory factory = ServiceFactory.getInstance();
         ResourceTypeModel model = factory.getResourceTypeService(gridId).get(domainId, typeId);

         add(new Label("id", model.getResourceTypeId()));
         add(new HyphenedLabel("name", model.getResourceTypeName()));
         String domainName = factory.getDomainService(gridId).get(model.getDomainId()).getDomainName();
         add(new Label("domainName", domainName));
         add(new HyphenedUrlExtractionLabel("description", model.getDescription()));
         // meta attributes
         RepeatingView attrView = new RepeatingView("attributes");
         add(attrView);
         for(ResourceMetaAttributeModel smam : model.getMetaAttrbuteList()){
            WebMarkupContainer wmc = new WebMarkupContainer(attrView.newChildId());
            attrView.add(wmc);
            // TODO getAttributeName()?
            wmc.add(new Label("attrName", smam.getAttributeId()));
//            wmc.add(new Label("attrDescription", smam.getDescription()));
         }
      } catch(ServiceManagerException e) {
         doErrorProcessForPopup(e);
      }
   }
}
