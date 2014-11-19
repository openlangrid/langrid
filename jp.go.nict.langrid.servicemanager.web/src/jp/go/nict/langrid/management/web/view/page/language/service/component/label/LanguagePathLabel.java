package jp.go.nict.langrid.management.web.view.page.language.service.component.label;

import java.util.HashSet;
import java.util.Set;

import jp.go.nict.langrid.language.InvalidLanguageTagException;
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.language.LanguagePath;
import jp.go.nict.langrid.language.util.LanguagePathUtil;
import jp.go.nict.langrid.management.web.model.InternalLanguageModel;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

public class LanguagePathLabel extends Label {
   public LanguagePathLabel(String componentId) throws InvalidLanguageTagException{
      this(componentId, new LanguagePath[]{});
   }
   
	public LanguagePathLabel(String componentId, LanguagePath[] paths)
	throws InvalidLanguageTagException 
	{
		super(componentId, new Model<String>());
		if(paths == null || paths.length == 0){
		   return;
		}
		Set<String> codeSet = new HashSet<String>();
		for(LanguagePath path : paths){
			for(Language code : path.getPath()){
				codeSet.add(code.getCode());
			}
		}
		String language = LanguagePathUtil.encodeToSimplifiedExpressionByName(paths, getLocale());
		for(String code : codeSet){
			String regex = "";
			String replace = "";
			if(code.equals("*")){
				regex = "\\" + InternalLanguageModel.getWildcard().getCode();
				replace = InternalLanguageModel.getNameByCode(InternalLanguageModel.getWildcard().getCode(), getLocale());
			}else{
				regex = InternalLanguageModel.getNameByCode(code, getLocale());
				replace = InternalLanguageModel.getDisplayString(code, getLocale());
			}
			language = language.replaceAll(regex, replace);
		}
		setDefaultModelObject(language);
	}
}
