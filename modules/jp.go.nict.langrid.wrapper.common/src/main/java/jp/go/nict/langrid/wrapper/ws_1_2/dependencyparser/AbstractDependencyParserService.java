/*
 * $Id: AbstractDependencyParserService.java 507 2012-05-24 04:34:29Z t-nakaguchi $
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
package jp.go.nict.langrid.wrapper.ws_1_2.dependencyparser;

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
import jp.go.nict.langrid.service_1_2.dependencyparser.Chunk;
import jp.go.nict.langrid.service_1_2.dependencyparser.DependencyParserService;
import jp.go.nict.langrid.service_1_2.util.validator.LanguageValidator;
import jp.go.nict.langrid.service_1_2.util.validator.StringValidator;
import jp.go.nict.langrid.wrapper.ws_1_2.AbstractLanguageService;

/**
 * 
 * Abstract class for dependency parsing.
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 507 $
 */
public abstract class AbstractDependencyParserService
extends AbstractLanguageService
implements DependencyParserService{
	/**
	 * 
	 * Constructor that doesn't take parameter(s).
	 * 
	 */
	public AbstractDependencyParserService() {
	}

	/**
	 * 
	 * Constructor that takes the supported language as a parameter.
	 * @param supportedLanguages Set of supported languages
	 * 
	 */
	public AbstractDependencyParserService(
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
	public AbstractDependencyParserService(ServiceContext serviceContext,
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

	public Chunk[] parseDependency(String language, String sentence)
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
		String stc = new StringValidator("sentence", sentence).notNull().trim()
				.notEmpty().getValue();
		if(sentence.length() > maxSourceLength){
			throw new InvalidParameterException("sentence", "too long");
		}
		
		acquireSemaphore();
		try {
			return CollectionUtil.toArray(
					doParseDependency(lang, stc)
					, Chunk.class, 0, getMaxResults()
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

	/**
	 * 
	 * Executes dependency analysis.
	 * @param language Language
	 * @param sentence Sentence to analyze
	 * @return Analysis results
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws ProcessFailedException Process failed
	 * 
	 */
	protected abstract Collection<Chunk> doParseDependency(
			Language language, String sentence)
	throws InvalidParameterException, ProcessFailedException;

	private int maxSourceLength = 5000;

	private static Logger logger = Logger
			.getLogger(AbstractDependencyParserService.class.getName());
}
