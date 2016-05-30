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
package jp.go.nict.langrid.wrapper.ws_1_2.textsummarize;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import jp.go.nict.langrid.service_1_2.textsummarize.TextSummarizeService;
import jp.go.nict.langrid.service_1_2.util.validator.LanguageValidator;
import jp.go.nict.langrid.service_1_2.util.validator.StringValidator;
import jp.go.nict.langrid.wrapper.ws_1_2.AbstractLanguageService;

public abstract class AbstractTextSummarizeService
extends AbstractLanguageService
implements TextSummarizeService{
	/**
	 * 
	 * Constructor that doesn't take parameter(s).
	 * 
	 */
	public AbstractTextSummarizeService(){
	}

	/**
	 * 
	 * Constructor that takes the service context as a parameter(s).
	 * @param serviceContext Service context
	 * 
	 */
	public AbstractTextSummarizeService(ServiceContext serviceContext){
		super(serviceContext);
	}

	/**
	 * 
	 * Constructor that takes the service context and supported language pair(s) as a parameter(s).
	 * @param serviceContext Service context
	 * @param supportedPairs Supported language pairs
	 * 
	 */
	public AbstractTextSummarizeService(
		ServiceContext serviceContext
		, Collection<Language> supportedLanguages
		)
	{
		super(serviceContext);
		setSupportedLanguageCollection(supportedLanguages);
	}

	@Override
	public String summarize(String language, String text)
			throws AccessLimitExceededException, InvalidParameterException,
			LanguageNotUniquelyDecidedException, NoAccessPermissionException,
			ProcessFailedException, NoValidEndpointsException,
			ServerBusyException, ServiceNotActiveException,
			ServiceNotFoundException, UnsupportedLanguageException {
		checkStartupException();
		Language lang = new LanguageValidator("language", language).notNull()
				.trim().notEmpty().getUniqueLanguage(
						getSupportedLanguageCollection()
						);
		String txt = new StringValidator("text", text).notNull().trim()
				.notEmpty().shorterThanOrEqualsTo(maxSourceLength).getValue();

		acquireSemaphore();
		try {
			return doSummarize(lang, txt);
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

	public void setMaxSourceLength(int maxSourceLength) {
		this.maxSourceLength = maxSourceLength;
	}

	protected abstract String doSummarize(
			Language language, String text)
	throws InvalidParameterException, ProcessFailedException;

	private int maxSourceLength = 5000;

	private static Logger logger = Logger
			.getLogger(AbstractTextSummarizeService.class.getName());
}
