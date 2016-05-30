package jp.go.nict.langrid.management.web.view.page.language.resource.admin.metaattribute.component.text;

import jp.go.nict.langrid.management.web.view.component.text.IdTextField;
import jp.go.nict.langrid.management.web.view.component.validator.HalfSizeAlphaNumeralSiglumValidator;
import jp.go.nict.langrid.management.web.view.component.validator.LengthValidator;
import jp.go.nict.langrid.management.web.view.page.language.resource.admin.metaattribute.component.form.validator.AlreadyExistsResourceMetaAttributeIdValidator;

public class ResourceMetaAttributeIdTextField extends IdTextField {
   public ResourceMetaAttributeIdTextField(String componentId, String gridId) {
      super(componentId, gridId, true);
   }
   
   @Override
   protected void addValidater(String gridId) {
      add(new HalfSizeAlphaNumeralSiglumValidator("LanguageGridOperator.resourcemetaattribute.error.validate.Id"));
      add(new LengthValidator(4, 254, "LanguageGridOperator.resourcemetaattribute.error.validate.Id"));
      add(new AlreadyExistsResourceMetaAttributeIdValidator(gridId, this));
   }
}
