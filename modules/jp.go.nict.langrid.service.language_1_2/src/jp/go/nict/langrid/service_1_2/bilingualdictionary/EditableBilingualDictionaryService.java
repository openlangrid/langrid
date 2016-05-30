/*
 * $Id:AbstractBilingualDictionaryService.java 1495 2006-10-12 18:42:55 +0900 (木, 12 10 2006) nakaguchi $
 *
 * Copyright (C) 2008 Language Grid Assocation.
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2008 NICT Language Grid Project.
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
package jp.go.nict.langrid.service_1_2.bilingualdictionary;

import java.util.Calendar;

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

/**
 * 
 * 
 * @author $Author:sagawa $
 * @version $Revision:1 $
 */
@Service(namespace="servicegrid:servicetype:nict.nlp:EditableBilingualDictionary")
public interface EditableBilingualDictionaryService {
	/**
	 * 
	 * 
	 */
	public TermEntrySearchResult searchTerm(
			@Parameter(name="startIndex") int startIndex
			, @Parameter(name="maxCount") int maxCount
		    , @Parameter(name="language") String[] language
		    , @Parameter(name="conditions") TermEntrySearchCondition[] conditions
		    , @Parameter(name="orders") Order[] orders
	    )
	throws AccessLimitExceededException, InvalidParameterException
	, LanguageNotUniquelyDecidedException, NoAccessPermissionException
	, NoValidEndpointsException, ProcessFailedException
	, ServerBusyException, ServiceNotActiveException
	, ServiceNotFoundException, UnsupportedLanguageException
	;

	/**
	 * 
	 * 
	 */			 
	public void addTerm(@Parameter(name="terms") Term[] terms)
	throws AccessLimitExceededException, InvalidParameterException
	, LanguageNotUniquelyDecidedException, NoAccessPermissionException
	, NoValidEndpointsException, ProcessFailedException
	, ServerBusyException, ServiceNotActiveException
	, ServiceNotFoundException, UnsupportedLanguageException
	;

	 /**
	  * 
	 * 
	 */
	public void deleteTerm(@Parameter(name="termIds") int[] termIds)
	throws AccessLimitExceededException, InvalidParameterException
	, NoAccessPermissionException, NoValidEndpointsException
	, ProcessFailedException, ServerBusyException
	, ServiceNotActiveException, ServiceNotFoundException
	;

	/**
	 * 
	 * 
	 */
	public void setTerm(@Parameter(name="entries") TermEntry[] entries)
	throws AccessLimitExceededException, InvalidParameterException
	, LanguageNotUniquelyDecidedException, NoAccessPermissionException
	, NoValidEndpointsException, ProcessFailedException
	, ServerBusyException, ServiceNotActiveException
	, ServiceNotFoundException, UnsupportedLanguageException
	;

	/**
	 * 
	 * 
	 */
	public String[] listLanguage()
	throws AccessLimitExceededException, NoAccessPermissionException
	, NoValidEndpointsException, ProcessFailedException
	, ServerBusyException, ServiceNotActiveException
	, ServiceNotFoundException
	;

	/**
	 * 
	 * 
	 */
	public void addLanguage(@Parameter(name="languages") String[] languages)
	throws AccessLimitExceededException, InvalidParameterException
	, NoAccessPermissionException
	, NoValidEndpointsException, ProcessFailedException
	, ServerBusyException, ServiceNotActiveException
	, ServiceNotFoundException
	;

	/**
	 * 
	 * 
	 */
	public void deleteLanguage(@Parameter(name="languages") String[] languages)
	throws AccessLimitExceededException, InvalidParameterException
	, LanguageNotUniquelyDecidedException, NoAccessPermissionException
	, NoValidEndpointsException, ProcessFailedException
	, ServerBusyException, ServiceNotActiveException
	, ServiceNotFoundException, UnsupportedLanguageException
	;

	/**
	 * 
	 * 
	 */
	String[] getSupportedMatchingMethods()
	throws AccessLimitExceededException, NoAccessPermissionException
	, NoValidEndpointsException, ProcessFailedException
	, ServerBusyException, ServiceNotActiveException
	, ServiceNotFoundException
	;

	/**
	 * 
	 * 
	 */
	Calendar getLastUpdate()
	throws AccessLimitExceededException, NoAccessPermissionException
	, NoValidEndpointsException, ProcessFailedException
	, ServerBusyException, ServiceNotActiveException
	, ServiceNotFoundException
	;
}

