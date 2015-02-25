package jp.go.nict.langrid.management.web.view.page.other;

import jp.go.nict.langrid.management.web.annotation.RequireLogin;
import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.other.component.form.RegisterUserForm;
import jp.go.nict.langrid.management.web.view.page.user.EditUserProfileResultPage;
import jp.go.nict.langrid.management.web.view.page.user.admin.AllLanguageGridUsersPage;
import jp.go.nict.langrid.management.web.view.page.user.component.form.EditUserProfileForm;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.form.SubmitLink;
/**
 * 
 * @author Trang
 *
 */

@RequireLogin(false)
public class SignupPage extends ServiceManagerPage {
	
	public SignupPage() {
		add(new RegisterUserForm("form", getSelfGridId()) {

			/**
			 * 
			 */
			@Override
			protected Page getCancelPage(){
				return new LoginPage();
			}

			@Override
			protected boolean hasCancel(){
				return true;
			}
			private static final long serialVersionUID = 1L;
		});
	}

}
