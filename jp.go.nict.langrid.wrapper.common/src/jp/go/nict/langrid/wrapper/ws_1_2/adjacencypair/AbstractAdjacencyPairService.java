/*
 * $Id: AbstractAdjacencyPairService.java 409 2011-08-25 03:12:59Z t-nakaguchi $
 *
 * This is a program to wrap language resources as Web services.
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
package jp.go.nict.langrid.wrapper.ws_1_2.adjacencypair;

import java.util.Collection;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.go.nict.langrid.commons.util.CollectionUtil;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.LanguageNotUniquelyDecidedException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.NoValidEndpointsException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.ServerBusyException;
import jp.go.nict.langrid.service_1_2.ServiceConfigurationException;
import jp.go.nict.langrid.service_1_2.ServiceNotActiveException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.service_1_2.UnknownException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguageException;
import jp.go.nict.langrid.service_1_2.UnsupportedMatchingMethodException;
import jp.go.nict.langrid.service_1_2.adjacencypair.AdjacencyPair;
import jp.go.nict.langrid.service_1_2.adjacencypair.AdjacencyPairService;
import jp.go.nict.langrid.service_1_2.typed.MatchingMethod;
import jp.go.nict.langrid.service_1_2.util.validator.LanguageValidator;
import jp.go.nict.langrid.service_1_2.util.validator.MatchingMethodValidator;
import jp.go.nict.langrid.service_1_2.util.validator.StringValidator;
import jp.go.nict.langrid.wrapper.ws_1_2.AbstractLanguageService;

/**
 * 
 * Base class for the adjacency pair service.
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 409 $
 */
public abstract class AbstractAdjacencyPairService
	extends AbstractLanguageService
	implements AdjacencyPairService
{
	/**
	 * 
	 * Constructor that doesn't take parameter(s).
	 * 
	 */
	public AbstractAdjacencyPairService(){
	}

	/**
	 * 
	 * The Constructor.
	 * @param serviceContext The service context.
	 * 
	 */
	public AbstractAdjacencyPairService(ServiceContext serviceContext){
		super(serviceContext);
	}

	/**
	 * 
	 * Constructor that takes the set of supported languages as a parameter.
	 * @param supportedLanguages Set of supported languages
	 * 
	 */
	public AbstractAdjacencyPairService(
			Collection<Language> supportedLanguages){
		setSupportedLanguageCollection(supportedLanguages);
	}

	/**
	 * 
	 * Constructor that takes the service context and supported language pair(s) as a parameter(s).
	 * @param serviceContext Service context
	 * @param supportedLanguages Set of supported languages
	 * 
	 */
	public AbstractAdjacencyPairService(
		ServiceContext serviceContext
		, Collection<Language> supportedLanguages
		)
	{
		super(serviceContext);
		setSupportedLanguageCollection(supportedLanguages);
	}

	public AdjacencyPair[] search(
			String category, String language
			, String firstTurn, String matchingMethod)
	throws AccessLimitExceededException,
	InvalidParameterException, NoAccessPermissionException
	, NoValidEndpointsException, LanguageNotUniquelyDecidedException
	, ProcessFailedException, ServerBusyException
	, ServiceNotActiveException, ServiceNotFoundException
	, UnsupportedLanguageException, UnsupportedMatchingMethodException
	{
		checkStartupException();
		Language l = new LanguageValidator("language", language)
				.notNull().trim().notEmpty().getUniqueLanguage(
						getSupportedLanguageCollection());
		String ft = new StringValidator("firstTurn", firstTurn)
				.notNull().trim().notEmpty().getValue();
		MatchingMethod sm = new MatchingMethodValidator(
				"matchingMethod", matchingMethod
				)
				.notNull().trim().notEmpty().getMatchingMethod(supportedMatchingMethods);

		String trimmedFt = ft.trim();
		if(trimmedFt.length() > 0){
			ft = trimmedFt;
		}

		acquireSemaphore();
		try{
			return CollectionUtil.toArray(
					doSearch(category, l, ft, sm)
					, AdjacencyPair.class, 0, getMaxResults()
					);
		} catch(InvalidParameterException e){
			throw e;
		} catch(ProcessFailedException e){
			throw e;
		} catch(Throwable t){
			logger.log(Level.SEVERE, "unknown error occurred.", t);
			throw new ProcessFailedException(t);
		} finally{
			releaseSemaphore();
		}
	}

	/**
	 * 
	 * Sets supported search methods.
	 * @param supportedMatchingMethods Set of supported search methods
	 * 
	 */
	protected void setSupportedMatchingMethods(Set<MatchingMethod> supportedMatchingMethods){
		this.supportedMatchingMethods = supportedMatchingMethods;
	}

	/**
	 * 
	 * Gets a list of responses corresponding to the specified utterance.
	 * @param category Category
	 * @param language Language of utterance (RFC3066 compliant)
	 * @param firstTurn Utterance(required)
	 * @param matchingMethod Search method
	 * @return Search results
	 * @throws InvalidParameterException language, firstPart or matchingMethod is null or else an empty string.language does not comply with RFC3066. String not provided for by matchingMethod
	 * @throws SearchFailedException The response search failed due to some cause
	 * @throws ServiceConfigurationException The service is not set up appropriately
	 * @throws UnknownException An unknown error occurred
	 * 
	 */
	protected abstract Collection<AdjacencyPair> doSearch(
			String category, Language language
			, String firstTurn, MatchingMethod matchingMethod
			)
			throws InvalidParameterException, ProcessFailedException
			;

	private Set<MatchingMethod> supportedMatchingMethods = MINIMUM_MATCHINGMETHODS;
	private static Logger logger = Logger.getLogger(
			AbstractAdjacencyPairService.class.getName()
			);
}
