package jp.go.nict.langrid.management.web.view.component.link;

import java.io.Serializable;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public abstract class ConfirmLink<T extends Serializable> extends Link<T>{
	/**
	 * 
	 * 
	 */
	public ConfirmLink(String componentId, Model<T> model, String message){
		super(componentId, model);
		this.message = message;
	}

	protected void onComponentTag(org.apache.wicket.markup.ComponentTag tag) {
      super.onComponentTag(tag);
      StringBuilder sb = new StringBuilder();
      sb.append("if(confirm('");
      sb.append(message);
      sb.append("')){");
      sb.append(tag.getString("onclick"));
      sb.append("}");
      tag.put("onclick", sb.toString());
   };
	
	private String message;
	private static final long serialVersionUID = 1L;
}
