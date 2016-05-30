/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
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
package jp.go.nict.langrid.composite.async;

import jp.go.nict.langrid.commons.ws.param.URLParameterContext;
import jp.go.nict.langrid.composite.backtranslation.BackTranslationCombinedWithBilingualDictionaryWithLongestMatchSearch;
import jp.go.nict.langrid.composite.commons.thread.GradualTask;
import jp.go.nict.langrid.composite.commons.thread.Task;
import jp.go.nict.langrid.composite.commons.thread.TaskManager;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.LangridException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.backtranslation.AsyncBackTranslationResult;
import jp.go.nict.langrid.service_1_2.backtranslation.AsyncBackTranslationWithTemporalDictionaryService;
import jp.go.nict.langrid.service_1_2.backtranslation.BackTranslationResult;
import jp.go.nict.langrid.service_1_2.backtranslation.BackTranslationWithTemporalDictionaryService;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.Translation;
import jp.go.nict.langrid.servicecontainer.handler.RIProcessor;
import jp.go.nict.langrid.servicecontainer.service.ComponentServiceFactory;

/**
 * Asynchronous back translation combined with bilingual dictionary with longest match search service implementation.
 * @author Takao Nakaguchi
 */
public class AsyncBackTranslationCombinedWithBilingualDictionaryWithLongestMatchSearch
extends AbstractAsyncCompositeService
implements AsyncBackTranslationWithTemporalDictionaryService{
	@Override
	public String startBackTranslation(final String sourceLang, final String intermediateLang,
			final String[] sources, final Translation[] temporalDict,
			final String dictTargetLang)
	throws InvalidParameterException, ProcessFailedException{
		Task<BackTranslationResult> task = new GradualTask<BackTranslationResult>(){
			protected void doWork() throws LangridException {
				BackTranslationWithTemporalDictionaryService s = new BackTranslationCombinedWithBilingualDictionaryWithLongestMatchSearch(){
					public ComponentServiceFactory getComponentServiceFactory(){
						return AsyncBackTranslationCombinedWithBilingualDictionaryWithLongestMatchSearch.this.getComponentServiceFactory();
					};
				};
				for(String source : sources){
					if(interrupted()) break;
					addResult(s.backTranslate(sourceLang, intermediateLang, source, temporalDict, dictTargetLang));
				}
			};
		};
		URLParameterContext url = new URLParameterContext(RIProcessor.getCurrentServiceContext().getRequestUrl());
		task.setExpirationPeriod(url.getInteger("expire", 60 * 10 * 1000));
		return manager.addAndRunTask(task);
	}

	@Override
	public AsyncBackTranslationResult getCurrentResult(String token)
			throws InvalidParameterException, ProcessFailedException {
		GradualTask<BackTranslationResult> t = manager.getTask(token);
		if(t == null){
			return new AsyncBackTranslationResult(true, emptyResults);
		}
		try{
			synchronized(t){
				return new AsyncBackTranslationResult(
						t.isDone() && !t.isExceptionOccurred()
						, t.getCurrentResult(getQueueWaitMillis()).toArray(emptyResults)
						);
			}
		} catch(InvalidParameterException e){
			throw e;
		} catch(ProcessFailedException e){
			throw e;
		} catch(Exception e){
			throw new ProcessFailedException(e);
		}
	}
	
	@Override
	public void terminate(String token)
			throws InvalidParameterException, ProcessFailedException {
		manager.removeTask(token);
	}

	public TaskManager getTaskManager(){
		return manager;
	}

	private static TaskManager manager = new TaskManager();
	private static final BackTranslationResult[] emptyResults = {};
}
