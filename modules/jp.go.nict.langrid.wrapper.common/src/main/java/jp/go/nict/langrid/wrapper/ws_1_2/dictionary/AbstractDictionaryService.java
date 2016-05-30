/*
 * $Id: AbstractDictionaryService.java 265 2010-10-03 10:25:32Z t-nakaguchi $
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
package jp.go.nict.langrid.wrapper.ws_1_2.dictionary;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.go.nict.langrid.commons.lang.ExceptionUtil;
import jp.go.nict.langrid.commons.lang.StringUtil;
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
import jp.go.nict.langrid.service_1_2.dictionary.ConceptNode;
import jp.go.nict.langrid.service_1_2.dictionary.DictionaryService;
import jp.go.nict.langrid.service_1_2.dictionary.LemmaNode;
import jp.go.nict.langrid.service_1_2.dictionary.NodeNotFoundException;
import jp.go.nict.langrid.service_1_2.dictionary.typed.DictMatchingMethod;
import jp.go.nict.langrid.service_1_2.typed.PartOfSpeech;
import jp.go.nict.langrid.service_1_2.util.ParameterValidator;
import jp.go.nict.langrid.service_1_2.util.LanguageParameterValidator;
import jp.go.nict.langrid.service_1_2.util.validator.LanguagePairValidator;
import jp.go.nict.langrid.service_1_2.util.validator.LanguageValidator;
import jp.go.nict.langrid.service_1_2.util.validator.StringValidator;
import jp.go.nict.langrid.wrapper.ws_1_2.AbstractLanguagePairService;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 265 $
 */
