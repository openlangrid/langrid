package jp.go.nict.langrid.management.web.view.page.language.service.atomic.component.form.panel;

import jp.go.nict.langrid.commons.ws.Protocols;
import jp.go.nict.langrid.management.web.model.ServiceEndpointModel;
import jp.go.nict.langrid.management.web.view.component.validator.PasswordConfirmValidator;
import jp.go.nict.langrid.management.web.view.page.language.service.atomic.component.form.validator.RelatedAtoBRequiredFormValidator;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

public class EndpointAuthInfoFieldPanel extends Panel {
	public EndpointAuthInfoFieldPanel(String componentId, Form parentForm) {
		super(componentId);
		setRenderBodyOnly(true);
		userName = new TextField<String>("userName", new Model<String>());
		add(userName);
		password = new PasswordTextField("password", new Model<String>());
		password.setRequired(false);
		add(password);
		PasswordTextField confirm = new PasswordTextField("confirm", new Model<String>());
		confirm.setRequired(false);
		confirm.add(new PasswordConfirmValidator(password));
		add(confirm);
		
		RelatedAtoBRequiredFormValidator<String> validator = new RelatedAtoBRequiredFormValidator<String>(
				password, confirm);
		validator.setMessageValue("A", "Password");
		validator.setMessageValue("B", "Confirm Password");
		parentForm.add(validator);
		RelatedAtoBRequiredFormValidator<String> validator2 = new RelatedAtoBRequiredFormValidator<String>(
				password, userName);
		validator2.setMessageValue("A", "Password");
		validator2.setMessageValue("B", "User Name");
		parentForm.add(validator2);
	}
	
	public void doSubmitProcess(ServiceEndpointModel obj) {
		obj.setProtocolId(Protocols.SOAP_RPCENCODED);
		obj.setAuthUserName(userName.getModelObject());
		obj.setAuthPassword(password.getModelObject());
		obj.setAverage(0);
		obj.setExperience(0);
		obj.setEnabled(true);
	}

	private TextField<String> userName;
	private PasswordTextField password;
}
