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
package jp.go.nict.langrid.wrapper.ws_1_2.keyphraseextract;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import jp.go.nict.langrid.service_1_2.keyphraseextract.Keyphrase;
import jp.go.nict.langrid.service_1_2.keyphraseextract.KeyphraseExtractService;
import jp.go.nict.langrid.service_1_2.util.validator.LanguageValidator;
import jp.go.nict.langrid.service_1_2.util.validator.StringValidator;
import jp.go.nict.langrid.wrapper.ws_1_2.AbstractLanguageService;
import jp.go.nict.langrid.wrapper.ws_1_2.dependencyparser.AbstractDependencyParserService;

public abstract class AbstractKeyphraseExtractService
extends AbstractLanguageService
implements KeyphraseExtractService{
	/**
	 * 
	 * Constructor that doesn't take parameter(s).
	 * 
	 */
	public AbstractKeyphraseExtractService() {
	}

	/**
	 * 
	 * Constructor that takes the supported language as a parameter.
	 * @param supportedLanguages Set of supported languages
	 * 
	 */
	public AbstractKeyphraseExtractService(
			Collection<Language> supportedLanguages) {
		setSupportedLanguageCollection(supportedLanguages);
	}

	/**
	 * 
	 * Constructor that takes the service context and supported language as a parameter(s).
	 * @param serviceContext Service context
	 * @param supportedLanguages Set of supported languages
	 * 
	 */
	public AbstractKeyphraseExtractService(ServiceContext serviceContext,
			Collection<Language> supportedLanguages) {
		super(serviceContext);
		setSupportedLanguageCollection(supportedLanguages);
	}

	public int getMaxSourceLength() {
		return maxSourceLength;
	}

	public void setMaxSourceLength(int maxSourceLength) {
		this.maxSourceLength = maxSourceLength;
	}

	@Override
	public Keyphrase[] extract(String language, String text)
	throws AccessLimitExceededException, InvalidParameterException,
	LanguageNotUniquelyDecidedException, NoAccessPermissionException,
	ProcessFailedException, NoValidEndpointsException,
	ServerBusyException, ServiceNotActiveException,
	ServiceNotFoundException, UnsupportedLanguageException
	{
		checkStartupException();
		Language lang = new LanguageValidator("language", language).notNull()
				.trim().notEmpty().getUniqueLanguage(
						getSupportedLanguageCollection()
						);
		String txt = new StringValidator("text", text).notNull().trim()
				.notEmpty().shorterThanOrEqualsTo(maxSourceLength).getValue();

		acquireSemaphore();
		try {
			return CollectionUtil.toArray(
					doExtract(lang, txt)
					, Keyphrase.class, 0, getMaxResults()
					);
		} catch (InvalidParameterException e) {
			throw e;
		} catch (ProcessFailedException e) {
			throw e;
		} catch (Throwable t) {
			logger.log(Level.SEVERE, "unknown error occurred.", t);
			throw new ProcessFailedException(t);
		} finally {
			releaseSemaphore();
		}
	}

	protected abstract Collection<Keyphrase> doExtract(
			Language language, String text)
	throws InvalidParameterException, ProcessFailedException;

	private int maxSourceLength;
	private static Logger logger = Logger
			.getLogger(AbstractDependencyParserService.class.getName());
}
