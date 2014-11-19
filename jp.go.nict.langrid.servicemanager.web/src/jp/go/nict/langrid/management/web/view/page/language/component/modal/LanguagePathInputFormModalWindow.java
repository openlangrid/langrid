package jp.go.nict.langrid.management.web.view.page.language.component.modal;

import java.util.Collection;

import jp.go.nict.langrid.language.LanguagePath;
import jp.go.nict.langrid.management.web.model.enumeration.MetaAttribute;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.language.LanguageInputPopUpPage;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;

public class LanguagePathInputFormModalWindow extends ModalWindow {
   public LanguagePathInputFormModalWindow(
      String windowId, final MetaAttribute metaAttribute)
   {
      super(windowId);
      formPage = new LanguageInputPopUpPage(this, metaAttribute);
      setInitialHeight(600);
      setInitialWidth(770);
      setTitle("Language Path Form");
      setPageCreator(new ModalWindow.PageCreator(){
          public Page createPage(){
             return formPage;
          }
      });
      setWindowClosedCallback(new WindowClosedCallback() {
         @Override
         public void onClose(AjaxRequestTarget target) {
            if(formPage.isAdd()) {
               try {
                  addLanguagePathCallBack(target, formPage.getValues(), metaAttribute);
               } catch(ServiceManagerException e) {
                  ((ServiceManagerPage)getPage()).doErrorProcess(new ServiceManagerException(e));
               }
            }
            formPage.closeEvent();
         }
      });
   }
   
   public void addLanguagePathCallBack(
      AjaxRequestTarget target, Collection<LanguagePath[]> modelList, MetaAttribute metaAttribute)
   throws ServiceManagerException
   {
      // noop
   }

   public void setPath(Collection<LanguagePath[]> paths) throws ServiceManagerException {
      formPage.setValues(paths);
   }

   public Collection<LanguagePath[]> getValues() {
      return formPage.getValues();
   }
   
   public LanguagePath[] getPathArray(){
      LanguagePath[] paths = new LanguagePath[]{};
      for(LanguagePath[] originalPaths : formPage.getValues()){
         LanguagePath[] temp = new LanguagePath[paths.length + originalPaths.length];
         int i = 0;
         for(LanguagePath path : paths){
            temp[i++] = path;
         }
         for(LanguagePath path : originalPaths){
            temp[i++] = path;
         }
         paths = temp;
      }
      return paths;
   }

   private LanguageInputPopUpPage formPage;
}
