/*
 * $Id: AbstractTemplateParallelTextService.java 409 2011-08-25 03:12:59Z t-nakaguchi $
 *
 * This is a program to wrap language resources as Web services.
 * Copyright (C) 2010 NICT Language Grid Project.
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
package jp.go.nict.langrid.wrapper.ws_1_2.templateparalleltext;

import java.util.Collection;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.go.nict.langrid.commons.lang.ExceptionUtil;
import jp.go.nict.langrid.commons.transformer.ToStringTransformer;
import jp.go.nict.langrid.commons.transformer.TransformationException;
import jp.go.nict.langrid.commons.transformer.Transformer;
import jp.go.nict.langrid.commons.util.ArrayUtil;
import jp.go.nict.langrid.commons.util.CollectionUtil;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.Category;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.NoValidEndpointsException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.ServerBusyException;
import jp.go.nict.langrid.service_1_2.ServiceNotActiveException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguageException;
import jp.go.nict.langrid.service_1_2.UnsupportedMatchingMethodException;
import jp.go.nict.langrid.service_1_2.templateparalleltext.BoundChoiceParameter;
import jp.go.nict.langrid.service_1_2.templateparalleltext.BoundValueParameter;
import jp.go.nict.langrid.service_1_2.templateparalleltext.Template;
import jp.go.nict.langrid.service_1_2.templateparalleltext.TemplateParallelTextService;
import jp.go.nict.langrid.service_1_2.typed.MatchingMethod;
import jp.go.nict.langrid.service_1_2.util.validator.LanguageValidator;
import jp.go.nict.langrid.service_1_2.util.validator.MatchingMethodValidator;
import jp.go.nict.langrid.service_1_2.util.validator.ObjectValidator;
import jp.go.nict.langrid.service_1_2.util.validator.StringValidator;
import jp.go.nict.langrid.wrapper.ws_1_2.AbstractLanguageService;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 409 $
 */
