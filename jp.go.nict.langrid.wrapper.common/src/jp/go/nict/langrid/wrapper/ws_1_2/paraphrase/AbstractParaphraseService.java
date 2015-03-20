/*
 * $Id: AbstractParaphraseService.java 265 2010-10-03 10:25:32Z t-nakaguchi $
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
package jp.go.nict.langrid.wrapper.ws_1_2.paraphrase;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.go.nict.langrid.commons.lang.ExceptionUtil;
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
import jp.go.nict.langrid.service_1_2.paraphrase.ParaphraseService;
import jp.go.nict.langrid.service_1_2.util.validator.LanguageValidator;
import jp.go.nict.langrid.service_1_2.util.validator.StringValidator;
import jp.go.nict.langrid.wrapper.ws_1_2.AbstractLanguageService;

/**
 * 
 * This is the base class for the paraphrasing service.
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 265 $
 */
public abstract class AbstractParaphraseService
	extends AbstractLanguageService
	implements ParaphraseService
{
	/**
	 * 
	 * Constructor that doesn't take parameter(s).
	 * 
	 */
	public AbstractParaphraseService(){
	}

	/**
	 * 
	 * Constructor that takes the set of supported languages as a parameter.
	 * @param supportedLanguages Set of supported languages
	 * 
	 */
	public AbstractParaphraseService(Collection<Language> supportedLanguages){
		setSupportedLanguageCollection(supportedLanguages);
	}

	/**
	 * 
	 * Constructor that takes the service context and supported language as a parameter(s).
	 * @param serviceContext Service context
	 * @param supportedLanguages Set of supported languages
	 * 
	 */
	public AbstractParaphraseService(
		ServiceContext serviceContext
		, Collection<Language> supportedLanguages
		)
	{
		super(serviceContext);
		setSupportedLanguageCollection(supportedLanguages);
	}

	public String[] paraphrase(String language, String text)
	throws AccessLimitExceededException, InvalidParameterException
	, LanguageNotUniquelyDecidedException, NoAccessPermissionException
	, NoValidEndpointsException, ProcessFailedException
	, ServerBusyException, ServiceNotActiveException
	, ServiceNotFoundException, UnsupportedLanguageException
	{
		checkStartupException();
		Language l = new LanguageValidator("language", language)
			.notNull().trim().notEmpty().getUniqueLanguage(
					getSupportedLanguageCollection());
		String txt = new StringValidator("text", text)
			.notNull().trim().notEmpty().getValue();

		acquireSemaphore();
		try{
			return CollectionUtil.toArray(
					doParaphrase(l, txt)
					, String.class, 0, getMaxResults()
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

	/**
	 * 
	 * Paraphrases the text specified by text,language, returns the results.
	 * @param language Language of text(RFC3066 compliant)
	 * @param text Text (required)
	 * @return Results of paraphrasing
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws ProcessFailedException Process failed
	 * 
	 */
	protected abstract Collection<String> doParaphrase(Language language, String text)
	throws InvalidParameterException, ProcessFailedException;

	private static Logger logger = Logger.getLogger(
			AbstractParaphraseService.class.getName()
			);
}
