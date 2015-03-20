package jp.go.nict.langrid.management.web.view.page.language.component.panel;

import java.util.ArrayList;
import java.util.List;

import jp.go.nict.langrid.language.InvalidLanguageTagException;
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.management.web.model.InternalLanguageModel;
import jp.go.nict.langrid.management.web.model.enumeration.MetaAttribute;
import jp.go.nict.langrid.management.web.view.component.link.CssClassChangableLink;
import jp.go.nict.langrid.management.web.view.model.provider.LanguageDataProvider;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.GridView;
import org.apache.wicket.model.Model;

public class LanguageTablePanel extends Panel {
   public LanguageTablePanel(String panelId, MetaAttribute metaAttribute) {
      super(panelId);
      setOutputMarkupId(true);
      metaAttr = metaAttribute;
      
      add(languageTable = new GridView<Language>("languages", new LanguageDataProvider(getLocale())){
         @Override
         protected void populateItem(Item<Language> item) {
            CssClassChangableLink<Language> a = new CssClassChangableLink<Language>(
               "languageSelect", new Model<Language>(item.getModelObject()))
            {
               @Override
               public void onClick(AjaxRequestTarget target) {
                  if(isSetCssClass()){
                     setCssClass("");
                     selectedLanguageList.remove(this);
                  }else{
                     if(metaAttr.equals(MetaAttribute.SUPPORTEDLANGUAGEPAIRS_PAIRLIST)
                        && 2 <= selectedLanguageList.size())
                     {
                        return;
                     }
                     setCssClass(cssClassName);
                     selectedLanguageList.add(this);
                  }
                  target.addComponent(this);
                  onSelected(target);
               }
            };
            try {
               a.add(new Label("language", InternalLanguageModel.getDisplayString(
                  item.getModelObject().getCode(), getLocale()).replaceAll("\\S\\(", " \\(")));
            } catch(InvalidLanguageTagException e) {
               a.add(new Label("language", item.getModelObject().getLocalizedName(getLocale()).replaceAll("\\S\\(", " \\(")));
            }
            a.setOutputMarkupId(true);
            item.add(a);
         }
         @Override
         protected void populateEmptyItem(Item<Language> item) {
            Link<Language> a = new Link<Language>("languageSelect"){@Override public void onClick() {}};
            a.add(new Label("language", ""));
            a.setVisible(false);
            item.add(a);
         }
      });
      languageTable.setColumns(6);      
   }
   
   public boolean isValidPath(){
      return
          (! metaAttr.equals(MetaAttribute.SUPPORTEDLANGUAGEPAIRS_PAIRLIST)
             && 0 < selectedLanguageList.size())
          || (metaAttr.equals(MetaAttribute.SUPPORTEDLANGUAGEPAIRS_PAIRLIST)
             && selectedLanguageList.size() == 2)
          ||(metaAttr.equals(MetaAttribute.SUPPORTEDLANGUAGEPAIRS_PAIRLIST)
             && selectedLanguageList.size() == 1
             && selectedLanguageList.get(0).getModelObject().equals(InternalLanguageModel.getWildcard())
          );
   }
   
   public List<Language> getValueList(){
      List<Language> list = new ArrayList<Language>();
      for(CssClassChangableLink<Language> link : selectedLanguageList){
         list.add(link.getModelObject());
      }
      return list;
   }
   
   public void clear(){
      for(CssClassChangableLink<Language> links : selectedLanguageList) {
         links.setCssClass("");
      }
      selectedLanguageList = new ArrayList<CssClassChangableLink<Language>>();
   }
   
   public void onSelected(AjaxRequestTarget target){
      // noop
   }
   
   public boolean isSelectWildcard(){
      return selectedLanguageList.size() == 1
         && selectedLanguageList.get(0).getModelObject().equals(InternalLanguageModel.getWildcard());
   }
   
   private List<CssClassChangableLink<Language>> selectedLanguageList = new ArrayList<CssClassChangableLink<Language>>();
   private GridView<Language> languageTable;
   private MetaAttribute metaAttr;
   private String cssClassName = "selected-link";
}
