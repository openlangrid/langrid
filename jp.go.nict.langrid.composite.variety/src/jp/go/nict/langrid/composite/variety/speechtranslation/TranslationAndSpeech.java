package jp.go.nict.langrid.composite.variety.speechtranslation;

import jp.go.nict.langrid.language.LanguagePair;
import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.NoValidEndpointsException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.ServerBusyException;
import jp.go.nict.langrid.service_1_2.ServiceNotActiveException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguagePairException;
import jp.go.nict.langrid.service_1_2.speech.Speech;
import jp.go.nict.langrid.service_1_2.speech.TextToSpeechService;
import jp.go.nict.langrid.service_1_2.speechtranslation.TranslationAndSpeechService;
import jp.go.nict.langrid.service_1_2.translation.TranslationService;
import jp.go.nict.langrid.service_1_2.util.validator.LanguagePairValidator;
import jp.go.nict.langrid.service_1_2.util.validator.LanguageValidator;
import jp.go.nict.langrid.service_1_2.util.validator.StringValidator;
import jp.go.nict.langrid.servicecontainer.service.ComponentServiceFactory;
import jp.go.nict.langrid.servicecontainer.service.composite.AbstractCompositeService;

public class TranslationAndSpeech
extends AbstractCompositeService
implements TranslationAndSpeechService
{
	public Speech translateAndSpeak(String sourceLang, String targetLang, String source
			, String targetVoiceType, String targetAudioType)
	throws AccessLimitExceededException ,InvalidParameterException
	, NoAccessPermissionException, NoValidEndpointsException
	, ProcessFailedException, ServerBusyException, ServiceNotActiveException
	, ServiceNotFoundException, UnsupportedLanguagePairException {
		LanguagePair pair = new LanguagePairValidator(
				new LanguageValidator("sourceLang", sourceLang)
				, new LanguageValidator("targetLang", targetLang)
				).notNull().trim().notEmpty().getLanguagePair();
		String src = new StringValidator("source", source)
				.notNull().trim().notEmpty().getValue();
		String vt = new StringValidator("voiceType", targetVoiceType)
				.notNull().trim().getValue();
		String at = new StringValidator("audioType", targetAudioType)
				.notNull().trim().getValue();
		ComponentServiceFactory factory = getComponentServiceFactory();
		
		TranslationService trans = factory.getService(
				"TranslationPL", TranslationService.class);
		String result = trans.translate(
				pair.getSource().getCode(), pair.getTarget().getCode(), src);
		
		TextToSpeechService tts = factory.getService(
				"TextToSpeechPL", TextToSpeechService.class);
		return tts.speak(pair.getTarget().getCode(), result, vt, at);
	}
}
