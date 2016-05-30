package jp.go.nict.langrid.management.web.view.page.language.service.admin.component.form.validator;

import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.page.language.service.admin.component.form.panel.InterfaceDefinitionFormPanel;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.IFormValidator;

public class SameInterfaceProtocolValidator implements IFormValidator {
   public SameInterfaceProtocolValidator(InterfaceDefinitionFormPanel panel) {
      this.panel = panel;
   }

   @Override
   public FormComponent<?>[] getDependentFormComponents() {
      return null;
   }

   @Override
   public void validate(Form<?> form) {
      for(int i = 0; i < (panel.getProtocolList().size() - 1); i++){
         if(panel.getProtocolList().get(i).equals(panel.getProtocolList().get(i + 1))){
            form.error(MessageManager.getMessage(
               "LanguageGridOperator.type.service.error.validate.protocol.Id", form.getLocale()));
            return;
         }
      }
   }
   
   private InterfaceDefinitionFormPanel panel;
}
