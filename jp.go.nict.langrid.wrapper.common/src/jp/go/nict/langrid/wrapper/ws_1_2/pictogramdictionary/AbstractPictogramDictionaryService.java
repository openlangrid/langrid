/*
 * $Id: AbstractPictogramDictionaryService.java 265 2010-10-03 10:25:32Z t-nakaguchi $
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
package jp.go.nict.langrid.wrapper.ws_1_2.pictogramdictionary;

import java.util.Collection;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.go.nict.langrid.commons.transformer.ToStringTransformer;
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
import jp.go.nict.langrid.service_1_2.pictogramdictionary.Pictogram;
import jp.go.nict.langrid.service_1_2.pictogramdictionary.PictogramDictionaryService;
import jp.go.nict.langrid.service_1_2.typed.MatchingMethod;
import jp.go.nict.langrid.service_1_2.util.validator.LanguageValidator;
import jp.go.nict.langrid.service_1_2.util.validator.MatchingMethodValidator;
import jp.go.nict.langrid.service_1_2.util.validator.StringValidator;
import jp.go.nict.langrid.wrapper.ws_1_2.AbstractLanguageService;
import jp.go.nict.langrid.wrapper.ws_1_2.bilingualdictionary.AbstractBilingualDictionaryService;

/**
 * 
 * Abstract class for the pictogram service.
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 265 $
 */
public abstract class AbstractPictogramDictionaryService
extends AbstractLanguageService
implements PictogramDictionaryService
{
	/**
	 * 
	 * Constructor that doesn't take parameter(s).
	 * 
	 */
	public AbstractPictogramDictionaryService(){
		recalcSupportedValues();
	}

	/**
	 * 
	 * Constructor that takes the set of supported languages as a parameter.
	 * @param supportedLanguages Set of supported languages
	 * 
	 */
	public AbstractPictogramDictionaryService(
			Collection<jp.go.nict.langrid.language.Language> supportedLanguages)
	{
		setSupportedLanguageCollection(supportedLanguages);
		recalcSupportedValues();
	}

	/**
	 * 
	 * Constructor that takes the service context and set of supported languages as a parameter(s).
	 * @param context Service context
	 * @param supportedLanguages Set of supported languages
	 * 
	 */
	public AbstractPictogramDictionaryService(
			ServiceContext context
			, Collection<Language> supportedLanguages){
		super(context);
		setSupportedLanguageCollection(supportedLanguages);
		recalcSupportedValues();
	}

	public Pictogram[] search(
			String language, String word
			, String matchingMethod
			)
	throws AccessLimitExceededException, InvalidParameterException,
	LanguageNotUniquelyDecidedException, NoAccessPermissionException,
	NoValidEndpointsException, ProcessFailedException,
	ServerBusyException, ServiceNotActiveException,
	ServiceNotFoundException, UnsupportedLanguageException
	, UnsupportedMatchingMethodException
	{
		checkStartupException();
		Language l = new LanguageValidator(
				"language", language
				).notNull().trim().notEmpty().getUniqueLanguage(
						getSupportedLanguageCollection());
		String w = new StringValidator("word", word)
				.notNull().trim().notEmpty().getValue();
		MatchingMethod m = new MatchingMethodValidator(
				"matchingMethod", matchingMethod
				)
				.notNull().trim().notEmpty().getMatchingMethod(matchingMethods);

		acquireSemaphore();
		try{
			return CollectionUtil.toArray(
					doSearch(l, w, m)
					, Pictogram.class, 0, getMaxResults()
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
	 * Returns the  corresponding to the term. When none exist, returns a collection of 0 elements.
	 * @param language Language
	 * @param word Word
	 * @param matchingMethod Matching method
	 * @return Array of pictograms corresponding to the term
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws ProcessFailedException Process failed
	 * 
	 */
	protected abstract Collection<Pictogram> doSearch(
			Language language, String word, MatchingMethod matchingMethod
			)
	throws InvalidParameterException, ProcessFailedException;
	;

	public String[] getSupportedMatchingMethods()
		throws AccessLimitExceededException, NoAccessPermissionException,
		NoValidEndpointsException, ProcessFailedException,
		ServerBusyException, ServiceNotActiveException,
		ServiceNotFoundException
	{
		return supportedMatchingMethods;
	}

	protected void setSupportedMatchingMethods(
			Set<MatchingMethod> supportedMatchingMethods)
	{
		this.matchingMethods = supportedMatchingMethods;
		recalcSupportedValues();
	}

	private void recalcSupportedValues(){
		this.supportedMatchingMethods = CollectionUtil.collect(
				matchingMethods, new ToStringTransformer<MatchingMethod>()
				).toArray(new String[]{});
	}

	private Set<MatchingMethod> matchingMethods = MINIMUM_MATCHINGMETHODS;
	private String[] supportedMatchingMethods;
	private static Logger logger = Logger.getLogger(
			AbstractBilingualDictionaryService.class.getName()
			);
}
