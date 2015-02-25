package jp.go.nict.langrid.management.web.view.page.user.component.text;

import jp.go.nict.langrid.management.web.view.component.validator.URLValidator;

import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class TermofUseField extends TextArea<String>{
	
	public TermofUseField(String componentId, Model<String> model){
		super(componentId, model);
	}

	/**
	 * 
	 * 
	 */
	public TermofUseField(String componentId, IModel<String> model){
		super(componentId, model);
	}
}
