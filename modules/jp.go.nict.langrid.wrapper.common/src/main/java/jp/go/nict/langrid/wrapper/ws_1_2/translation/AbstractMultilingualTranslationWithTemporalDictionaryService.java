/*
 * $Id: AbstractMultilingualTranslationWithTemporalDictionaryService.java 265 2010-10-03 10:25:32Z t-nakaguchi $
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
package jp.go.nict.langrid.wrapper.ws_1_2.translation;

import java.util.logging.Level;
import java.util.logging.Logger;

import jp.go.nict.langrid.commons.transformer.TransformationException;
import jp.go.nict.langrid.commons.transformer.Transformer;
import jp.go.nict.langrid.commons.util.ArrayUtil;
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
import jp.go.nict.langrid.service_1_2.bilingualdictionary.Translation;
import jp.go.nict.langrid.service_1_2.multilingualtranslation.MultilingualTranslationWithTemporalDictionaryService;
import jp.go.nict.langrid.service_1_2.util.validator.LanguageValidator;
import jp.go.nict.langrid.service_1_2.util.validator.StringValidator;
import jp.go.nict.langrid.wrapper.ws_1_2.AbstractLanguagePairService;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 265 $
 */
public abstract class AbstractMultilingualTranslationWithTemporalDictionaryService
extends AbstractLanguagePairService
implements MultilingualTranslationWithTemporalDictionaryService
{
	public String translate(String sourceLang, String[] targetLangs,
			String source, Translation[] temporalDict,
			String[] dictTargetLangs)
	throws AccessLimitExceededException,
	InvalidParameterException, LanguagePairNotUniquelyDecidedException,
	NoAccessPermissionException, ProcessFailedException,
	NoValidEndpointsException, ServerBusyException,
	ServiceNotActiveException, ServiceNotFoundException,
	UnsupportedLanguagePairException{
		checkStartupException();
		Language sl = new LanguageValidator("sourceLang", sourceLang)
			.notNull().trim().notEmpty().getLanguage();
		Language[] tls = validatingConvert("targetLangs", targetLangs);
		String src = new StringValidator("source", source)
				.notNull().trim().notEmpty().getValue();
		Translation[] dict = temporalDict;
		if(dict == null){
			dict = new Translation[]{};
		}
		Language[] dictLangs = null;
		if(dictTargetLangs != null){
			dictLangs = validatingConvert("dictTargetLangs", dictTargetLangs);
		} else{
			dictLangs = tls;
		}

		acquireSemaphore();
		try{
			return doTranslation(sl, tls, src, dict, dictLangs);
		} catch(InvalidParameterException e){
			throw e;
		} catch(ProcessFailedException e){
			logger.log(Level.WARNING, "process failed.", e);
			throw e;
		} catch(Throwable t){
			logger.log(Level.SEVERE, "unknown error occurred.", t);
			throw new ProcessFailedException(t);
		} finally{
			releaseSemaphore();
		}
	}

	protected abstract String doTranslation(
		Language sourceLang, Language[] targetLangs, String source
		, Translation[] temporalDictionary,
		Language[] dictionaryTargetLangs
		)
	throws InvalidParameterException, ProcessFailedException;

	private Language[] validatingConvert(final String parameterName, String[] langauges)
	throws InvalidParameterException{
		try{
			return ArrayUtil.collect(
					ArrayUtil.collect(langauges, LanguageValidator.class, new Transformer<String, LanguageValidator>() {
						@Override
						public LanguageValidator transform(String value){
							return new LanguageValidator(parameterName, value);
						}
					})
					, Language.class
					, new Transformer<LanguageValidator, Language>() {
						@Override
						public Language transform(LanguageValidator value){
							try{
								return value.notNull().trim().notEmpty().getLanguage();
							} catch(InvalidParameterException e){
								throw new TransformationException(e);
							}
						}
					});
		} catch(TransformationException e){
			if(e.getCause() instanceof InvalidParameterException){
				throw (InvalidParameterException)e.getCause();
			} else{
				throw e;
			}
		}
	}

	private static Logger logger = Logger.getLogger(
			AbstractMultilingualTranslationWithTemporalDictionaryService.class.getName()
			);
}
