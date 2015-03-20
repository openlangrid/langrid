/*
 * $Id: AbstractTemporalBilingualDictionaryWithLongestMatchSearchService.java 409 2011-08-25 03:12:59Z t-nakaguchi $
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

package jp.go.nict.langrid.wrapper.ws_1_2.workflowsupport;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.go.nict.langrid.commons.lang.ExceptionUtil;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.NoValidEndpointsException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.ServerBusyException;
import jp.go.nict.langrid.service_1_2.ServiceNotActiveException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguageException;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.Translation;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.TranslationWithPosition;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme;
import jp.go.nict.langrid.service_1_2.workflowsupport.TemporalBilingualDictionaryWithLongestMatchSearchSerivce;
import jp.go.nict.langrid.wrapper.ws_1_2.AbstractLanguageService;

/**
 * 
 * 
 * @author koyama
 * @author $Author: t-nakaguchi $
 * @version $Revision: 409 $
 */
public abstract class AbstractTemporalBilingualDictionaryWithLongestMatchSearchService
extends AbstractLanguageService
implements TemporalBilingualDictionaryWithLongestMatchSearchSerivce
{
	/**
	 * 
	 * 
	 */
	public AbstractTemporalBilingualDictionaryWithLongestMatchSearchService() {
//		setSupportedLanguageCollection(Arrays.asList(
//				ja, en, zh, ko, es, pt, fr, de, it));
	}

	public AbstractTemporalBilingualDictionaryWithLongestMatchSearchService(ServiceContext context) {
		super(context);
	}

	/* (non-Javadoc)
	 * @see jp.go.nict.langrid.service_1_2.bilingualdictionary.UserDictionaryMorphemeBasedMatchingTranslationSerivce#searchAllLongestMatchingTerms(java.lang.String, jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme[], jp.go.nict.langrid.service_1_2.bilingualdictionary.Translation[])
	 */
	public TranslationWithPosition[] searchAllLongestMatchingTerms(String headLang,
			Morpheme[] morphemes, Translation[] temporalDict)
		throws AccessLimitExceededException, InvalidParameterException,
		NoAccessPermissionException, NoValidEndpointsException,
		ProcessFailedException, ServerBusyException,
		ServiceNotActiveException, ServiceNotFoundException,
		UnsupportedLanguageException {

//		Language language = new LanguageValidator(
//				"headLang", headLang).notNull().trim().notEmpty()
//				.getUniqueLanguage(getSupportedLanguageCollection());
		acquireSemaphore();
		try{
			return doSearchAllLongestMatchingTerms(new Language(headLang), morphemes, temporalDict).toArray(new TranslationWithPosition[]{});
		} catch(InvalidParameterException e){
			throw e;
		} catch(ProcessFailedException e){
			throw e;
		} catch(Throwable t){
			logger.log(Level.SEVERE, "unknown error occurred.", t);
			throw new ProcessFailedException(
					ExceptionUtil.getMessageWithStackTrace(t)
					);
		} finally{
			releaseSemaphore();
		}
	}

	protected abstract Collection<TranslationWithPosition>
	doSearchAllLongestMatchingTerms(
			Language language, Morpheme[] morphemes, Translation[] translations)
			throws InvalidParameterException, ProcessFailedException;

	private static Logger logger = Logger.getLogger(
			AbstractTemporalBilingualDictionaryWithLongestMatchSearchService.class.getName()
			);
}
