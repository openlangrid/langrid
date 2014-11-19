package jp.go.nict.langrid.management.web.view.component.modal;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;

public class AdvancedSearchModalWindow extends ModalWindow {
   public AdvancedSearchModalWindow(String gridId, String windowId) {
      super(windowId);
      formPage = new AdvancedServiceSearchFormPage(gridId);
      setInitialHeight(210);
      setInitialWidth(460);
      setTitle("Advanced Service Search");
      setPageCreator(new ModalWindow.PageCreator(){
          public Page createPage(){
             return formPage;
          }
      });
      setWindowClosedCallback(new WindowClosedCallback() {
         @Override
         public void onClose(AjaxRequestTarget target) {
         }
      });
   }
   
   private AdvancedServiceSearchFormPage formPage;
}
