/*
 * $Id: AbstractParallelTextService.java 265 2010-10-03 10:25:32Z t-nakaguchi $
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
package jp.go.nict.langrid.wrapper.ws_1_2.paralleltext;

import java.util.Collection;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.go.nict.langrid.commons.transformer.ToStringTransformer;
import jp.go.nict.langrid.commons.util.CollectionUtil;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.language.LanguagePair;
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
import jp.go.nict.langrid.service_1_2.paralleltext.ParallelText;
import jp.go.nict.langrid.service_1_2.paralleltext.ParallelTextService;
import jp.go.nict.langrid.service_1_2.typed.MatchingMethod;
import jp.go.nict.langrid.service_1_2.util.validator.LanguagePairValidator;
import jp.go.nict.langrid.service_1_2.util.validator.LanguageValidator;
import jp.go.nict.langrid.service_1_2.util.validator.MatchingMethodValidator;
import jp.go.nict.langrid.service_1_2.util.validator.StringValidator;
import jp.go.nict.langrid.wrapper.ws_1_2.AbstractLanguagePairService;
import jp.go.nict.langrid.wrapper.ws_1_2.util.collector.Collector;
import jp.go.nict.langrid.wrapper.ws_1_2.util.collector.DupulicatesEliminatingParallelTextCollector;
import jp.go.nict.langrid.wrapper.ws_1_2.util.collector.PassThroughCollector;

/**
 * 
 * This is the base class for the parallel text service.
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 265 $
 */
public abstract class AbstractParallelTextService
extends AbstractLanguagePairService
implements ParallelTextService
{
	/**
	 * 
	 * Constructor.
	 * 
	 */
	public AbstractParallelTextService(){
		recalcSupportedValues();
	}

	/**
	 * 
	 * Constructor that takes the service context as a parameter.
	 * @param serviceContext Service context
	 * 
	 */
	public AbstractParallelTextService(ServiceContext serviceContext){
		super(serviceContext);
		recalcSupportedValues();
	}

	/**
	 * 
	 * Constructor.
	 * Takes the set of supported language pairs as a parameter.
	 * @param supportedPairs Set of supported language pairs
	 * 
	 */
	public AbstractParallelTextService(Collection<LanguagePair> supportedPairs){
		setSupportedLanguagePairs(supportedPairs);
		recalcSupportedValues();
	}

	/**
	 * 
	 * Constructor.
	 * Takes the supported language pair(s) and service context as parameter.
	 * @param serviceContext Service context
	 * @param supportedPairs Supported language pairs
	 * 
	 */
	public AbstractParallelTextService(
		ServiceContext serviceContext
		, Collection<LanguagePair> supportedPairs
		)
	{
		super(serviceContext);
		setSupportedLanguagePairs(supportedPairs);
		recalcSupportedValues();
	}

	public ParallelText[] search(String sourceLang, String targetLang,
			String source, String matchingMethod
			)
	throws AccessLimitExceededException, InvalidParameterException
	, LanguagePairNotUniquelyDecidedException, NoAccessPermissionException
	, NoValidEndpointsException, ProcessFailedException
	, ServerBusyException, ServiceNotActiveException
	, ServiceNotFoundException, UnsupportedLanguagePairException
	, UnsupportedMatchingMethodException
	{
		checkStartupException();
		LanguagePair pair = getValidLanguagePair(
				"sourceLang", sourceLang, "targetLang", targetLang);
		String src = new StringValidator("source", source)
				.notNull().notEmpty().getValue();
		MatchingMethod sm = getValidMatchingMethod(
				"matchingMethod", matchingMethod);

		String trimmedSrc = src.trim();
		if(trimmedSrc.length() > 0){
			src = trimmedSrc;
		}

		acquireSemaphore();
		try{
			Collection<ParallelText> result = doSearch(pair.getSource(), pair.getTarget(), src, sm);
			Collector<ParallelText> c = getEliminateDuplicates() ?
					new DupulicatesEliminatingParallelTextCollector(getMaxResults())
					: new PassThroughCollector<ParallelText>(getMaxResults());
			for(ParallelText p : result){
				c.collect(p);
			}
			return CollectionUtil.toArray(c.getCollection(), ParallelText.class);
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
	 * Runs a search.
	 * @param sourceLang Source language
	 * @param targetLang Target language
	 * @param source Keywords to be searched
	 * @param matchingMethod Matching method
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws ProcessFailedException Process failed
	 * 
	 */
	protected abstract Collection<ParallelText> doSearch(
		Language sourceLang, Language targetLang
		, String source, MatchingMethod matchingMethod
		)
		throws InvalidParameterException, ProcessFailedException
		;

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

	protected LanguagePair getValidLanguagePair(
			String parameterName1, String lang1
			, String parameterName2, String lang2)
	throws LanguagePairNotUniquelyDecidedException
	, UnsupportedLanguagePairException
	, InvalidParameterException
	{
		return new LanguagePairValidator(
				new LanguageValidator(parameterName1, lang1)
				, new LanguageValidator(parameterName2, lang2)
				).notNull().trim().notEmpty().getUniqueLanguagePair(
						getSupportedLanguagePairCollection());
	}

	protected MatchingMethod getValidMatchingMethod(String parameterName
			, String matchingMethod)
	throws InvalidParameterException
	{
		return new MatchingMethodValidator(
				parameterName, matchingMethod
				).notNull().trim().notEmpty()
				.getMatchingMethod(matchingMethods);
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
	private static Logger logger = Logger.getLogger(
			AbstractParallelTextService.class.getName());
}
