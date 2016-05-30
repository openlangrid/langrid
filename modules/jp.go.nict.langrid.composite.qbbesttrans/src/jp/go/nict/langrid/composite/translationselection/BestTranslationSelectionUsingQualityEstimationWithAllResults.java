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
package jp.go.nict.langrid.composite.translationselection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

import jp.go.nict.langrid.composite.commons.thread.Task;
import jp.go.nict.langrid.composite.commons.thread.TaskGroup;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.LangridException;
import jp.go.nict.langrid.service_1_2.LanguagePairNotUniquelyDecidedException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.ServiceNotActiveException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguagePairException;
import jp.go.nict.langrid.service_1_2.qualityestimation.QualityEstimationService;
import jp.go.nict.langrid.service_1_2.translation.TranslationService;
import jp.go.nict.langrid.service_1_2.translationselection.EstimationResult;
import jp.go.nict.langrid.service_1_2.translationselection.SelectionResult;
import jp.go.nict.langrid.service_1_2.translationselection.TranslationSelectionService;
import jp.go.nict.langrid.servicecontainer.service.composite.AbstractCompositeService;

public class BestTranslationSelectionUsingQualityEstimationWithAllResults
extends AbstractCompositeService implements TranslationSelectionService {

	@Override
	public SelectionResult select(String sourceLang, String targetLang,
			String source) throws ProcessFailedException,
			LanguagePairNotUniquelyDecidedException,
			UnsupportedLanguagePairException, InvalidParameterException {


		// Read bindings
		List<TranslationService> transServices = new ArrayList<TranslationService>();
		for(int i=1; i<=maxTransNum; i++) {
			TranslationService translation = getComponentServiceFactory().getService(
					"TranslationPL" + i, TranslationService.class);
			if(translation != null) {
				transServices.add(translation);
			}
		}
		if(transServices.isEmpty())  throw new ProcessFailedException("Translation service not bound.");

		QualityEstimationService qualityEstimation = getComponentServiceFactory().getService(
				"QualityEstimationPL", QualityEstimationService.class);
		if(qualityEstimation == null) throw new ProcessFailedException("QualityEstimationPL not bound.");

		// Set bindings
		List<Callable<EstimationResult>> tasks = new ArrayList<Callable<EstimationResult>>();
		Iterator<TranslationService> iter = transServices.iterator();
		while(iter.hasNext()) {
			TranslationService translation = iter.next();
			tasks.add(new TranslationAndEstimationCallable(translation, qualityEstimation,
					sourceLang, targetLang, source));
		}

		TaskGroup group = new TaskGroup();
		final List<EstimationResult> results = Collections.synchronizedList(
				new ArrayList<EstimationResult>());
		for(int i = 0; i < tasks.size(); i++){
			results.add(null);
		}
		int i = 0;
		for(final Callable<EstimationResult> t : tasks){
			final int index = i++;
			group.add(new Task() {
				@Override
				protected void doWork() throws LangridException {
					try{
						results.set(index,t.call());
					} catch(Exception e){
						e.printStackTrace();
					}
				}
			});
		}
		try {
			group.runAndWait();
		} catch (InterruptedException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}


		// Invoke
/*		ExecutorService executor = Executors.newFixedThreadPool(tasks.size());
		List<Future<EstimationResult>> futures = null;
		try {
			futures = executor.invokeAll(tasks);
		} catch (InterruptedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		executor.shutdown();
*/
		// Find best result
		double maxEstimation = 0.0;
		int indexBest = 0, indexCurrent = 0;
		List<EstimationResult> arrayEstimation = new ArrayList<EstimationResult>();

		for (EstimationResult estimation : results) {
			if(estimation == null){
				continue;
			}
			if(estimation.getQuality() > maxEstimation) {
				maxEstimation = estimation.getQuality();
				indexBest = indexCurrent;
			}
			indexCurrent++;

			arrayEstimation.add(estimation);
		}

		SelectionResult result = new SelectionResult();
		result.setEstimationResults((EstimationResult[])arrayEstimation.toArray(new EstimationResult[0]));
		result.setSelectedIndex(indexBest+1);

		return result;
	}

	class TranslationAndEstimationCallable implements Callable<EstimationResult> {

		TranslationService transService;
		QualityEstimationService estimationService;
		String sourceLang;
		String targetLang;
		String source;

		public TranslationAndEstimationCallable(TranslationService transService,
				QualityEstimationService estimationService, String sourceLang,
				String targetLang, String source) {
			this.transService = transService;
			this.estimationService = estimationService;
			this.sourceLang = sourceLang;
			this.targetLang = targetLang;
			this.source = source;
		}

		@Override
		public EstimationResult call() throws Exception {

			try {
				String target = transService.translate(sourceLang, targetLang, source);
				double similarity = estimationService.estimate(sourceLang, targetLang, source, target);

				EstimationResult result = new EstimationResult();
				result.setQuality(similarity);
				result.setTarget(target);

				return result;
			} catch(ServiceNotActiveException e){
				return null;
			} catch(LanguagePairNotUniquelyDecidedException e){
				throw e;
			} catch(UnsupportedLanguagePairException e){
				throw e;
			} catch(InvalidParameterException e){
				throw e;
			} catch(ProcessFailedException e) {
				throw e;
			} catch(Throwable t){
				throw new ProcessFailedException(t);
			}
		}

	}

	public EstimationResult translate(TranslationService transService, QualityEstimationService estimationService,
			String sourceLang, String targetLang, String source) throws ProcessFailedException,
			LanguagePairNotUniquelyDecidedException,
			UnsupportedLanguagePairException, InvalidParameterException {

		try {
			String target = transService.translate(sourceLang, targetLang, source);
			double similarity = estimationService.estimate(sourceLang, targetLang, source, target);

			EstimationResult result = new EstimationResult();
			result.setQuality(similarity);
			result.setTarget(target);

			return result;

		} catch(LanguagePairNotUniquelyDecidedException e){
			throw e;
		} catch(UnsupportedLanguagePairException e){
			throw e;
		} catch(InvalidParameterException e){
			throw e;
		} catch(ProcessFailedException e) {
			throw e;
		} catch(Throwable t){
			throw new ProcessFailedException(t);
		}
	}


	private final int maxTransNum = 5;
}
