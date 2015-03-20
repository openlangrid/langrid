package jp.go.nict.langrid.management.web.view.page.domain.component.text;

import jp.go.nict.langrid.management.web.view.component.text.IdTextField;
import jp.go.nict.langrid.management.web.view.component.validator.HalfSizeAlphaNumeralSiglumValidator;
import jp.go.nict.langrid.management.web.view.component.validator.LengthValidator;
import jp.go.nict.langrid.management.web.view.page.domain.component.form.validator.AlreadyExistsDomainIdValidator;

public class DomainIdTextField extends IdTextField {
   public DomainIdTextField(String componentId, String gridId) {
      super(componentId, gridId);
   }
   
   @Override
   protected void addValidater(String gridId) {
      add(new HalfSizeAlphaNumeralSiglumValidator("LanguageGridOperator.domainSettings.error.validate.Id"));
      add(new LengthValidator(4, 254, "LanguageGridOperator.domainSettings.error.validate.Id"));
      add(new AlreadyExistsDomainIdValidator(gridId, this));
   }
}
