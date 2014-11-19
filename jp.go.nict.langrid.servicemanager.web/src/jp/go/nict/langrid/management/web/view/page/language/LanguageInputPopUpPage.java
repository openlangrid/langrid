package jp.go.nict.langrid.management.web.view.page.language;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jp.go.nict.langrid.language.InvalidLanguageTagException;
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.language.LanguagePath;
import jp.go.nict.langrid.language.util.LanguagePathUtil;
import jp.go.nict.langrid.management.web.model.InternalLanguageModel;
import jp.go.nict.langrid.management.web.model.enumeration.LanguagePathDirection;
import jp.go.nict.langrid.management.web.model.enumeration.MetaAttribute;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.view.page.language.component.panel.LanguagePathDirectionTablePanel;
import jp.go.nict.langrid.management.web.view.page.language.component.panel.LanguagePathListPanel;
import jp.go.nict.langrid.management.web.view.page.language.component.panel.LanguageTablePanel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

public class LanguageInputPopUpPage extends LanguageDomainPopUpPage {
   public LanguageInputPopUpPage(ModalWindow modal, MetaAttribute serviceMetaAttribute) {
      callback = modal;
      this.metaAttribute = serviceMetaAttribute;
      add(rewriteContainer = new WebMarkupContainer("rewriteContainer"));
      rewriteContainer.setOutputMarkupId(true);
      
      rewriteContainer.add(languageTablePanel = new LanguageTablePanel("languageTable", serviceMetaAttribute){
         @Override
         public void onSelected(AjaxRequestTarget target) {
            if(isValidPath()){
               selectedPathLabel.setDefaultModelObject(makeLanguagePathString());
               target.addComponent(selectedPathLabel);
            }else{
               selectedPathLabel.setDefaultModelObject("");
            }
            target.addComponent(selectedPathLabel);
         }
      });
      
      try {
         rewriteContainer.add(pathListPanel = new LanguagePathListPanel("languagesPathList", serviceMetaAttribute){
            @Override
            public void onDelete(AjaxRequestTarget target, LanguagePath[] paths) {
               valueMap.remove(LanguagePathUtil.encodeLanguagePathArray(paths));
               target.addComponent(this);
            }
         });
      } catch(ServiceManagerException e) {
         doErrorProcessForPopup(e);
      }
      
      rewriteContainer.add(
         directionTablePanel = new LanguagePathDirectionTablePanel("pathDirectionTable", serviceMetaAttribute){
            @Override
            public void onSelected(AjaxRequestTarget target) {
               selectedPathLabel.setDefaultModelObject(makeLanguagePathString());
               target.addComponent(selectedPathLabel);
            }
         });
      
      rewriteContainer.add(new AjaxLink("close"){
         @Override 
         public void onClick(AjaxRequestTarget target){
            pushAdd = true;
            callback.close(target);
         }});
      rewriteContainer.add(new AjaxLink("addLanguagePath"){
         @Override
         public void onClick(AjaxRequestTarget target) {
            if( ! languageTablePanel.isValidPath()){
               return;
            }
            LanguagePath[] paths = makePathArray();
            valueMap.put(LanguagePathUtil.encodeLanguagePathArray(paths), paths);
            try {
               pathListPanel.setLanguagePathLabel(valueMap.values());
            } catch(InvalidLanguageTagException e) {
               doErrorProcessForPopup(new ServiceManagerException(e));
            }
            target.addComponent(pathListPanel);
         }
      });
      rewriteContainer.add(new AjaxLink("clearLanguage"){
         @Override
         public void onClick(AjaxRequestTarget target) {
            languageTablePanel.clear();
            selectedPathLabel.setDefaultModelObject("");
            target.addComponent(languageTablePanel);
            target.addComponent(selectedPathLabel);
         }
      });
      rewriteContainer.add(selectedPathLabel = new Label("selectedPath", new Model<String>()));
      selectedPathLabel.setOutputMarkupId(true);
   }
   
