/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2011 NICT Language Grid Project.
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

import java.util.Collection;

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
import jp.go.nict.langrid.service_1_2.translation.TranslationWithInternalDictionaryService;
import jp.go.nict.langrid.service_1_2.util.validator.LanguagePairValidator;
import jp.go.nict.langrid.service_1_2.util.validator.LanguageValidator;

public abstract class AbstractTranslationWithInternalDictionaryService
extends AbstractTranslationService
implements TranslationWithInternalDictionaryService{
	/**
	 * 
	 * Constructor that doesn't take parameter(s).
	 * 
	 */
	public AbstractTranslationWithInternalDictionaryService(){
	}

	/**
	 * 
	 * Constructor that takes the service context as a parameter(s).
	 * @param serviceContext Service context
	 * 
	 */
	public AbstractTranslationWithInternalDictionaryService(ServiceContext serviceContext){
		super(serviceContext);
	}

	/**
	 * 
	 * Constructor that takes the service context and supported language pair(s) as a parameter(s).
	 * @param serviceContext Service context
	 * @param supportedPairs Supported language pairs
	 * 
	 */
	public AbstractTranslationWithInternalDictionaryService(
		ServiceContext serviceContext
		, Collection<LanguagePair> supportedPairs
		)
	{
		super(serviceContext, supportedPairs);
		setSupportedLanguagePairs(supportedPairs);
	}

	@Override
	public String[] getSupportedInternalDictionaryIds(String sourceLang, String targetLang)
	throws AccessLimitExceededException, InvalidParameterException
	, LanguagePairNotUniquelyDecidedException, NoAccessPermissionException
	, ProcessFailedException, NoValidEndpointsException
	, ServerBusyException, ServiceNotActiveException
	, ServiceNotFoundException, UnsupportedLanguagePairException {
		LanguagePair pair = validateLanguagePair(new LanguagePairValidator(
				new LanguageValidator("sourceLang", sourceLang)
				, new LanguageValidator("targetLang", targetLang)
				));
		return doGetSupportedInternalDictionaryIds(pair.getSource(), pair.getTarget());
	}

	protected abstract String[] doGetSupportedInternalDictionaryIds(Language sourceLang, Language targetLang)
	throws ProcessFailedException;
}
