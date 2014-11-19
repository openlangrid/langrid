package jp.go.nict.langrid.management.web.view.page.admin.federation;

import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class RequestOfDisconnectionResultPage extends ServiceManagerPage {
	/**
	 * 
	 * 
	 */
	public RequestOfDisconnectionResultPage(String url, boolean isSuccessRemoteDeletion) {
		if( ! isSuccessRemoteDeletion){
			warn(MessageManager.getMessage(
					"LanguageGridOperator.federation.message.disconnect.error.remote.notDeletion"
				, getLocale()
				, url));
		}
		add(new Label("url", url));
		add(new Link("back"){
			@Override
			public void onClick(){
				setResponsePage(new AllOperatorsPage());
			}
		});
	}
}
