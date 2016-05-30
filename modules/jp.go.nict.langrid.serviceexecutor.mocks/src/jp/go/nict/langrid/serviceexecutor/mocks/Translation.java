package jp.go.nict.langrid.serviceexecutor.mocks;

import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.translation.TranslationService;

public class Translation
implements TranslationService{
	public Translation() {
	}

	@Override
	public String translate(String sourceLang, String targetLang, String source)
	throws InvalidParameterException,
			ProcessFailedException {
		if(targetLang.equals("en")){
			return "Hello.";
		} else{
			return "こんにちは。";
		}
	}
}
