package jp.go.nict.langrid.management.web.view.page.other;

import jp.go.nict.langrid.management.web.annotation.RequireLogin;
import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.other.component.form.RegisterUserForm;
import jp.go.nict.langrid.management.web.view.page.user.EditUserProfileResultPage;
import jp.go.nict.langrid.management.web.view.page.user.component.form.EditUserProfileForm;

import org.apache.wicket.markup.html.form.SubmitLink;

@RequireLogin(false)
public class SignupPage extends ServiceManagerPage {
	
	public SignupPage() {
		RegisterUserForm form = new RegisterUserForm("form", getSelfGridId()){
			@Override
			protected void setResultPage(UserModel resultParameter){
				setResponsePage(new EditUserProfileResultPage(resultParameter));
			}
		
				private static final long serialVersionUID = 0L;
		};
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
