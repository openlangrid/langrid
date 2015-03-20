package jp.go.nict.langrid.management.web.view.page.language.service.admin.protocol.component.text;

import jp.go.nict.langrid.management.web.view.component.text.IdTextField;
import jp.go.nict.langrid.management.web.view.component.validator.HalfSizeAlphaNumeralSiglumValidator;
import jp.go.nict.langrid.management.web.view.component.validator.LengthValidator;
import jp.go.nict.langrid.management.web.view.page.language.service.admin.protocol.component.form.validator.AlreadyExistsProtocolIdValidator;

public class ProtocolIdTextField extends IdTextField {
   public ProtocolIdTextField(String componentId, String gridId) {
      super(componentId, gridId);
   }
   
   @Override
   protected void addValidater(String gridId) {
      add(new HalfSizeAlphaNumeralSiglumValidator("LanguageGridOperator.protocol.error.validate.Id"));
      add(new LengthValidator(4, 254, "LanguageGridOperator.protocol.error.validate.Id"));
      add(new AlreadyExistsProtocolIdValidator(gridId, this));
   }
}