public abstract class AbstractDictionaryService
extends AbstractLanguagePairService
implements DictionaryService{
	/**
	 * 
	 * 
	 */
	public AbstractDictionaryService(
		Collection<LanguagePair> supportedPairs)
	{
		this.serviceName = getClass().getSimpleName().toLowerCase();
		setSupportedLanguagePairs(supportedPairs);
	}

	/**
	 * 
	 * 
	 */
	public AbstractDictionaryService(
		String serviceName, Collection<LanguagePair> supportedPairs)
	{
		this.serviceName = serviceName;
		setSupportedLanguagePairs(supportedPairs);
	}

	/**
	 * 
	 * 
	 */
	public AbstractDictionaryService(
		ServiceContext serviceContext
		, Collection<LanguagePair> supportedPairs
		)
	{
		super(serviceContext);
		this.serviceName = getClass().getSimpleName().toLowerCase();
		setSupportedLanguagePairs(supportedPairs);
	}

	public final String[] searchLemmaNodes(
		String headLang, String lemmaLang
		, String headWord, String pronounciation
		, String partOfSpeech, String matchingMethod
	)
	throws AccessLimitExceededException, InvalidParameterException
	, LanguagePairNotUniquelyDecidedException, NoAccessPermissionException
	, NoValidEndpointsException, ProcessFailedException
	, ServerBusyException, ServiceNotActiveException
	, ServiceNotFoundException, UnsupportedLanguagePairException
	{
		checkStartupException();
		LanguagePair pair = new LanguagePairValidator(
				new LanguageValidator("headLang", headLang)
				, new LanguageValidator("lemmaLang", lemmaLang)
				).trim().notNull().notEmpty().getUniqueLanguagePair(
						getSupportedLanguagePairCollection());
		String hw = new StringValidator("headWord", headWord)
				.notNull().trim().getValue();
		String pro = new StringValidator("pronounciation", pronounciation)
				.notNull().trim().getValue();
		PartOfSpeech ps = null;
		if(partOfSpeech.trim().length() > 0){
			ps = LanguageParameterValidator.getValidPartOfSpeech("partOfSpeech", partOfSpeech);
		}
		DictMatchingMethod sm = LanguageParameterValidator.getValidDictSearchMethod("matchingMethod", matchingMethod);

		acquireSemaphore();
		try{
			List<URI> uris = doSearchLemmaNodes(
					pair.getSource(), pair.getTarget(), hw, pro, ps, sm
					);
			int n = Math.min(uris.size(), getMaxResults());
			String[] ids = new String[n];
			for(int i = 0; i < n; i++){
				ids[i] = uris.get(i).toString();
			}
			return ids;
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

	public final LemmaNode getLemma(String lemmaNodeId)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, NodeNotFoundException
		, ProcessFailedException, ServerBusyException
		, ServiceNotActiveException, ServiceNotFoundException
	{
		checkStartupException();
		ParameterValidator.objectNotNull("lemmaNodeId", lemmaNodeId);
		URI id = ParameterValidator.getValidURI("lemmaNodeId", lemmaNodeId);
		URI lemmaId = getValidDictURISpecificPart(
			"lemmaNodeId", id, serviceName
			);

		acquireSemaphore();
		try{
			return doGetLemma(lemmaId);
		} catch(InvalidParameterException e){
			throw e;
		} catch(ProcessFailedException e){
			throw e;
		} catch(NodeNotFoundException e){
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

	public final ConceptNode getConcept(String conceptNodeId)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, NodeNotFoundException
		, ProcessFailedException, ServerBusyException
		, ServiceNotActiveException, ServiceNotFoundException
	{
		checkStartupException();
		ParameterValidator.objectNotNull("conceptNodeId", conceptNodeId);
		URI id = ParameterValidator.getValidURI("conceptNodeId", conceptNodeId);
		URI conceptId = getValidDictURISpecificPart("lemmaNodeId", id, serviceName);

		acquireSemaphore();
		try{
			return doGetConcept(conceptId);
		} catch(InvalidParameterException e){
			throw e;
		} catch(ProcessFailedException e){
			throw e;
		} catch(NodeNotFoundException e){
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

	protected abstract List<URI> doSearchLemmaNodes(
		Language headLang, Language lemmaLang
		, String headWord, String pronounciation
		, PartOfSpeech partOfSpeech, DictMatchingMethod matchingMethod
		)
	throws InvalidParameterException, ProcessFailedException;

	protected abstract LemmaNode doGetLemma(
		URI lemmaNodeId
		)
	throws InvalidParameterException, NodeNotFoundException
	, ProcessFailedException;

	protected abstract ConceptNode doGetConcept(URI conceptNodeId)
	throws InvalidParameterException, NodeNotFoundException
	, ProcessFailedException;

	/**
	 * 
	 * 
	 */
	static URI getValidDictURISpecificPart(
		String parameterName, URI uri, String serviceName
		)
		throws InvalidParameterException
	{
		if(!uri.getScheme().equals("urn")){
			throw new InvalidParameterException(parameterName, "invalid dict urn. uri must start with 'urn'.");
		}
		String[] specs = uri.getSchemeSpecificPart().split(":");
		if(specs.length < 3){
			throw new InvalidParameterException(
					parameterName, "invalid dict urn. number of specs must >= 3."
					);
		}
		if(!specs[0].equals("langrid")){
			throw new InvalidParameterException(
					parameterName, "invalid dict urn. specs[1] must be 'langrid'");
		}
		if(!specs[1].equals("dict")){
			throw new InvalidParameterException(
					parameterName, "invalid dict urn. specs[2] must be 'dict'");
		}
		if(!specs[2].equals(serviceName)){
			throw new InvalidParameterException(
					parameterName, "invalid dict urn. specs[3] must be '" + serviceName + "'"
					);
		}
		try{
			return new URI(StringUtil.join(specs, ":", 3, specs.length));
		} catch(URISyntaxException e){
			throw new InvalidParameterException(
					parameterName, "invalid dict urn. failed to create URI.");
		}
	}

	private String serviceName;
	private static Logger logger = Logger.getLogger(
			AbstractDictionaryService.class.getName()
			);
}
