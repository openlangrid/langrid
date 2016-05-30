/*
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
package jp.go.nict.langrid.wrapper.ws_1_2.examplebasedtranslation;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import jp.go.nict.langrid.service_1_2.examplebasedtranslation.ExampleBasedTranslationService;
import jp.go.nict.langrid.service_1_2.examplebasedtranslation.TrainingCorpusInfo;
import jp.go.nict.langrid.service_1_2.paralleltext.ParallelText;
import jp.go.nict.langrid.service_1_2.util.validator.LanguagePairValidator;
import jp.go.nict.langrid.service_1_2.util.validator.LanguageValidator;
import jp.go.nict.langrid.service_1_2.util.validator.ObjectValidator;
import jp.go.nict.langrid.service_1_2.util.validator.StringValidator;
import jp.go.nict.langrid.wrapper.ws_1_2.AbstractLanguagePairService;

/**
 * 
 * Base class for the example based translation service.
 * 
 * @author Takao Nakaguchi
 */
public abstract class AbstractExampleBasedTranslationService
extends AbstractLanguagePairService
implements ExampleBasedTranslationService{
	/**
	 * 
	 * Constructor that doesn't take parameter(s).
	 * 
	 */
	public AbstractExampleBasedTranslationService(){
	}

	/**
	 * 
	 * Constructor that takes the service context and supported language pair(s) as a parameter(s).
	 * @param serviceContext Service context
s	 * 
	 */
	public AbstractExampleBasedTranslationService(
		ServiceContext serviceContext
		){
		super(serviceContext);
	}

	@Override
	public String createTrainingCorpus(String sourceLang, String targetLang,
			ParallelText[] examples) throws AccessLimitExceededException,
			InvalidParameterException, NoAccessPermissionException,
			ProcessFailedException, NoValidEndpointsException,
			ServerBusyException, ServiceNotActiveException,
			ServiceNotFoundException, UnsupportedLanguagePairException {
		LanguagePair pair = new LanguagePairValidator(
				new LanguageValidator("sourceLang", sourceLang)
				, new LanguageValidator("targetLang", targetLang)
				).notNull().trim().notEmpty().getUniqueLanguagePair(
						getSupportedLanguagePairCollection()
				);
		new ObjectValidator("examples", examples).notNull();

		processStart();
		try{
			acquireSemaphore();
			try{
				Language s = pair.getSource();
				Language t = pair.getTarget();
				return doCreateTrainingCorpus(s, t, examples);
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
		} finally{
			processEnd();
		}
	}

	@Override
	public void destroyTrainingCorpus(String corpusId)
			throws AccessLimitExceededException, InvalidParameterException,
			NoAccessPermissionException, ProcessFailedException,
			NoValidEndpointsException, ServerBusyException,
			ServiceNotActiveException, ServiceNotFoundException {
		String cid = new StringValidator("corpusId", corpusId)
				.notNull().notEmpty().getValue();

		processStart();
		try{
			acquireSemaphore();
			try{
				doDestroyTrainingCorpus(cid);
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
		} finally{
			processEnd();
		}
	}
	
	@Override
	public TrainingCorpusInfo getTrainingCorpusInfo(String corpusId)
			throws AccessLimitExceededException, InvalidParameterException,
			NoAccessPermissionException, ProcessFailedException,
			NoValidEndpointsException, ServerBusyException,
			ServiceNotActiveException, ServiceNotFoundException {
		String cid = new StringValidator("corpusId", corpusId)
				.notNull().notEmpty().getValue();

		processStart();
		try{
			acquireSemaphore();
			try{
				return doGetTrainingCorpusInfo(cid);
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
		} finally{
			processEnd();
		}
	}
	
	@Override
	public void appendExamples(String corpusId, String sourceLang,
			String targetLang, ParallelText[] examples)
			throws AccessLimitExceededException, InvalidParameterException,
			NoAccessPermissionException, ProcessFailedException,
			NoValidEndpointsException, ServerBusyException,
			ServiceNotActiveException, ServiceNotFoundException,
			UnsupportedLanguagePairException {
		String cid = new StringValidator("corpusId", corpusId)
				.notNull().notEmpty().getValue();
		LanguagePair pair = new LanguagePairValidator(
				new LanguageValidator("sourceLang", sourceLang)
				, new LanguageValidator("targetLang", targetLang)
				).notNull().trim().notEmpty().getUniqueLanguagePair(
						getSupportedLanguagePairCollection()
						);
		new ObjectValidator("examples", examples).notNull();

		processStart();
		try{
			acquireSemaphore();
			try{
				Language s = pair.getSource();
				Language t = pair.getTarget();
				doAppendExamples(cid, s, t, examples);
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
		} finally{
			processEnd();
		}
	}

	@Override
	public void replaceExamples(String corpusId, String sourceLang,
			String targetLang, ParallelText[] examples)
			throws AccessLimitExceededException, InvalidParameterException,
			NoAccessPermissionException, ProcessFailedException,
			NoValidEndpointsException, ServerBusyException,
			ServiceNotActiveException, ServiceNotFoundException,
			UnsupportedLanguagePairException {
		String cid = new StringValidator("corpusId", corpusId)
				.notNull().notEmpty().getValue();
		LanguagePair pair = new LanguagePairValidator(
				new LanguageValidator("sourceLang", sourceLang)
				, new LanguageValidator("targetLang", targetLang)
				).notNull().trim().notEmpty().getUniqueLanguagePair(
						getSupportedLanguagePairCollection()
						);
		new ObjectValidator("examples", examples).notNull();

		processStart();
		try{
			acquireSemaphore();
			try{
				Language s = pair.getSource();
				Language t = pair.getTarget();
				doReplaceExamples(cid, s, t, examples);
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
		} finally{
			processEnd();
		}
	}

	@Override
	public String translate(String sourceLang, String targetLang, String source)
			throws AccessLimitExceededException, InvalidParameterException,
			LanguagePairNotUniquelyDecidedException,
			NoAccessPermissionException, NoValidEndpointsException,
			ProcessFailedException, ServerBusyException,
			ServiceNotActiveException, ServiceNotFoundException,
			UnsupportedLanguagePairException {
		String src = new StringValidator("source", source)
			.notNull().trim().getValue();
		String q = getServiceContext().getRequestUrl().getQuery();
		List<String> corpusIds = new ArrayList<String>();
		if(q != null) for(String nv : q.split("^\\?|\\&")){
			if(nv.length() == 0) continue;
			String[] v = nv.split("=", 2);
			if(v[0].equals("corpusIds") && v.length > 1 && v[1].length() > 0){
				for(String id : v[1].split(",")){
					id = id.trim();
					if(id.length() > 0){
						corpusIds.add(id);
					}
				}
			}
		}
		return translateWithTrainingCorpus(
				corpusIds.toArray(new String[]{})
				, sourceLang, targetLang, src);
	}

	@Override
	public String translateWithTrainingCorpus(String[] corpusIds,
			String sourceLang, String targetLang, String source)
			throws AccessLimitExceededException, InvalidParameterException,
			NoAccessPermissionException, ProcessFailedException,
			NoValidEndpointsException, ServerBusyException,
			ServiceNotActiveException, ServiceNotFoundException,
			UnsupportedLanguagePairException {
		new ObjectValidator("corpusIds", corpusIds).notNull();
		LanguagePair pair = new LanguagePairValidator(
				new LanguageValidator("sourceLang", sourceLang)
				, new LanguageValidator("targetLang", targetLang)
				).notNull().trim().notEmpty().getUniqueLanguagePair(
						getSupportedLanguagePairCollection()
				);
		String src = new StringValidator("source", source)
				.notNull().trim().getValue();
		return doTranslateWithTrainingCorpus(corpusIds
				, pair.getSource(), pair.getTarget(), src);
	}

	protected abstract String doCreateTrainingCorpus(
		Language sourceLang, Language targetLang, ParallelText[] examples
		)
	throws InvalidParameterException, ProcessFailedException;

	protected abstract void doDestroyTrainingCorpus(String corpusId)
	throws InvalidParameterException, ProcessFailedException;

	protected abstract TrainingCorpusInfo doGetTrainingCorpusInfo(String corpusId)
	throws InvalidParameterException, ProcessFailedException;

	protected abstract void doAppendExamples(String corpusId, Language sourceLang
			, Language targetLang, ParallelText[] examples)
	throws InvalidParameterException, ProcessFailedException;

	protected abstract void doReplaceExamples(String corpusId, Language sourceLang
			, Language targetLang, ParallelText[] examples)
	throws InvalidParameterException, ProcessFailedException;

	protected abstract String doTranslateWithTrainingCorpus(String[] corpusIds
			, Language sourceLang, Language targetLang, String source)
	throws InvalidParameterException, ProcessFailedException;

	private static Logger logger = Logger.getLogger(
			AbstractExampleBasedTranslationService.class.getName()
			);
}
