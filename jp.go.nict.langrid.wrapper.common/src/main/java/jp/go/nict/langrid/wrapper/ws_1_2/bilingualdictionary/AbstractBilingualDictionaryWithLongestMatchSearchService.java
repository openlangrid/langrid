/*
 * $Id: AbstractBilingualDictionaryWithLongestMatchSearchService.java 409 2011-08-25 03:12:59Z t-nakaguchi $
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
import java.util.Calendar;
import java.util.Collection;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.go.nict.langrid.commons.net.URLUtil;
import jp.go.nict.langrid.commons.transformer.ToStringTransformer;
import jp.go.nict.langrid.commons.util.CollectionUtil;
import jp.go.nict.langrid.commons.util.Quartet;
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
import jp.go.nict.langrid.service_1_2.bilingualdictionary.BilingualDictionaryWithLongestMatchSearchService;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.Translation;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.TranslationWithPosition;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme;
import jp.go.nict.langrid.service_1_2.typed.MatchingMethod;
import jp.go.nict.langrid.service_1_2.util.validator.LanguagePairValidator;
import jp.go.nict.langrid.service_1_2.util.validator.LanguageValidator;
import jp.go.nict.langrid.service_1_2.util.validator.MatchingMethodValidator;
import jp.go.nict.langrid.service_1_2.util.validator.ObjectValidator;
import jp.go.nict.langrid.service_1_2.util.validator.StringValidator;
import jp.go.nict.langrid.wrapper.ws_1_2.AbstractLanguagePairService;
import jp.go.nict.langrid.wrapper.ws_1_2.bilingualdictionary.scanner.Scanner;
import jp.go.nict.langrid.wrapper.ws_1_2.bilingualdictionary.scanner.ScannerFactory;

import com.opensymphony.oscache.base.Cache;
import com.opensymphony.oscache.base.NeedsRefreshException;

/**
 * 
 * The base class of the specialized translation service.
 * 
 * @author $Author:koyama $
 * @version $Revision:1495 $
 */
