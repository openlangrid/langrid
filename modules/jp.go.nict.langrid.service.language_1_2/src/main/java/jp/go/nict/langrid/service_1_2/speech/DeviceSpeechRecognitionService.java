package jp.go.nict.langrid.service_1_2.speech;

import jp.go.nict.langrid.commons.rpc.intf.Parameter;
import jp.go.nict.langrid.commons.rpc.intf.Service;
import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.NoValidEndpointsException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.ServerBusyException;
import jp.go.nict.langrid.service_1_2.ServiceNotActiveException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguageException;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
@Service(namespace="servicegrid:servicetype:nict.nlp:DeviceSpeechRecognition")
public interface DeviceSpeechRecognitionService {
	/**
	 * 
	 * 
	 */
	String recognize(
			@Parameter(name="language") String language)
	throws ServerBusyException,
	ServiceNotFoundException, ServiceNotActiveException,
	AccessLimitExceededException, InvalidParameterException,
	NoAccessPermissionException, NoValidEndpointsException,
	UnsupportedLanguageException, ProcessFailedException
	;

	/**
	 * 
	 * 
	 */
	String[] getSupportedLanguages()
	throws NoValidEndpointsException, ProcessFailedException
	, ServerBusyException, ServiceNotActiveException
	, ServiceNotFoundException
	;
}
