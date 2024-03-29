package jp.go.nict.langrid.management.web.view.page.language;

import java.util.Set;

import jp.go.nict.langrid.language.InvalidLanguageTagException;
import jp.go.nict.langrid.management.web.model.InternalLanguageModel;
import jp.go.nict.langrid.management.web.view.page.PopupPage;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public abstract class LanguageDomainPopUpPage extends PopupPage{
	protected Component getPathComponent(String componentId, String paths, Set<String> set)
	throws InvalidLanguageTagException
	{
		for(String code : set){
			String regex = "";
			String replace = "";
			if(code.equals("*")){
				regex = "\\" + InternalLanguageModel.getWildcard().getCode();
				replace = InternalLanguageModel.getNameByCode(InternalLanguageModel.getWildcard().getCode(), getLocale());
			}else{
				regex = InternalLanguageModel.getNameByCode(code, getLocale());
				replace = InternalLanguageModel.getDisplayString(code, getLocale());
			}
			paths = paths.replaceAll(regex, replace);
		}
			
		return new Label(componentId, paths);
	}
}
