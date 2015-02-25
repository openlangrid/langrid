package jp.go.nict.langrid.management.web.view.page.user.component.text;

import org.apache.wicket.markup.html.form.TextArea;
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

	private static final long serialVersionUID = 8459987228973946705L;
}
