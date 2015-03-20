package jp.go.nict.langrid.wrapper.echo;

import static jp.go.nict.langrid.language.LangridLanguageTags.any;

import java.util.Arrays;

import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.language.LanguagePair;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.wrapper.ws_1_2.translation.AbstractTranslationService;

public class EchoTranslation extends AbstractTranslationService{
	public EchoTranslation(){
		setSupportedLanguagePairs(Arrays.asList(new LanguagePair(
				any, any)));
	}
	@Override
	protected String doTranslation(Language sourceLang, Language targetLang,
			String source) throws InvalidParameterException,
			ProcessFailedException {
		return source;
	}
}
