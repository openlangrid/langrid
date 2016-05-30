package jp.go.nict.langrid.management.web.view.page.language.component.panel;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import jp.go.nict.langrid.language.InvalidLanguageTagException;
import jp.go.nict.langrid.language.LanguagePath;
import jp.go.nict.langrid.management.web.model.enumeration.MetaAttribute;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.utility.LanguagePathUtil;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;

public class LanguagePathListPanel extends Panel {
   public LanguagePathListPanel(String panelId, MetaAttribute metaAttribute) throws ServiceManagerException {
      super(panelId);
      setOutputMarkupId(true);
      metaAttr = metaAttribute;
      
      if(metaAttribute.equals(MetaAttribute.SUPPORTEDLANGUAGEPAIRS_ANYCOMBINATION)
         || metaAttribute.equals(MetaAttribute.SUPPORTEDLANGUAGEPATHS_ANYCOMBINATION))
      {
         add(new Label("pathType"
            , MessageManager.getMessage("Common.label.Combinations", getLocale())));
      }else if(metaAttribute.equals(MetaAttribute.SUPPORTEDLANGUAGEPAIRS_PAIRLIST)
         || metaAttribute.equals(MetaAttribute.SUPPORTEDLANGUAGEPATHS_PATHLIST))
      {
         add(new Label("pathType"
            , MessageManager.getMessage("Common.label.LanguagePaths", getLocale())));
      }else{
         add(new Label("pathType"
            , MessageManager.getMessage("Common.label.Languages", getLocale())));
      }
      add(repeater = new RepeatingView("repeater"));
      try {
         setLanguagePathLabel(valueMap.values());
      } catch(InvalidLanguageTagException e) {
         throw new ServiceManagerException(e);
      }
   }
   
   public void onDelete(AjaxRequestTarget target, LanguagePath[] paths){
      // noop
   }
   
   public void setLanguagePathLabel(Collection<LanguagePath[]> pathList) throws InvalidLanguageTagException {
      repeater.removeAll();
      for(LanguagePath[] paths : pathList){
         WebMarkupContainer wmc = new WebMarkupContainer(repeater.newChildId());
         repeater.add(wmc);
         wmc.add(new Label("languagePathLabel"
            , LanguagePathUtil.makeLanguagePathString(paths, metaAttr, getLocale())));
         wmc.add(new AjaxLink<WebMarkupContainer>(
         "deleteLink", new Model<WebMarkupContainer>(wmc))
         {
            @Override
            public void onClick(AjaxRequestTarget target) {
               onDelete(target, valueMap.get(getModelObject()));
               valueMap.remove(getModelObject());
               repeater.remove(getModelObject());
            }
         });
         valueMap.put(wmc, paths);
      }
   }
   
   private MetaAttribute metaAttr;
   private RepeatingView repeater;
   private Map<WebMarkupContainer, LanguagePath[]> valueMap = new LinkedHashMap<WebMarkupContainer, LanguagePath[]>();
}
