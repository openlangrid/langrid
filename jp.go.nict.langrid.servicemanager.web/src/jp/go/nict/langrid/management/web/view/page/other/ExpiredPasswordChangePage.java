package jp.go.nict.langrid.management.web.view.page.other;

import java.util.HashMap;
import java.util.Map;

import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.user.component.form.ChangePasswordForm;
import jp.go.nict.langrid.management.web.view.session.ServiceManagerSession;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class ExpiredPasswordChangePage extends ServiceManagerPage {
	public ExpiredPasswordChangePage() {
	   Map<String, String> map = new HashMap<String, String>();
	   try {
         map.put("defineDay", String.valueOf(ServiceFactory.getInstance().getGridService().getPasswordExpiredDay()));
      } catch(ServiceManagerException e) {
         doErrorProcess(e);
      }
	   add(new Label("message", MessageManager.getMessage("Login.expired.ShouldChange", map)
	      ).setEscapeModelStrings(false));
		ChangePasswordForm passwordForm = new ChangePasswordForm("form", getSelfGridId()) {
			@Override
			protected void setResultPage(HashMap<String, String> resultParameter) {
				setResponsePage(getApplication().getHomePage());
			}
		};
		add(passwordForm);
		
		passwordForm.add(new AjaxLink("noChange") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				((ServiceManagerSession)getSession()).setIsExpiredPassword(false);
				if(!continueToOriginalDestination()) {
					setResponsePage(getApplication().getHomePage());
				}
			}
		});
	}
}
