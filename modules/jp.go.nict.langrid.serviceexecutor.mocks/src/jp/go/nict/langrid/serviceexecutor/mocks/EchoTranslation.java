package jp.go.nict.langrid.serviceexecutor.mocks;

import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.translation.TranslationService;

public class EchoTranslation
implements TranslationService{
	public EchoTranslation() {
	}

	@Override
	public String translate(String sourceLang, String targetLang, String source)
	throws InvalidParameterException,
			ProcessFailedException {
		return source;
	}
}
