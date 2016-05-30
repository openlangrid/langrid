package jp.go.nict.langrid.management.web.view.page.language.service.admin.component.form.panel;

import java.util.List;

import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;


public class ServiceInterfacePanel extends Panel {
   public ServiceInterfacePanel(String panelId, String protocolName, List<String>fileList)
   throws ServiceManagerException 
   {
      super(panelId);

      add(new Label("protocol", protocolName));
      RepeatingView rv = new RepeatingView("files");
      add(rv);
      for (String name : fileList) {
         WebMarkupContainer wmc = new WebMarkupContainer(rv.newChildId());
         wmc.add(new Label("fileName", name));
         rv.add(wmc);
      }
   }
}