public abstract class AbstractBilingualDictionaryWithLongestMatchSearchService
extends AbstractLanguagePairService
implements BilingualDictionaryWithLongestMatchSearchService {
	/**
	 * 
	 * Constructor.
	 * It does not take parameter(s).
	 * 
	 */
	public AbstractBilingualDictionaryWithLongestMatchSearchService(){
		init();
	}

	/**
	 * 
	 * 
	 */
	public AbstractBilingualDictionaryWithLongestMatchSearchService(ServiceContext serviceContext){
		super(serviceContext);
		init();
	}

	/**
	 * 
	 * Constructor.
	 * Takes the set of supported language pairs as a parameter.
	 * @param supportedPairs Set of supported language pairs
	 * 
	 */
	public AbstractBilingualDictionaryWithLongestMatchSearchService(
			Collection<jp.go.nict.langrid.language.LanguagePair> supportedPairs)
	{
		setSupportedLanguagePairs(supportedPairs);
		init();
	}

	public void setCacheSearchResult(boolean cacheSearchResult) {
		this.cacheSearchResult = cacheSearchResult;
	}

	public void setCacheTtlSec(int cacheTtlSec) {
		this.cacheTtlSec = cacheTtlSec;
	}

	public Translation[] search(String headLang, String targetLang, String headWord, String matchingMethod)
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
				.notNull().getValue();
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
				return CollectionUtil.toArray(
						invokeDoSearchWithCache(pair.getSource(), pair.getTarget(), hw, mm)
						, Translation.class, 0, getMaxResults()
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
		} finally{
			processEnd();
		}
	}

	public TranslationWithPosition[] searchLongestMatchingTerms(
			final String headLang, final String targetLang, final Morpheme[] morphemes)
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
		Morpheme[] m = (Morpheme[])new ObjectValidator("morpheme", morphemes)
				.notNull().getValue();

		TranslationWithPosition[] result = null;
		processStart();
		try{
			acquireSemaphore();
			try {
				result = doSearchLongestMatchingTerms(
						pair.getSource(), pair.getTarget(), m
						).toArray(
						new TranslationWithPosition[]{});
				return result;
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
			final TranslationWithPosition[] r = result;
			processEnd(new Runnable(){
				@Override
				public void run() {
					logger.warning(
							"requests and response[headLang: \"" + headLang + "\""
							+ ", targetLang: " + "\"" + targetLang + "\""
							+ ", morphemes.length: " + morphemes.length
							+ ", result.length: " + ((r != null) ? r.length : null)
							+ "]"
							);
				}
			});
		}
	}

	public String[] getSupportedMatchingMethods()
			throws AccessLimitExceededException, NoAccessPermissionException,
			NoValidEndpointsException, ProcessFailedException,
			ServerBusyException, ServiceNotActiveException,
			ServiceNotFoundException {
		return supportedMatchingMethods;
	}

	public static void clearCache(){
		translateCache = new Cache(true, false, false, false, null, 10000);
	}

	/**
	 * 
	 * Sets supported search methods.
	 * @param supportedMatchingMethods Set of supported search methods
	 * 
	 */
	protected void setSupportedMatchingMethods(Set<MatchingMethod> supportedMatchingMethods){
		this.matchingMethods = supportedMatchingMethods;
		recalcSupportedValues();
	}

	/**
	 * 
	 * Searches the bilingual dictionary using the specified search term(s) and matching method, and returns bilingual translation.
	 * @param headLang Source language of bilingual translation(RFC3066 compliant)
	 * @param targetLang Target language of bilingual translation (RFC3066 compliant)
	 * @param headWord Bilingual translation search language
	 * @param matchingMethod Search method
	 * @return Array with search results stored.If none exist, an empty array.
	 * @throws InvalidParameterException One of either headLang,targetLang,matchingMethod is null or else is an empty string. headLang or targetLang do not comply with RFC3066.String not provided for by matchingMethod
	 * @throws ProcessFailedException Bilingual translation search failed due to some cause
	 * 
	 */
	protected abstract Collection<Translation> doSearch(
			Language headLang, Language targetLang
			, String headWord, MatchingMethod matchingMethod)
	throws InvalidParameterException, ProcessFailedException;

	protected Collection<TranslationWithPosition> doSearchLongestMatchingTerms(
			Language headLang, Language targetLang, Morpheme[] morphemes)
	throws InvalidParameterException, ProcessFailedException{
		return scan(headLang, targetLang, morphemes);
	}

	protected Calendar doGetLastUpdate()
	throws ProcessFailedException{
		return parseDateMacro("$Date: 2011-08-25 12:12:59 +0900 (Thu, 25 Aug 2011) $");
	}

	/**
	 * 
	 * Runs a scan.
	 * @param languages Bilingual translation languages
	 * @param morphemes Array of morphemes
	 * @return Results array
	 * @throws InvalidParameterException One of either headLang,targetLang,matchingMethod is null or else is an empty string. headLang or targetLang do not comply with RFC3066.String not provided for by matchingMethod
	 * @throws ProcessFailedException Bilingual translation search failed due to some cause
	 * 
	 */
	protected Collection<TranslationWithPosition> scan(
			Language headLang, Language targetLang, Morpheme[] morphemes)
	throws InvalidParameterException, ProcessFailedException
	{
		Collection<TranslationWithPosition> result = new ArrayList<TranslationWithPosition>();
		Scanner scanner = ScannerFactory.getInstance(headLang, Scanner.TYPE_LONGEST_MATCH);
		for (int i = 0; i < morphemes.length; i++) {
			Morpheme m = morphemes[i];
			// 
			// Runs specialized dictionary search
			// 
			processLap("executing invokeDoSearchWithCache()");
			Collection<Translation> translations = invokeDoSearchWithCache(
					headLang, targetLang, m.getLemma(), MatchingMethod.PREFIX);
			long lap = processLap("executing invokeDoSearchWithCache() done with " + translations.size() + "translations");
			if(lap > 1000){
				processLap("lap is too long: invokeDoSearchWithCache("
						+ headLang.getCode() + ":" + targetLang.getCode()
						+ "," + m + ",PREFIX)"
						);
			}
			// 
			// When there are no translation results
			// 
			if (translations == null || translations.size() == 0) {
				continue;
			}
			// 
			// When there are translation results
			// 
			Translation[] trans = new Translation[translations.size()];
			translations.toArray(trans);
			processLap("executing scanner.doScan with "
					+ morphemes.length + "morphemes and " + trans.length + "translations");
			int ret = scanner.doScan(headLang, i, morphemes, trans, result);
			processLap("executing scanner.doScan done with resultCode: " + ret);
			if (ret != -1) {
				i = ret;
			}
		}
		return result;
	}

	protected boolean isCacheSearchResult(){
		return cacheSearchResult;
	}

	private void init(){
		recalcSupportedValues();
		cacheSearchResult = getInitParameterBoolean("langrid.cacheSearchResult", true);
		cacheTtlSec = getInitParameterInt("langrid.cacheTtlSec", 60 * 10);
	}

	@SuppressWarnings("unchecked")
	private Collection<Translation> invokeDoSearchWithCache(
			Language headLang, Language targetLang
			, String headWord, MatchingMethod matchingMethod
			)
	throws InvalidParameterException, ProcessFailedException
	{
		Collection<Translation> translations = null;
		String cacheKey = Quartet.create(URLUtil.getUntilQuery(getServiceContext().getRequestUrl())
				, new jp.go.nict.langrid.language.LanguagePair(headLang, targetLang)
				, headWord, matchingMethod).toString();
		if (cacheSearchResult) {
			Cache c = translateCache;
			try{
				translations = (Collection<Translation>)c.getFromCache(cacheKey, cacheTtlSec);
			} catch(NeedsRefreshException e){
				try{
					translations = doSearch(
							headLang, targetLang
							, headWord, matchingMethod);
				} finally{
					if(translations != null){
						c.putInCache(cacheKey, translations);
					} else{
						c.cancelUpdate(cacheKey);
					}
				}
			}
		} else{
			translations = doSearch(headLang, targetLang, headWord, matchingMethod);
		}
		if(translations != null){
			return translations;
		} else{
			return new ArrayList<Translation>();
		}
	}

	private void recalcSupportedValues(){
		this.supportedMatchingMethods = CollectionUtil.collect(
				matchingMethods, new ToStringTransformer<MatchingMethod>()
				).toArray(new String[]{});
	}

	private String[] supportedMatchingMethods = CollectionUtil.collect(
			MINIMUM_MATCHINGMETHODS, new ToStringTransformer<MatchingMethod>()
			).toArray(new String[]{});
	private Set<MatchingMethod> matchingMethods = MINIMUM_MATCHINGMETHODS;
	private boolean cacheSearchResult = true;
	private int cacheTtlSec = 10 * 60;
	private static Cache translateCache = new Cache(true, false, false, false, null, 10000);
	private static Logger logger = Logger.getLogger(
			AbstractBilingualDictionaryWithLongestMatchSearchService.class.getName()
			);
}
