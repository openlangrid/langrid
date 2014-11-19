package jp.go.nict.langrid.management.web.view.component.link;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public abstract class CssClassChangableLink<T> extends AjaxLink<T> {
   public CssClassChangableLink(String linkId, IModel<T> model) {
      super(linkId, model);
      add(new AttributeModifier("class", true, cssModel = new Model<String>("")));
   }
   public boolean isSetCssClass(){
      return ! cssModel.getObject().equals("");
   }
   public void setCssClass(String className){
      cssModel.setObject(className);
   }

   private Model<String> cssModel;
}