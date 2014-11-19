/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2011 NICT Language Grid Project.
 *
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by 
 * the Free Software Foundation, either version 2.1 of the License, or (at 
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser 
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License 
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package jp.go.nict.langrid.service_1_2.dependencyparser;

import jp.go.nict.langrid.commons.rpc.intf.Parameter;
import jp.go.nict.langrid.commons.rpc.intf.Service;
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
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
@Service(namespace="servicegrid:servicetype:nict.nlp:MorphemesDependencyParser")
public interface MorphemesDependencyParserService {
	/**
	 * 
	 * Analyzes the dependency relationship of morpheme array.
	 * @param language Language of the input text
	 * @param mophs Input mopheme array
	 * @return Dependency analysis results
	 * @throws AccessLimitExceededException Exceeded an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed.
	 * @throws LanguageNotUniquelyDecidedException Multiple searchable languages exist, and one is not singly determined.
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws ProcessFailedException No valid endpoint exists.
	 * @throws NoValidEndpointsException The process failed.
	 * @throws ServerBusyException Cannot complete the process as the server is in a busy state.
	 * @throws ServiceNotActiveException The service is not active.
	 * @throws ServiceNotFoundException The requested service was not found.
	 * @throws UnsupportedLanguageException An unsupported language was specified.
	 * 
	 */
	Chunk[] parseDependency(
			@Parameter(name="language") String language
			, @Parameter(name="morphs") Morpheme[] morphs)
	throws AccessLimitExceededException, InvalidParameterException
	, LanguageNotUniquelyDecidedException, NoAccessPermissionException
	, ProcessFailedException, NoValidEndpointsException
	, ServerBusyException, ServiceNotActiveException
	, ServiceNotFoundException, UnsupportedLanguageException;
}