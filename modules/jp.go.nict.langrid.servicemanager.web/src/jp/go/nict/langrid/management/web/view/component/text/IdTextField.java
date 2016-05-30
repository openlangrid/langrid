package jp.go.nict.langrid.management.web.view.component.text;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class IdTextField extends TextField<String> {
   public IdTextField(String componentId, String value, String gridId, boolean isRequired) {
      super(componentId, new Model<String>(value));
      setRequired(isRequired);
      addValidater(gridId);
   }
   
   public IdTextField(String componentId, String gridId, boolean isRequired) {
      this(componentId, "", gridId ,isRequired);
   }
   
   public IdTextField(String componentId, String gridId) {
      this(componentId, "", gridId, false);
   }
   /**
    * 
    * 
    */
   protected void addValidater(String gridId){
      // noop
   }
}
