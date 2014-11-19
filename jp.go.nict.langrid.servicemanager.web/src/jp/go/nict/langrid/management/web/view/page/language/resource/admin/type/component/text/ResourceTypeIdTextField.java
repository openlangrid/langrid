package jp.go.nict.langrid.management.web.view.page.language.resource.admin.type.component.text;

import jp.go.nict.langrid.management.web.view.component.text.IdTextField;
import jp.go.nict.langrid.management.web.view.component.validator.HalfSizeAlphaNumeralSiglumValidator;
import jp.go.nict.langrid.management.web.view.component.validator.LengthValidator;
import jp.go.nict.langrid.management.web.view.page.language.resource.admin.type.component.form.validator.AlreadyExistsResourceTypeIdValidator;

public class ResourceTypeIdTextField extends IdTextField {
   public ResourceTypeIdTextField(String componentId, String gridId) {
      super(componentId, gridId, true);
   }
   
   @Override
   protected void addValidater(String gridId) {
      add(new HalfSizeAlphaNumeralSiglumValidator("LanguageGridOperator.resourcetype.error.validate.Id"));
      add(new LengthValidator(4, 254, "LanguageGridOperator.resourcetype.error.validate.Id"));
      add(new AlreadyExistsResourceTypeIdValidator(gridId, this));
   }
}
