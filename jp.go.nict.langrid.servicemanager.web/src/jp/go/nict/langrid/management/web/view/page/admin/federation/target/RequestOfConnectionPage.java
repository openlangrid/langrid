package jp.go.nict.langrid.management.web.view.page.admin.federation.target;

import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.admin.federation.component.form.RequestOfConnectionForm;

import org.apache.wicket.markup.html.form.Form;

public class RequestOfConnectionPage extends ServiceManagerPage {
	public RequestOfConnectionPage() {
		Form form;
		add(form = new RequestOfConnectionForm("form", getSelfGridId()) {
			@Override
			protected void setResultPage(String resultParameter) {
				setResponsePage(new RequestOfConnectionResultPage(getRequestUrl(), getUserId(), getTargetGridId()));
			}

			private static final long serialVersionUID = 1L;
		});
	}
}
