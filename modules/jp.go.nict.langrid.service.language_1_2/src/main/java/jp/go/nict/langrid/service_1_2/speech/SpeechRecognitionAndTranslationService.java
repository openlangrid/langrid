package jp.go.nict.langrid.service_1_2.speech;

import jp.go.nict.langrid.commons.rpc.intf.Parameter;
import jp.go.nict.langrid.commons.rpc.intf.Service;
import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.LanguagePairNotUniquelyDecidedException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.NoValidEndpointsException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.ServerBusyException;
import jp.go.nict.langrid.service_1_2.ServiceNotActiveException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguagePairException;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
@Service(namespace="servicegrid:servicetype:nict.nlp:SpeechRecognitionAndTranslation")
public interface SpeechRecognitionAndTranslationService {
	/**
	 * 
	 */
	RecognizeAndTranslateResult recognizeAndTranslate(
			@Parameter(name="speechLang") String speechLang,
			@Parameter(name="speech") Speech speech,
			@Parameter(name="targetLang") String targetLang)
	throws ServerBusyException,
	AccessLimitExceededException, InvalidParameterException,
	LanguagePairNotUniquelyDecidedException, NoAccessPermissionException,
	NoValidEndpointsException,UnsupportedLanguagePairException,
	ProcessFailedException,
	ServiceNotFoundException, ServiceNotActiveException
	;
}
