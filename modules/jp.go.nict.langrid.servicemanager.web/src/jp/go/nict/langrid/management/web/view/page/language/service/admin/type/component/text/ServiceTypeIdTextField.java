package jp.go.nict.langrid.management.web.view.page.language.service.admin.type.component.text;

import jp.go.nict.langrid.management.web.view.component.text.IdTextField;
import jp.go.nict.langrid.management.web.view.component.validator.HalfSizeAlphaNumeralSiglumValidator;
import jp.go.nict.langrid.management.web.view.component.validator.LengthValidator;
import jp.go.nict.langrid.management.web.view.page.language.service.admin.type.component.form.validator.AlreadyExistsServiceTypeIdValidator;

public class ServiceTypeIdTextField extends IdTextField {
   public ServiceTypeIdTextField(String componentId, String gridId) {
      super(componentId, gridId, true);
   }
   
   @Override
   protected void addValidater(String gridId) {
      add(new HalfSizeAlphaNumeralSiglumValidator("LanguageGridOperator.type.service.error.validate.Id"));
      add(new LengthValidator(4, 254, "LanguageGridOperator.type.service.error.validate.Id"));
      add(new AlreadyExistsServiceTypeIdValidator(gridId, this));
   }
}
