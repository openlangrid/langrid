package jp.go.nict.langrid.management.web.view.component.text;

import jp.go.nict.langrid.management.web.view.component.validator.HalfSizeAlphaNumeralSiglumSpaceValidator;
import jp.go.nict.langrid.management.web.view.component.validator.LengthValidator;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;

public class NameTextField extends TextField<String> {
   public NameTextField(String componentId, String value, boolean isRequired) {
      super(componentId, new Model<String>(value));
      setRequired(isRequired);
      addValidater();
   }
   
   public NameTextField(String componentId, boolean isRequired) {
      this(componentId, "", isRequired);
   }

   public NameTextField(String componentId) {
      this(componentId, "", false);
   }
   
   private void addValidater(){
      add(new LengthValidator(1, 254));
      add(new HalfSizeAlphaNumeralSiglumSpaceValidator());
   }
}
