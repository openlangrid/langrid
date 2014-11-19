/*
 * $Id: AbstractBackTranslationService.java 265 2010-10-03 10:25:32Z t-nakaguchi $
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
package jp.go.nict.langrid.wrapper.ws_1_2.translation.backtranslation;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.go.nict.langrid.commons.lang.ExceptionUtil;
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
import jp.go.nict.langrid.service_1_2.backtranslation.BackTranslationResult;
import jp.go.nict.langrid.service_1_2.backtranslation.BackTranslationService;
import jp.go.nict.langrid.service_1_2.util.validator.LanguagePairValidator;
import jp.go.nict.langrid.service_1_2.util.validator.LanguageValidator;
import jp.go.nict.langrid.service_1_2.util.validator.StringValidator;
import jp.go.nict.langrid.wrapper.ws_1_2.AbstractLanguagePairService;

/**
 * 
 * Base class for the back translation service
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 265 $
 */
public abstract class AbstractBackTranslationService
extends AbstractLanguagePairService
implements BackTranslationService{
	/**
	 * 
	 * Constructor.
	 * 
	 */
	public AbstractBackTranslationService(){
	}

	/**
	 * 
	 * Constructor.
	 * Takes the supported anguage pair(s) as a parameter.
	 * @param supportedPairs Supported language pairs
	 * 
	 */
	public AbstractBackTranslationService(
		Collection<LanguagePair> supportedPairs
		){
		setSupportedLanguagePairs(supportedPairs);
	}

	/**
	 * 
	 * Constructor.
	 * Takes the supported language pair(s) and service context as parameter.
	 * @param serviceContext Service context
	 * @param supportedPairs Supported language pairs
	 * 
	 */
	public AbstractBackTranslationService(
		ServiceContext serviceContext
		, Collection<LanguagePair> supportedPairs
		){
		super(serviceContext);
		setSupportedLanguagePairs(supportedPairs);
	}

	public BackTranslationResult backTranslate(String sourceLang, String intermediateLang, String source)
		throws AccessLimitExceededException, InvalidParameterException
		, LanguagePairNotUniquelyDecidedException, NoAccessPermissionException
		, NoValidEndpointsException, ServiceNotActiveException
		, ProcessFailedException, ServerBusyException
		, ServiceNotFoundException, UnsupportedLanguagePairException
	{
		checkStartupException();
		LanguagePair pair = new LanguagePairValidator(
				new LanguageValidator("sourceLang", sourceLang)
				, new LanguageValidator("intermediateLang", intermediateLang)
				).notNull().trim().notEmpty().getUniqueLanguagePair(getSupportedLanguagePairCollection());
		String src = new StringValidator("source", source)
				.notNull().trim().notEmpty().getValue();

		Language s = pair.getSource();
		Language t = pair.getTarget();

		acquireSemaphore();
		try{
			return doBackTranslation(s, t, src);
		} catch(InvalidParameterException e){
			throw e;
		} catch(ProcessFailedException e){
			throw e;
		} catch(Throwable th){
			logger.log(Level.SEVERE, "unknown error occurred.", th);
			throw new ProcessFailedException(
					ExceptionUtil.getMessageWithStackTrace(th)
					);
		} finally{
			releaseSemaphore();
		}
	}

	/**
	 * 
	 * Template method supporting backTranstlate.
	 * @param sourceLang Translation source language
	 * @param intermediateLang Translation intermediate language
	 * @param source String to be translated
	 * @return The translated string
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws ProcessFailedException Process failed
	 * 
	 */
	protected abstract BackTranslationResult doBackTranslation(
		Language sourceLang, Language intermediateLang, String source
		)
		throws InvalidParameterException, ProcessFailedException;

	private static Logger logger = Logger.getLogger(
			AbstractBackTranslationService.class.getName()
			);
}
