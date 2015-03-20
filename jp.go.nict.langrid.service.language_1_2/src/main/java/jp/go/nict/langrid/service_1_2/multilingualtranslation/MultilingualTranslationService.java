package jp.go.nict.langrid.service_1_2.multilingualtranslation;

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

@Service(namespace="servicegrid:servicetype:nict.nlp:MultilingualTranslation")
public interface MultilingualTranslationService {
	/**
	 * 
	 * Run a multilingual translation.
	 * @param sourceLang Translation source language (RFC3066 compliant)
	 * @param intermediateLangs Intermediate languages (RFC3066 compliant)
	 * @param targetLang Target language of the translation (RFC3066 compliant)
	 * @param source String to be translated
	 * @return Translated results
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws LanguagePairNotUniquelyDecidedException Multiple candidate language pairs exist
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws NoValidEndpointsException There is no valid endpoint
	 * @throws ProcessFailedException Translation process failed
	 * @throws ServerBusyException The server is loaded and cannot process it.
	 * @throws ServiceNotActiveException Service not active
	 * @throws ServiceNotFoundException The specified service was not found
	 * @throws UnsupportedLanguagePairException An unsupported language pair was specified
	 */
	String[] multilingualTranslate(
			@Parameter(name="sourceLang") String sourceLang
			, @Parameter(name="targetLangs") String[] targetLangs
			, @Parameter(name="source") String source)
		throws AccessLimitExceededException, InvalidParameterException
		, LanguagePairNotUniquelyDecidedException, NoAccessPermissionException
		, NoValidEndpointsException, ProcessFailedException
		, ServerBusyException, ServiceNotActiveException
		, ServiceNotFoundException, UnsupportedLanguagePairException
		;
}
