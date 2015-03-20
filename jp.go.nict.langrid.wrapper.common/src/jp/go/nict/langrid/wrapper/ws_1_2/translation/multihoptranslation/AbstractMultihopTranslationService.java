/*
 * $Id: AbstractMultihopTranslationService.java 265 2010-10-03 10:25:32Z t-nakaguchi $
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
package jp.go.nict.langrid.wrapper.ws_1_2.translation.multihoptranslation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.go.nict.langrid.commons.lang.ExceptionUtil;
import jp.go.nict.langrid.language.LanguagePath;
import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.LanguagePathNotUniquelyDecidedException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.NoValidEndpointsException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.ServerBusyException;
import jp.go.nict.langrid.service_1_2.ServiceNotActiveException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguagePathException;
import jp.go.nict.langrid.service_1_2.multihoptranslation.MultihopTranslationResult;
import jp.go.nict.langrid.service_1_2.multihoptranslation.MultihopTranslationService;
import jp.go.nict.langrid.service_1_2.util.validator.LanguagePathValidator;
import jp.go.nict.langrid.service_1_2.util.validator.StringValidator;
import jp.go.nict.langrid.wrapper.ws_1_2.AbstractService;

/**
 * 
 * The base class for the multi-hop translation service.
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 265 $
 */
public abstract class AbstractMultihopTranslationService
extends AbstractService
implements MultihopTranslationService
{
	/**
	 * 
	 * Constructor.
	 * Doesn't take parameter(s).
	 * 
	 */
	public AbstractMultihopTranslationService(){
		this.maxHop = getInitParameterInt("langrid.multihopTranslation.maxHop", 20);
		this.supportedPaths = new ArrayList<LanguagePath>();
	}

	public MultihopTranslationResult multihopTranslate(
			String sourceLang, String[] intermediateLangs, String targetLang
			, String source
			)
		throws AccessLimitExceededException, InvalidParameterException
		, LanguagePathNotUniquelyDecidedException, NoAccessPermissionException
		, NoValidEndpointsException, ProcessFailedException
		, ServerBusyException, ServiceNotActiveException
		, ServiceNotFoundException, UnsupportedLanguagePathException
	{
		checkStartupException();
		if(intermediateLangs.length >= maxHop){
			throw new InvalidParameterException(
					"intermediateLangs", "too many hop: "
					+ intermediateLangs.length + "(max: " + maxHop + ")"
					);
		}
		LanguagePath path = new LanguagePathValidator(
				"sourceLang", sourceLang
				, "intermediateLangs", intermediateLangs
				, "targetLang", targetLang
				).notNull().trim().notEmpty().getUniqueLanguagePath(
						supportedPaths);
		String src = new StringValidator("source", source)
				.notNull().trim().notEmpty().getValue();

		acquireSemaphore();
		try{
			return doMultihopTranslation(path, src);
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
	 * Template method supporting multihopTranstlate.
	 * @param languagePath Translation language path
	 * @param source String to be translated
	 * @return The translated string
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws ProcessFailedException Process failed
	 * 
	 */
	protected abstract MultihopTranslationResult doMultihopTranslation(
			LanguagePath languagePath, String source
			)
		throws InvalidParameterException, ProcessFailedException;

	/**
	 * 
	 * Sets the supported language path group.
	 * @param paths Language paths group
	 * 
	 */
	protected void setSupportedLanguagePaths(Collection<LanguagePath> paths){
		this.supportedPaths = paths;
	}

	private int maxHop;
	private Collection<LanguagePath> supportedPaths;
	private static Logger logger = Logger.getLogger(
			AbstractMultihopTranslationService.class.getName()
			);
}
