package jp.go.nict.langrid.management.web.view.page.language;

import jp.go.nict.langrid.management.web.utility.resource.MessageManager;

import org.apache.wicket.markup.html.basic.Label;

public class LanguageInputFormPopupPage extends LanguageDomainPopUpPage{
	public LanguageInputFormPopupPage(){
		add(new Label("message"
				, MessageManager.getMessage("LanguageInputForm.message.InputForm", getLocale()
						).replaceAll("\r\n", "<br/>")).setEscapeModelStrings(false));
	}
}
