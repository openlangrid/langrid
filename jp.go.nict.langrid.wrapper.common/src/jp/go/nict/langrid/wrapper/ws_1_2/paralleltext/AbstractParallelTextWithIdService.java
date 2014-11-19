/*
 * $Id: AbstractParallelTextWithIdService.java 265 2010-10-03 10:25:32Z t-nakaguchi $
 *
 * This is a program to wrap language resources as Web services.
 * Copyright (C) 2009 NICT Language Grid Project.
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
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.go.nict.langrid.commons.lang.ExceptionUtil;
import jp.go.nict.langrid.commons.transformer.TransformationException;
import jp.go.nict.langrid.commons.transformer.Transformer;
import jp.go.nict.langrid.commons.util.CollectionUtil;
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.language.LanguagePair;
import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.Category;
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
import jp.go.nict.langrid.service_1_2.paralleltext.ParallelTextWithId;
import jp.go.nict.langrid.service_1_2.paralleltext.ParallelTextWithIdService;
import jp.go.nict.langrid.service_1_2.typed.MatchingMethod;
import jp.go.nict.langrid.service_1_2.util.validator.StringValidator;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 265 $
 */
public abstract class AbstractParallelTextWithIdService
extends AbstractParallelTextService
implements ParallelTextWithIdService
{
	/**
	 * 
	 * Constructor.
	 * 
	 */
	public AbstractParallelTextWithIdService(){
	}

	public ParallelTextWithId[] searchParallelTexts(
			String sourceLang, String targetLang
			, String source, String matchingMethod
			, String[] categoryIds
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
				.notNull().trim().notEmpty().getValue();
		MatchingMethod sm = getValidMatchingMethod(
				"matchingMethod", matchingMethod);

		acquireSemaphore();
		try{
			return CollectionUtil.toArray(
					doSearchParallelTexts(pair.getSource(), pair.getTarget(), src, sm, categoryIds)
					, ParallelTextWithId.class, 0, getMaxResults()
					);
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

	public Category[] listCategories() throws AccessLimitExceededException,
			NoAccessPermissionException, NoValidEndpointsException,
			ProcessFailedException, ServerBusyException,
			ServiceNotActiveException, ServiceNotFoundException {
		acquireSemaphore();
		try{
			return doListCategories().toArray(new Category[]{});
		} finally{
			releaseSemaphore();
		}
	}

	protected Collection<ParallelText> doSearch(
			Language sourceLang, Language targetLang
			, String source, MatchingMethod matchingMethod
			)
			throws InvalidParameterException, ProcessFailedException
	{
		return CollectionUtil.collect(
				doSearchParallelTexts(
						sourceLang, targetLang, source, matchingMethod, new String[]{}
						)
				, new Transformer<ParallelTextWithId, ParallelText>(){
					public ParallelText transform(ParallelTextWithId value) throws TransformationException {
						return new ParallelText(value.getSource(), value.getTarget());
					};
				});
	}

	protected abstract Collection<ParallelTextWithId> doSearchParallelTexts(
			Language sourceLang, Language targetLang
			, String source, MatchingMethod matchingMethod
			, String[] categoryIds)
	throws InvalidParameterException, ProcessFailedException;

	protected abstract Collection<Category> doListCategories()
	throws ProcessFailedException;

	private static Logger logger = Logger.getLogger(
			AbstractParallelTextWithIdService.class.getName());
}
