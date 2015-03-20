package jp.go.nict.langrid.management.web.view.component.panel;

import jp.go.nict.langrid.management.web.view.component.modal.AdvancedSearchModalWindow;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;

public class SearchTextPanel extends Panel {
   public SearchTextPanel(String gridId, String panelId) {
      super(panelId);
      add(form = new StatelessForm<String>("searchForm"){
         protected void onSubmit() {
            doSearch(searchText.getModelObject());
         };
      });
      form.add(searchText = new TextField<String>("searchText"));
      form.add(modal = new AdvancedSearchModalWindow(gridId, "advancedSearch"));
      form.add(new AjaxLink<String>("advancedSearchLink") {
         @Override
         public void onClick(AjaxRequestTarget target) {
            modal.show(target);
         }
      });
   }
   
   public void doSearch(String searchText) {
      // noop
   }
   
   private AdvancedSearchModalWindow modal;
   private Form form;
   private TextField<String> searchText;
   
   private enum SearchModel {
      USER
      , SERVICE
      , RESOURCE
      , GRID
      , DOMAIN
      , FEDERATION
   }
}
