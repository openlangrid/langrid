package jp.go.nict.langrid.management.web.view.page.language.component.panel;

import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.management.web.model.enumeration.LanguagePathDirection;
import jp.go.nict.langrid.management.web.model.enumeration.MetaAttribute;
import jp.go.nict.langrid.management.web.view.component.link.CssClassChangableLink;
import jp.go.nict.langrid.management.web.view.model.provider.DirectionDataProvider;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.GridView;
import org.apache.wicket.model.Model;

public class LanguagePathDirectionTablePanel extends Panel {
   public LanguagePathDirectionTablePanel(String panelId, MetaAttribute metaAttribute) {
      super(panelId);

      setOutputMarkupId(true);
      setVisible(metaAttribute.equals(MetaAttribute.SUPPORTEDLANGUAGEPAIRS_PAIRLIST));
      
      DirectionDataProvider ddp = new DirectionDataProvider();
      add(directionTable = new GridView<LanguagePathDirection>("directions", ddp){
         @Override
         protected void populateItem(Item<LanguagePathDirection> item) {
            CssClassChangableLink<LanguagePathDirection> a = new CssClassChangableLink<LanguagePathDirection>(
               "selectLink", new Model<LanguagePathDirection>(item.getModelObject()))
            {
               @Override
               public void onClick(AjaxRequestTarget target) {
                  if(selectedLink == this){
                     return;
                  }
                  selectedLink.setCssClass("");
                  target.addComponent(selectedLink);
                  selectedLink = this;
                  setCssClass("selected-link");
                  target.addComponent(this);
                  onSelected(target);
               }
            };
            if(selectedLink == null){
               selectedLink = a;
               selectedLink.setCssClass("selected-link");
            }
            a.add(new Label("direction", item.getModelObject().getCode()));
            a.setOutputMarkupId(true);
            item.add(a);
         }
         @Override
         protected void populateEmptyItem(Item<LanguagePathDirection> item) {
            Link<Language> a = new Link<Language>("directionSelect"){@Override public void onClick() {}};
            a.add(new Label("direction", ""));
            a.setVisible(false);
            item.add(a);
         }
      });
      directionTable.setColumns(ddp.size());
   }
   
   public void onSelected(AjaxRequestTarget target){
      // noop
   }
   
   public void clear(){
      selectedLink = null;
   }
   
   public LanguagePathDirection getDirection(){
      return selectedLink.getModelObject();
   }
   
   private GridView<LanguagePathDirection> directionTable;
   private CssClassChangableLink<LanguagePathDirection> selectedLink;
}
