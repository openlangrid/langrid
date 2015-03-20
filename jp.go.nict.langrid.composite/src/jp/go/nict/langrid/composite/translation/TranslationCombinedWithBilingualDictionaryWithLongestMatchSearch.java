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
import jp.go.nict.langrid.commons.util.ArrayUtil;
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
import jp.go.nict.langrid.service_1_2.translation.TranslationService;
import jp.go.nict.langrid.service_1_2.translation.TranslationWithTemporalDictionaryService;
import jp.go.nict.langrid.service_1_2.util.validator.LanguagePairValidator;
import jp.go.nict.langrid.service_1_2.util.validator.LanguageValidator;
import jp.go.nict.langrid.service_1_2.util.validator.StringValidator;
import jp.go.nict.langrid.service_1_2.workflowsupport.SourceAndMorphemesAndCodes;
import jp.go.nict.langrid.servicecontainer.service.composite.AbstractCompositeService;
import jp.go.nict.langrid.servicecontainer.service.composite.Invocation;
import jp.go.nict.langrid.servicecontainer.service.composite.ServiceLoadingFailedException;
import jp.go.nict.langrid.wrapper.workflowsupport.ConstructSourceAndMorphemesAndCodes;
import jp.go.nict.langrid.wrapper.workflowsupport.DefaultMorphologicalAnalysis;
import jp.go.nict.langrid.wrapper.workflowsupport.ReplacementTerm;
import jp.go.nict.langrid.wrapper.workflowsupport.TemporalBilingualDictionaryWithLongestMatchSearch;

/**
 * Translation combined with bilingual dictionary with longest match search service implementation.
 * @author Takao Nakaguchi
 */
