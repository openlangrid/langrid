package jp.go.nict.langrid.management.web.view.page.other;

import jp.go.nict.langrid.management.web.annotation.RequireLogin;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.other.component.form.RegisterUserForm;

import org.apache.wicket.Page;

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
