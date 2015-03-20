/*
 * $Id: AbstractGetLongestMatchingTermService.java 265 2010-10-03 10:25:32Z t-nakaguchi $
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
import jp.go.nict.langrid.service_1_2.util.validator.LanguageValidator;
import jp.go.nict.langrid.service_1_2.workflowsupport.GetLongestMatchingTermService;
import jp.go.nict.langrid.wrapper.ws_1_2.AbstractLanguageService;

/**
 * 
 * 
 * @author koyama
 * @author $Author: t-nakaguchi $
 * @version $Revision: 265 $
 */
public abstract class AbstractGetLongestMatchingTermService
extends AbstractLanguageService
implements GetLongestMatchingTermService {
	/* (non-Javadoc)
	 * @see jp.go.nict.langrid.service_1_2.bilingualdictionary.BilingualDictionaryMorphemeBasedMatchingTranslationSerivce#getLongestMatchingTerm(java.lang.String, jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme[], int, jp.go.nict.langrid.service_1_2.bilingualdictionary.Translation[])
	 */
	public TranslationWithPosition[] getLongestMatchingTerm(String headLang,
			Morpheme[] morphemes, int startIndex, Translation[] translations)
			throws AccessLimitExceededException, InvalidParameterException,
			NoAccessPermissionException, NoValidEndpointsException,
			ProcessFailedException, ServerBusyException,
			ServiceNotActiveException, ServiceNotFoundException,
			UnsupportedLanguageException {
		Language language = new LanguageValidator(
				"headLang", headLang).notNull().trim().notEmpty()
				.getUniqueLanguage(getSupportedLanguageCollection());

		acquireSemaphore();
		try{
			return doGetLongestMatchingTerm(
					language, morphemes, startIndex, translations).toArray(
							new TranslationWithPosition[]{});
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
		doGetLongestMatchingTerm(
			Language headLang,
			Morpheme[] morphemes, int startIndex, Translation[] translations)
			throws AccessLimitExceededException, InvalidParameterException,
			NoAccessPermissionException, NoValidEndpointsException,
			ProcessFailedException, ServerBusyException,
			ServiceNotActiveException, ServiceNotFoundException,
			UnsupportedLanguageException;

	private static Logger logger = Logger.getLogger(
			AbstractGetLongestMatchingTermService.class.getName()
			);
}
