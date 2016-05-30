package jp.go.nict.langrid.management.web.view.page.language.service.admin.component.form.validator;

import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.page.language.service.admin.component.form.panel.FileMultipleUploadPanel;
import jp.go.nict.langrid.management.web.view.page.language.service.admin.component.form.panel.InterfaceDefinitionFormPanel;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.IFormValidator;

public class InterfaceFileUploadValidator implements IFormValidator {
   public InterfaceFileUploadValidator(InterfaceDefinitionFormPanel panel) {
      this.panel = panel;
   }

   @Override
   public FormComponent<?>[] getDependentFormComponents() {
      return null;
   }

   @Override
   public void validate(Form<?> form) {
      for(FileMultipleUploadPanel upload : panel.getFileUploadComponentList()){
         if(upload.isEmpty()){
            form.error(MessageManager.getMessage(
               "LanguageGridOperator.type.service.error.validate.protocol.file.Required", form.getLocale()));
            return;
         }
      }
   }
   
   private InterfaceDefinitionFormPanel panel;
}
