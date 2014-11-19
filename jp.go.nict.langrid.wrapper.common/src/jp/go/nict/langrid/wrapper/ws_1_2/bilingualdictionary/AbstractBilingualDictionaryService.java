/*
 * $Id: AbstractBilingualDictionaryService.java 265 2010-10-03 10:25:32Z t-nakaguchi $
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
package jp.go.nict.langrid.wrapper.ws_1_2.bilingualdictionary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.go.nict.langrid.commons.transformer.ToStringTransformer;
import jp.go.nict.langrid.commons.util.ArrayUtil;
import jp.go.nict.langrid.commons.util.CollectionUtil;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.language.Language;
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
import jp.go.nict.langrid.service_1_2.UnsupportedMatchingMethodException;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.BilingualDictionaryService;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.Translation;
import jp.go.nict.langrid.service_1_2.typed.MatchingMethod;
import jp.go.nict.langrid.service_1_2.util.validator.LanguagePairValidator;
import jp.go.nict.langrid.service_1_2.util.validator.LanguageValidator;
import jp.go.nict.langrid.service_1_2.util.validator.MatchingMethodValidator;
import jp.go.nict.langrid.service_1_2.util.validator.StringValidator;
import jp.go.nict.langrid.wrapper.ws_1_2.AbstractLanguagePairService;
import jp.go.nict.langrid.wrapper.ws_1_2.util.MatchingMethodMatcher;

/**
 * 
 * $Id:AbstractBilingualDictionaryService.java 1495 2006-10-12 18:42:55 +0900 (Thu, 12 10 2006) nakaguchi $
 * Base class for the bilingual dictionary service
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 265 $
 */
public abstract class AbstractBilingualDictionaryService
extends AbstractLanguagePairService
implements BilingualDictionaryService
{
	/**
	 * 
	 * Constructor.
	 * It does not take parameter(s).
	 * 
	 */
	public AbstractBilingualDictionaryService(){
		recalcSupportedValues();
	}

	/**
	 * 
	 * Constructor.
	 * Takes the set of supported language pairs as a parameter.
	 * @param context Service context
	 * 
	 */
	public AbstractBilingualDictionaryService(
			ServiceContext context){
		super(context);
		recalcSupportedValues();
	}

	/**
	 * 
	 * Constructor.
	 * Takes the set of supported language pairs as a parameter.
	 * @param supportedPairs Set of supported language pairs
	 * 
	 */
	public AbstractBilingualDictionaryService(
			Collection<jp.go.nict.langrid.language.LanguagePair> supportedPairs)
	{
		setSupportedLanguagePairs(supportedPairs);
		recalcSupportedValues();
	}

	/**
	 * 
	 * Constructor.
	 * Takes the set of supported language pairs as a parameter.
	 * @param context Service context
	 * @param supportedPairs Set of supported language pairs
	 * 
	 */
	public AbstractBilingualDictionaryService(
			ServiceContext context
			, Collection<jp.go.nict.langrid.language.LanguagePair> supportedPairs){
		super(context);
		setSupportedLanguagePairs(supportedPairs);
		recalcSupportedValues();
	}

	public Translation[] search(String headLang, String targetLang,
			String headWord, String matchingMethod)
	throws AccessLimitExceededException, InvalidParameterException
	, LanguagePairNotUniquelyDecidedException, NoAccessPermissionException
	, NoValidEndpointsException, ProcessFailedException
	, ServerBusyException, ServiceNotActiveException
	, ServiceNotFoundException, UnsupportedLanguagePairException
	, UnsupportedMatchingMethodException
	{
		checkStartupException();
		jp.go.nict.langrid.language.LanguagePair pair = new LanguagePairValidator(
				new LanguageValidator("headLang", headLang)
				, new LanguageValidator("targetLang", targetLang)
				).notNull().trim().notEmpty().getUniqueLanguagePair(
						getSupportedLanguagePairCollection());
		String hw = new StringValidator("headWord", headWord)
				.notNull().trim().getValue();
		MatchingMethod mm = new MatchingMethodValidator(
				"matchingMethod", matchingMethod
				)
				.notNull().trim().notEmpty().getMatchingMethod(matchingMethods);

		String trimmedHw = hw.trim();
		if(trimmedHw.length() > 0){
			hw = trimmedHw;
		}

		processStart();
		try{
			acquireSemaphore();
			try{
				Translation[] result = doSearch(
						pair.getSource(), pair.getTarget(), hw, mm
						).toArray(new Translation[]{});
				if(getDoDoubleSearch()){
					result = doubleSearch(result, hw, mm);
				}
				if(result.length <= getMaxResults()){
					return result;
				} else{
					return ArrayUtil.subArray(result, 0, getMaxResults());
				}
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
		} finally{
			processEnd();
		}
	}

	public String[] getSupportedMatchingMethods()
			throws AccessLimitExceededException, NoAccessPermissionException,
			NoValidEndpointsException, ProcessFailedException,
			ServerBusyException, ServiceNotActiveException,
			ServiceNotFoundException {
		return supportedMatchingMethods;
	}

	protected void setSupportedMatchingMethods(Set<MatchingMethod> supportedMatchingMethods){
		this.matchingMethods = supportedMatchingMethods;
		recalcSupportedValues();
	}

	protected Translation[] doubleSearch(Translation[] result,
			String headWord, MatchingMethod matchingMethod){
		MatchingMethodMatcher m = new MatchingMethodMatcher(headWord, matchingMethod);
		List<Translation> r = new ArrayList<Translation>();
		for(Translation t : result){
			if(m.matches(t.getHeadWord())){
				r.add(t);
			}
		}
		return r.toArray(new Translation[]{});
	}


	protected abstract Collection<Translation> doSearch(
			Language headLang, Language targetLang,
			String headWord, MatchingMethod matchingMethod)
	throws InvalidParameterException, ProcessFailedException;

	private void recalcSupportedValues(){
		this.supportedMatchingMethods = CollectionUtil.collect(
				matchingMethods, new ToStringTransformer<MatchingMethod>()
				).toArray(new String[]{});
	}

	private String[] supportedMatchingMethods = CollectionUtil.collect(
			MINIMUM_MATCHINGMETHODS, new ToStringTransformer<MatchingMethod>()
			).toArray(new String[]{});
	private Set<MatchingMethod> matchingMethods = MINIMUM_MATCHINGMETHODS;
	private static Logger logger = Logger.getLogger(
			AbstractBilingualDictionaryService.class.getName()
			);
}
