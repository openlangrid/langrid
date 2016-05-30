package jp.go.nict.langrid.management.web.view.page.language.service.admin.metaattribute.component.text;

import jp.go.nict.langrid.management.web.view.component.text.IdTextField;
import jp.go.nict.langrid.management.web.view.component.validator.HalfSizeAlphaNumeralSiglumValidator;
import jp.go.nict.langrid.management.web.view.component.validator.LengthValidator;
import jp.go.nict.langrid.management.web.view.page.language.service.admin.metaattribute.component.form.validator.AlreadyExistsServiceMetaAttributeIdValidator;

public class ServiceMetaAttributeIdTextField extends IdTextField {
   public ServiceMetaAttributeIdTextField(String componentId, String gridId) {
      super(componentId, gridId, true);
   }
   
   @Override
   protected void addValidater(String gridId) {
      add(new HalfSizeAlphaNumeralSiglumValidator("LanguageGridOperator.metaattribute.error.validate.Id"));
      add(new LengthValidator(4, 254, "LanguageGridOperator.metaattribute.error.validate.Id"));
      add(new AlreadyExistsServiceMetaAttributeIdValidator(gridId, this));
   }
}
