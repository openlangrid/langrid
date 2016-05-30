/*
 * $Id: AbstractConceptDictionaryService.java 265 2010-10-03 10:25:32Z t-nakaguchi $
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
package jp.go.nict.langrid.wrapper.ws_1_2.conceptdictionary;

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
import jp.go.nict.langrid.service_1_2.ServiceNotActiveException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguageException;
import jp.go.nict.langrid.service_1_2.UnsupportedMatchingMethodException;
import jp.go.nict.langrid.service_1_2.conceptdictionary.Concept;
import jp.go.nict.langrid.service_1_2.conceptdictionary.ConceptDictionaryService;
import jp.go.nict.langrid.service_1_2.conceptdictionary.typed.ConceptualRelation;
import jp.go.nict.langrid.service_1_2.typed.MatchingMethod;
import jp.go.nict.langrid.service_1_2.util.validator.EnumValidator;
import jp.go.nict.langrid.service_1_2.util.validator.LanguageValidator;
import jp.go.nict.langrid.service_1_2.util.validator.MatchingMethodValidator;
import jp.go.nict.langrid.service_1_2.util.validator.StringValidator;
import jp.go.nict.langrid.wrapper.ws_1_2.AbstractLanguageService;

/**
 * 
 * Abstract class for the concept dictionary.
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 265 $
 */
public abstract class AbstractConceptDictionaryService
extends AbstractLanguageService
implements ConceptDictionaryService
{
	/**
	 * 
	 * Constructor that doesn't take parameter(s).
	 * 
	 */
	public AbstractConceptDictionaryService() {
	}

	/**
	 * 
	 * Constructor that takes the set of supported languages as a parameter.
	 * @param supportedLanguages Set of supported languages
	 * 
	 */
	public AbstractConceptDictionaryService(
			Collection<Language> supportedLanguages) {
		setSupportedLanguageCollection(supportedLanguages);
	}

	/**
	 * 
	 * Constructor that takes the service context and supported language as a parameter(s).
	 * @param serviceContext Service context
	 * @param supportedLanguages Set of supported languages
	 * 
	 */
	public AbstractConceptDictionaryService(ServiceContext serviceContext,
			Collection<Language> supportedLanguages) {
		super(serviceContext);
		setSupportedLanguageCollection(supportedLanguages);
	}

	public final Concept[] searchConcepts(String language, String word,
			String matchingMethod)
	throws AccessLimitExceededException,
	InvalidParameterException, LanguageNotUniquelyDecidedException,
	NoAccessPermissionException, NoValidEndpointsException,
	ProcessFailedException, ServerBusyException,
	ServiceNotActiveException, ServiceNotFoundException,
	UnsupportedLanguageException, UnsupportedMatchingMethodException
	{
		checkStartupException();
		Language lang = new LanguageValidator("language", language).notNull()
				.trim().notEmpty().getUniqueLanguage(
						getSupportedLanguageCollection());
		String src = new StringValidator("word", word).notNull().trim()
				.getValue();
		MatchingMethod method = new MatchingMethodValidator(
				"matchingMethod", matchingMethod
				)
				.notNull().trim().notEmpty().getMatchingMethod(supportedMatchingMethods);

		String trimmedSrc = src.trim();
		if(trimmedSrc.length() > 0){
			src = trimmedSrc;
		}

		processStart();
		try{
			acquireSemaphore();
			try {
				return CollectionUtil.toArray(
						doSearchConcepts(lang, src, method)
						, Concept.class, 0, getMaxResults()
						);
			} catch (InvalidParameterException e) {
				throw e;
			} catch (ProcessFailedException e) {
				throw e;
			} catch (Throwable t) {
				logger.log(Level.SEVERE, "unknown error occurred.", t);
				throw new ProcessFailedException(t);
			} finally {
				releaseSemaphore();
			}
		} finally{
			processEnd();
		}
	}

	public final Concept[] getRelatedConcepts(String language,
			String conceptId, String relation)
	throws AccessLimitExceededException, InvalidParameterException,
	LanguageNotUniquelyDecidedException, NoAccessPermissionException,
	ProcessFailedException, NoValidEndpointsException,
	ServerBusyException, ServiceNotActiveException,
	ServiceNotFoundException, UnsupportedLanguageException {
		checkStartupException();
		Language lang = new LanguageValidator("language", language).notNull()
				.trim().notEmpty().getUniqueLanguage(
						getSupportedLanguageCollection());
		String src = new StringValidator("conceptId", conceptId).notNull()
				.trim().notEmpty().getValue();
		ConceptualRelation cr = new EnumValidator<ConceptualRelation>(
				"relation", relation, ConceptualRelation.class).notNull().getEnum();
		processStart();
		try{
			acquireSemaphore();
			try {
				return doGetRelatedConcepts(lang, src, cr).toArray(new Concept[]{});
			} catch (InvalidParameterException e) {
				throw e;
			} catch (ProcessFailedException e) {
				throw e;
			} catch (Throwable t) {
				logger.log(Level.SEVERE, "unknown error occurred.", t);
				throw new ProcessFailedException(t);
			} finally {
				releaseSemaphore();
			}
		} finally{
			processEnd();
		}
	}

	/**
	 * 
	 * Runs a search on concepts.
	 * @param language Language
	 * @param word Word
	 * @param matchingMethod Matching method
	 * @return Set of supported concepts
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws ProcessFailedException Process failed
	 * 
	 */
	protected abstract Collection<Concept> doSearchConcepts(Language language,
			String word, MatchingMethod matchingMethod)
			throws InvalidParameterException, ProcessFailedException;

	/**
	 * 
	 * Runs a search on related concepts.
	 * @param language Language
	 * @param conceptId Concept ID
	 * @param relation Relation with concept ID
	 * @return Set of related concepts
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws ProcessFailedException Process failed
	 * 
	 */
	protected abstract Collection<Concept> doGetRelatedConcepts(Language language,
			String conceptId, ConceptualRelation relation)
			throws InvalidParameterException, ProcessFailedException;

	/**
	 * 
	 * Sets supported search methods.
	 * @param supportedMatchingMethods Set of supported search methods
	 * 
	 */
	protected void setSupportedMatchingMethods(Set<MatchingMethod> supportedMatchingMethods){
		this.supportedMatchingMethods = supportedMatchingMethods;
	}

	private Set<MatchingMethod> supportedMatchingMethods = MINIMUM_MATCHINGMETHODS;
	private static Logger logger = Logger.getLogger(
			AbstractConceptDictionaryService.class.getName());
}
