/*
 * $Id: AbstractMetadataForParallelTextService.java 265 2010-10-03 10:25:32Z t-nakaguchi $
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
package jp.go.nict.langrid.wrapper.ws_1_2.paralleltext;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.go.nict.langrid.commons.util.ArrayUtil;
import jp.go.nict.langrid.commons.ws.ServiceContext;
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
import jp.go.nict.langrid.service_1_2.paralleltext.MetadataForParallelTextService;
import jp.go.nict.langrid.service_1_2.typed.MatchingMethod;
import jp.go.nict.langrid.service_1_2.util.validator.MatchingMethodValidator;
import jp.go.nict.langrid.service_1_2.util.validator.StringValidator;
import jp.go.nict.langrid.wrapper.ws_1_2.AbstractLanguagePairService;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 265 $
 */
public abstract class AbstractMetadataForParallelTextService
extends AbstractLanguagePairService
implements MetadataForParallelTextService
{
	/**
	 * 
	 * 
	 */
	public AbstractMetadataForParallelTextService(){
	}

	/**
	 * 
	 * 
	 */
	public AbstractMetadataForParallelTextService(
			Collection<LanguagePair> supportedPairs){
		setSupportedLanguagePairs(supportedPairs);
	}

	/**
	 * 
	 * 
	 */
	public AbstractMetadataForParallelTextService(
			ServiceContext serviceContext, Collection<LanguagePair> supportedPairs)
	{
		super(serviceContext);
		setSupportedLanguagePairs(supportedPairs);
		this.supportedMatchingMethods = new HashSet<MatchingMethod>(
				Arrays.asList(MatchingMethod.values()));
	}

	public String[] getMetadata(String Id)
		throws AccessLimitExceededException
		, InvalidParameterException, LanguagePairNotUniquelyDecidedException
		, NoAccessPermissionException, NoValidEndpointsException
		, ProcessFailedException, ServerBusyException, ServiceNotActiveException
		, ServiceNotFoundException, UnsupportedLanguagePairException
	{
		checkStartupException();
		String id = new StringValidator("Id", Id).notNull().trim().notEmpty().getValue();
		acquireSemaphore();
		try{
			String[] results = doGetMetadata(id);
			if(results.length <= getMaxResults()){
				return results;
			} else{
				return ArrayUtil.subArray(results, 0, getMaxResults());
			}
		}catch(InvalidParameterException e){
			throw e;
		}catch(ProcessFailedException e){
			throw e;
		}catch(Throwable t){
			logger.log(Level.SEVERE, "unknown error occurred.", t);
			throw new ProcessFailedException(t);
		}finally{
			releaseSemaphore();
		}
	}

	public String[] getMetadataSchema()
		throws AccessLimitExceededException
		, InvalidParameterException, LanguagePairNotUniquelyDecidedException
		, NoAccessPermissionException, NoValidEndpointsException
		, ProcessFailedException, ServerBusyException, ServiceNotActiveException
		, ServiceNotFoundException, UnsupportedLanguagePairException
	{
		checkStartupException();
		acquireSemaphore();
		try{
			String[] results = doGetMetadataSchema();
			if(results.length <= getMaxResults()){
				return results;
			} else{
				return ArrayUtil.subArray(results, 0, getMaxResults());
			}
		} catch(ProcessFailedException e){
			throw e;
		} catch(Throwable t){
			logger.log(Level.SEVERE, "unknown error occurred.", t);
			throw new ProcessFailedException(t);
		} finally{
			releaseSemaphore();
		}
	}

	public String[] searchCandidatesWithMetadata(String query, String matchingMethod)
		throws AccessLimitExceededException, InvalidParameterException
		, LanguagePairNotUniquelyDecidedException, NoAccessPermissionException
		, NoValidEndpointsException, ProcessFailedException, ServerBusyException
		, ServiceNotActiveException, ServiceNotFoundException
		, UnsupportedLanguagePairException
	{
		checkStartupException();
		String qry = new StringValidator("query", query).notNull().trim().notEmpty()
				.getValue();
		MatchingMethod sm = new MatchingMethodValidator("matchingMethod", matchingMethod)
				.notNull().trim().notEmpty().getMatchingMethod(supportedMatchingMethods);
		acquireSemaphore();
		try{
			String[] results = doSearchCandidatesWithMetadata(qry, sm);
			if(results.length <= getMaxResults()){
				return results;
			} else{
				return ArrayUtil.subArray(results, 0, getMaxResults());
			}
		}catch(InvalidParameterException e){
			throw e;
		}catch(ProcessFailedException e){
			throw e;
		}catch(Throwable t){
			logger.log(Level.SEVERE, "unknown error occurred.", t);
			throw new ProcessFailedException(t);
		}finally{
			releaseSemaphore();
		}
	}

	/**
	 * 
	 * 
	 */
	protected abstract String[] doGetMetadata(String Id)
		throws InvalidParameterException, ProcessFailedException;

	/**
	 * 
	 * 
	 */
	protected abstract String[] doGetMetadataSchema()
		throws ProcessFailedException;

	/**
	 * 
	 * 
	 */
	protected abstract String[] doSearchCandidatesWithMetadata(
		String query, MatchingMethod matchingMethod
		)
		throws InvalidParameterException, ProcessFailedException;

	/**
	 * 
	 * 
	 */
	protected void setSupportedMatchingMethods(
		Set<MatchingMethod> supportedMatchingMethods)
	{
		this.supportedMatchingMethods = supportedMatchingMethods;
	}

	private Set<MatchingMethod> supportedMatchingMethods = MINIMUM_MATCHINGMETHODS;
	private static Logger logger = Logger
			.getLogger(AbstractParallelTextWithExternalMetadataService.class.getName());
}