   public void closeEvent(){
      languageTablePanel.clear();
      directionTablePanel.clear();
      selectedPathLabel.setDefaultModelObject("");
      pushAdd = false;
   }
   
   public boolean isAdd(){
      return pushAdd;
   }
   
   public Collection<LanguagePath[]> getValues(){
      return valueMap.values();
   }
   
   public void setValues(Collection<LanguagePath[]> values) throws ServiceManagerException{
      for(LanguagePath[] paths : values){
         valueMap.put(LanguagePathUtil.encodeLanguagePathArray(paths), paths);
      }
      try {
         pathListPanel.setLanguagePathLabel(valueMap.values());
      } catch(InvalidLanguageTagException e) {
         throw new ServiceManagerException(e);
      }
   }

   public void setValue(LanguagePath[] paths) throws InvalidLanguageTagException{
      valueMap.put(LanguagePathUtil.encodeLanguagePathArray(paths), paths);
      pathListPanel.setLanguagePathLabel(valueMap.values());
   }
     
   private String makeLanguagePathString(){
      StringBuilder sb = new StringBuilder();
      int i = 1;
      List<Language> list = languageTablePanel.getValueList();
      if(languageTablePanel.isSelectWildcard()){
         list.add(InternalLanguageModel.getWildcard());
      }
      for(Language l : list) {
         sb.append(l.getLocalizedName(getLocale()).replaceAll("\\S\\(", " \\("));
         if(i < list.size()){
            if(metaAttribute.equals(MetaAttribute.SUPPORTEDLANGUAGEPAIRS_PAIRLIST)){
               sb.append(" ");
               sb.append(directionTablePanel.getDirection().getCode());
               sb.append(" ");
            }else{
               sb.append(", ");
            }
         }
         i++;
      }
      return sb.toString();
   }
   
   private LanguagePath[] makePathArray(){
      LanguagePath[] paths;
      List<Language> list = languageTablePanel.getValueList();
      if(languageTablePanel.isSelectWildcard()){
         list.add(InternalLanguageModel.getWildcard());
      }
      if(metaAttribute.equals(MetaAttribute.SUPPORTEDLANGUAGEPAIRS_PAIRLIST)){
         if(directionTablePanel.getDirection().equals(LanguagePathDirection.BOTH)){
            paths = new LanguagePath[2];
            paths[0] = new LanguagePath(list.get(0), list.get(1));
            paths[1] = paths[0].reverse();
         }else{
            paths = new LanguagePath[1];
            paths[0] = new LanguagePath(list.get(0), list.get(1));
         }
      }else if(metaAttribute.equals(MetaAttribute.SUPPORTEDLANGUAGEPAIRS_ANYCOMBINATION)
         || metaAttribute.equals(MetaAttribute.SUPPORTEDLANGUAGEPATHS_ANYCOMBINATION)
         || metaAttribute.equals(MetaAttribute.SUPPORTEDLANGUAGEPATHS_PATHLIST))
      {
         paths = new LanguagePath[]{new LanguagePath(list.toArray(new Language[]{}))};
      }else{
         List<LanguagePath> pathList = new ArrayList<LanguagePath>();
         for(Language l : list){
            pathList.add(new LanguagePath(l));
         }
         paths = pathList.toArray(new LanguagePath[]{});
      }
      return paths;
   }
   
   // all container
   private WebMarkupContainer rewriteContainer;
   // input interface
   private LanguageTablePanel languageTablePanel;
   private LanguagePathDirectionTablePanel directionTablePanel;
   // path label
   private Label selectedPathLabel;
   private LanguagePathListPanel pathListPanel;
   // status
   private boolean pushAdd = false;
   private ModalWindow callback;
   private MetaAttribute metaAttribute;
   // values
   private Map<String, LanguagePath[]> valueMap = new LinkedHashMap<String, LanguagePath[]>();
}
