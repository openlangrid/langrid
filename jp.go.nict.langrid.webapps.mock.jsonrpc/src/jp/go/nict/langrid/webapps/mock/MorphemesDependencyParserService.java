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
import jp.go.nict.langrid.service_1_2.dependencyparser.Chunk;
import jp.go.nict.langrid.service_1_2.dependencyparser.Dependency;
import jp.go.nict.langrid.service_1_2.dependencyparser.typed.DependencyLabel;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme;

public class MorphemesDependencyParserService
implements jp.go.nict.langrid.service_1_2.dependencyparser.MorphemesDependencyParserService {
	@Override
	public Chunk[] parseDependency(String language, Morpheme[] morphs)
			throws AccessLimitExceededException, InvalidParameterException,
			LanguageNotUniquelyDecidedException, NoAccessPermissionException,
			ProcessFailedException, NoValidEndpointsException,
			ServerBusyException, ServiceNotActiveException,
			ServiceNotFoundException, UnsupportedLanguageException {
		return new Chunk[]{new Chunk("1", morphs, new Dependency(DependencyLabel.DEPENDENCY.name(), "0"))};
	}
}