public abstract class AbstractTemplateParallelTextService
extends AbstractLanguageService
implements TemplateParallelTextService
{
	/**
	 * 
	 * 
	 */
	public AbstractTemplateParallelTextService() {
		recalcSupportedValues();
	}

	/**
	 * 
	 * 
	 */
	public AbstractTemplateParallelTextService(ServiceContext context) {
		super(context);
		recalcSupportedValues();
	}

	/**
	 * 
	 * Constructor.
	 * Takes the set of supported language pairs as a parameter.
	 * @param supportedPairs Set of supported language pairs
	 * 
	 */
	public AbstractTemplateParallelTextService(Collection<Language> supportedLanguages){
		setSupportedLanguageCollection(supportedLanguages);
		recalcSupportedValues();
	}

	@Override
	public Category[] listTemplateCategories(String language)
			throws AccessLimitExceededException, InvalidParameterException,
			NoAccessPermissionException, NoValidEndpointsException,
			ProcessFailedException, ServerBusyException,
			ServiceNotActiveException, ServiceNotFoundException,
			UnsupportedLanguageException {
		checkStartupException();
		Language l = new LanguageValidator("language", language)
				.notNull().trim().notEmpty().getUniqueLanguage(
						getSupportedLanguageCollection());
		acquireSemaphore();
		try {
			return doListTemplateCategories(l);
		} catch(Throwable t) {
			logger.log(Level.SEVERE, "unknown error occurred.", t);
			throw new ProcessFailedException(
					ExceptionUtil.getMessageWithStackTrace(t)
					);
		} finally{
			releaseSemaphore();
		}
	}

	@Override
	public String[] getCategoryNames(String categoryId, String[] languages)
			throws AccessLimitExceededException, InvalidParameterException,
			NoAccessPermissionException, NoValidEndpointsException,
			ProcessFailedException, ServerBusyException,
			ServiceNotActiveException, ServiceNotFoundException,
			UnsupportedLanguageException {
		checkStartupException();
		Language[] langs = null;
		try{
			langs = ArrayUtil.collect(languages, Language.class
					, new Transformer<String, Language>() {
				@Override
				public Language transform(String value)
						throws TransformationException {
					try{
						return new LanguageValidator("languages", value)
							.notNull().trim().notEmpty().getUniqueLanguage(
								getSupportedLanguageCollection());
					} catch(InvalidParameterException e){
						throw new TransformationException(e);
					}
				}});
		} catch(TransformationException e){
			if(e.getCause() instanceof InvalidParameterException){
				throw (InvalidParameterException)e.getCause();
			} else{
				throw new InvalidParameterException(
						"languages", ExceptionUtil.getMessageWithStackTrace(e));
			}
		}
		acquireSemaphore();
		try {
			return doGetCategoryNames(categoryId, langs);
		} catch(Throwable t) {
			logger.log(Level.SEVERE, "unknown error occurred.", t);
			throw new ProcessFailedException(t);
		} finally{
			releaseSemaphore();
		}
	}

	@Override
	public Template[] searchTemplates(String language, String text,
			String matchingMethod, String[] categoryIds)
			throws AccessLimitExceededException, InvalidParameterException,
			NoAccessPermissionException, NoValidEndpointsException,
			ProcessFailedException, ServerBusyException,
			ServiceNotActiveException, ServiceNotFoundException,
			UnsupportedLanguageException, UnsupportedMatchingMethodException {
		checkStartupException();
		Language l = new LanguageValidator("language", language)
				.notNull().trim().notEmpty().getUniqueLanguage(
						getSupportedLanguageCollection());
		String txt = text != null ? text.trim() : null;
		MatchingMethod mm = matchingMethod != null ? getValidMatchingMethod(
				"matchingMethod", matchingMethod) : null;
		String[] cids = categoryIds != null ? categoryIds : new String[]{};


		acquireSemaphore();
		try {
			return doSearchTemplates(l, txt, mm, cids);
		} catch(Throwable t) {
			logger.log(Level.SEVERE, "unknown error occurred.", t);
			throw new ProcessFailedException(
					ExceptionUtil.getMessageWithStackTrace(t)
					);
		} finally{
			releaseSemaphore();
		}
	}

	@Override
	public Template[] getTemplatesByTemplateId(String language,
			String[] templateIds) throws AccessLimitExceededException,
			InvalidParameterException, NoAccessPermissionException,
			NoValidEndpointsException, ProcessFailedException,
			ServerBusyException, ServiceNotActiveException,
			ServiceNotFoundException, UnsupportedLanguageException {
		checkStartupException();
		Language l = new LanguageValidator("language", language)
				.notNull().trim().notEmpty().getUniqueLanguage(
						getSupportedLanguageCollection());
		String[] tids = (String[])new ObjectValidator("templateIds", templateIds)
				.notNull().getValue();
		
		acquireSemaphore();
		try {
			return doGetTemplatesByTemplateId(l, tids);
		} catch(Throwable t) {
			logger.log(Level.SEVERE, "unknown error occurred.", t);
			throw new ProcessFailedException(
					ExceptionUtil.getMessageWithStackTrace(t)
					);
		} finally{
			releaseSemaphore();
		}
	}

	@Override
	public String generateSentence(String language, String templateId,
			BoundChoiceParameter[] boundChoiceParameters,
			BoundValueParameter[] boundRangeParameters)
			throws AccessLimitExceededException, InvalidParameterException,
			NoAccessPermissionException, NoValidEndpointsException,
			ProcessFailedException, ServerBusyException,
			ServiceNotActiveException, ServiceNotFoundException,
			UnsupportedLanguageException {
		checkStartupException();
		Language l = new LanguageValidator("language", language)
				.notNull().trim().notEmpty().getUniqueLanguage(
						getSupportedLanguageCollection());
		String tid = new StringValidator("templateId", templateId)
				.notNull().trim().notEmpty().getValue();
		BoundChoiceParameter[] bcp = (BoundChoiceParameter[])new ObjectValidator(
				"boundChoiceParameters", boundChoiceParameters).notNull().getValue();
		BoundValueParameter[] brp = (BoundValueParameter[])new ObjectValidator(
				"boundValueParameters", boundRangeParameters).notNull().getValue();
		
		acquireSemaphore();
		try {
			return doGenerateSentence(l, tid, bcp, brp);
		} catch(Throwable t) {
			logger.log(Level.SEVERE, "unknown error occurred.", t);
			throw new ProcessFailedException(
					ExceptionUtil.getMessageWithStackTrace(t)
					);
		} finally{
			releaseSemaphore();
		}
	}

	protected abstract Category[] doListTemplateCategories(Language language)
	throws InvalidParameterException, ProcessFailedException;

	protected abstract String[] doGetCategoryNames(String categoryId, Language[] languages)
	throws InvalidParameterException, ProcessFailedException;

	protected abstract Template[] doSearchTemplates(Language language, String text
			, MatchingMethod matchingMethod, String[] categoryIds)
	throws InvalidParameterException, ProcessFailedException;

	protected abstract Template[] doGetTemplatesByTemplateId(Language language,
			String[] templateIds)
	throws InvalidParameterException, ProcessFailedException;

	protected abstract String doGenerateSentence(Language language, String templateId,
			BoundChoiceParameter[] boundChoiceParameters,
			BoundValueParameter[] boundRangeParameters)
	throws InvalidParameterException, ProcessFailedException;

	protected MatchingMethod getValidMatchingMethod(String parameterName
			, String matchingMethod)
	throws InvalidParameterException{
		return new MatchingMethodValidator(
				parameterName, matchingMethod
				).notNull().trim().notEmpty()
				.getMatchingMethod(matchingMethods);
	}

	private void recalcSupportedValues(){
		this.supportedMatchingMethods = CollectionUtil.collect(
				matchingMethods, new ToStringTransformer<MatchingMethod>()
				).toArray(new String[]{});
	}

	private String[] supportedMatchingMethods = CollectionUtil.collect(
			MINIMUM_MATCHINGMETHODS, new ToStringTransformer<MatchingMethod>()
			).toArray(new String[]{});
	private Set<MatchingMethod> matchingMethods = MINIMUM_MATCHINGMETHODS;

	private static Logger logger = Logger.getLogger(
			AbstractTemplateParallelTextService.class.getName()
			);
}
