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
import jp.go.nict.langrid.service_1_2.namedentitytagging.NamedEntity;
import jp.go.nict.langrid.service_1_2.namedentitytagging.typed.Tag;

public class NamedEntityTaggingService
		implements
		jp.go.nict.langrid.service_1_2.namedentitytagging.NamedEntityTaggingService {

	@Override
	public NamedEntity[] tag(String language, String text)
			throws AccessLimitExceededException, InvalidParameterException,
			LanguageNotUniquelyDecidedException, NoAccessPermissionException,
			ProcessFailedException, NoValidEndpointsException,
			ServerBusyException, ServiceNotActiveException,
			ServiceNotFoundException, UnsupportedLanguageException {
		return new NamedEntity[]{new NamedEntity(text, Tag.NONE.name())};
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
