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
package jp.go.nict.langrid.composite.translation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import jp.go.nict.langrid.commons.lang.ExceptionUtil;
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
import jp.go.nict.langrid.service_1_2.bilingualdictionary.BilingualDictionaryWithLongestMatchSearchService;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.Translation;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.TranslationWithPosition;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.MorphologicalAnalysisService;
import jp.go.nict.langrid.service_1_2.translation.TranslationWithTemporalDictionaryService;
import jp.go.nict.langrid.service_1_2.util.validator.LanguagePairValidator;
import jp.go.nict.langrid.service_1_2.util.validator.LanguageValidator;
import jp.go.nict.langrid.service_1_2.util.validator.StringValidator;
import jp.go.nict.langrid.servicecontainer.handler.RIProcessor;
import jp.go.nict.langrid.servicecontainer.service.ComponentServiceFactory;
import jp.go.nict.langrid.servicecontainer.service.composite.AbstractCompositeService;
import jp.go.nict.langrid.wrapper.workflowsupport.ConstructSource;
import jp.go.nict.langrid.wrapper.workflowsupport.DefaultMorphologicalAnalysis;
import jp.go.nict.langrid.wrapper.workflowsupport.MergeMorphemesAndTranslationWithPositions;
import jp.go.nict.langrid.wrapper.workflowsupport.TemporalBilingualDictionaryWithLongestMatchSearch;

/**
 * Translation combined with bilingual dictionary with longest match search service implementation.
 * @author Takao Nakaguchi
 */
public class ParaphraseCombinedWithBilingualDictionaryWithLongestMatchSearch
extends AbstractCompositeService
implements TranslationWithTemporalDictionaryService{
	public String translate(String sourceLang, String targetLang,
			String source, Translation[] temporalDict,
			String dictTargetLang)
	throws AccessLimitExceededException,
	InvalidParameterException, LanguagePairNotUniquelyDecidedException,
	NoAccessPermissionException, ProcessFailedException,
	NoValidEndpointsException, ServerBusyException,
	ServiceNotActiveException, ServiceNotFoundException,
	UnsupportedLanguagePairException
	{
		LanguagePair pair = new LanguagePairValidator(
				new LanguageValidator("sourceLang", sourceLang)
				, new LanguageValidator("targetLang", targetLang)
				).notNull().trim().notEmpty().getLanguagePair();
		String src = new StringValidator("source", source)
				.notNull().trim().notEmpty().getValue();
		Translation[] dict = temporalDict;
		if(dict == null){
			dict = new Translation[]{};
		}
		Language dictLang = null;
		if(dictTargetLang != null){
			dictLang = new LanguageValidator("dictTargetLang", dictTargetLang)
					.getLanguage();
		} else{
			dictLang = pair.getTarget();
		}

		String sl = pair.getSource().getCode();
		String tl = pair.getTarget().getCode();
		Translation[] tempDict = dict;
		String dl = dictLang.getCode();

		ComponentServiceFactory factory = getComponentServiceFactory();
		try{
			TemporalBilingualDictionaryWithLongestMatchSearch tempDictMatch = new TemporalBilingualDictionaryWithLongestMatchSearch(
					RIProcessor.getCurrentServiceContext());
			MergeMorphemesAndTranslationWithPositions merger = new MergeMorphemesAndTranslationWithPositions();
			ConstructSource constructSource = new ConstructSource();

			Morpheme[] analyzeResult = null;
			{
				log("invoke MorphologicalAnalysis.analyze");
				try{
					Morpheme[] r = factory.getService(
							"MorphologicalAnalysisPL", MorphologicalAnalysisService.class
							).analyze(sl, src);
					if(r != null && r.length > 0){
						analyzeResult = r;
					} else{
						log("MorphologicalAnalysis.analyze returns null or empty result.");
					}
				} catch(ServiceNotActiveException e){
					log("service is not active: " + e.getServiceId());
				} catch(Exception e){
					log("exception occurred: " + ExceptionUtil.getMessageWithStackTrace(e));
				}
				if(analyzeResult == null){
					log("analyze result is null. calling DefaultMorphologicalAnalysis.");
					analyzeResult = new DefaultMorphologicalAnalysis().analyze(sl, src);
				}
				log("invoke MorphologicalAnalysis.analyze done(" + analyzeResult.length + "morphs)");
			}

			Collection<TranslationWithPosition> tempDictResult = new ArrayList<TranslationWithPosition>();
			{
				log("prepare for TemporalParaphraseDictionaryWithLongestMatchSearch.doSearchAllLongestMatchingTerms()");
				log("invoke TemporalParaphraseDictionaryWithLongestMatchSearch.doSearchAllLongestMatchingTerms("
						+ analyzeResult.length + "morphes["
						+ (analyzeResult.length < 10
								? Arrays.toString(analyzeResult)
								: analyzeResult[0])
						+ "], "
						+ tempDict.length + "translations["
						+ (tempDict.length < 10
								? Arrays.toString(tempDict)
										: tempDict[0])
						+ "])");
				try{
					tempDictResult = tempDictMatch.doSearchAllLongestMatchingTerms(
						pair.getSource(), analyzeResult, tempDict);
				} catch(Exception e){
					log("exception occurred: " + ExceptionUtil.getMessageWithStackTrace(e));
				}
				log("invoke TemporalParaphraseDictionaryWithLongestMatchSearch.doSearchAllLongestMatchingTerms done("
						+ tempDictResult.size() + "translations)");
			}
			
			analyzeResult = merger.doMerge(analyzeResult, tempDictResult);

			TranslationWithPosition[] dictResult = new TranslationWithPosition[]{};
			{
				log("prepare for BilingualDictionaryWithLongestMatchSearchService.searchLongestMatchingTerms()");
				Morpheme[] m = analyzeResult;
				log("invoke BilingualDictionaryWithLongestMatchSearchService.searchLongestMatchingTerms(" +
						m.length + "morphs["
						+ (m.length > 0 ? "word:" + m[0].getWord()
								+ ",lemma:" + m[0].getLemma() + ",pos:" + m[0].getPartOfSpeech()
								: "")
						+ "])");
				try{
					dictResult = factory.getService(
							"BilingualDictionaryWithLongestMatchSearchPL"
							, BilingualDictionaryWithLongestMatchSearchService.class
							).searchLongestMatchingTerms(sl, dl, m);
				} catch(ServiceNotActiveException e){
					log("service is not active: " + e.getServiceId());
				} catch(Exception e){
					log("exception occurred: " + ExceptionUtil.getMessageWithStackTrace(e));
				}
				log("invoke BilingualDictionaryWithLongestMatchSearchService.searchLongestMatchingTerms done(" +
						dictResult.length + "translations)");
			}

			analyzeResult = merger.merge(analyzeResult, dictResult);

			return constructSource.doConstructSource(tl, analyzeResult);
		} catch(InvalidParameterException e){
			throw e;
		} catch(ProcessFailedException e){
			warning("process failed.", e);
			throw e;
		} catch(Throwable t){
			warning("unknown error occurred.", t);
			throw new ProcessFailedException(t);
		}
	}
}
