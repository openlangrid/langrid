/*
 * $Id: AbstractParallelTextWithExternalMetadataService.java 265 2010-10-03 10:25:32Z t-nakaguchi $
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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import jp.go.nict.langrid.service_1_2.paralleltext.ParallelTextWithExternalMetadataService;
import jp.go.nict.langrid.service_1_2.paralleltext.ParallelTextWithId;
import jp.go.nict.langrid.service_1_2.typed.MatchingMethod;
import jp.go.nict.langrid.service_1_2.util.validator.LanguagePairValidator;
import jp.go.nict.langrid.service_1_2.util.validator.LanguageValidator;
import jp.go.nict.langrid.service_1_2.util.validator.MatchingMethodValidator;
import jp.go.nict.langrid.service_1_2.util.validator.StringValidator;
import jp.go.nict.langrid.wrapper.ws_1_2.AbstractLanguagePairService;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 265 $
 */
public abstract class AbstractParallelTextWithExternalMetadataService
extends AbstractLanguagePairService
implements ParallelTextWithExternalMetadataService{
	/**
	 * 
	 * 
	 */
	public AbstractParallelTextWithExternalMetadataService(){
	}

	/**
	 * 
	 * 
	 */
	public AbstractParallelTextWithExternalMetadataService(
			Collection<LanguagePair> supportedPairs){
		setSupportedLanguagePairs(supportedPairs);
	}

	/**
	 * 
	 * 
	 */
	public AbstractParallelTextWithExternalMetadataService(
			ServiceContext serviceContext, Collection<LanguagePair> supportedPairs)
	{
		super(serviceContext);
		setSupportedLanguagePairs(supportedPairs);
		this.supportedMatchingMethods = new HashSet<MatchingMethod>(
				Arrays.asList(MatchingMethod.values()));
	}

	public ParallelTextWithId[] searchParallelTextsFromCandidates(
			String sourceLang, String targetLang, String text
			, String matchingMethod, String[] candidateIds
		)
		throws AccessLimitExceededException, InvalidParameterException
		, LanguagePairNotUniquelyDecidedException, NoAccessPermissionException
		, NoValidEndpointsException, ProcessFailedException, ServerBusyException
		, ServiceNotActiveException, ServiceNotFoundException
		, UnsupportedLanguagePairException
	{
		checkStartupException();
		LanguagePair pair = new LanguagePairValidator(new LanguageValidator("sourceLang",
				sourceLang), new LanguageValidator("targetLang", targetLang)).notNull()
				.trim().notEmpty().getUniqueLanguagePair(
						getSupportedLanguagePairCollection());
		String txt = new StringValidator("text", text).notNull().trim().notEmpty()
				.getValue();
		String[] cis = new String[candidateIds.length];
		int i = 0;
		for(String id : candidateIds){
			cis[i++] = new StringValidator("candedateIds[" + i + "]", id).notNull()
					.trim().notEmpty().getValue();
		}
		MatchingMethod sm = new MatchingMethodValidator("matchingMethod", matchingMethod)
				.notNull().trim().notEmpty().getMatchingMethod(supportedMatchingMethods);
		acquireSemaphore();
		try{
			return CollectionUtil.toArray(
					doSearchParallelTextsFromCandidates(pair
							.getSource(), pair.getTarget(), txt, sm, cis)
					, ParallelTextWithId.class, 0, getMaxResults()
					);
		}catch(InvalidParameterException e){
			throw e;
		}catch(ProcessFailedException e){
			throw e;
		}catch(Throwable t){
			logger.log(Level.SEVERE, "unknown error occurred.", t);
			throw new ProcessFailedException(t);
		}finally{
			releaseSemaphore();
		}
	}

	/**
	 * 
	 * 
	 */
	protected abstract Collection<ParallelTextWithId> doSearchParallelTextsFromCandidates(
		Language sourceLang, Language targetLang, String text,
		MatchingMethod matchingMethod, String[] candidateIds
		)
		throws InvalidParameterException, ProcessFailedException;

	/**
	 * 
	 * 
	 */
	protected void setSupportedMatchingMethods(
		Set<MatchingMethod> supportedMatchingMethods)
	{
		this.supportedMatchingMethods = supportedMatchingMethods;
	}

	private Set<MatchingMethod> supportedMatchingMethods = MINIMUM_MATCHINGMETHODS;
	private static Logger logger = Logger
			.getLogger(AbstractParallelTextWithExternalMetadataService.class.getName());
}
