package jp.go.nict.langrid.webapps.mock;

import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.LanguageNotUniquelyDecidedException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.NoValidEndpointsException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.ServerBusyException;
import jp.go.nict.langrid.service_1_2.ServiceNotActiveException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguageException;

public class TextSummarizeService implements
		jp.go.nict.langrid.service_1_2.textsummarize.TextSummarizeService {

	@Override
	public String summarize(String language, String text)
			throws AccessLimitExceededException, InvalidParameterException,
			LanguageNotUniquelyDecidedException, NoAccessPermissionException,
			ProcessFailedException, NoValidEndpointsException,
			ServerBusyException, ServiceNotActiveException,
			ServiceNotFoundException, UnsupportedLanguageException {
		return text.substring(0, 10);
	}

	@Override
	public String[] getSupportedLanguages()
			throws AccessLimitExceededException, NoAccessPermissionException,
			ProcessFailedException, NoValidEndpointsException,
			ServerBusyException, ServiceNotActiveException,
			ServiceNotFoundException {
		return new String[]{"en", "ja"};
	}

}
