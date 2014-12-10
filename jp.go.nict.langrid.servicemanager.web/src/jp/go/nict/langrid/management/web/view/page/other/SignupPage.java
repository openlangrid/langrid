package jp.go.nict.langrid.management.web.view.page.other;

import java.util.HashMap;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.model.Model;

import jp.go.nict.langrid.management.web.model.ResourceModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ResourceService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.user.component.text.RequiredEMailAddressField;
import jp.go.nict.langrid.management.web.view.session.ServiceManagerSession;

public class SignupPage extends ServiceManagerPage {
	
	private RequiredEMailAddressField email;
	
	public SignupPage() {
		Form form = new Form("form");
		
		form.add(email = new RequiredEMailAddressField("email", new Model<String>()));
		form.add(getSubmitLink("submit"));
		add(form);
	}

	private SubmitLink getSubmitLink(String componentId) {
		return new SubmitLink(componentId){
			@Override
			public void onSubmit() {
				
			}
		};
	}

}
