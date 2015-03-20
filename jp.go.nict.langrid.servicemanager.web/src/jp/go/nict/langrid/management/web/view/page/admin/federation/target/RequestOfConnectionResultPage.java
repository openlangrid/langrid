package jp.go.nict.langrid.management.web.view.page.admin.federation.target;

import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.admin.federation.AllOperatorsPage;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;

public class RequestOfConnectionResultPage extends ServiceManagerPage {
	public RequestOfConnectionResultPage(String url, String userId, String gridId) {
		add(new Label("url", url));
		add(new Label("userId", userId));
		add(new Link("back"){
			@Override
			public void onClick(){
				setResponsePage(new AllOperatorsPage());
			}
		});
	}
	
}