public class TranslationCombinedWithBilingualDictionaryWithLongestMatchSearch
extends AbstractCompositeService
implements TranslationWithTemporalDictionaryService{
	public static class Services{
		@Invocation(name="BilingualDictionaryWithLongestMatchSearchPL")
		private BilingualDictionaryWithLongestMatchSearchService bdict;
		@Invocation(name="MorphologicalAnalysisPL")
		private MorphologicalAnalysisService morph;
		@Invocation(name="TranslationPL")
		private TranslationService trans;
	}

	public TranslationCombinedWithBilingualDictionaryWithLongestMatchSearch() {
		super(Services.class);
	}

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

		try{
			Services s = loadServices();
			TemporalBilingualDictionaryWithLongestMatchSearch tempDictMatch = new TemporalBilingualDictionaryWithLongestMatchSearch();
			ConstructSourceAndMorphemesAndCodes csmc = getConstructSourceAndMorphemesAndCodes();
			ReplacementTerm rt = new ReplacementTerm();

			Morpheme[] analyzeResult = null;
			{
				log("invoke MorphologicalAnalysis.analyze");
				try{
					if(s.morph != null){
						log("calling service specified for MorphologicalAnalysisPL"
								+ " with lang(%s) and text(length: %d).", sl, src.length());
						analyzeResult = s.morph.analyze(sl, src);
					}
				} catch(ServiceNotActiveException e){
					if(!e.getServiceId().equals("AbstractService")){
						log("service is not active: " + e.getServiceId());
					}
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
			Morpheme[] analyzeResultAlternate = null;
			{
				log("prepare for TemporalBilingualDictionaryWithLongestMatchSearch.doSearchAllLongestMatchingTerms()");
				analyzeResultAlternate = analyzeResult;
				log("invoke TemporalBilingualDictionaryWithLongestMatchSearch.doSearchAllLongestMatchingTerms("
						+ analyzeResultAlternate.length + " morphes["
						+ (analyzeResultAlternate.length < 10
								? Arrays.toString(analyzeResultAlternate)
								: analyzeResultAlternate[0])
						+ "], "
						+ tempDict.length + " translations["
						+ (tempDict.length < 10
								? Arrays.toString(tempDict)
										: tempDict[0])
						+ "])");
				try{
					tempDictResult = tempDictMatch.doSearchAllLongestMatchingTerms(
						pair.getSource(), analyzeResultAlternate, tempDict);
				} catch(Exception e){
					log("exception occurred: " + ExceptionUtil.getMessageWithStackTrace(e));
				}
				log("invoke TemporalBilingualDictionaryWithLongestMatchSearch.doSearchAllLongestMatchingTerms done("
						+ tempDictResult.size() + "translations)");
			}
			SourceAndMorphemesAndCodes firstSmc = null;
			{
				log("prepare for ConstructSourceAndMorphemesAndCodes.doConstructSMC()");
				TranslationWithPosition[] bdictResult = tempDictResult.toArray(emptyTranslation);
				log("invoke ConstructSourceAndMorphemesAndCodes.doConstructSMC("
						+ bdictResult.length + "morphs,"
						+ tempDict.length + "translations)");
				firstSmc = csmc.doConstructSMC(sl, analyzeResultAlternate, bdictResult);
				log("invoke ConstructSourceAndMorphemesAndCodes.doConstructSMC done(" +
						+ firstSmc.getMorphemes().length + "morphs," +
						+ firstSmc.getCodes().length + "codes,"
						+ firstSmc.getTargetWords().length + "targetWords)");
			}
			TranslationWithPosition[] dictResult = new TranslationWithPosition[]{};
			{
				log("prepare for BilingualDictionaryWithLongestMatchSearchService.searchLongestMatchingTerms()");
				Morpheme[] m = firstSmc.getMorphemes();
				log("invoke BilingualDictionaryWithLongestMatchSearchService.searchLongestMatchingTerms(" +
						m.length + "morphs["
						+ (m.length > 0 ? "word:" + m[0].getWord()
								+ ",lemma:" + m[0].getLemma() + ",pos:" + m[0].getPartOfSpeech()
								: "")
						+ "])");
				try{
					if(s.bdict != null){
						dictResult = s.bdict.searchLongestMatchingTerms(sl, dl, m);
					}
				} catch(ServiceNotActiveException e){
					if(!e.getServiceId().equals("AbstractService")){
						log("service is not active: " + e.getServiceId());
					}
				} catch(Exception e){
					log("exception occurred: " + ExceptionUtil.getMessageWithStackTrace(e));
				}
				log("invoke BilingualDictionaryWithLongestMatchSearchService.searchLongestMatchingTerms done(" +
						dictResult.length + "translations)");
			}

			TranslationWithPosition[] dictResultAlt = null;
			SourceAndMorphemesAndCodes secondSmc = null;
			{
				log("prepare for ConstructSourceAndMorphemesAndCodes.doConstructSMC()");
				dictResultAlt = dictResult;
				log("invoke ConstructSourceAndMorphemesAndCodes.doConstructSMC("
						+ firstSmc.getMorphemes().length + "morphs," + dictResult.length + "translations)"
						);
				secondSmc = csmc.doConstructSMC(sl, firstSmc.getMorphemes(), dictResultAlt);
				log("invoke ConstructSourceAndMorphemesAndCodes.doConstructSMC done(" +
						+ secondSmc.getMorphemes().length + "morphs," +
						+ secondSmc.getCodes().length + "codes,"
						+ secondSmc.getTargetWords().length + "targetWords)"
						);
			}

			log("joining mrophemes and codes");
			SourceAndMorphemesAndCodes joinedSmc = new SourceAndMorphemesAndCodes(
					secondSmc.getSource()
					, secondSmc.getMorphemes()
					, ArrayUtil.append(firstSmc.getCodes(), secondSmc.getCodes())
					, ArrayUtil.append(firstSmc.getTargetWords(), secondSmc.getTargetWords())
			);
			log("joining mrophemes and codes done(" +
					+ joinedSmc.getMorphemes().length + "morphs," +
					+ joinedSmc.getCodes().length + "codes,"
					+ joinedSmc.getTargetWords().length + "targetWords)");

			log("invoke TranslationService.translate(" + joinedSmc.getSource().length() + "chars)");
			String fwTranslationResult = null;
			if(s.trans == null){
				if(!sl.equals(tl)){
					throw new ProcessFailedException("TranslationPL not binded.");
				}
			} else{
				try{
					fwTranslationResult = s.trans.translate(sl, tl, joinedSmc.getSource());
				} catch(ServiceNotActiveException e){
					if(!e.getServiceId().equals("AbstractService") || !sl.equals(tl)){
						throw e;
					}
				}
			}
			if(fwTranslationResult == null){
				fwTranslationResult = joinedSmc.getSource();
			}
			log("invoke TranslationService.translate done");
			log("replacing codes(" + joinedSmc.getCodes().length + "codes" +
					"," + joinedSmc.getTargetWords().length + "words)");
			String fwFinalResult = rt.doReplace(tl, fwTranslationResult
					, joinedSmc.getCodes(), joinedSmc.getTargetWords());
			log("replacing codes done");

			return fwFinalResult;
		} catch(InvalidParameterException e){
			throw e;
		} catch(ProcessFailedException e){
			warning("process failed.", e);
			throw e;
		} catch(ServiceLoadingFailedException e){
			throw new ServiceNotFoundException(e.getServiceId());
		} catch(Throwable t){
			warning("unknown error occurred.", t);
			throw new ProcessFailedException(t);
		}
	}

	protected ConstructSourceAndMorphemesAndCodes getConstructSourceAndMorphemesAndCodes(){
		return new ConstructSourceAndMorphemesAndCodes();
	}
	private static final TranslationWithPosition[] emptyTranslation = {};
}
